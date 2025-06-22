package io.github.veerakumarak.validations;

import io.github.veerakumarak.fp.Failure;
import io.github.veerakumarak.fp.Failures;
import io.github.veerakumarak.fp.failures.InvalidRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record ValidationResult(boolean valid, Map<String, List<String>> reasons) {

    public static ValidationResult of(Map<String, List<String>> reasons) {
        Objects.requireNonNull(reasons);
        return new ValidationResult(reasons.isEmpty(), reasons);
    }

    public static ValidationResult of(FieldResult fieldResult) {
        return of(List.of(fieldResult));
    }

    public static ValidationResult of(List<FieldResult> fieldResults) {
        return of(fieldResults.stream()
                .filter(result -> !result.valid())
                .collect(Collectors.toMap(
                        FieldResult::field,
                        FieldResult::reasons,
                        (existingReasons, newReasons) -> {
                            List<String> combined = new ArrayList<>(existingReasons);
                            combined.addAll(newReasons);
                            return combined;
                        }
                )));
    }

    public Boolean isValid() {
        return valid;
    }

    public void orThrow() {
        throw new InvalidRequest(reasons);
    }

    public Failure toFailure() {
        return !isValid() ? new InvalidRequest(reasons) : Failures.empty();
    }

    public Map<String, List<String>> reasons() {
        return reasons;
    }

    public String getMessage() {
        if (reasons == null || reasons.isEmpty()) {
            return "No failures detected.";
        }

        String detailedReasons = reasons.entrySet().stream()
                .map(entry -> {
                    String field = entry.getKey();
                    String fieldReasons = String.join(",", entry.getValue());
                    return "  \"" + field + "\": \"" + String.join(", ", fieldReasons) + "\"";
                })
                .collect(Collectors.joining("\n"));

        return "{\n" + detailedReasons + "\n}";
    }

}
