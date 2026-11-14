package com.gestionfincas.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FincaPrueba {

    private final Usuario usuario = new Usuario("usuario-1", "ana", "hash", "ana@ejemplo.com", "Ana");

    @Test
    void aceptaAreaHectareasCalculadaDesdeMetrosCuadrados() {
        Finca finca = crearFinca("1.23", "12345.00", usuario, usuario);

        assertEquals(new BigDecimal("1.23"), finca.areaHa());
        assertEquals(new BigDecimal("12345.00"), finca.areaM2());
        assertEquals(finca.propietario().id(), finca.capataz().id());
    }

    @Test
    void aceptaAreaMetrosCuadradosCalculadaDesdeHectareas() {
        Finca finca = crearFinca("1.23", "12300.00", usuario, null);

        assertEquals(new BigDecimal("12300.00"), finca.areaM2());
        assertNull(finca.capataz());
    }

    @Test
    void rechazaAreasInconsistentes() {
        assertThrows(IllegalArgumentException.class, () -> crearFinca("1.25", "12345.00", usuario, null));
    }

    @Test
    void rechazaFincaSinPropietario() {
        assertThrows(IllegalArgumentException.class, () -> crearFinca("1.00", "10000.00", null, null));
    }

    private Finca crearFinca(String areaHa, String areaM2, Usuario propietario, Usuario capataz) {
        return new Finca(
                "finca-1",
                "Finca de prueba",
                new BigDecimal(areaHa),
                new BigDecimal(areaM2),
                propietario,
                capataz,
                "Colombia",
                "Antioquia",
                "Medellín",
                true,
                false,
                false,
                false);
    }
}
