package com.example.web;

import com.example.service.UserDto;
import com.example.service.UserService;
import com.example.validation.EmailExistsException;
import com.example.validation.RegistrationValidator;
import com.example.validation.UserExistsException;
import com.google.code.kaptcha.servlet.KaptchaExtend;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RegisterController extends KaptchaExtend {

    @Autowired
    UserService userService;

    @Autowired
    MessageSource messageSource;

//    @Autowired
//    @Qualifier("registrationValidator")
//    Validator validator;
//
//    @InitBinder("user")
//    private void initBinder(WebDataBinder binder) {
//        binder.setValidator(validator);
//    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegister(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerAccount(@ModelAttribute("user") /*@Valid*/ UserDto user, Errors errors,
                                  Model model, HttpServletRequest request, RegistrationValidator validator) {

        validator.validate(user, errors);
        String captcha1 = user.getCaptcha();
        String captcha2 = getGeneratedKey(request);
        if (!StringUtils.equals(captcha1, captcha2)) {
            errors.rejectValue("captcha", "Invalid");
        }

        if (errors.hasErrors()) {
            return "register";
        }

        try {
            userService.registerNewUserAccount(user);

            // generate session if one doesn't exist
            request.getSession();
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(token);

            return "redirect:/";

        } catch (EmailExistsException e) {
            errors.rejectValue("email", "exists");
        } catch (UserExistsException e) {
            errors.rejectValue("username", "exists");
        }
        return "register";
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.captcha(request, response);
    }

}
