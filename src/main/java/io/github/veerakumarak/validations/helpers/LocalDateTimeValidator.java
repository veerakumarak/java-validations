package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.Validator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

public class LocalDateTimeValidator extends Validator<LocalDateTime> {

	private LocalDateTimeValidator(Predicate<LocalDateTime> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static LocalDateTimeValidator nonNull() {
		return new LocalDateTimeValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "date value must be non null");
	}

	public static IValidation<LocalDateTime> isNull() {
		return new LocalDateTimeValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "date value must be null");
	}

	public static LocalDateTimeValidator optional() {
		return new LocalDateTimeValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.NONE, "");
	}

}
