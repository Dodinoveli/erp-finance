package com.logikaintermedia.erp.validation;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.boot.jackson.JsonComponent;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@JsonComponent
public class HtmlCleanupDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getValueAsString();
        if (value == null) {
            return null;
        }

        // Jsoup.clean akan menghapus semua tag HTML termasuk <script>, <div>, dll.
        // Menghasilkan teks murni saja. lewat request body
        return Jsoup.clean(value, Safelist.none());
    }

}
