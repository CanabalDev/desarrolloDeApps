package com.gestionfincas.infraestructura.persistencia;

import com.gestionfincas.dominio.Finca;
import com.gestionfincas.dominio.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest(properties = {
        "spring.flyway.enabled=true",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@ActiveProfiles("pruebas")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositorioFincasJpaPrueba {

    @Autowired
    private RepositorioFincasJpa repositorioFincas;

    @Autowired
    private RepositorioUsuariosJpa repositorioUsuarios;

    @Test
    void persisteRelacionesYPermiteDesvincularCapataz() {
        Usuario usuario = new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana");
        UsuarioEntidad propietario = repositorioUsuarios.saveAndFlush(new UsuarioEntidad(usuario));
        Finca finca = new Finca(
                "finca-1",
                "Finca de prueba",
                new BigDecimal("1.23"),
                new BigDecimal("12300.00"),
                usuario,
                usuario,
                "Colombia",
                "Antioquia",
                "Medellín",
                true,
                false,
                false,
                false);
        repositorioFincas.saveAndFlush(new FincaEntidad(finca, propietario, propietario));

        assertEquals(1, repositorioFincas.contarPorPropietario("usuario-1"));
        repositorioFincas.quitarCapataz("usuario-1");

        Finca recuperada = repositorioFincas.findById("finca-1").orElseThrow().aDominio();
        assertEquals("usuario-1", recuperada.propietario().id());
        assertNull(recuperada.capataz());
    }
}
