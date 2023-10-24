package br.dev.mhc.nomeaplicacao.services.db;

import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import br.dev.mhc.nomeaplicacao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DBSeedServiceTest {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public DBSeedServiceTest(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void databaseSeeding() {
        List<User> users = new ArrayList<>();
        users.add(User.builder().nickname("Admin").email("admin@mhc.dev.br").password(passwordEncoder.encode("Admin@123")).active(true).profiles(Set.of(ProfileEnum.ADMIN.getCod())).build());
        users.add(User.builder().nickname("Basic").email("basic@mhc.dev.br").password(passwordEncoder.encode("Basic@123")).active(true).profiles(Set.of(ProfileEnum.BASIC.getCod())).build());
        userRepository.saveAll(users);
    }
}
