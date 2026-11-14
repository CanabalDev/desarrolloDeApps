package com.gestionfincas.aplicacion.usuario;

import com.gestionfincas.dominio.Usuario;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public final class GestionUsuariosServicio implements GestionUsuarios {

    private final RepositorioUsuarios repositorioUsuarios;
    private final CodificadorContrasena codificadorContrasena;

    public GestionUsuariosServicio(
            RepositorioUsuarios repositorioUsuarios,
            CodificadorContrasena codificadorContrasena) {
        this.repositorioUsuarios = repositorioUsuarios;
        this.codificadorContrasena = codificadorContrasena;
    }

    @Override
    public Usuario crear(String nombreUsuario, String contrasena, String correo, String nombre) {
        validarContrasena(contrasena);
        String nombreUsuarioNormalizado = Usuario.normalizarNombreUsuario(nombreUsuario);
        String correoNormalizado = Usuario.normalizarCorreo(correo);
        String nombreNormalizado = Usuario.normalizarNombre(nombre);

        validarUnicidad(nombreUsuarioNormalizado, correoNormalizado, null);
        Usuario usuario = new Usuario(
                UUID.randomUUID().toString(),
                nombreUsuarioNormalizado,
                codificadorContrasena.codificar(contrasena),
                correoNormalizado,
                nombreNormalizado);
        return repositorioUsuarios.guardar(usuario);
    }

    @Override
    public Usuario consultar(String usuarioId) {
        return buscarUsuario(usuarioId);
    }

    @Override
    public List<Usuario> listar() {
        return repositorioUsuarios.listar();
    }

    @Override
    public Usuario actualizar(
            String usuarioId,
            String nombreUsuario,
            String contrasena,
            String correo,
            String nombre) {
        buscarUsuario(usuarioId);
        validarContrasena(contrasena);
        String nombreUsuarioNormalizado = Usuario.normalizarNombreUsuario(nombreUsuario);
        String correoNormalizado = Usuario.normalizarCorreo(correo);
        String nombreNormalizado = Usuario.normalizarNombre(nombre);

        validarUnicidad(nombreUsuarioNormalizado, correoNormalizado, usuarioId);
        Usuario usuario = new Usuario(
                usuarioId,
                nombreUsuarioNormalizado,
                codificadorContrasena.codificar(contrasena),
                correoNormalizado,
                nombreNormalizado);
        return repositorioUsuarios.guardar(usuario);
    }

    @Override
    public void eliminar(String usuarioId) {
        buscarUsuario(usuarioId);
        repositorioUsuarios.eliminar(usuarioId);
    }

    @Override
    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || nombreUsuario.isBlank() || contrasena == null || contrasena.isBlank()) {
            return false;
        }

        String nombreUsuarioNormalizado;
        try {
            nombreUsuarioNormalizado = Usuario.normalizarNombreUsuario(nombreUsuario);
        } catch (IllegalArgumentException excepcion) {
            return false;
        }

        return repositorioUsuarios.buscarPorNombreUsuario(nombreUsuarioNormalizado)
                .map(usuario -> codificadorContrasena.coincide(contrasena, usuario.contrasenaCodificada()))
                .orElse(false);
    }

    private Usuario buscarUsuario(String usuarioId) {
        return repositorioUsuarios.buscarPorId(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("No existe un usuario con el identificador indicado."));
    }

    private void validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
    }

    private void validarUnicidad(String nombreUsuario, String correo, String usuarioIdExcluir) {
        if (repositorioUsuarios.existeNombreUsuario(nombreUsuario, usuarioIdExcluir)) {
            throw new IllegalStateException("El nombre de usuario ya está registrado.");
        }
        if (repositorioUsuarios.existeCorreo(correo, usuarioIdExcluir)) {
            throw new IllegalStateException("El correo ya está registrado.");
        }
    }
}
