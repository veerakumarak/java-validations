package io.github.veerakumarak.validations.exmaple;

import io.github.veerakumarak.validations.FieldResult;
import io.github.veerakumarak.validations.ValidationResult;
import io.github.veerakumarak.validations.Validator;
import io.github.veerakumarak.validations.helpers.IntegerFieldValidator;
import io.github.veerakumarak.validations.helpers.StringFieldValidator;

public class CombinedValidationExample {
    public static void main(String[] args) {
        FieldResult nameResult = StringFieldValidator.nonNull().minLength(3).validate("name", "Alice");
        FieldResult ageResult = IntegerFieldValidator.nonNull().greaterThan(18).validate("age", 25);
        FieldResult cityResult = StringFieldValidator.nonNull().notEmpty().validate("city", ""); // Will fail

        // Combine all results
        ValidationResult overallResult = Validator.allValid(nameResult, ageResult, cityResult);

        if (overallResult.isValid()) {
            System.out.println("All fields are valid!");
        } else {
            System.out.println("Validation failed for the following fields:");
            System.out.println(overallResult.getMessage());
            /* Output:
            Validation failed for the following fields:
            {
              "city": "should not be empty"
            }
            */
        }

        // Example for anyValid (less common)
        ValidationResult anyValidResult = Validator.anyValid(
                IntegerFieldValidator.nonNull().equals(10).validate("score1", 5), // Best practice
                IntegerFieldValidator.nonNull().equals(10).validate("score2", 10)  // Best practice
        );
        System.out.println("\nAny score is 10: " + anyValidResult.isValid()); // Output: true
    }
}