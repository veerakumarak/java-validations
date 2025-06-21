
# java-validations



A lightweight and flexible Java library for building expressive and composable validation rules. Designed with a functional programming inspired approach to error handling, providing clear and structured validation results.

## Table of Contents

* [Features](https://www.google.com/search?q=%23features)
* [Installation](https://www.google.com/search?q=%23installation)
* [Core Concepts](https://www.google.com/search?q=%23core-concepts)
    * [Error Handling with `Failure` and `Reasons`](https://www.google.com/search?q=%23error-handling-with-failure-and-reasons)
    * [Building Validation Rules with `FieldValidator`](https://www.google.com/search?q=%23building-validation-rules-with-fieldvalidator)
    * [Executing Validations with `Validator`](https://www.google.com/search?q=%23executing-validations-with-validator)
* [Usage](https://www.google.com/search?q=%23usage)
    * [Basic Field Validation](https://www.google.com/search?q=%23basic-field-validation)
    * [Composing Complex Rules](https://www.google.com/search?q=%23composing-complex-rules)
    * [Handling Validation Results](https://www.google.com/search?q=%23handling-validation-results)
    * [Custom Validation Rules](https://www.google.com/search?q=%23custom-validation-rules)
* [Project Structure](https://www.google.com/search?q=%23project-structure)
* [Contributing](https://www.google.com/search?q=%23contributing)
* [License](https://www.google.com/search?q=%23license)

## Features

* **Fluent API:** Easily chain validation rules for readability and expressiveness.
* **Structured Error Handling:** Collects all validation failures by field into a `Reasons` object.
* **Type-Safe Validators:** Dedicated field validators (e.g., `IntegerFieldValidator`) for common types.
* **Composable Rules:** Build complex validation logic from smaller, reusable `IRule` units.
* **Functional Programming Inspired:** Leverages concepts like immutability and clear separation of concerns for robust error management.
* **Lightweight:** Minimal dependencies.

## Installation

This library is available on [Maven Central](https://www.google.com/search?q=https://search.maven.org/search%3Fq%3Dg:io.github.veerakumarak.validations).

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.veerakumarak</groupId>
    <artifactId>validations</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle`:

```gradle
implementation 'io.github.veerakumarak:validations:2.0.0'
```

## Core Concepts

### Error Handling with `Failure` and `Reasons`

* `Failure`: Your base class for representing any kind of failure.
* `InvalidRequest`: A specific `Failure` subclass designed to encapsulate validation failures, holding a `Map<String, List<String>>` of reasons.
* `Reasons` (record): An immutable data carrier that aggregates validation messages. It maps field names to a list of error strings. It provides methods like `isValid()`, `orThrow()`, and `toError()` for convenient error management.

### Building Validation Rules with `FieldValidator`

* `FieldValidator<T>` (abstract base class): Provides the foundation for building fluent validation chains for a specific type `T`.
* Specialized `FieldValidator`s (e.g., `IntegerFieldValidator`, `StringFieldValidator`, etc.): Extend `FieldValidator` to offer type-specific validation rules (e.g., `greaterThan`, `lowerThan`, `nonEmpty`, `matchesRegex`). These are designed to be chained.

### Executing Validations with `Validator`

* `Validator` (utility class): The main entry point for executing a collection of `IRule`s. It collects all `FieldResult`s and aggregates them into a `Reasons` object. It offers various overloads to validate single rules, lists of rules, or combinations of lists.

## Usage

### Basic Field Validation

```java
import io.github.veerakumarak.validations.Validator;
import io.github.veerakumarak.validations.Reasons;
import io.github.veerakumarak.validations.helpers.IntegerFieldValidator;
import io.github.veerakumarak.validations.FieldResult; // Assuming FieldResult is available

public class BasicValidationExample {

    public static void main(String[] args) {
        // Validate a single integer field
        IntegerFieldValidator ageValidator = IntegerFieldValidator.nonNull()
                                                                 .greaterThan(0)
                                                                 .lowerThan(150);

        FieldResult ageResult1 = ageValidator.validate("age", 25); // Valid
        FieldResult ageResult2 = ageValidator.validate("age", -5); // Invalid: not greater than 0
        FieldResult ageResult3 = ageValidator.validate("age", 200); // Invalid: not lower than 150
        FieldResult ageResult4 = ageValidator.validate("age", null); // Invalid: non-null check fails

        System.out.println("Age 25: " + ageResult1);
        System.out.println("Age -5: " + ageResult2);
        System.out.println("Age 200: " + ageResult3);
        System.out.println("Age null: " + ageResult4);

        // Use the static Validator to collect results
        Reasons reasons = Validator.validate(ageResult1, ageResult2, ageResult3, ageResult4);

        if (!reasons.isValid()) {
            System.out.println("\nValidation Errors for the batch:");
            System.out.println(reasons.getMessage()); // Or reasons.reasons() for the raw map
            reasons.orThrow(); // Throws an InvalidRequest exception
        }
    }
}
```

### Composing Complex Rules

You can create reusable `IRule` instances and combine them.

```java
import io.github.veerakumarak.validations.IRule;
import io.github.veerakumarak.validations.Validator;
import io.github.veerakumarak.validations.Reasons;
import io.github.veerakumarak.validations.helpers.IntegerFieldValidator;
import io.github.veerakumarak.validations.helpers.StringFieldValidator;

public class ComplexValidationExample {

    record User(String username, String email, Integer age) {}

    public static void main(String[] args) {
        User user1 = new User("john.doe", "john@example.com", 30);
        User user2 = new User("jd", "invalid-email", -5);

        // Define reusable rules
        IRule usernameRule = StringFieldValidator.nonNull()
                                                 .nonEmpty()
                                                 .minLength(5)
                                                 .maxLength(20)
                                                 .validate("username", user1.username());

        IRule emailRule = StringFieldValidator.nonNull()
                                              .matchesRegex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                                              .validate("email", user1.email());

        IRule ageRule = IntegerFieldValidator.optional() // age can be null but if present, must be > 0 and < 150
                                             .greaterThan(0)
                                             .lowerThan(150)
                                             .validate("age", user1.age());

        // Validate a valid user
        Reasons validUserReasons = Validator.validate(usernameRule, emailRule, ageRule);
        System.out.println("Valid User Validation Status: " + validUserReasons.isValid()); // true
        System.out.println(validUserReasons.getMessage()); // No failures detected.

        // Validate an invalid user
        IRule usernameRule2 = StringFieldValidator.minLength(5).validate("username", user2.username()); // "jd" too short
        IRule emailRule2 = StringFieldValidator.matchesRegex("...").validate("email", user2.email()); // "invalid-email"
        IRule ageRule2 = IntegerFieldValidator.greaterThan(0).validate("age", user2.age()); // -5 not > 0

        Reasons invalidUserReasons = Validator.validate(usernameRule2, emailRule2, ageRule2);
        System.out.println("\nInvalid User Validation Status: " + invalidUserReasons.isValid()); // false
        System.out.println("Invalid User Reasons:\n" + invalidUserReasons.getMessage());
        // Output will show details for username, email, and age.

        // Example: Combining lists of rules
        List<IRule> commonRules = List.of(usernameRule);
        List<IRule> specificRules = List.of(emailRule, ageRule);

        Reasons combinedReasons = Validator.validate(commonRules, specificRules);
        System.out.println("\nCombined Validation Status: " + combinedReasons.isValid());
    }
}
```

### Handling Validation Results

```java
import io.github.veerakumarak.validations.Reasons;
import io.github.veerakumarak.fp.Failure; // Using Failure now

public class ResultHandlingExample {
    public static Reasons performValidation(int value) {
        // ... (your validation logic here)
        if (value <= 0) {
            return Reasons.of(new FieldResult("value", List.of("must be positive")));
        }
        return Reasons.of(new FieldResult("value", List.of("is valid"), true)); // Or an empty Reasons
    }

    public static void main(String[] args) {
        Reasons reasons = performValidation(-10);

        if (!reasons.isValid()) {
            // Option 1: Access the map of reasons directly
            System.out.println("Raw reasons map: " + reasons.reasons());

            // Option 2: Get a human-readable message
            System.out.println("Formatted message:\n" + reasons.getMessage());

            // Option 3: Convert to an Failure object (e.g., for consistent API responses)
            Failure validationFailure = reasons.toError(); // toError returns Failure
            System.out.println("Converted to Failure: " + validationFailure.getMessage());
            // You can then cast to InvalidRequest if you need to access its specific fields
            // InvalidRequest ir = (InvalidRequest) validationFailure;

            // Option 4: Throw an exception immediately
            try {
                reasons.orThrow();
            } catch (io.github.veerakumarak.fp.errors.InvalidRequest e) {
                System.out.println("Caught InvalidRequest exception: " + e.getMessage());
            }
        } else {
            System.out.println("Validation successful!");
        }
    }
}
```

### Custom Validation Rules

You can create your own custom `FieldValidator`s or `IRule` implementations for complex business logic.

```java
import io.github.veerakumarak.validations.FieldValidator;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.ErrorOn; // ErrorOn is still fine as it refers to a concept, not the class
import java.util.function.Predicate;
import static java.lang.String.format;

// Example: A validator for ISBN numbers
public class IsbnFieldValidator extends FieldValidator<String> {

    private IsbnFieldValidator(Predicate<String> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
        super(predicate, terminate, errorOn, onErrorMessage);
    }

    public static IsbnFieldValidator nonNull() {
        return new IsbnFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "ISBN must be non-null");
    }

    public IsbnFieldValidator isValidIsbn13() {
        // Simplified ISBN-13 validation logic (implement actual checksum)
        return (IsbnFieldValidator) this.add(
            s -> s != null && s.matches("^(\\d{3}-){2}\\d{3}(\\d|X)$"), // Simplified regex
            Terminate.NONE, ErrorOn.FAILURE, "must be a valid ISBN-13 format"
        );
    }

    // You would then add more methods for ISBN-10, proper checksum validation etc.
}

// How to use:
// IRule isbnRule = IsbnFieldValidator.nonNull().isValidIsbn13().validate("bookIsbn", "978-3-16-148410-0");
// Reasons reasons = Validator.validate(isbnRule);
```

## Project Structure

* `io.github.veerakumarak.fp`: Contains foundational functional programming inspired types like `Failure` and `Result` (if applicable), and specific error types like `InvalidRequest`.
* `io.github.veerakumarak.validations`: Core validation interfaces (`IRule`), abstract classes (`FieldValidator`), and the central `Validator` and `Reasons` classes.
* `io.github.veerakumarak.validations.helpers`: Concrete implementations of `FieldValidator` for common types like `IntegerFieldValidator`, `StringFieldValidator`, etc.

## Contributing

Contributions are welcome\! If you have suggestions for improvements, new features, or bug fixes, please open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](https://www.google.com/search?q=LICENSE) file for details.

