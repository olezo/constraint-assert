### Constraint Violation Assert Library

```java
    var constraintViolations = validator.validate(person);

    ConstraintViolationAssert.assertThat(constraintViolations)
        .field("name").containsExactlyInAnyOrder("must not be blank", "must not be null")
        .field("age").containsExactlyInAnyOrder("must be greater than 0")
        .verifyNoMoreFieldsWithErrors();
``` 