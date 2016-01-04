package com.example.test;

import com.example.config.RootConfig;
import com.example.config.WebConfig;
import com.example.model.User;
import com.example.service.UserDto;
import com.example.service.UserService;
import com.example.validation.EmailExistsException;
import com.example.validation.RegistrationValidator;
import com.example.validation.UserExistsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class})
public class RegistrationTest {

    private final static String REGISTER_URL = "/register";
    private final static String CAPTCHA_URL = "/captcha";

    @Autowired
    RegistrationValidator registrationValidator;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    private UserDto userForm() {

        UserDto user = new UserDto();
        String username = "user123";
        String password = "123456";
        String email = "user@user.com";
        String captcha = "invalid";
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        user.setEmail(email);
        user.setCaptcha(captcha);
        return user;
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void showRegistration() throws Exception {
        mockMvc.perform(get(REGISTER_URL))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", hasProperty("username", isEmptyOrNullString())))
                .andExpect(model().attribute("user", hasProperty("password", isEmptyOrNullString())))
                .andExpect(model().attribute("user", hasProperty("passwordConfirmation", isEmptyOrNullString())))
                .andExpect(model().attribute("user", hasProperty("email", isEmptyOrNullString())))
                .andExpect(model().attribute("user", hasProperty("captcha", isEmptyOrNullString())))
        ;
    }

    @Test
    public void registerInvalidUser() throws Exception {

        UserDto user = new UserDto();
        user.setUsername("   ");
        user.setPassword("   ");
        user.setPasswordConfirmation("password");
        user.setEmail("invalid@email");
        user.setCaptcha("invalid captcha");


        mockMvc.perform(post(REGISTER_URL)
                .locale(Locale.ENGLISH)
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .param("passwordConfirmation", user.getPasswordConfirmation())
                .param("email", user.getEmail())
                .param("captcha", user.getCaptcha())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", hasProperty("username", is(user.getUsername()))))
                .andExpect(model().attribute("user", hasProperty("password", is(user.getPassword()))))
                .andExpect(model().attribute("user", hasProperty("passwordConfirmation", is(user.getPasswordConfirmation()))))
                .andExpect(model().attribute("user", hasProperty("email", is(user.getEmail()))))
                .andExpect(model().attribute("user", hasProperty("captcha", is(user.getCaptcha()))))
                .andExpect(model()
                        .attributeHasFieldErrors("user", "username", "password", "passwordConfirmation", "email", "captcha"))
        ;

    }

    @Test
    public void registerValidUser() throws Exception {
        UserDto user = this.userForm();
        HttpSession session = mockMvc.perform(get(CAPTCHA_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession();

        String captcha = (String) session.getAttribute("KAPTCHA_SESSION_KEY");
        Assert.assertNotNull(captcha);
        Assert.assertNotNull(session);
        user.setCaptcha(captcha);

        mockMvc.perform(post(REGISTER_URL)
                .session((MockHttpSession) session)
                .locale(Locale.ENGLISH)
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .param("passwordConfirmation", user.getPasswordConfirmation())
                .param("email", user.getEmail())
                .param("captcha", user.getCaptcha())
        )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", hasProperty("username", is(user.getUsername()))))
                .andExpect(model().attribute("user", hasProperty("password", is(user.getPassword()))))
                .andExpect(model().attribute("user", hasProperty("passwordConfirmation", is(user.getPasswordConfirmation()))))
                .andExpect(model().attribute("user", hasProperty("email", is(user.getEmail()))))
                .andExpect(model().attribute("user", hasProperty("captcha", is(user.getCaptcha()))))
                .andExpect(model().attributeHasNoErrors("user"))
        ;

        User saved = userService.findByName(user.getUsername());
        Assert.assertNotNull(saved);
        Assert.assertEquals(user.getUsername(), saved.getUsername());
        Assert.assertTrue(passwordEncoder.matches(user.getPassword(), saved.getPassword()));

    }


    @Test
    public void invalidCaptcha() throws Exception {

        UserDto user = this.userForm();
        HttpSession session = mockMvc.perform(get(CAPTCHA_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession();

        String captcha = (String) session.getAttribute("KAPTCHA_SESSION_KEY");
        Assert.assertNotNull(captcha);
        Assert.assertNotNull(session);
        user.setCaptcha("invalid captcha");

        session = mockMvc.perform(post(REGISTER_URL)
                .session((MockHttpSession) session)
                .locale(Locale.ENGLISH)
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .param("passwordConfirmation", user.getPasswordConfirmation())
                .param("email", user.getEmail())
                .param("captcha", user.getCaptcha())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn()
                .getRequest().getSession();

        captcha = (String) session.getAttribute("KAPTCHA_SESSION_KEY");
        Assert.assertNotNull(session);
        Assert.assertNotNull(captcha);
        Assert.assertNotEquals(captcha, user.getCaptcha());
    }

    @Test
    public void registerNewAccount() throws EmailExistsException, UserExistsException {

        UserDto userDto = userForm();
        userService.delete(userDto.getUsername());

        userService.registerNewUserAccount(userDto);

        User user = userService.findByName(userDto.getUsername());
        Assert.assertNotNull(user);

        Assert.assertEquals(userDto.getUsername(), user.getUsername());
        Assert.assertTrue(passwordEncoder.matches(userDto.getPassword(), user.getPassword()));

        userService.delete(userDto.getUsername());
    }


}
