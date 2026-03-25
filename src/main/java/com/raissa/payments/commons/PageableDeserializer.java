package com.raissa.payments.commons;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public class PageableDeserializer extends JsonDeserializer<Pageable> {

    @Override
    public Pageable deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Deserializar el JSON en un mapa
        JsonNode node = p.getCodec().readTree(p);

        // Obtener el número de página y el tamaño de la página
        int pageNumber = node.get("pageNumber").asInt();
        int pageSize = node.get("pageSize").asInt();

        return PageRequest.of(pageNumber, pageSize);
    }
}
