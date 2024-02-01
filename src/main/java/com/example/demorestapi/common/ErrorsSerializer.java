package com.example.demorestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeFieldName("errors");
        jsonGenerator.writeStartArray();

        errors.getFieldErrors().forEach(err -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field",err.getField());
                jsonGenerator.writeStringField("objectName",err.getObjectName());
                jsonGenerator.writeStringField("code",err.getCode());
                jsonGenerator.writeStringField("defaultMessage",err.getDefaultMessage());
                Object rejectedValue = err.getRejectedValue();
                if(rejectedValue != null) {
                    jsonGenerator.writeStringField("rejectedValue",rejectedValue.toString());
                }
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        errors.getGlobalErrors().forEach(err -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName",err.getObjectName());
                jsonGenerator.writeStringField("code",err.getCode());
                jsonGenerator.writeStringField("defaultMessage",err.getDefaultMessage());

                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        jsonGenerator.writeEndArray();
    }
}
