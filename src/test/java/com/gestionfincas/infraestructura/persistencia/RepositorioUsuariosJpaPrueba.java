package com.gestionfincas.infraestructura.persistencia;

import com.gestionfincas.dominio.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(properties = {
        "spring.flyway.enabled=true",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@ActiveProfiles("pruebas")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositorioUsuariosJpaPrueba {

    @Autowired
    private RepositorioUsuariosJpa repositorio;

    @Test
    void impideNombresDeUsuarioDuplicadosSinDistinguirMayusculas() {
        repositorio.saveAndFlush(new UsuarioEntidad(
                new Usuario("usuario-1", "ana", "hash-1", "ana@ejemplo.com", "Ana")));

        assertThrows(DataIntegrityViolationException.class, () -> repositorio.saveAndFlush(new UsuarioEntidad(
                new Usuario("usuario-2", "ANA", "hash-2", "otra@ejemplo.com", "Otra Ana"))));
    }

    @Test
    void impideCorreosDuplicadosSinDistinguirMayusculas() {
        repositorio.saveAndFlush(new UsuarioEntidad(
                new Usuario("usuario-1", "ana", "hash-1", "ana@ejemplo.com", "Ana")));

        assertThrows(DataIntegrityViolationException.class, () -> repositorio.saveAndFlush(new UsuarioEntidad(
                new Usuario("usuario-2", "otra", "hash-2", "ANA@EJEMPLO.COM", "Otra Ana"))));
    }
}
