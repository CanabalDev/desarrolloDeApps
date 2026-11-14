package com.gestionfincas.aplicacion.finca;

import java.math.BigDecimal;

public record DatosFinca(
        String nombreFinca,
        BigDecimal areaHa,
        BigDecimal areaM2,
        String propietarioId,
        String capatazId,
        String pais,
        String departamento,
        String ciudad,
        boolean produceLeche,
        boolean produceCereales,
        boolean produceFrutas,
        boolean produceVerduras) {
}
