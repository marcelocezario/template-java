package br.dev.mhc.nomeaplicacao.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public enum ProfileEnum {

    ADMIN(1, "ADMIN"),
    BASIC(2, "BASIC");

    @Getter private final int cod;
    @Getter private final String description;

    ProfileEnum(int cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public static ProfileEnum toEnum(Integer cod) {
        if (Objects.isNull(cod)) return null;
        return Arrays.stream(ProfileEnum.values())
                .filter(p -> p.getCod() == cod)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }


}
