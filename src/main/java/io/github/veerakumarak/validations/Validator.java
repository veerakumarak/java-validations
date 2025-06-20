package io.github.veerakumarak.validations;

import java.util.List;
import java.util.stream.Stream;

public class Validator {

	public static Reasons validate(List<IRule> rules) {
		return Reasons.of(rules.stream().map(IRule::validate).toList());
	}

	public static Reasons validate(IRule... rules) {
		return validate(List.of(rules));
	}

	public static Reasons validate(List<IRule> first, IRule... second) {
		return validate(first, List.of(second));
	}

	public static Reasons validate(List<IRule> first, List<IRule> second) {
		return validate(Stream.concat(first.stream(), second.stream()).toList());
	}

}
