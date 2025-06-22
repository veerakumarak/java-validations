package io.github.veerakumarak.validations;

import java.util.List;
import java.util.Objects;

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

}
