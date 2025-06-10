package io.github.veerakumarak.validations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Result(boolean valid, String field, List<String> reasons) {

	public static Result ok(String field){
		return new Result(true, field, List.of());
	}
	
	public static Result fail(String field, String message){
		return new Result(false, field, List.of(message));
	}

	public Result and(Result other){
		return new Result(
				valid && other.valid,
				field,
				Stream.concat(this.reasons.stream(), other.reasons.stream()).toList()
		);
	}

	public static List<Result> concat(List<Result> first, List<Result> second) {
		return Stream.concat(first.stream(), second.stream()).toList();
	}

	public static Map<String, List<String>> check(List<Result> results) {
		 return results.stream()
				.filter(result -> !result.valid())
				.collect(Collectors.toMap(Result::field, Result::reasons));
	}
	public static Map<String, List<String>> check(Result... results) {
		return check(Arrays.asList(results));
	}
	public static Map<String, List<String>> check(List<Result> first, List<Result> second) {
		return check(concat(first, second));
	}

}
