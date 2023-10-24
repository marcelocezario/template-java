package br.dev.mhc.nomeaplicacao.controllers.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

public class URIUtil {

    public static URI buildUri(Long id) {
        Objects.nonNull(id);
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
