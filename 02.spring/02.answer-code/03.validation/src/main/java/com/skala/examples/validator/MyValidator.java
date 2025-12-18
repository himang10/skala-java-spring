package com.skala.examples.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.skala.examples.domain.User;

@Component
public class MyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (user.getAge() >= 100) {
            errors.rejectValue("age", "too.old", "age는 99보다 작아야 합니다.");
        }

        if (user.getAge() <= 0) {
            errors.rejectValue("age", "too.young", "age는 1보다 커야 합니다.");
        }
    }
}