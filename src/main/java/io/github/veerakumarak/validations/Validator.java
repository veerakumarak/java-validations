package io.github.veerakumarak.validations;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class Validator {

	private Validator() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static Reasons validate(List<IRule> rules) {
		Objects.requireNonNull(rules, "Rules list cannot be null.");
		return Reasons.of(rules.stream().map(IRule::validate).toList());
	}

	public static Reasons validate(IRule... rules) {
		Objects.requireNonNull(rules, "Rules list cannot be null.");
		return validate(List.of(rules));
	}

	public static Reasons validate(List<IRule> first, IRule... second) {
		Objects.requireNonNull(first, "First Rules list cannot be null.");
		Objects.requireNonNull(second, "Second Rules list cannot be null.");
		return validate(first, List.of(second));
	}

	public static Reasons validate(List<IRule> first, List<IRule> second) {
		Objects.requireNonNull(first, "First Rules list cannot be null.");
		Objects.requireNonNull(second, "Second Rules list cannot be null.");
		return validate(Stream.concat(first.stream(), second.stream()).toList());
	}

}
