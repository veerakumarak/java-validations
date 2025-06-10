package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.Validator;

import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class IntegerValidator extends Validator<Integer> {

	private IntegerValidator(Predicate<Integer> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static IntegerValidator nonNull() {
		return new IntegerValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "integer value must be non null");
	}

	public static IValidation<Integer> isNull() {
		return new IntegerValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "must be null");
	}

	public static IntegerValidator optional() {
		return new IntegerValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.NONE, "");
	}

	public IntegerValidator lowerThan(int max){
		return (IntegerValidator) this.add((i) -> i < max, Terminate.NONE, ErrorOn.FAILURE, format("must be lower than %s.", max));
    }
	
	public IntegerValidator greaterThan(int min){
		return (IntegerValidator) this.add((i) -> i > min, Terminate.NONE, ErrorOn.FAILURE, format("must be greather than %s.", min));
	}

	public IntegerValidator between(int min, int max){
		return greaterThan(min).lowerThan(max);
	}

	public IntegerValidator equals(int value){
		return (IntegerValidator) this.add((i) -> i == value, Terminate.NONE, ErrorOn.FAILURE, format("must be equal to %s.", value));
	}

	public IntegerValidator notEquals(int value){
		return (IntegerValidator) this.add((i) -> i != value, Terminate.NONE, ErrorOn.FAILURE, format("must be not equal to %s.", value));
	}

}
