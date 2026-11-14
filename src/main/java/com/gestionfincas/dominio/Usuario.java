package com.gestionfincas.dominio;

import java.util.Locale;

public final class Usuario {

    public static final int LONGITUD_MAXIMA_ID = 36;
    public static final int LONGITUD_MAXIMA_NOMBRE_USUARIO = 50;
    public static final int LONGITUD_MAXIMA_CONTRASENA_CODIFICADA = 100;
    public static final int LONGITUD_MAXIMA_CORREO = 254;
    public static final int LONGITUD_MAXIMA_NOMBRE = 100;

    private final String id;
    private final String nombreUsuario;
    private final String contrasenaCodificada;
    private final String correo;
    private final String nombre;

    public Usuario(String id, String nombreUsuario, String contrasenaCodificada, String correo, String nombre) {
        this.id = validarTexto(id, "El identificador es obligatorio.", LONGITUD_MAXIMA_ID);
        this.nombreUsuario = normalizarNombreUsuario(nombreUsuario);
        this.contrasenaCodificada = validarTexto(
                contrasenaCodificada,
                "La contraseña codificada es obligatoria.",
                LONGITUD_MAXIMA_CONTRASENA_CODIFICADA);
        this.correo = normalizarCorreo(correo);
        this.nombre = normalizarNombre(nombre);
    }

    public static String normalizarNombreUsuario(String nombreUsuario) {
        return validarTexto(nombreUsuario, "El nombre de usuario es obligatorio.", LONGITUD_MAXIMA_NOMBRE_USUARIO)
                .toLowerCase(Locale.ROOT);
    }

    public static String normalizarCorreo(String correo) {
        return validarTexto(correo, "El correo es obligatorio.", LONGITUD_MAXIMA_CORREO)
                .toLowerCase(Locale.ROOT);
    }

    public static String normalizarNombre(String nombre) {
        return validarTexto(nombre, "El nombre es obligatorio.", LONGITUD_MAXIMA_NOMBRE);
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

    public String id() {
        return id;
    }

    public String nombreUsuario() {
        return nombreUsuario;
    }

    public String contrasenaCodificada() {
        return contrasenaCodificada;
    }

    public String correo() {
        return correo;
    }

    public String nombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Usuario[id=" + id + ", nombreUsuario=" + nombreUsuario + ", correo=" + correo + ", nombre="
                + nombre + "]";
    }
}
