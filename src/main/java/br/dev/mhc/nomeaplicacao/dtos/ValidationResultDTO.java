package br.dev.mhc.nomeaplicacao.dtos;

import br.dev.mhc.nomeaplicacao.controllers.exceptions.FieldMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Getter
@Setter
public class ValidationResultDTO<T> {

    private T object;
    @Setter(AccessLevel.NONE)
    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationResultDTO(T object) {
        this.object = object;
    }

    public boolean isValid() {
        return isNull(errors) || errors.isEmpty();
    }

    public void addError(String fieldName, String message) {
        if (isNull(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new FieldMessage(fieldName, message));
    }

}
