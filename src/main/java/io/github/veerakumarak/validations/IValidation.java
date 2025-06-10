package io.github.veerakumarak.validations;

public interface IValidation<K> {
	Result test(String field, K param);
}
