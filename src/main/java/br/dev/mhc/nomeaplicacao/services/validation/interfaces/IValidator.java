package br.dev.mhc.nomeaplicacao.services.validation.interfaces;

import br.dev.mhc.nomeaplicacao.dtos.ValidationResultDTO;

public interface IValidator<DTO> {

    ValidationResultDTO<DTO> validate(DTO dto);

    default boolean isValid(DTO dto) {
        return this.validate(dto).isValid();
    }

}
