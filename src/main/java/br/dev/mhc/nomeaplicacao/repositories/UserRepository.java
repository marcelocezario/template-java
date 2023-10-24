package br.dev.mhc.nomeaplicacao.repositories;

import br.dev.mhc.nomeaplicacao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);

}
