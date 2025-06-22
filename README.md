# Java Validation Library

A lightweight, fluent, and extensible validation library for Java, designed to simplify data validation with clear error reporting and flexible control over validation flows.

## Table of Contents

* [Features](https://www.google.com/search?q=%23features)
* [Core Concepts](https://www.google.com/search?q=%23core-concepts)
* [Installation](https://www.google.com/search?q=%23installation)
* [Usage](https://www.google.com/search?q=%23usage)
  * [Best Practice: Handling Nullability](https://www.google.com/search?q=%23best-practice-handling-nullability)
  * [Basic Validation](https://www.google.com/search?q=%23basic-validation)
  * [Chaining Rules](https://www.google.com/search?q=%23chaining-rules)
  * [Optional Fields](https://www.google.com/search?q=%23optional-fields)
  * [Combining Multiple Field Validations](https://www.google.com/search?q=%23combining-multiple-field-validations)
  * [Handling Validation Results](https://www.google.com/search?q=%23handling-validation-results)
* [Helper Validators](https://www.google.com/search?q=%23helper-validators)
  * [StringFieldValidator](https://www.google.com/search?q=%23stringfieldvalidator)
  * [IntegerFieldValidator](https://www.google.com/search?q=%23integerfieldvalidator)
  * [ListFieldValidator](https://www.google.com/search?q=%23listfieldvalidator)
* [Extensibility](https://www.google.com/search?q=%23extensibility)
* [Contributing](https://www.google.com/search?q=%23contributing)
* [License](https://www.google.com/search?q=%23license)

## Features

* **Fluent API:** Build complex validation rules with readable and chainable method calls.
* **Type-Safe Validation:** Generic `FieldValidator<K>` and type-specific helpers for `String`, `Integer`, and `List`.
* **Configurable Flow Control:** Fine-grained control over when to terminate validation (`Terminate`) and when to record errors (`ErrorOn`).
* **Detailed Error Reporting:** Collects multiple validation failure reasons per field.
* **Aggregated Results:** Combine multiple field validations into a single `ValidationResult`.
* **Extensible:** Easily add custom validation rules or create new field validator types.
* **Functional Integration:** Leverages Java's `Predicate` for validation logic and integrates with common functional error handling patterns.

## Core Concepts

* **`IValidation<K>`:** The fundamental interface for any validation rule, defining the `validate(String field, K param)` method.
* **`FieldValidator<K>`:** The abstract base class that implements `IValidation<K>`. It allows chaining multiple validation predicates (`Predicate<K>`) for a single field.
  * **`Terminate`:** An enum controlling when to stop processing further predicates for a field (e.g., `SUCCESS` or `FAILURE`).
  * **`ErrorOn`:** An enum controlling when to add an error message to the results (e.g., `FAILURE` or `NONE`).
* **`FieldResult`:** (Inferred from usage) Represents the result of validating a single field, indicating success/failure and associated reasons.
* **`ValidationResult`:** Aggregates one or more `FieldResult` objects, providing an overall validity status and a map of all failure reasons grouped by field. It also offers utility methods for checking validity, retrieving messages, or throwing exceptions.
* **`Validator`:** A utility class for combining multiple `FieldResult` instances into a `ValidationResult` using `allValid()` or `anyValid()` logic.

## Installation

This library is designed as a set of Java classes. To use it in your project, copy the provided source code into your project's structure.

If this were a public library, you would typically add it as a dependency in your `pom.xml` (Maven) or `build.gradle` (Gradle) file:

**Maven:**

```xml
<dependency>
    <groupId>io.github.veerakumarak</groupId>
    <artifactId>validations</artifactId>
    <version>3.0.0</version>
</dependency>
```

**Gradle:**

```gradle
implementation 'io.github.veerakumarak:validations:3.0.0' // Use the actual version
```

## Usage

### Best Practice: Handling Nullability

Every validation chain for a field should start by explicitly defining its nullability behavior using one of the following methods:

* **`nonNull()`**: If the field must *not* be `null`. Subsequent validations will run only if the field is not `null`.
* **`isNull()`**: If the field must *be* `null`. This is less common but useful for specific cases.
* **`optional()`**: If the field *can* be `null`. If the field is `null`, it's considered valid, and no further chained validations for that field are executed. If it's *not* `null`, subsequent chained validations will proceed.

This practice prevents `NullPointerException`s and makes your validation rules clear and robust.

### Basic Validation

Start by creating a field validator for the desired type and apply a rule. Always begin with a nullability check.

```java
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
```

### Chaining Rules

Combine multiple validation rules for a single field using the fluent API, always starting with a nullability check.

```java
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
```

### Optional Fields

Use the `optional()` method for fields that can be `null`. If the field is `null`, validation for that field passes and stops. If it's not `null`, subsequent chained validations will apply.

```java
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
```

### Combining Multiple Field Validations

Use the `Validator` utility class to aggregate `FieldResult` instances. Remember to apply nullability checks for each field.

```java
import io.github.veerakumarak.validations.FieldResult;
import io.github.veerakumarak.validations.ValidationResult;
import io.github.veerakumarak.validations.Validator;
import io.github.veerakumarak.validations.helpers.IntegerFieldValidator;
import io.github.veerakumarak.validations.helpers.StringFieldValidator;

import java.util.List;

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
```

### Handling Validation Results

The `ValidationResult` provides various ways to interact with the aggregated validation outcome.

```java
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
```

## Helper Validators

The library provides pre-built fluent validators for common data types:

### `StringFieldValidator`

Provides methods for validating `String` objects.

* `nonNull()`: Ensures the string is not `null`. **(Recommended starting point)**
* `isNull()`: Ensures the string is `null`. **(Alternative starting point)**
* `optional()`: Allows `null` strings; if `null`, subsequent validations are skipped for this field. **(Alternative starting point)**
* `minLength(int size)`: Checks minimum length.
* `maxLength(int size)`: Checks maximum length.
* `lengthBetween(int minSize, int maxSize)`: Checks length within a range.
* `contains(String subString)`: Checks if the string contains a substring.
* `equals(String value)`: Checks for equality.
* `notEquals(String value)`: Checks for inequality.
* `isEmpty()`: Checks if the string is empty.
* `notEmpty()`: Checks if the string is not empty.
* `matchesRegex(String regex)`: Checks if the string matches a regular expression (handles `null` gracefully).

### `IntegerFieldValidator`

Provides methods for validating `Integer` objects.

* `nonNull()`: Ensures the integer is not `null`. **(Recommended starting point)**
* `isNull()`: Ensures the integer is `null`. **(Alternative starting point)**
* `optional()`: Allows `null` integers; if `null`, subsequent validations are skipped for this field. **(Alternative starting point)**
* `lowerThan(int max)`: Checks if the integer is less than `max`.
* `greaterThan(int min)`: Checks if the integer is greater than `min`.
* `between(int min, int max)`: Checks if the integer is within a range.
* `equals(int value)`: Checks for equality.
* `notEquals(int value)`: Checks for inequality.

### `ListFieldValidator`

Provides methods for validating `List<K>` objects.

* `nonNull()`: Ensures the list is not `null`. **(Recommended starting point)**
* `isNull()`: Ensures the list is `null`. **(Alternative starting point)**
* `moreThan(int size)`: Checks if the list has more than or equal to `size` elements.
* `lessThan(int size)`: Checks if the list has less than or equal to `size` elements.
* `between(int minSize, int maxSize)`: Checks list size within a range.
* `contains(K item)`: Checks if the list contains a specific item.
* `containsAll(List<K> list)`: Checks if the list contains all items from another list.
* `notContains(K item)`: Checks if the list does not contain a specific item.
* `equals(List<K> list)`: Checks for list equality.
* `notEquals(List<String> list)`: Checks for list inequality (note: currently uses `List<String>`, should ideally be `List<K>`).
* `isEmpty()`: Checks if the list is empty.
* `notEmpty()`: Checks if the list is not empty.

## Extensibility

You can easily extend this library by:

1.  **Creating custom `FieldValidator` implementations:** Inherit from `FieldValidator<K>` to create validators for new types (e.g., `DateFieldValidator`, `BigDecimalFieldValidator`).
2.  **Adding custom validation predicates:** Use the `add()` method within your custom validators to incorporate any `Predicate<K>` logic.

<!-- end list -->

```java
import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.FieldValidator;

import java.time.LocalDate;
import java.util.Objects; // Added for nonNull/isNull/optional
import java.util.function.Predicate;

import static java.lang.String.format;

public class DateFieldValidator extends FieldValidator<LocalDate> {

    private DateFieldValidator(Predicate<LocalDate> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
        super(predicate, terminate, errorOn, onErrorMessage);
    }

    public static DateFieldValidator nonNull() {
        return new DateFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "date should not be null");
    }

    public static DateFieldValidator isNull() {
        return new DateFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "date should be null");
    }

    public static DateFieldValidator optional() {
        return new DateFieldValidator(Objects::isNull, Terminate.SUCCESS, ErrorOn.NONE, "");
    }

    public DateFieldValidator isFuture(){
        return (DateFieldValidator) this.add(d -> d.isAfter(LocalDate.now()), Terminate.NONE, ErrorOn.FAILURE, "date must be in the future");
    }

    public DateFieldValidator isPast(){
        return (DateFieldValidator) this.add(d -> d.isBefore(LocalDate.now()), Terminate.NONE, ErrorOn.FAILURE, "date must be in the past");
    }

    // Add more date-specific validations here...
}
```

## Contributing

Contributions are welcome\! If you have suggestions for improvements, new features, or bug fixes, please feel free to open an issue or submit a pull request.

## License

This library is released under the MIT License. See the `LICENSE` file for more details.

-----