package com.gestionfincas.aplicacion.usuario;

public interface CodificadorContrasena {

    String codificar(String contrasena);

    boolean coincide(String contrasena, String contrasenaCodificada);
}
