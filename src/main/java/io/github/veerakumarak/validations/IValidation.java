package io.github.veerakumarak.validations;

public interface IValidation<K> {
	IRule check(String field, K param);
}
