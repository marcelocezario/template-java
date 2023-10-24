package br.dev.mhc.nomeaplicacao.services.interfaces;

import java.util.List;

public interface ICrudService<DTO, IDTYPE> {

    DTO insert(DTO dto);
    DTO update(DTO dto);
    void delete(IDTYPE id);
    void inactivate(IDTYPE id);
    List<DTO> getAll();
    DTO getById(IDTYPE id);

}