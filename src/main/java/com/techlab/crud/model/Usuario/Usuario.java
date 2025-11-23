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

    // La contraseña debe almacenarse hasheada (cifrada con bcrypt, por ejemplo)
    @Column(nullable = false)
    private String password; 
    private String nombreCompleto; 
    private Boolean activo = true; 
    // Se asume que un Usuario tiene UN Rol (ManyToOne)
    @ManyToOne(fetch = FetchType.EAGER) // FetchType.EAGER: Cargar el rol inmediatamente
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;
    
    // --- Constructor helper (Opcional, si usas la lógica de negocio en el modelo) ---
    
    @PrePersist
    protected void onCreate() {
        if (Objects.isNull(this.activo)) {
            this.activo = true;
        }
    }
}
