package com.gestionfincas.infraestructura.api;

import com.gestionfincas.aplicacion.finca.DatosFinca;
import com.gestionfincas.aplicacion.finca.GestionFincas;
import com.gestionfincas.aplicacion.finca.RepositorioFincas;
import com.gestionfincas.aplicacion.usuario.CodificadorContrasena;
import com.gestionfincas.aplicacion.usuario.GestionUsuarios;
import com.gestionfincas.aplicacion.usuario.RepositorioUsuarios;
import com.gestionfincas.dominio.Finca;
import com.gestionfincas.dominio.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UsuarioControlador.class, FincaControlador.class})
class ControladoresApiPrueba {

    @Autowired
    private MockMvc solicitudes;

    @MockBean
    private GestionUsuarios gestionUsuarios;

    @MockBean
    private GestionFincas gestionFincas;

    @MockBean
    private RepositorioUsuarios repositorioUsuarios;

    @MockBean
    private RepositorioFincas repositorioFincas;

    @MockBean
    private CodificadorContrasena codificadorContrasena;

    @Test
    void devuelveUsuarioSinExponerContrasena() throws Exception {
        Usuario usuario = new Usuario("usuario-1", "ana", "hash-secreto", "ana@ejemplo.com", "Ana");
        when(gestionUsuarios.consultar("usuario-1")).thenReturn(usuario);

        solicitudes.perform(get("/api/usuario/usuario-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUsuario").value("ana"))
                .andExpect(jsonPath("$.contrasena").doesNotExist())
                .andExpect(jsonPath("$.contrasenaCodificada").doesNotExist());
    }

    @Test
    void listaUsuariosAplicandoFiltros() throws Exception {
        Usuario usuario = new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana");
        when(gestionUsuarios.filtrar("ana", null, null)).thenReturn(List.of(usuario));

        solicitudes.perform(get("/api/usuarios").param("nombreUsuario", "ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreUsuario").value("ana"))
                .andExpect(jsonPath("$[0].contrasenaCodificada").doesNotExist());
    }

    @Test
    void eliminaUsuarioYDevuelveSinContenido() throws Exception {
        solicitudes.perform(delete("/api/usuario/usuario-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void rechazaInicioDeSesionConCredencialesIncorrectas() throws Exception {
        when(gestionUsuarios.iniciarSesion("ana", "incorrecta")).thenReturn(false);

        solicitudes.perform(post("/api/usuarios/iniciar-sesion")
                        .contentType("application/json")
                        .content("""
                                {"nombreUsuario":"ana","contrasena":"incorrecta"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.autenticado").value(false));
    }

    @Test
    void validaDatosObligatoriosAlCrearUsuario() throws Exception {
        solicitudes.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content("""
                                {"nombreUsuario":"","contrasena":"clave","correo":"ana@ejemplo.com","nombre":"Ana"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void devuelveFincaConReferenciasDeUsuarioSinContrasena() throws Exception {
        Usuario usuario = new Usuario("usuario-1", "ana", "hash-secreto", "ana@ejemplo.com", "Ana");
        Finca finca = new Finca(
                "finca-1", "Finca Verde", new BigDecimal("1.23"), new BigDecimal("12345.00"), usuario, null,
                "Colombia", "Antioquia", "Medellín", true, false, true, false);
        when(gestionFincas.crear(any(DatosFinca.class))).thenReturn(finca);

        solicitudes.perform(post("/api/fincas")
                        .contentType("application/json")
                        .content("""
                                {
                                  "nombreFinca":"Finca Verde",
                                  "areaHa":1.23,
                                  "areaM2":12345.00,
                                  "propietarioId":"usuario-1",
                                  "pais":"Colombia",
                                  "departamento":"Antioquia",
                                  "ciudad":"Medellín",
                                  "produceLeche":true,
                                  "produceCereales":false,
                                  "produceFrutas":true,
                                  "produceVerduras":false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.propietario.nombreUsuario").value("ana"))
                .andExpect(jsonPath("$.propietario.contrasena").doesNotExist())
                .andExpect(jsonPath("$.capataz").value(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    void listaFincasAplicandoFiltroDeNombre() throws Exception {
        Usuario usuario = new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana");
        Finca finca = new Finca(
                "finca-1", "Finca Verde", new BigDecimal("1.23"), new BigDecimal("12345.00"), usuario, null,
                "Colombia", "Antioquia", "Medellín", true, false, true, false);
        when(gestionFincas.filtrar("verde", null, null, null, null)).thenReturn(List.of(finca));

        solicitudes.perform(get("/api/fincas").param("nombreFinca", "verde"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreFinca").value("Finca Verde"));
    }

    @Test
    void eliminaFincaYDevuelveSinContenido() throws Exception {
        solicitudes.perform(delete("/api/finca/finca-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void convierteUsuarioNoEncontradoEnRespuesta404() throws Exception {
        when(gestionUsuarios.consultar("ausente"))
                .thenThrow(new NoSuchElementException("No existe el usuario solicitado."));

        solicitudes.perform(get("/api/usuario/ausente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe el usuario solicitado."));
    }
}
