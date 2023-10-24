package br.dev.mhc.nomeaplicacao.controllers;

import br.dev.mhc.nomeaplicacao.dtos.CredentialsDTO;
import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    private final String PATH = "/login";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    private CredentialsDTO credentials;
    private User user;

    @BeforeEach
    public void setup() {
        credentials = buildCredentials();
        user = buildUser(1L);
    }

    private User buildUser(Long id) {
        return User.builder()
                .id(id)
                .email("test@test.com")
                .password(new BCryptPasswordEncoder().encode("password"))
                .active(true)
                .build();
    }

    private CredentialsDTO buildCredentials() {
        return new CredentialsDTO("test@test.com", "password");
    }

    @Test
    public void shouldReturnAuthorizationToken_WhenLogin() throws Exception {
        Mockito.when(userRepository.findByEmail(credentials.getUsername())).thenReturn(Optional.of(user));
        mockMvc.perform(
                        post(PATH)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(header().exists("Authorization"));
    }

    @Test
    public void shouldReturn4xxError_WhenInvalidUser() throws Exception {
        credentials.setPassword("changed-password");
        Mockito.when(userRepository.findByEmail(credentials.getUsername())).thenReturn(Optional.of(user));
        mockMvc.perform(
                        post(PATH)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().is4xxClientError());
    }

    @WithAnonymousUser
    @Test
    public void shouldReturnStatus403_WhenAnonymousUser() throws Exception {
        mockMvc.perform(get(PATH)).andExpect(status().isForbidden());
        mockMvc.perform(put(PATH)).andExpect(status().isForbidden());
        mockMvc.perform(delete(PATH)).andExpect(status().isForbidden());

        mockMvc.perform(get(PATH)).andExpect(status().isForbidden());
        mockMvc.perform(put(PATH)).andExpect(status().isForbidden());
        mockMvc.perform(delete(PATH)).andExpect(status().isForbidden());
    }
}
