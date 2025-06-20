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

	public static IntegerFieldValidator nonNull() {
		return new IntegerFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "integer value must be non null");
	}

	public static IValidation<Integer> isNull() {
		return new IntegerFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "must be null");
	}

	public static IntegerFieldValidator optional() {
		return new IntegerFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.NONE, "");
	}

	public IntegerFieldValidator lowerThan(int max){
		return (IntegerFieldValidator) this.add((i) -> i < max, Terminate.NONE, ErrorOn.FAILURE, format("must be lower than %s.", max));
    }
	
	public IntegerFieldValidator greaterThan(int min){
		return (IntegerFieldValidator) this.add((i) -> i > min, Terminate.NONE, ErrorOn.FAILURE, format("must be greather than %s.", min));
	}

	public IntegerFieldValidator between(int min, int max){
		return greaterThan(min).lowerThan(max);
	}

	public IntegerFieldValidator equals(int value){
		return (IntegerFieldValidator) this.add((i) -> i == value, Terminate.NONE, ErrorOn.FAILURE, format("must be equal to %s.", value));
	}

	public IntegerFieldValidator notEquals(int value){
		return (IntegerFieldValidator) this.add((i) -> i != value, Terminate.NONE, ErrorOn.FAILURE, format("must be not equal to %s.", value));
	}

}
