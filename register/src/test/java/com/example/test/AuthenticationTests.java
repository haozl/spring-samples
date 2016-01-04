/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.test;

import com.example.config.RootConfig;
import com.example.config.WebConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class})
@WebAppConfiguration
public class AuthenticationTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .defaultRequest(get("/").with(testSecurityContext()))
                .build();
    }

    @Test
    public void requiresAuthentication() throws Exception {
        mvc
                .perform(get("/"))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void authenticationSuccess() throws Exception {
        mvc.perform(formLogin("/login").user("tom").password("123"))
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("tom"));
    }

    @Test
    public void authenticationFailed() throws Exception {
        mvc
                .perform(formLogin().user("sa").password("invalid"))
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    public void requestProtectedUrlWithUser() throws Exception {
        mvc
                .perform(get("/").with(user("sa")))
                        // Ensure we got past Security
                .andExpect(status().isOk())
                        // Ensure it appears we are authenticated with user
                .andExpect(authenticated().withUsername("sa"))
                .andExpect(view().name("hello"));
    }

    @Test
    public void requestProtectedUrlWithAdmin() throws Exception {
        HttpSession session = mvc.perform(formLogin().user("sa").password("123"))
                .andExpect(authenticated())
                .andReturn()
                .getRequest().getSession();
        Assert.assertNotNull(session);
        mvc.perform(get("/admin").session((MockHttpSession) session).locale(Locale.ENGLISH))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        session = mvc.perform(formLogin().user("tom").password("123"))
                .andExpect(authenticated())
                .andReturn()
                .getRequest().getSession();
        Assert.assertNotNull(session);
        mvc.perform(get("/admin").session((MockHttpSession) session).locale(Locale.ENGLISH))
                .andExpect(status().isForbidden());
    }


    @Test
    public void testLogout() throws Exception {
        mvc.perform(logout()).andExpect(unauthenticated());
    }


}