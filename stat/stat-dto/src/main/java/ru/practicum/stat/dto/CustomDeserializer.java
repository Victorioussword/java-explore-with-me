package ru.practicum.stat.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDeserializer extends JsonDeserializer {


  //  protected CustomDeserializer() {
 //       this(null);
  //  }

   // protected CustomDeserializer(Class<?> vc) {
 //       super(vc);
  //  }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String value = jsonParser.getText();
        if (!"".equals(value)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(value, formatter);
        }
        return null;
    }
}
