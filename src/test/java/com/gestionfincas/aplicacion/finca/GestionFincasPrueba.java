package com.gestionfincas.aplicacion.finca;

import com.gestionfincas.aplicacion.usuario.RepositorioUsuarios;
import com.gestionfincas.dominio.Finca;
import com.gestionfincas.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestionFincasPrueba {

    @Mock
    private RepositorioFincas repositorioFincas;

    @Mock
    private RepositorioUsuarios repositorioUsuarios;

    private GestionFincasServicio gestionFincas;

    @BeforeEach
    void preparar() {
        gestionFincas = new GestionFincasServicio(repositorioFincas, repositorioUsuarios);
    }

    @Test
    void asignaPropietarioYPermiteCapatazOpcional() {
        Usuario usuario = crearUsuario("usuario-1");
        when(repositorioUsuarios.buscarPorId("usuario-1")).thenReturn(Optional.of(usuario));
        when(repositorioFincas.guardar(any(Finca.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Finca finca = gestionFincas.crear(datosFinca("usuario-1", null));

        assertEquals("usuario-1", finca.propietario().id());
        assertNull(finca.capataz());
    }

    @Test
    void permiteLaMismaPersonaComoPropietarioYCapataz() {
        Usuario usuario = crearUsuario("usuario-1");
        when(repositorioUsuarios.buscarPorId("usuario-1")).thenReturn(Optional.of(usuario));
        when(repositorioFincas.guardar(any(Finca.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Finca finca = gestionFincas.crear(datosFinca("usuario-1", "usuario-1"));

        assertEquals(finca.propietario().id(), finca.capataz().id());
    }

    @Test
    void exigeQueExistaElPropietario() {
        when(repositorioUsuarios.buscarPorId("usuario-ausente")).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class,
                () -> gestionFincas.crear(datosFinca("usuario-ausente", null)));
    }

    private DatosFinca datosFinca(String propietarioId, String capatazId) {
        return new DatosFinca(
                "Finca de prueba",
                new BigDecimal("1.23"),
                new BigDecimal("12345.00"),
                propietarioId,
                capatazId,
                "Colombia",
                "Antioquia",
                "Medellín",
                true,
                false,
                false,
                false);
    }

    private Usuario crearUsuario(String usuarioId) {
        return new Usuario(usuarioId, "ana", "hash", "ana@ejemplo.com", "Ana");
    }
}
