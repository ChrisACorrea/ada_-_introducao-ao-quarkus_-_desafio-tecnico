package results;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Result<T>(boolean success, T data, List<String> errors) {

    public Result {
        // Exemplo de validação:
        if (success && errors != null && !errors.isEmpty()) {
            throw new IllegalArgumentException("Sucesso não pode ter erros.");
        }
        if (!success && (errors == null || errors.isEmpty())) {
            throw new IllegalArgumentException("Falha deve ter erros.");
        }
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, List.of());
    }

    @SuppressWarnings("null")
    public static <T> Result<T> failure(List<String> errors) {
        return new Result<>(false, null, errors);
    }

    @JsonIgnore
    public boolean failure() {
        return !success;
    }

}
