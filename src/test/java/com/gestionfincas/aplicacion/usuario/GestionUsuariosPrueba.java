package com.gestionfincas.aplicacion.usuario;

import com.gestionfincas.aplicacion.finca.RepositorioFincas;
import com.gestionfincas.dominio.Usuario;
import com.gestionfincas.infraestructura.seguridad.AdaptadorCodificadorContrasena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestionUsuariosPrueba {

    @Mock
    private RepositorioUsuarios repositorioUsuarios;

    @Mock
    private RepositorioFincas repositorioFincas;

    @Mock
    private CodificadorContrasena codificadorContrasena;

    private GestionUsuariosServicio gestionUsuarios;

    @BeforeEach
    void preparar() {
        gestionUsuarios = new GestionUsuariosServicio(repositorioUsuarios, repositorioFincas, codificadorContrasena);
    }

    @Test
    void creaUsuarioConDatosNormalizadosYContrasenaCodificada() {
        when(codificadorContrasena.codificar("clave")).thenReturn("hash");
        when(repositorioUsuarios.guardar(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario usuario = gestionUsuarios.crear(" Ana ", "clave", " ANA@EJEMPLO.COM ", "Ana Pérez");

        assertFalse(usuario.id().isBlank());
        assertEquals("ana", usuario.nombreUsuario());
        assertEquals("ana@ejemplo.com", usuario.correo());
        assertEquals("hash", usuario.contrasenaCodificada());
        verify(repositorioUsuarios).existeNombreUsuario("ana", null);
        verify(repositorioUsuarios).existeCorreo("ana@ejemplo.com", null);
    }

    @Test
    void rechazaNombreDeUsuarioDuplicado() {
        when(repositorioUsuarios.existeNombreUsuario("ana", null)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> gestionUsuarios.crear("Ana", "clave", "ana@ejemplo.com", "Ana"));
        verify(repositorioUsuarios, never()).guardar(any(Usuario.class));
        verify(codificadorContrasena, never()).codificar(any());
    }

    @Test
    void permiteConservarDatosUnicosAlActualizarElMismoUsuario() {
        Usuario existente = new Usuario("usuario-1", "ana", "hash-anterior", "ana@ejemplo.com", "Ana");
        when(repositorioUsuarios.buscarPorId("usuario-1")).thenReturn(Optional.of(existente));
        when(codificadorContrasena.codificar("clave-nueva")).thenReturn("hash-nuevo");
        when(repositorioUsuarios.guardar(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario actualizado = gestionUsuarios.actualizar(
                "usuario-1", "ANA", "clave-nueva", "ANA@EJEMPLO.COM", "Ana Pérez");

        assertEquals("usuario-1", actualizado.id());
        assertEquals("hash-nuevo", actualizado.contrasenaCodificada());
        verify(repositorioUsuarios).existeNombreUsuario("ana", "usuario-1");
        verify(repositorioUsuarios).existeCorreo("ana@ejemplo.com", "usuario-1");
    }

    @Test
    void iniciaSesionConNombreDeUsuarioSinDistinguirMayusculas() {
        Usuario usuario = new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana");
        when(repositorioUsuarios.buscarPorNombreUsuario("ana")).thenReturn(Optional.of(usuario));
        when(codificadorContrasena.coincide("clave", "hash")).thenReturn(true);

        assertTrue(gestionUsuarios.iniciarSesion("ANA", "clave"));
        assertFalse(gestionUsuarios.iniciarSesion("ana", " "));
    }

    @Test
    void filtraUsuariosPorNombreUsuarioCorreoYNombre() {
        Usuario usuario = new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana Pérez");
        when(repositorioUsuarios.listar()).thenReturn(List.of(usuario));

        assertEquals(List.of(usuario), gestionUsuarios.filtrar("AN", "ejemplo", "pÉREZ"));
    }

    @Test
    void impideEliminarUsuarioQueEsPropietario() {
        when(repositorioUsuarios.buscarPorId("usuario-1"))
                .thenReturn(Optional.of(new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana")));
        when(repositorioFincas.existePropietario("usuario-1")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> gestionUsuarios.eliminar("usuario-1"));

        verify(repositorioFincas, never()).quitarCapataz("usuario-1");
        verify(repositorioUsuarios, never()).eliminar("usuario-1");
    }

    @Test
    void alEliminarUsuarioDesvinculaPrimeroSuAsignacionComoCapataz() {
        when(repositorioUsuarios.buscarPorId("usuario-1"))
                .thenReturn(Optional.of(new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana")));

        gestionUsuarios.eliminar("usuario-1");

        verify(repositorioFincas).quitarCapataz("usuario-1");
        verify(repositorioUsuarios).eliminar("usuario-1");
    }

    @Test
    void elCodificadorNoGuardaLaContrasenaEnTextoPlano() {
        AdaptadorCodificadorContrasena codificador = new AdaptadorCodificadorContrasena();

        String contrasenaCodificada = codificador.codificar("clave-segura");

        assertNotEquals("clave-segura", contrasenaCodificada);
        assertTrue(codificador.coincide("clave-segura", contrasenaCodificada));
        assertFalse(codificador.coincide("clave-incorrecta", contrasenaCodificada));
    }
}
