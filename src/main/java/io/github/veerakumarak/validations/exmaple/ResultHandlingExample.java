package io.github.veerakumarak.validations.exmaple;

import io.github.veerakumarak.validations.FieldResult;
import io.github.veerakumarak.validations.ValidationResult;
import io.github.veerakumarak.validations.Validator;
import io.github.veerakumarak.validations.helpers.StringFieldValidator;
import io.github.veerakumarak.fp.failures.InvalidRequest; // Assuming this class exists

public class ResultHandlingExample {
    public static void main(String[] args) {
        FieldResult firstName = StringFieldValidator.nonNull().notEmpty().validate("firstName", "");
        FieldResult lastName = StringFieldValidator.nonNull().notEmpty().validate("lastName", "Smith");

        ValidationResult result = Validator.allValid(firstName, lastName);

        // 1. Check validity
        System.out.println("Is valid? " + result.isValid()); // Output: false

        // 2. Get detailed reasons map
        System.out.println("Reasons map: " + result.reasons());
        // Output: Reasons map: {firstName=[should not be empty]}

        // 3. Get formatted message
        System.out.println("Formatted message:\n" + result.getMessage());
        /* Output:
        Formatted message:
        {
          "firstName": "should not be empty"
        }
        */

        // 4. Throw an exception if invalid (integrates with a custom Failure class)
        try {
            result.orThrow();
        } catch (InvalidRequest e) {
            System.out.println("Caught InvalidRequest exception: " + e.getMessage());
            // Output: Caught InvalidRequest exception: {
            //   "firstName": "should not be empty"
            // }
        }

        // 5. Convert to a Failure object
        // This is useful if you have a functional programming-style error handling
        io.github.veerakumarak.fp.Failure failure = result.toFailure();
        if (!failure.isEmpty()) {
            System.out.println("Converted to Failure: " + failure.getMessage());
            // Output: Converted to Failure: {
            //   "firstName": "should not be empty"
            // }
        }
    }
}