package com.logikaintermedia.erp.validation;

import java.math.BigDecimal;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    @Nullable
    public BigDecimal convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }

        source = source.trim();

        try {
            if (source.contains(",") && source.contains(".")) {
                if (source.lastIndexOf(",") > source.lastIndexOf(".")) {
                    // format Indonesia: 30.000,50
                    source = source.replace(".", "").replace(",", ".");
                } else {
                    // format US: 30,000.50
                    source = source.replace(",", "");
                }
            } else if (source.contains(",")) {
                // misal: 30000,50 → 30000.50
                source = source.replace(",", ".");
            }
            // kalau cuma titik → sudah benar

            return new BigDecimal(source);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format angka tidak valid: " + source);
        }
    }

}
