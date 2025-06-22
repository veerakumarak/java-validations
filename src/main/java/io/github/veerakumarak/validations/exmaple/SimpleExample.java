package io.github.veerakumarak.validations.exmaple;

import io.github.veerakumarak.validations.FieldResult;
import io.github.veerakumarak.validations.helpers.IntegerFieldValidator;

public class SimpleExample {
    public static void main(String[] args) {
        // Validate an integer: should be non-null and greater than 10
        FieldResult ageResult = IntegerFieldValidator.nonNull() // Best practice: start with nullability check
                .greaterThan(10)
                .validate("age", 15);

        if (ageResult.valid()) {
            System.out.println("Age is valid."); // Output: Age is valid.
        } else {
            System.out.println("Age validation failed: " + ageResult.reasons());
        }

        FieldResult invalidAgeResult = IntegerFieldValidator.nonNull() // Best practice
                .greaterThan(10)
                .validate("age", 5);

        if (!invalidAgeResult.valid()) {
            System.out.println("Invalid age reasons: " + invalidAgeResult.reasons());
            // Output: Invalid age reasons: [should be greater than 10]
        }

        FieldResult nullAgeResult = IntegerFieldValidator.nonNull()
                .greaterThan(10)
                .validate("age", null);

        if (!nullAgeResult.valid()) {
            System.out.println("Null age reasons: " + nullAgeResult.reasons());
            // Output: Null age reasons: [should not be null]
        }
    }
}
