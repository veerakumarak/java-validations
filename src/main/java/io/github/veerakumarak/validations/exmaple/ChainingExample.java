package io.github.veerakumarak.validations.exmaple;

import io.github.veerakumarak.validations.FieldResult;
import io.github.veerakumarak.validations.helpers.StringFieldValidator;

public class ChainingExample {
    public static void main(String[] args) {
        // Validate a string: non-null, min length 5, max length 20
        FieldResult usernameResult = StringFieldValidator.nonNull() // Best practice
                .minLength(5)
                .maxLength(20)
                .validate("username", "john_doe");

        if (usernameResult.valid()) {
            System.out.println("Username is valid."); // Output: Username is valid.
        } else {
            System.out.println("Username validation failed: " + usernameResult.reasons());
        }

        // Example with multiple failures
        FieldResult badUsernameResult = StringFieldValidator.nonNull() // Best practice
                .minLength(5)
                .maxLength(20)
                .validate("username", ""); // Too short and empty

        if (!badUsernameResult.valid()) {
            System.out.println("Bad username reasons: " + badUsernameResult.reasons());
            // Output: Bad username reasons: [should have at least 5 characters, should not be empty]
        }
    }
}