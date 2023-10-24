package br.dev.mhc.nomeaplicacao.services.validation;

import br.dev.mhc.nomeaplicacao.dtos.UserDTO;
import br.dev.mhc.nomeaplicacao.dtos.ValidationResultDTO;
import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.repositories.UserRepository;
import br.dev.mhc.nomeaplicacao.services.validation.interfaces.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.dev.mhc.nomeaplicacao.utils.Util.isValidEmail;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Service
public class UserValidatorImpl implements IValidator<UserDTO> {

    private final UserRepository repository;

    @Autowired
    public UserValidatorImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public ValidationResultDTO<UserDTO> validate(UserDTO userDTO) {
        requireNonNull(userDTO);

        ValidationResultDTO<UserDTO> validation = new ValidationResultDTO<>(userDTO);

        validateNickname(validation);
        validateEmail(validation);
        validatePassword(validation);

        return validation;
    }

    private void validatePassword(ValidationResultDTO<UserDTO> validation) {
        final String FIELD_NAME = "password";
        String password = validation.getObject().getPassword();
        if (isNull(password)) {
            if (isNull(validation.getObject().getId())) {
                validation.addError(FIELD_NAME, "Password cannot be null");
            }
            return;
        }
        if (password.isBlank()) {
            validation.addError(FIELD_NAME, "Password cannot be empty");
        }
        if (password.length() < 6) {
            validation.addError(FIELD_NAME, "Password cannot be less then 6 characters");
        }
    }

    private void validateEmail(ValidationResultDTO<UserDTO> validation) {
        final String FIELD_NAME = "email";
        String email = validation.getObject().getEmail();
        if (isNull(email)) {
            validation.addError(FIELD_NAME, "E-mail cannot be null");
            return;
        }
        if (email.isBlank()) {
            validation.addError(FIELD_NAME, "E-mail cannot be empty");
        }
        if (!isValidEmail(email)) {
            validation.addError(FIELD_NAME, "E-mail is not valid");
        }
        Optional<User> userByEmail = repository.findByEmail(email);
        if (userByEmail.isPresent()) {
            if (!userByEmail.get().getId().equals(validation.getObject().getId())) {
                validation.addError(FIELD_NAME, "There is already a user with the registered email");
            }
        }
    }

    private void validateNickname(ValidationResultDTO<UserDTO> validation) {
        final String FIELD_NAME = "nickname";
        String nickname = validation.getObject().getNickname();
        if (isNull(nickname)) {
            validation.addError(FIELD_NAME, "Nickname cannot be null");
            return;
        }
        if (nickname.isBlank()) {
            validation.addError(FIELD_NAME, "Nickname cannot be empty");
        }
        if (nickname.length() < 3) {
            validation.addError(FIELD_NAME, "Nickname cannot be less then 3 characters");
        }
        if (nickname.length() > 255) {
            validation.addError(FIELD_NAME, "Nickname cannot be longer then 255 characters");
        }
    }

}
