package io.github.veerakumarak.validations;

import io.github.veerakumarak.fp.errors.InvalidRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Reasons(Map<String, List<String>> reasons) {

    public static Reasons of(Map<String, List<String>> reasons) {
        return new Reasons(reasons);
    }

    public static Reasons of(FieldResult fieldResult) {
        return of(List.of(fieldResult));
    }

    public static Reasons of(List<FieldResult> fieldResults) {
        return new Reasons(fieldResults.stream()
                .filter(result -> !result.valid())
                .collect(Collectors.toMap(FieldResult::field, FieldResult::reasons)));
    }

    public Boolean isValid() {
        return reasons.isEmpty();
    }

    public void orThrow() {
        throw new InvalidRequest(reasons);
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
