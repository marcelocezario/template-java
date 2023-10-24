package br.dev.mhc.nomeaplicacao.controllers;

import static br.dev.mhc.nomeaplicacao.enums.ProfileEnum.ADMIN;
import static br.dev.mhc.nomeaplicacao.enums.ProfileEnum.BASIC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Set;

import br.dev.mhc.nomeaplicacao.testconfig.WithMockCustomUser;
import br.dev.mhc.nomeaplicacao.dtos.UserDTO;
import br.dev.mhc.nomeaplicacao.dtos.ValidationResultDTO;
import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import br.dev.mhc.nomeaplicacao.services.interfaces.IUserService;
import br.dev.mhc.nomeaplicacao.services.validation.interfaces.IValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final String PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserService service;

//    @MockBean
//    private UserValidatorImpl userValidator;

    @MockBean
    private IValidator<UserDTO> userValidator;

    @BeforeEach
    public void setup() {
    }

    private UserDTO buildUser(Long id) {
        return UserDTO.builder()
                .id(id)
                .nickname("Test")
                .email("test@test.com")
                .password("password")
                .active(true)
                .createdAt(Instant.parse("2023-10-19T23:00:00Z"))
                .updatedAt(Instant.parse("2023-10-19T23:00:00Z"))
                .profiles(Set.of(BASIC))
                .build();
    }

    @Test
    public void shouldReturnStatus201AndUser_WhenInsertNewUser() throws Exception {
        UserDTO userBeforeInsert = buildUser(null);
        UserDTO userAfterInsert = buildUser(1L);
        Mockito.when(userValidator.validate(userBeforeInsert)).thenReturn(new ValidationResultDTO<>(userBeforeInsert));
        Mockito.when(service.insert(userBeforeInsert)).thenReturn(userAfterInsert);
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBeforeInsert)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(userAfterInsert.getId()))
                .andExpect(jsonPath("nickname").value(userAfterInsert.getNickname()))
                .andExpect(jsonPath("email").value(userAfterInsert.getEmail()))
                .andExpect(jsonPath("password").doesNotExist())
                .andExpect(jsonPath("active").value(userAfterInsert.isActive()))
                .andExpect(jsonPath("profiles").isNotEmpty())
                .andExpect(jsonPath("$.profiles.[*]").value("BASIC"));
    }

    @WithMockCustomUser(id = 1L)
    @Test
    public void shouldReturnStatus204_WhenBasicUserTriesToUpdateOwnId() throws Exception {
        UserDTO user = buildUser(1L);
        Mockito.when(service.update(user)).thenReturn(user);
        Mockito.when(userValidator.validate(user)).thenReturn(new ValidationResultDTO<>(user));
        mockMvc.perform(put(PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
    }

    @WithMockCustomUser(id = 1L, profile = ADMIN)
    @Test
    public void shouldReturnStatus204_WhenAdminUserTriesToUpdateOtherId() throws Exception {
        UserDTO user = buildUser(2L);
        Mockito.when(service.update(user)).thenReturn(user);
        Mockito.when(userValidator.validate(user)).thenReturn(new ValidationResultDTO<>(user));
        mockMvc.perform(put(PATH + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
    }

    @WithMockCustomUser(profile = ProfileEnum.ADMIN)
    @Test
    public void shouldReturnStatus200AndUsers_WhenAdminUserTriesToRetrieveAllUsers() throws Exception {
        mockMvc.perform(get(PATH)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
    }

    @WithMockCustomUser(id = 1L, profile = ProfileEnum.ADMIN)
    @Test
    public void shouldReturnStatus200AndUser_WhenAdminUserTriesToRetrieveAnotherId() throws Exception {
        Mockito.when(service.getById(2L)).thenReturn(buildUser(2L));
        mockMvc.perform(get(PATH + "/2")).andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty());
    }

    @WithMockCustomUser(id = 1L)
    @Test
    public void shouldReturnStatus200AndUser_WhenBasicUserTriesToRetrieveOwnId() throws Exception {
        Mockito.when(service.getById(1L)).thenReturn(buildUser(1L));
        mockMvc.perform(get(PATH + "/1")).andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty());
    }


    @Test
    public void shouldReturnStatus403_WhenUnauthenticatedUserTriesToPutGetDelete() throws Exception {
        mockMvc.perform(put(PATH)).andExpect(status().isForbidden());
        mockMvc.perform(put(PATH + "/1")).andExpect(status().isForbidden());
        mockMvc.perform(get(PATH)).andExpect(status().isForbidden());
        mockMvc.perform(get(PATH + "/1")).andExpect(status().isForbidden());
        mockMvc.perform(delete(PATH)).andExpect(status().isForbidden());
        mockMvc.perform(delete(PATH + "/1")).andExpect(status().isForbidden());
    }

    @WithMockCustomUser
    @Test
    public void shouldReturnStatus403_WhenBasicUserTriesToRetrieveAllUsers() throws Exception {
        mockMvc.perform(get(PATH)).andExpect(status().isForbidden());
    }

    @WithMockCustomUser(id = 1L)
    @Test
    public void shouldReturnStatus403_WhenBasicUserTriesToRetrieveAnotherId() throws Exception {
        mockMvc.perform(get(PATH + "/2")).andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnStatus422_WhenInsertingUserWithInvalidData() throws Exception {
        UserDTO user = buildUser(null);
        ValidationResultDTO<UserDTO> validationResultDTO = new ValidationResultDTO<>(user);
        validationResultDTO.addError("email", "Invalid Email");
        Mockito.when(userValidator.validate(user)).thenReturn(validationResultDTO);
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }

}
