package com.example.validation;

import com.example.service.UserDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class RegistrationValidator implements Validator {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirmation", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "captcha", "NotEmpty");

        UserDto userDto = (UserDto) target;
        if (!StringUtils.equals(userDto.getPasswordConfirmation(), userDto.getPassword())) {
            errors.rejectValue("passwordConfirmation", "NotMatch.password");
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if (!pattern.matcher(userDto.getEmail()).matches()) {
            errors.rejectValue("email", "Invalid");
        }
    }
}
