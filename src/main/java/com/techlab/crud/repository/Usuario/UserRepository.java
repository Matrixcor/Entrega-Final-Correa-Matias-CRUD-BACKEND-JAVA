package com.techlab.crud.repository.Usuario;

import com.techlab.crud.model.Usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailAndActivoTrue(String email);
    List<Usuario> findByActivoTrue();
    boolean existsByEmail(String email);
}
