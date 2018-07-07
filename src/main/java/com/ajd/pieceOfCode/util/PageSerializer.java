package com.ajd.pieceOfCode.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.domain.Page;

import java.io.IOException;

public class PageSerializer extends JsonSerializer<Page> {
    @Override
    public Class<Page> handledType() {
        return Page.class;
    }

    @Override
    public void serialize(Page page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeFieldName("elements");
        serializerProvider.defaultSerializeValue(page.getContent(), jsonGenerator);
        jsonGenerator.writeEndObject();
    }
}
