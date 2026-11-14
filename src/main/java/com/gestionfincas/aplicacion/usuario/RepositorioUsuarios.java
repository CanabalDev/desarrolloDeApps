package com.gestionfincas.aplicacion.usuario;

import com.gestionfincas.dominio.Usuario;

import java.util.List;
import java.util.Optional;

public interface RepositorioUsuarios {

    Optional<Usuario> buscarPorId(String usuarioId);

    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);

    List<Usuario> listar();

    boolean existeNombreUsuario(String nombreUsuario, String usuarioIdExcluir);

    boolean existeCorreo(String correo, String usuarioIdExcluir);

    Usuario guardar(Usuario usuario);

    void eliminar(String usuarioId);
}
