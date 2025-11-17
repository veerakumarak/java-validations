package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.FieldValidator;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.function.Predicate;

public class LocalDateFieldValidator extends FieldValidator<LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private LocalDateFieldValidator(Predicate<LocalDate> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static LocalDateFieldValidator nonNull() {
		return new LocalDateFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "local date value must be non null");
	}

	public static IValidation<LocalDate> isNull() {
		return new LocalDateFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "local date value must be null");
	}

	public static LocalDateFieldValidator optional() {
		return new LocalDateFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.NONE, "");
	}

    public LocalDateFieldValidator before(LocalDate date) {
        return new LocalDateFieldValidator(d -> d.isBefore(date), Terminate.NONE, ErrorOn.FAILURE, "must be before " + date);
    }

    public LocalDateFieldValidator after(LocalDate date) {
        return new LocalDateFieldValidator(d -> d.isAfter(date), Terminate.NONE, ErrorOn.FAILURE, "must be after " + date);
    }

    public LocalDateFieldValidator beforeOrEqual(LocalDate date) {
        return new LocalDateFieldValidator(d -> !d.isAfter(date), Terminate.NONE, ErrorOn.FAILURE, "must be on or before " + date);
    }

    public LocalDateFieldValidator afterOrEqual(LocalDate date) {
        return new LocalDateFieldValidator(d -> !d.isBefore(date), Terminate.NONE, ErrorOn.FAILURE, "must be on or after " + date);
    }

    public LocalDateFieldValidator equal(LocalDate date) {
        return new LocalDateFieldValidator(d -> d.equals(date), Terminate.NONE, ErrorOn.FAILURE, "must be equal " + date);
    }

    public LocalDateFieldValidator notEqual(LocalDate date) {
        return new LocalDateFieldValidator(d -> !d.equals(date), Terminate.NONE, ErrorOn.FAILURE, "must be not equal " + date);
    }

    private static boolean isValidFormat(String dateString) {
        try {
            LocalDate.parse(dateString, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDateFieldValidator validFormat() {
        return new LocalDateFieldValidator(d -> isValidFormat(d.toString()), Terminate.NONE, ErrorOn.FAILURE, "must follow yyyy-MM-dd format");
    }

}
