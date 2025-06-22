package io.github.veerakumarak.validations;

public interface IValidation<K> {
	FieldResult validate(String field, K param);
}
