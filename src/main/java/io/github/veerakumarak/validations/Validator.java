package io.github.veerakumarak.validations;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Validator {

	private Validator() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static ValidationResult allValid(List<FieldResult> fieldResults) {
		Objects.requireNonNull(fieldResults, "Results list cannot be null.");
		return ValidationResult.of(fieldResults);
	}

	public static ValidationResult allValid(FieldResult... fieldResults) {
		return allValid(List.of(fieldResults));
	}

    public static ValidationResult allValid(List<FieldResult> first, List<FieldResult> second) {
        return allValid(Stream.concat(first.stream(), second.stream()).toList());
    }

    public static ValidationResult allValid(FieldResult first, List<FieldResult> second) {
        return allValid(List.of(first), second);
    }

    public static ValidationResult anyValid(List<FieldResult> fieldResults) {
		Objects.requireNonNull(fieldResults, "Results list cannot be null.");
		if (!fieldResults.isEmpty() && fieldResults.stream().anyMatch(FieldResult::valid)) {
			return ValidationResult.of(List.of());
		}
		return ValidationResult.of(fieldResults);
	}

	public static ValidationResult anyValid(FieldResult... fieldResults) {
		return anyValid(List.of(fieldResults));
	}

    public static ValidationResult anyValid(List<FieldResult> first, List<FieldResult> second) {
        return anyValid(Stream.concat(first.stream(), second.stream()).toList());
    }

    public static ValidationResult anyValid(FieldResult first, List<FieldResult> second) {
        return anyValid(List.of(first), second);
    }


}
