package br.dev.mhc.nomeaplicacao.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object id, Class<?> classType) {
        super("Resource not found, id [" + id.toString() + "], class name: [" + classType.getName() + "]");
    }
}
