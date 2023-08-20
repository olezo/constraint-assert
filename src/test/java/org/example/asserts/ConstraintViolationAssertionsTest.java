package org.example.asserts;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.asserts.fixture.PersonFixture.Person;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.asserts.ConstraintViolationAssertions.assertThat;
import static org.example.asserts.fixture.PersonFixture.getPerson;
import static org.example.asserts.fixture.PersonFixture.getPersonWithConstraintErrors;

class ConstraintViolationAssertionsTest {
    private static Validator validator;

    @BeforeAll
    public static void before() {
        try (ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {

            validator = factory.getValidator();
        }
    }

    @Test
    void shouldVerifyNameFieldAndIgnoreAgeField() {
        var person = getPersonWithConstraintErrors();

        var constraintViolations = validator.validate(person);

        assertThat(constraintViolations)
                .field("name").containsExactlyInAnyOrder("must not be blank", "must not be null");
    }

    @Test
    void shouldVerifyThatThereAreNoMoreErrors() {
        var person = getPersonWithConstraintErrors();

        var constraintViolations = validator.validate(person);

        assertThat(constraintViolations)
                .field("name").containsExactlyInAnyOrder("must not be blank", "must not be null")
                .field("age").containsExactlyInAnyOrder("must be greater than 0")
                .verifyNoMoreFieldsWithErrors();
    }

    @Test
    void shouldThrowError_whenNotAllFieldsWereVerified() {
        var person = getPersonWithConstraintErrors();

        var constraintViolations = validator.validate(person);

        assertThatThrownBy(() ->
                assertThat(constraintViolations)
                        .field("name").containsExactlyInAnyOrder("must not be blank", "must not be null")
                        .verifyNoMoreFieldsWithErrors()
        ).isInstanceOf(AssertionError.class)
                .hasMessage("""
                        [Error checking was not performed on all fields]\s
                        Verified fields: [name]. Expected fields to verify: [name, age]""");
    }

    @Test
    void shouldPass_whenPersonHasValidNameAndAge() {
        var person = getPerson(10, "Name");

        var constraintViolations = validator.validate(person);

        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void shouldThrowError_whenViolationSetIsNotEmpty() {
        var person = getPersonWithConstraintErrors();

        var constraintViolations = validator.validate(person);

        assertThatThrownBy(() -> assertThat(constraintViolations).isEmpty())
                .isInstanceOf(AssertionError.class)
                .hasMessage("Violations list is not empty");
    }

    @Test
    void shouldPass_whenConstraintViolationSetIsNull() {
        assertThat((Set<ConstraintViolation<Person>>) null).isEmpty();
    }
}