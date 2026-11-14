package com.gestionfincas.infraestructura.api;

import com.gestionfincas.aplicacion.usuario.GestionUsuarios;
import com.gestionfincas.dominio.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class UsuarioControlador {

    private final GestionUsuarios gestionUsuarios;

    public UsuarioControlador(GestionUsuarios gestionUsuarios) {
        this.gestionUsuarios = gestionUsuarios;
    }

    @GetMapping("/api/usuarios")
    public List<RespuestaUsuario> listar(
            @RequestParam(name = "nombreUsuario", required = false) String nombreUsuario,
            @RequestParam(name = "correo", required = false) String correo,
            @RequestParam(name = "nombre", required = false) String nombre) {
        return gestionUsuarios.filtrar(nombreUsuario, correo, nombre).stream().map(this::respuesta).toList();
    }

    @PostMapping("/api/usuarios")
    public ResponseEntity<RespuestaUsuario> crear(@Valid @RequestBody SolicitudUsuario solicitud) {
        Usuario usuario = gestionUsuarios.crear(
                solicitud.nombreUsuario(), solicitud.contrasena(), solicitud.correo(), solicitud.nombre());
        return ResponseEntity.created(URI.create("/api/usuario/" + usuario.id())).body(respuesta(usuario));
    }

    @PostMapping("/api/usuarios/iniciar-sesion")
    public ResponseEntity<RespuestaInicioSesion> iniciarSesion(@Valid @RequestBody SolicitudInicioSesion solicitud) {
        boolean autenticado = gestionUsuarios.iniciarSesion(solicitud.nombreUsuario(), solicitud.contrasena());
        RespuestaInicioSesion respuesta = new RespuestaInicioSesion(autenticado);
        return autenticado
                ? ResponseEntity.ok(respuesta)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    @GetMapping("/api/usuario/{usuarioId}")
    public RespuestaUsuario consultar(@PathVariable("usuarioId") String usuarioId) {
        return respuesta(gestionUsuarios.consultar(usuarioId));
    }

    @PutMapping("/api/usuario/{usuarioId}")
    public RespuestaUsuario actualizar(
            @PathVariable("usuarioId") String usuarioId,
            @Valid @RequestBody SolicitudUsuario solicitud) {
        Usuario usuario = gestionUsuarios.actualizar(
                usuarioId,
                solicitud.nombreUsuario(),
                solicitud.contrasena(),
                solicitud.correo(),
                solicitud.nombre());
        return respuesta(usuario);
    }

    @DeleteMapping("/api/usuario/{usuarioId}")
    public ResponseEntity<Void> eliminar(@PathVariable("usuarioId") String usuarioId) {
        gestionUsuarios.eliminar(usuarioId);
        return ResponseEntity.noContent().build();
    }

    private RespuestaUsuario respuesta(Usuario usuario) {
        return new RespuestaUsuario(usuario.id(), usuario.nombreUsuario(), usuario.correo(), usuario.nombre());
    }

    public record SolicitudUsuario(
            @NotBlank @Size(max = 50) String nombreUsuario,
            @NotBlank String contrasena,
            @NotBlank @Email @Size(max = 254) String correo,
            @NotBlank @Size(max = 100) String nombre) {
    }

    public record SolicitudInicioSesion(
            @NotBlank String nombreUsuario,
            @NotBlank String contrasena) {
    }

    public record RespuestaUsuario(String id, String nombreUsuario, String correo, String nombre) {
    }

    public record RespuestaInicioSesion(boolean autenticado) {
    }
}
