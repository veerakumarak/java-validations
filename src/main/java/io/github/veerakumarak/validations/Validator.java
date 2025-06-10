package io.github.veerakumarak.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// != null && val > 30
// == null
// (==null) or (val > 30)
public class Validator<K> implements IValidation<K> {

	private final List<Val<K>> vals;

	private record Val<L>(Predicate<L> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
	}

	protected Validator<K> add(Predicate<K> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		this.vals.add(new Val<>(predicate, terminate, errorOn, onErrorMessage));
		return this;
	}

	protected Validator(Predicate<K> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		this.vals = new ArrayList<>();
		this.add(predicate, terminate, errorOn, onErrorMessage);
	}

	@Override
	public Result test(String field, K param) {
		List<String> reasons = new ArrayList<>();
		boolean hasFailures = false;

		for (Val<K> val : this.vals) {
			boolean testPassed = val.predicate.test(param);

			if (!testPassed) {
				hasFailures = true;
				if (val.errorOn == ErrorOn.FAILURE) {
					reasons.add(val.onErrorMessage);
				}
				if (val.terminate == Terminate.FAILURE) {
					break;
				}
			} else {
				if (val.terminate == Terminate.SUCCESS) {
					break;
				}
			}
		}

		Result result;
		if (hasFailures) {
			result = new Result(false, field, reasons);
		} else {
			result = Result.ok(field);
		}

		return result;
	}

}
