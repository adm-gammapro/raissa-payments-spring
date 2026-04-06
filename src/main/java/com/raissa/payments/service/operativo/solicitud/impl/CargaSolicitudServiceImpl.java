package com.raissa.payments.service.operativo.solicitud.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.operativo.solicitud.request.CargaSolicitudJsonRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.CargaSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.LineaCargaRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.CargaSolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.TrackingResponseDto;
import com.raissa.payments.service.operativo.solicitud.CargaSolicitudService;
import com.raissa.payments.service.operativo.solicitud.CargarSolicitudConectorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CargaSolicitudServiceImpl implements CargaSolicitudService {
    private final CargarSolicitudConectorService cargarSolicitudConectorService;

    @Override
    @Transactional(readOnly = true)
    public CargaSolicitudResponseDto cargar(CargaSolicitudRequestDto req, HttpServletRequest request) {
        validarArchivo(req.getFile());

        List<LineaCargaRequestDto> lineas = parseExcelToLineas(req.getFile());
        if (lineas.stream().noneMatch(l -> "H".equalsIgnoreCase(l.getTipo()))) {
            throw new IllegalArgumentException("El archivo debe contener al menos una fila H");
        }

        CargaSolicitudJsonRequestDto payload = new CargaSolicitudJsonRequestDto();
        payload.setCodigoCliente(req.getIdEmpresa());
        payload.setUsuarioCarga(req.getUsuarioCarga());
        payload.setLineas(lineas);
        payload.setTipoCarga(Constante.CARGA_EXCEL);

        return cargarSolicitudConectorService.createSolicitud(payload, request);
    }

    @Override
    @Transactional(readOnly = true)
    public CargaSolicitudResponseDto cargarDesdeJson(CargaSolicitudJsonRequestDto req, HttpServletRequest request) {
        if (req == null || req.getLineas() == null || req.getLineas().isEmpty()) {
            throw new IllegalArgumentException("No hay líneas para procesar");
        }
        if (req.getLineas().stream().noneMatch(l -> "H".equalsIgnoreCase(l.getTipo()))) {
            throw new IllegalArgumentException("Debe existir al menos una línea H");
        }
        // Opcional: normalizar campos (trim) o validar tipos H/D
        req.getLineas().forEach(l -> {
            if (!"H".equalsIgnoreCase(l.getTipo()) && !"D".equalsIgnoreCase(l.getTipo())) {
                throw new IllegalArgumentException("Tipo debe ser H o D en todas las líneas");
            }
        });

        req.setTipoCarga(Constante.CARGA_JSON);

        return cargarSolicitudConectorService.createSolicitud(req, request);
    }

    private void validarArchivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo es obligatorio");
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("El archivo debe ser .xlsx");
        }
    }

    private List<LineaCargaRequestDto> parseExcelToLineas(MultipartFile file) {
        List<LineaCargaRequestDto> lineas = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook wb = WorkbookFactory.create(is)) {

            Sheet sheet = wb.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            if (rows <= 1) { // asumiendo fila 0 es header
                throw new IllegalArgumentException("El archivo no contiene datos");
            }

            for (int i = 1; i < rows; i++) { // empezamos después de la cabecera
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String tipo = getString(row, 0);
                String cuenta = getString(row, 1);
                String codEnt = getString(row, 2);
                String moneda = getString(row, 3);
                BigDecimal monto = getNumeric(row, 4);
                String beneficiario = "";
                String mismoTitular = "";

                if ("D".equalsIgnoreCase(tipo)) {
                    beneficiario = getString(row, 5);
                }
                if ("D".equalsIgnoreCase(tipo)) {
                    mismoTitular = getString(row, 6);
                }

                if (!"H".equalsIgnoreCase(tipo) && !"D".equalsIgnoreCase(tipo)) {
                    throw new IllegalArgumentException("Fila " + (i + 1) + ": Tipo debe ser H o D");
                }

                LineaCargaRequestDto l = new LineaCargaRequestDto();
                l.setTipo(tipo);
                l.setCuenta(cuenta);
                l.setCodigoEntidadFinanciera(codEnt);
                l.setMoneda(moneda);
                l.setMonto(monto);
                l.setBeneficiario(beneficiario);
                l.setMismoTitular(mismoTitular);
                lineas.add(l);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error leyendo el archivo Excel", e);
        }
        return lineas;
    }

    private String getString(Row row, int idx) {
        Cell cell = row.getCell(idx);
        return (cell == null) ? "" : cell.toString().trim();
    }

    private BigDecimal getNumeric(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return BigDecimal.ZERO;

        try {
            return BigDecimal.valueOf(cell.getNumericCellValue())
                    .setScale(2, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }
}
