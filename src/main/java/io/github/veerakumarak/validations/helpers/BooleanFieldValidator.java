package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.FieldValidator;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;

import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class BooleanFieldValidator extends FieldValidator<Boolean> {

	private BooleanFieldValidator(Predicate<Boolean> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

    private BooleanFieldValidator(BooleanFieldValidator other, Predicate<Boolean> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
        super(other, predicate, terminate, errorOn, onErrorMessage);
    }

    public static BooleanFieldValidator nonNull() {
		return new BooleanFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "should not be null");
	}

	public static IValidation<Boolean> isNull() {
		return new BooleanFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "should be null");
	}

	public static BooleanFieldValidator optional() {
		return new BooleanFieldValidator(Objects::isNull, Terminate.SUCCESS, ErrorOn.NONE, "");
	}

	public BooleanFieldValidator isTrue() {
		return new BooleanFieldValidator(this, (i) -> i == true, Terminate.NONE, ErrorOn.FAILURE, "should be true");
    }
	
	public BooleanFieldValidator isFalse() {
		return new BooleanFieldValidator(this, (i) -> i == false, Terminate.NONE, ErrorOn.FAILURE, "should be false");
    }

	public BooleanFieldValidator equals(boolean value){
		return new BooleanFieldValidator(this, (i) -> i == value, Terminate.NONE, ErrorOn.FAILURE, format("should be equal to %s", value));
	}

	public BooleanFieldValidator notEquals(boolean value){
		return new BooleanFieldValidator(this, (i) -> i != value, Terminate.NONE, ErrorOn.FAILURE, format("should not be equal to %s", value));
	}

}
