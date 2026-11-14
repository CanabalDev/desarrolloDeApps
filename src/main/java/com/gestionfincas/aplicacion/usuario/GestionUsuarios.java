package com.gestionfincas.aplicacion.usuario;

import com.gestionfincas.dominio.Usuario;

import java.util.List;

public interface GestionUsuarios {

    Usuario crear(String nombreUsuario, String contrasena, String correo, String nombre);

    Usuario consultar(String usuarioId);

    List<Usuario> listar();

    List<Usuario> filtrar(String nombreUsuario, String correo, String nombre);

    Usuario actualizar(String usuarioId, String nombreUsuario, String contrasena, String correo, String nombre);

    void eliminar(String usuarioId);

    boolean iniciarSesion(String nombreUsuario, String contrasena);
}
