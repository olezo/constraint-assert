package org.example.asserts.utils;

import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ConstraintVerificationUtils {
    public static <T> Map<String, List<String>> convertToPropertyErrorMap(Set<ConstraintViolation<T>> violations) {
        if (violations == null) {
            return Map.of();
        }

        return violations.stream()
                .collect(Collectors.groupingBy(ConstraintVerificationUtils::getPropertyName,
                        collectingAndThen(toList(),
                                list -> list.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .collect(Collectors.toList())
                        )));
    }

    private static String getPropertyName(ConstraintViolation<?> violation) {
        return violation.getPropertyPath().toString();
    }
}
