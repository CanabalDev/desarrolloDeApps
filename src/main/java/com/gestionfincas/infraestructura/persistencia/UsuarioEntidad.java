package com.gestionfincas.infraestructura.persistencia;

import com.gestionfincas.dominio.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuarios_nombre_usuario", columnNames = "nombre_usuario"),
        @UniqueConstraint(name = "uq_usuarios_correo", columnNames = "correo")
})
public class UsuarioEntidad {

    @Id
    @Column(name = "id", nullable = false, length = Usuario.LONGITUD_MAXIMA_ID)
    private String id;

    @Column(name = "nombre_usuario", nullable = false, length = Usuario.LONGITUD_MAXIMA_NOMBRE_USUARIO)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false, length = Usuario.LONGITUD_MAXIMA_CONTRASENA_CODIFICADA)
    private String contrasenaCodificada;

    @Column(name = "correo", nullable = false, length = Usuario.LONGITUD_MAXIMA_CORREO)
    private String correo;

    @Column(name = "nombre", nullable = false, length = Usuario.LONGITUD_MAXIMA_NOMBRE)
    private String nombre;

    protected UsuarioEntidad() {
    }

    public UsuarioEntidad(Usuario usuario) {
        id = usuario.id();
        nombreUsuario = usuario.nombreUsuario();
        contrasenaCodificada = usuario.contrasenaCodificada();
        correo = usuario.correo();
        nombre = usuario.nombre();
    }

    public Usuario aDominio() {
        return new Usuario(id, nombreUsuario, contrasenaCodificada, correo, nombre);
    }
}
