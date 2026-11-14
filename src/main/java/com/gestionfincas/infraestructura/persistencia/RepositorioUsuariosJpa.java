package com.gestionfincas.infraestructura.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepositorioUsuariosJpa extends JpaRepository<UsuarioEntidad, String> {

    @Query("SELECT usuario FROM UsuarioEntidad usuario WHERE LOWER(usuario.nombreUsuario) = LOWER(:nombreUsuario)")
    Optional<UsuarioEntidad> buscarPorNombreUsuario(@Param("nombreUsuario") String nombreUsuario);

    @Query("SELECT COUNT(usuario) FROM UsuarioEntidad usuario "
            + "WHERE LOWER(usuario.nombreUsuario) = LOWER(:nombreUsuario) "
            + "AND (:usuarioIdExcluir IS NULL OR usuario.id <> :usuarioIdExcluir)")
    long contarNombreUsuario(@Param("nombreUsuario") String nombreUsuario,
            @Param("usuarioIdExcluir") String usuarioIdExcluir);

    @Query("SELECT COUNT(usuario) FROM UsuarioEntidad usuario "
            + "WHERE LOWER(usuario.correo) = LOWER(:correo) "
            + "AND (:usuarioIdExcluir IS NULL OR usuario.id <> :usuarioIdExcluir)")
    long contarCorreo(@Param("correo") String correo, @Param("usuarioIdExcluir") String usuarioIdExcluir);
}
