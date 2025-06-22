package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.FieldValidator;

import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class StringFieldValidator extends FieldValidator<String> {

	private StringFieldValidator(Predicate<String> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static StringFieldValidator nonNull() {
		return new StringFieldValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "should not be null");
	}

	public static IValidation<String> isNull() {
		return new StringFieldValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "should be null");
	}

	public static StringFieldValidator optional() {
		return new StringFieldValidator(Objects::isNull, Terminate.SUCCESS, ErrorOn.NONE, "");
	}

	public StringFieldValidator minLength(int size){
		return (StringFieldValidator) this.add((s) -> s.length() >= size, Terminate.NONE, ErrorOn.FAILURE, format("should have at least %s characters", size));
	}
	
	public StringFieldValidator maxLength(int size){
		return (StringFieldValidator) this.add((s) -> s.length() <= size, Terminate.NONE, ErrorOn.FAILURE, format("should not exceed %s characters", size));
	}
	
	public StringFieldValidator lengthBetween(int minSize, int maxSize){
		return minLength(minSize).maxLength(maxSize);
	}
	
	public StringFieldValidator contains(String subString){
		return (StringFieldValidator) this.add((s) -> s.contains(subString), Terminate.NONE, ErrorOn.FAILURE, format("should contain %s", subString));
	}

	public StringFieldValidator equals(String value){
		return (StringFieldValidator) this.add((s) -> s.equals(value), Terminate.NONE, ErrorOn.FAILURE, format("should be equal to %s", value));
	}

	public StringFieldValidator notEquals(String value){
		return (StringFieldValidator) this.add((s) -> !s.equals(value), Terminate.NONE, ErrorOn.FAILURE, format("should not be equal to %s", value));
	}

	public StringFieldValidator isEmpty(){
		return (StringFieldValidator) this.add(String::isEmpty, Terminate.NONE, ErrorOn.FAILURE, "should be empty");
	}

	public StringFieldValidator notEmpty(){
		return (StringFieldValidator) this.add((s) -> !s.isEmpty(), Terminate.NONE, ErrorOn.FAILURE, "should not be empty");
	}

	public StringFieldValidator matchesRegex(String regex){
		return (StringFieldValidator) this.add((s) -> s == null || s.matches(regex), Terminate.NONE, ErrorOn.FAILURE, format("must match regex %s", regex));
	}

}
