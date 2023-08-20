package org.example.asserts.fixture;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PersonFixture {

    public static Person getPersonWithConstraintErrors() {
        return getPerson(-10, null);
    }

    public static Person getPerson(int age, String name) {
        var person = new Person();
        person.setName(name);
        person.setSurname("Surname");
        person.setAge(age);

        return person;
    }

    public static class Person {
        @NotBlank
        @NotNull
        public String name;

        @NotNull
        public String surname;

        @NotNull
        @Positive
        public Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
