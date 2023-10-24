package br.dev.mhc.nomeaplicacao.services.impl;

import br.dev.mhc.nomeaplicacao.dtos.UserDTO;
import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import br.dev.mhc.nomeaplicacao.repositories.UserRepository;
import br.dev.mhc.nomeaplicacao.services.exceptions.ResourceNotFoundException;
import br.dev.mhc.nomeaplicacao.services.interfaces.IUserService;
import br.dev.mhc.nomeaplicacao.utils.LogHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.util.Objects.*;

@Service
public class UserServiceImpl implements IUserService {

    private final LogHelper LOG = new LogHelper(this.getClass());

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDTO insert(UserDTO userDTO) {
        requireNonNull(userDTO);
        userDTO.setId(null);
        userDTO.setEmail(userDTO.getEmail().trim().toLowerCase());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setProfiles(Set.of(ProfileEnum.BASIC));
        User user = repository.save(userDTO.toEntity());
        LOG.info("User created", user.getId(), user.getEmail(), user.getProfiles());
        return new UserDTO(user);
    }

    @Transactional
    @Override
    public UserDTO update(UserDTO userDTO) {
        requireNonNull(userDTO);
        requireNonNull(userDTO.getId());
        User userEntity = repository.getReferenceById(userDTO.getId());
        try {
            updateData(userEntity, userDTO);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(userDTO.getId(), User.class);
        }
        userDTO = new UserDTO(repository.save(userEntity));
        LOG.debug("User updated", userDTO.getId(), userDTO.getEmail());
        return userDTO;
    }

    private void updateData(User userEntity, UserDTO userDTO) {
        userEntity.setNickname(userDTO.getNickname());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setActive(userDTO.isActive());
        if (nonNull(userDTO.getPassword()) && !userDTO.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
    }

    @Transactional
    @Override
    public void inactivate(Long userId) {
        requireNonNull(userId);
        User userEntity = repository.getReferenceById(userId);
        try {
            userEntity.setActive(false);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(userId, User.class);
        }
        repository.save(userEntity);
        LOG.info("User inactivated", userId);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        requireNonNull(userId);
        findEntityById(userId);
        repository.deleteById(userId);
        LOG.info("User deleted", userId);
    }

    private User findEntityById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId, User.class));
    }

    @Override
    public List<UserDTO> getAll() {
        return repository.findAll().stream().map(UserDTO::new).toList();
    }

    @Override
    public UserDTO getById(Long userId) {
        return new UserDTO(findEntityById(userId));
    }

}
