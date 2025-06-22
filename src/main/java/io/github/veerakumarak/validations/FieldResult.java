package io.github.veerakumarak.validations;

import java.util.List;

public record FieldResult(boolean valid, String field, List<String> reasons) {

	public static FieldResult ok(String field){
		return new FieldResult(true, field, List.of());
	}
	
	public static FieldResult fail(String field, String message){
		return new FieldResult(false, field, List.of(message));
	}

	public static FieldResult fail(String field, List<String> reasons){
		return new FieldResult(false, field, reasons);
	}

}
