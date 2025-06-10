package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.Validator;

import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class StringValidator extends Validator<String> {

	private StringValidator(Predicate<String> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static StringValidator nonNull() {
		return new StringValidator(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "should not be null");
	}

	public static IValidation<String> isNull() {
		return new StringValidator(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "should be null");
	}

	public static StringValidator optional() {
		return new StringValidator(s -> true, Terminate.NONE, ErrorOn.NONE, "");
	}

	public StringValidator minSize(int size){
		return (StringValidator) this.add((s) -> s.length() >= size, Terminate.NONE, ErrorOn.FAILURE, format("should have at least %s characters", size));
	}
	
	public StringValidator maxSize(int size){
		return (StringValidator) this.add((s) -> s.length() <= size, Terminate.NONE, ErrorOn.FAILURE, format("should not exceed %s characters", size));
	}
	
	public StringValidator between(int minSize, int maxSize){
		return minSize(minSize).maxSize(maxSize);
	}
	
	public StringValidator contains(String subString){
		return (StringValidator) this.add((s) -> s.contains(subString), Terminate.NONE, ErrorOn.FAILURE, format("should contain %s", subString));
	}

	public StringValidator equals(String value){
		return (StringValidator) this.add((s) -> s.equals(value), Terminate.NONE, ErrorOn.FAILURE, format("should be equal to %s", value));
	}

	public StringValidator notEquals(String value){
		return (StringValidator) this.add((s) -> !s.equals(value), Terminate.NONE, ErrorOn.FAILURE, format("should not be equal to %s", value));
	}

	public StringValidator isEmpty(){
		return (StringValidator) this.add(String::isEmpty, Terminate.NONE, ErrorOn.FAILURE, "should be empty");
	}

	public StringValidator notEmpty(){
		return (StringValidator) this.add((s) -> !s.isEmpty(), Terminate.NONE, ErrorOn.FAILURE, "should not be empty");
	}

	public StringValidator regex(String regex){
		return (StringValidator) this.add((s) -> s == null || s.matches(regex), Terminate.NONE, ErrorOn.FAILURE, format("must match regex %s", regex));
	}

}
