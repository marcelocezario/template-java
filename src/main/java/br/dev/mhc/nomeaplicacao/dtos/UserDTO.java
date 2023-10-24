package br.dev.mhc.nomeaplicacao.dtos;

import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import br.dev.mhc.nomeaplicacao.services.validation.annotations.UserValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@UserValidator
public class UserDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private String nickname;
    private String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Set<ProfileEnum> profiles = new HashSet<>();

    public UserDTO(User entity) {
        Objects.requireNonNull(entity);
        id = entity.getId();
        nickname = entity.getNickname();
        email = entity.getEmail();
        active = entity.isActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        profiles = entity.getProfiles();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .password(password)
                .active(active)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .profiles(profiles.stream().map(ProfileEnum::getCod).collect(Collectors.toSet()))
                .build();
    }
}
