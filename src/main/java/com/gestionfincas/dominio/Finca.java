package com.gestionfincas.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Finca {

    public static final int LONGITUD_MAXIMA_ID = 36;
    public static final int LONGITUD_MAXIMA_NOMBRE = 150;
    public static final int LONGITUD_MAXIMA_UBICACION = 100;
    public static final int PRECISION_AREA = 16;
    public static final int ESCALA_AREA = 2;

    private static final BigDecimal METROS_CUADRADOS_POR_HECTAREA = new BigDecimal("10000");

    private final String id;
    private final String nombreFinca;
    private final BigDecimal areaHa;
    private final BigDecimal areaM2;
    private final Usuario propietario;
    private final Usuario capataz;
    private final String pais;
    private final String departamento;
    private final String ciudad;
    private final boolean produceLeche;
    private final boolean produceCereales;
    private final boolean produceFrutas;
    private final boolean produceVerduras;

    public Finca(
            String id,
            String nombreFinca,
            BigDecimal areaHa,
            BigDecimal areaM2,
            Usuario propietario,
            Usuario capataz,
            String pais,
            String departamento,
            String ciudad,
            boolean produceLeche,
            boolean produceCereales,
            boolean produceFrutas,
            boolean produceVerduras) {
        this.id = validarTexto(id, "El identificador de la finca es obligatorio.", LONGITUD_MAXIMA_ID);
        this.nombreFinca = validarTexto(
                nombreFinca,
                "El nombre de la finca es obligatorio.",
                LONGITUD_MAXIMA_NOMBRE);
        this.areaHa = normalizarArea(areaHa, "El área en hectáreas es obligatoria.");
        this.areaM2 = normalizarArea(areaM2, "El área en metros cuadrados es obligatoria.");
        validarConsistenciaAreas(this.areaHa, this.areaM2);
        if (propietario == null) {
            throw new IllegalArgumentException("El propietario de la finca es obligatorio.");
        }
        this.propietario = propietario;
        this.capataz = capataz;
        this.pais = normalizarOpcional(pais, LONGITUD_MAXIMA_UBICACION);
        this.departamento = normalizarOpcional(departamento, LONGITUD_MAXIMA_UBICACION);
        this.ciudad = normalizarOpcional(ciudad, LONGITUD_MAXIMA_UBICACION);
        this.produceLeche = produceLeche;
        this.produceCereales = produceCereales;
        this.produceFrutas = produceFrutas;
        this.produceVerduras = produceVerduras;
    }

    private static BigDecimal normalizarArea(BigDecimal area, String mensaje) {
        if (area == null) {
            throw new IllegalArgumentException(mensaje);
        }
        BigDecimal areaNormalizada = area.setScale(2, RoundingMode.HALF_UP);
        if (areaNormalizada.precision() > PRECISION_AREA) {
            throw new IllegalArgumentException("El área supera la precisión máxima permitida.");
        }
        if (areaNormalizada.signum() < 0) {
            throw new IllegalArgumentException("Las áreas no pueden ser negativas.");
        }
        return areaNormalizada;
    }

    private static void validarConsistenciaAreas(BigDecimal areaHa, BigDecimal areaM2) {
        BigDecimal hectareasCalculadas = areaM2.divide(METROS_CUADRADOS_POR_HECTAREA, 2, RoundingMode.HALF_UP);
        BigDecimal metrosCalculados = areaHa.multiply(METROS_CUADRADOS_POR_HECTAREA)
                .setScale(2, RoundingMode.HALF_UP);

        if (areaHa.compareTo(hectareasCalculadas) != 0 && areaM2.compareTo(metrosCalculados) != 0) {
            throw new IllegalArgumentException(
                    "Las áreas en hectáreas y metros cuadrados no coinciden con su conversión a dos decimales.");
        }
    }

    private static String validarTexto(String valor, String mensaje, int longitudMaxima) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        String valorNormalizado = valor.strip();
        if (valorNormalizado.length() > longitudMaxima) {
            throw new IllegalArgumentException("El valor supera la longitud máxima permitida de "
                    + longitudMaxima + " caracteres.");
        }
        return valorNormalizado;
    }

    private static String normalizarOpcional(String valor, int longitudMaxima) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String valorNormalizado = valor.strip();
        if (valorNormalizado.length() > longitudMaxima) {
            throw new IllegalArgumentException("El valor supera la longitud máxima permitida de "
                    + longitudMaxima + " caracteres.");
        }
        return valorNormalizado;
    }

    public String id() {
        return id;
    }

    public String nombreFinca() {
        return nombreFinca;
    }

    public BigDecimal areaHa() {
        return areaHa;
    }

    public BigDecimal areaM2() {
        return areaM2;
    }

    public Usuario propietario() {
        return propietario;
    }

    public Usuario capataz() {
        return capataz;
    }

    public String pais() {
        return pais;
    }

    public String departamento() {
        return departamento;
    }

    public String ciudad() {
        return ciudad;
    }

    public boolean produceLeche() {
        return produceLeche;
    }

    public boolean produceCereales() {
        return produceCereales;
    }

    public boolean produceFrutas() {
        return produceFrutas;
    }

    public boolean produceVerduras() {
        return produceVerduras;
    }
}
