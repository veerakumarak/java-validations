package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.FieldValidator;

import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class IntegerFieldValidator extends FieldValidator<Integer> {

	private IntegerFieldValidator(Predicate<Integer> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

    private IntegerFieldValidator(IntegerFieldValidator other, Predicate<Integer> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
        super(other, predicate, terminate, errorOn, onErrorMessage);
    }

    public static IntegerFieldValidator nonNull() {
		return new IntegerFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "should not be null");
	}

	public static IValidation<Integer> isNull() {
		return new IntegerFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "should be null");
	}

	public static IntegerFieldValidator optional() {
		return new IntegerFieldValidator(Objects::isNull, Terminate.SUCCESS, ErrorOn.NONE, "");
	}

	public IntegerFieldValidator lowerThan(int max){
		return new IntegerFieldValidator(this, (i) -> i < max, Terminate.NONE, ErrorOn.FAILURE, format("should be lower than %s", max));
    }
	
	public IntegerFieldValidator greaterThan(int min){
		return new IntegerFieldValidator(this, (i) -> i > min, Terminate.NONE, ErrorOn.FAILURE, format("should be greater than %s", min));
	}

	public IntegerFieldValidator between(int min, int max){
		return greaterThan(min).lowerThan(max);
	}

	public IntegerFieldValidator equals(int value){
		return new IntegerFieldValidator(this, (i) -> i == value, Terminate.NONE, ErrorOn.FAILURE, format("should be equal to %s", value));
	}

	public IntegerFieldValidator notEquals(int value){
		return new IntegerFieldValidator(this, (i) -> i != value, Terminate.NONE, ErrorOn.FAILURE, format("should not be equal to %s", value));
	}

}
