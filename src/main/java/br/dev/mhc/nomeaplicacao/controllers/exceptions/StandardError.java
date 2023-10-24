package br.dev.mhc.nomeaplicacao.controllers.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Service
@Builder
public class StandardError {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    @Builder.Default
    private List<FieldMessage> fieldMessages = new ArrayList<>();

    public void addError(String fieldName, String message) {
        if (Objects.isNull(fieldMessages)) {
            fieldMessages = new ArrayList<>();
        }
        fieldMessages.add(new FieldMessage(fieldName, message));
    }
}
