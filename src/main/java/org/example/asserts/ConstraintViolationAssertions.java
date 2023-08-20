package org.example.asserts;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;
import org.example.asserts.ConstraintViolationAssert.ConstraintViolationAssertBuilder;
import org.example.asserts.utils.ConstraintVerificationUtils;

import java.util.Set;

public class ConstraintViolationAssertions extends Assertions {

    public static <T> ConstraintViolationAssertBuilder assertThat(Set<ConstraintViolation<T>> violations) {
        var violationMap = ConstraintVerificationUtils.convertToPropertyErrorMap(violations);
        return new ConstraintViolationAssertBuilder(violationMap);
    }
}
