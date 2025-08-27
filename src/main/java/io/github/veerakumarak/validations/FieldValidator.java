package io.github.veerakumarak.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

// != null && val > 30
// == null
// (==null) or (val > 30)
public class FieldValidator<K> implements IValidation<K> {

	private final List<Val<K>> vals;

	private record Val<L>(Predicate<L> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
	}

    protected FieldValidator(Predicate<K> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
		this.vals = List.of(new Val<>(predicate, terminate, errorOn, onErrorMessage));
	}
    protected FieldValidator(FieldValidator<K> other, Predicate<K> predicate, Terminate terminate, ErrorOn errorOn, String onErrorMessage) {
        this.vals = Stream.concat(other.vals.stream(), Stream.of(new Val<>(predicate, terminate, errorOn, onErrorMessage))).toList();
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
