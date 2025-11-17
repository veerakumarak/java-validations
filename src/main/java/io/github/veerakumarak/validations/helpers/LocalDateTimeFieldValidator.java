package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.FieldValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

public class LocalDateTimeFieldValidator extends FieldValidator<LocalDateTime> {

	private LocalDateTimeFieldValidator(Predicate<LocalDateTime> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static LocalDateTimeFieldValidator nonNull() {
		return new LocalDateTimeFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "local date time value must be non null");
	}

	public static IValidation<LocalDateTime> isNull() {
		return new LocalDateTimeFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "local date time value must be null");
	}

	public static LocalDateTimeFieldValidator optional() {
		return new LocalDateTimeFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.NONE, "");
	}

    public LocalDateTimeFieldValidator before(LocalDateTime dateTime) {
        return new LocalDateTimeFieldValidator(d -> d.isBefore(dateTime), Terminate.NONE, ErrorOn.FAILURE, "must be before " + dateTime);
    }

    public LocalDateTimeFieldValidator after(LocalDateTime dateTime) {
        return new LocalDateTimeFieldValidator(d -> d.isAfter(dateTime), Terminate.NONE, ErrorOn.FAILURE, "must be after " + dateTime);
    }

    public LocalDateTimeFieldValidator beforeOrEqual(LocalDateTime dateTime) {
        return new LocalDateTimeFieldValidator(d -> !d.isAfter(dateTime), Terminate.NONE, ErrorOn.FAILURE, "must be on or before " + dateTime);
    }

    public LocalDateTimeFieldValidator afterOrEqual(LocalDateTime dateTime) {
        return new LocalDateTimeFieldValidator(d -> !d.isBefore(dateTime), Terminate.NONE, ErrorOn.FAILURE, "must be on or after " + dateTime);
    }

    public LocalDateTimeFieldValidator equal(LocalDateTime dateTime) {
        return new LocalDateTimeFieldValidator(d -> d.equals(dateTime), Terminate.NONE, ErrorOn.FAILURE, "must be equal " + dateTime);
    }

    public LocalDateTimeFieldValidator notEqual(LocalDateTime dateTime) {
        return new LocalDateTimeFieldValidator(d -> !d.equals(dateTime), Terminate.NONE, ErrorOn.FAILURE, "must be not equal " + dateTime);
    }

}
