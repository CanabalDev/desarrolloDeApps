package com.gestionfincas.infraestructura.persistencia;

import com.gestionfincas.aplicacion.usuario.RepositorioUsuarios;
import com.gestionfincas.dominio.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdaptadorPersistenciaUsuarios implements RepositorioUsuarios {

    private final RepositorioUsuariosJpa repositorio;

    public AdaptadorPersistenciaUsuarios(RepositorioUsuariosJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Optional<Usuario> buscarPorId(String usuarioId) {
        return repositorio.findById(usuarioId).map(UsuarioEntidad::aDominio);
    }

    @Override
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return repositorio.buscarPorNombreUsuario(nombreUsuario).map(UsuarioEntidad::aDominio);
    }

    @Override
    public List<Usuario> listar() {
        return repositorio.findAll().stream().map(UsuarioEntidad::aDominio).toList();
    }

    @Override
    public boolean existeNombreUsuario(String nombreUsuario, String usuarioIdExcluir) {
        return repositorio.contarNombreUsuario(nombreUsuario, usuarioIdExcluir) > 0;
    }

    @Override
    public boolean existeCorreo(String correo, String usuarioIdExcluir) {
        return repositorio.contarCorreo(correo, usuarioIdExcluir) > 0;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return repositorio.save(new UsuarioEntidad(usuario)).aDominio();
    }

    @Override
    public void eliminar(String usuarioId) {
        repositorio.deleteById(usuarioId);
    }
}
