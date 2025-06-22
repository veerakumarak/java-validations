package io.github.veerakumarak.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// != null && val > 30
// == null
// (==null) or (val > 30)
public class FieldValidator<K> implements IValidation<K> {

	private final List<Val<K>> vals;

	private record Val<L>(Predicate<L> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
	}

	protected FieldValidator<K> add(Predicate<K> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		this.vals.add(new Val<>(predicate, terminate, errorOn, onErrorMessage));
		return this;
	}

	protected FieldValidator(Predicate<K> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		this.vals = new ArrayList<>();
		this.add(predicate, terminate, errorOn, onErrorMessage);
	}

	@Override
	public FieldResult validate(String field, K param) {
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

		return hasFailures ? FieldResult.fail(field, reasons) : FieldResult.ok(field);
	}

}
