package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class ListValidator<K> extends Validator<List<K>> {

	private ListValidator(Predicate<List<K>> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

	public static <K> ListValidator<K> nonNull() {
		return new ListValidator<>(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "list must be non null");
	}

	public static <K> IValidation<List<K>> isNull() {
		return new ListValidator<>(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "list must be null");
	}

	public ListValidator<K> moreThan(int size){
		return (ListValidator<K>) this.add((l) -> l.size() >= size, Terminate.NONE, ErrorOn.FAILURE, format("must have more than %s size.", size));
	}
	
	public ListValidator<K>lessThan(int size){
		return (ListValidator<K>) this.add((l) -> l.size() <= size, Terminate.NONE, ErrorOn.FAILURE, format("must have less than %s size.", size));
	}
	
	public ListValidator<K> between(int minSize, int maxSize){
		return moreThan(minSize).lessThan(maxSize);
	}

	public ListValidator<K> contains(K item){
		return (ListValidator<K>) this.add(l -> l.contains(item), Terminate.NONE, ErrorOn.FAILURE, format("must contain %s", item));
	}

	public ListValidator<K> containsAll(List<K> list){
		return (ListValidator<K>) this.add(l -> new HashSet<>(l).containsAll(list), Terminate.NONE, ErrorOn.FAILURE, format("must contain all of %s", list));
	}

	public ListValidator<K> notContains(K item){
		return (ListValidator<K>) this.add(l -> !l.contains(item), Terminate.NONE, ErrorOn.FAILURE, format("must contain %s", item));
	}

	public ListValidator<K> equals(List<K> list){
		return (ListValidator<K>) this.add(l -> l.equals(list), Terminate.NONE, ErrorOn.FAILURE, format("must be equal to %s", list));
	}

	public ListValidator<K> notEquals(List<String> list){
		return (ListValidator<K>) this.add(l -> !l.equals(list), Terminate.NONE, ErrorOn.FAILURE, format("must be not equal to %s", list));
	}

	public ListValidator<K> isEmpty(){
		return (ListValidator<K>) this.add(List::isEmpty, Terminate.NONE, ErrorOn.FAILURE, "must be empty");
	}

	public ListValidator<K> notEmpty(){
		return (ListValidator<K>) this.add(l -> !l.isEmpty(), Terminate.NONE, ErrorOn.FAILURE, "must be empty");
	}

}
