package io.github.veerakumarak.validations.exmaple;

import io.github.veerakumarak.validations.FieldResult;
import io.github.veerakumarak.validations.helpers.StringFieldValidator;

public class OptionalExample {
    public static void main(String[] args) {
        // Email is optional, but if present, must match regex
        FieldResult emailResult1 = StringFieldValidator.optional() // Best practice: use optional() for nullable fields
                .matchesRegex("^[A-Za-z0-9+_.-]+@(.+)$")
                .validate("email", null);

        System.out.println("Email (null) result valid: " + emailResult1.valid()); // Output: true

        FieldResult emailResult2 = StringFieldValidator.optional()
                .matchesRegex("^[A-Za-z0-9+_.-]+@(.+)$")
                .validate("email", "test@example.com");

        System.out.println("Email (valid) result valid: " + emailResult2.valid()); // Output: true

        FieldResult emailResult3 = StringFieldValidator.optional()
                .matchesRegex("^[A-Za-z0-9+_.-]+@(.+)$")
                .validate("email", "invalid-email");

        System.out.println("Email (invalid) result valid: " + emailResult3.valid()); // Output: false
        System.out.println("Email (invalid) reasons: " + emailResult3.reasons());
        // Output: Email (invalid) reasons: [must match regex ^[A-Za-z0-9+_.-]+@(.+)$]
    }
}