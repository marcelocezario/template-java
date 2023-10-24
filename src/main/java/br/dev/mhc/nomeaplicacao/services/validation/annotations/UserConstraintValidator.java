package br.dev.mhc.nomeaplicacao.services.validation.annotations;

import br.dev.mhc.nomeaplicacao.dtos.UserDTO;
import br.dev.mhc.nomeaplicacao.services.validation.UserValidatorImpl;
import br.dev.mhc.nomeaplicacao.services.validation.interfaces.IValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserConstraintValidator implements ConstraintValidator<UserValidator, UserDTO> {

    private final IValidator<UserDTO> userValidator;

    @Autowired
    public UserConstraintValidator(IValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {

        var validationResult = userValidator.validate(userDTO);

        validationResult.getErrors().forEach(error -> {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(error.getMessage())
                    .addPropertyNode(error.getFieldName())
                    .addConstraintViolation();
        });

        return validationResult.isValid();
    }
}
