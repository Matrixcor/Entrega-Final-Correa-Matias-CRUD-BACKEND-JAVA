package com.techlab.crud.controller;

import com.techlab.crud.model.Usuario.Usuario;
import com.techlab.crud.service.UserService.UserService;
import com.techlab.crud.exception.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UserService userService;

    @PostMapping 
    public ResponseEntity<Usuario> registerUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = userService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }
    
    /*

    SECCION SOLO PARA ADMINISTRADORES O SUPER USUARIOS
    APLICAR POLITICAS DE ACCESO POR ROLES, SE HARA UNA SECCION LOGIN CON JWT O SESSION
    
    */
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllActivos() {
        List<Usuario> usuarios = userService.findAllActivos();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        usuarioDetails.setId(id);
        
        try { 
            Usuario usuarioActualizado = userService.save(usuarioDetails);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (UsuarioNoEncontradoException e) {
            throw e; 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioLogico(@PathVariable Long id) {
        try {
            userService.deactivateById(id);
            return ResponseEntity.noContent().build(); 
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        }
    }
}