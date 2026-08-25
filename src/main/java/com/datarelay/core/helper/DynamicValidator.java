package com.datarelay.core.helper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.datarelay.core.entity.Feature;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DynamicValidator {

    public void validateDataStream(Map<String, Object> data, List<Feature> featureList) throws Exception {
        for (Feature feature : featureList) {
            String expectedName = feature.getName();
            String expectedType = feature.getDtype();

            if (!data.containsKey(expectedName)) {
                throw new RuntimeException("Column: " + expectedName + ". Not found");
            }

            Object value = data.get(expectedName);
            validateDtype(value, expectedType, expectedName);
        }
    }

    private void validateDtype(Object value, String expectedType, String featureName) {

        boolean isValid = switch (expectedType.toLowerCase()) {
            case "int64" -> value instanceof Integer || value instanceof Long;
            case "int32" -> value instanceof Integer;
            case "int16" -> value instanceof Integer &&
                (Integer) value >= Short.MIN_VALUE &&
                (Integer) value <= Short.MAX_VALUE;

            case "string" -> value instanceof String;
            case "char" -> value instanceof String &&
                ((String) value).length() <= 1;

            case "float64" -> value instanceof Double || value instanceof Float;
            case "float32" -> value instanceof Number &&
                Math.abs(((Number) value).doubleValue()) <= Float.MAX_VALUE;
            case "float16" -> value instanceof Number &&
                Math.abs(((Number) value).doubleValue()) <= 65504.0;

            default -> false;
        };

        if (!isValid) {
            throw new RuntimeException("Invalid data type for: " + featureName + ". Expected: " + expectedType + ". Got: " + value.getClass().getSimpleName());
        }
    }
}
