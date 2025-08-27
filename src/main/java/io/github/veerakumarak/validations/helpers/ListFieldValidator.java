package io.github.veerakumarak.validations.helpers;

import io.github.veerakumarak.validations.ErrorOn;
import io.github.veerakumarak.validations.IValidation;
import io.github.veerakumarak.validations.Terminate;
import io.github.veerakumarak.validations.FieldValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class ListFieldValidator<K> extends FieldValidator<List<K>> {

	private ListFieldValidator(Predicate<List<K>> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		super(predicate, terminate, errorOn, onErrorMessage);
	}

    private ListFieldValidator(ListFieldValidator<K> other, Predicate<List<K>> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
        super(other, predicate, terminate, errorOn, onErrorMessage);
    }

    public static <K> ListFieldValidator<K> nonNull() {
		return new ListFieldValidator<>(Objects::nonNull, Terminate.FAILURE, ErrorOn.FAILURE, "list must be non null");
	}

	public static <K> IValidation<List<K>> isNull() {
		return new ListFieldValidator<>(Objects::isNull, Terminate.FAILURE, ErrorOn.FAILURE, "list must be null");
	}

	public ListFieldValidator<K> moreThan(int size){
		return new ListFieldValidator<K>(this, (l) -> l.size() >= size, Terminate.NONE, ErrorOn.FAILURE, format("must have more than %s size", size));
	}
	
	public ListFieldValidator<K> lessThan(int size){
		return new ListFieldValidator<K>(this, (l) -> l.size() <= size, Terminate.NONE, ErrorOn.FAILURE, format("must have less than %s size", size));
	}
	
	public ListFieldValidator<K> between(int minSize, int maxSize){
		return moreThan(minSize).lessThan(maxSize);
	}

	public ListFieldValidator<K> contains(K item){
		return new ListFieldValidator<K>(this, l -> l.contains(item), Terminate.NONE, ErrorOn.FAILURE, format("must contain %s", item));
	}

	public ListFieldValidator<K> containsAll(List<K> list){
		return new ListFieldValidator<K>(this, l -> new HashSet<>(l).containsAll(list), Terminate.NONE, ErrorOn.FAILURE, format("must contain all of %s", list));
	}

	public ListFieldValidator<K> notContains(K item){
		return new ListFieldValidator<K>(this, l -> !l.contains(item), Terminate.NONE, ErrorOn.FAILURE, format("must contain %s", item));
	}

	public ListFieldValidator<K> equals(List<K> list){
		return new ListFieldValidator<K>(this, l -> l.equals(list), Terminate.NONE, ErrorOn.FAILURE, format("must be equal to %s", list));
	}

	public ListFieldValidator<K> notEquals(List<K> list){
		return new ListFieldValidator<K>(this, l -> !l.equals(list), Terminate.NONE, ErrorOn.FAILURE, format("must be not equal to %s", list));
	}

	public ListFieldValidator<K> isEmpty(){
		return new ListFieldValidator<K>(this, List::isEmpty, Terminate.NONE, ErrorOn.FAILURE, "must be empty");
	}

	public ListFieldValidator<K> notEmpty(){
		return new ListFieldValidator<K>(this, l -> !l.isEmpty(), Terminate.NONE, ErrorOn.FAILURE, "must be empty");
	}

}
