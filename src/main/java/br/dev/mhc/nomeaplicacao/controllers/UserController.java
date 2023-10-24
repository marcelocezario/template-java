package br.dev.mhc.nomeaplicacao.controllers;

import br.dev.mhc.nomeaplicacao.controllers.utils.URIUtil;
import br.dev.mhc.nomeaplicacao.dtos.UserDTO;
import br.dev.mhc.nomeaplicacao.services.interfaces.IUserService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public record UserController(IUserService service) {

    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserDTO userDTO) {
        userDTO = service.insert(userDTO);
        return ResponseEntity.created(URIUtil.buildUri(userDTO.getId())).body(userDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        userDTO.setId(id);
        service.update(userDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

}
