package org.example.asserts;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.FactoryBasedNavigableIterableAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConstraintViolationAssert extends FactoryBasedNavigableIterableAssert<ConstraintViolationAssert,
                Iterable<? extends String>, String, ObjectAssert<String>> {

    private final Map<String, List<String>> violations;
    private final Set<String> proceedFields;

    public ConstraintViolationAssert(Map<String, List<String>> violations, List<String> currentFieldErrors,
            Set<String> proceedFields, String fieldName) {
        super(currentFieldErrors, ConstraintViolationAssert.class, new ObjectAssertFactory<>());
        proceedFields.add(fieldName);
        verifyViolationsNotNull(currentFieldErrors, fieldName);

        this.violations = violations;
        this.proceedFields = proceedFields;
    }

    private void verifyViolationsNotNull(List<String> fieldViolations, String fieldName) {
        if (fieldViolations == null) {
            throw failure("Field '%s' does not have violations", fieldName);
        }
    }

    public ConstraintViolationAssert field(String fieldName) {
        return new ConstraintViolationAssert(violations, violations.get(fieldName), proceedFields, fieldName);
    }

    public void verifyNoMoreFieldsWithErrors() {
        Assertions.assertThat(violations.keySet())
                .as("Error checking was not performed on all fields")
                .withFailMessage("%nVerified fields: %s. Expected fields to verify: %s",
                        proceedFields, violations.keySet())
                .containsExactlyInAnyOrderElementsOf(proceedFields);
    }

    public static class ConstraintViolationAssertBuilder {
        private final Map<String, List<String>> violations;

        ConstraintViolationAssertBuilder(Map<String, List<String>> violations) {
            this.violations = violations;
        }

        public ConstraintViolationAssert field(String fieldName) {
            return new ConstraintViolationAssert(violations, violations.get(fieldName), new HashSet<>(), fieldName);
        }

        public void isEmpty() {
            if (violations != null && !violations.isEmpty()) {
                throw new AssertionError("Violations list is not empty", null);
            }
        }
    }

}