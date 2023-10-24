package br.dev.mhc.nomeaplicacao.controllers.exceptions;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FieldMessage implements Serializable {

    private String fieldName;
    private String message;

}
