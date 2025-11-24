package com.techlab.crud.model.Usuario;

import com.techlab.crud.model.Roles.Roles;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; 
    private String nombreCompleto; 
    private Boolean activo = true; 
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;
    
    @PrePersist
    protected void onCreate() {
        if (Objects.isNull(this.activo)) {
            this.activo = true;
        }
    }
}
