package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.FieldValidator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

public class LocalDateTimeFieldValidator extends FieldValidator<LocalDateTime> {

	private LocalDateTimeFieldValidator(Predicate<LocalDateTime> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static LocalDateTimeFieldValidator nonNull() {
		return new LocalDateTimeFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "date value must be non null");
	}

	public static IValidation<LocalDateTime> isNull() {
		return new LocalDateTimeFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "date value must be null");
	}

	public static LocalDateTimeFieldValidator optional() {
		return new LocalDateTimeFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.NONE, "");
	}

}
