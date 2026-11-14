package com.gestionfincas.infraestructura.seguridad;

import com.gestionfincas.aplicacion.usuario.CodificadorContrasena;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdaptadorCodificadorContrasena implements CodificadorContrasena {

    private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    @Override
    public String codificar(String contrasena) {
        return codificador.encode(contrasena);
    }

    @Override
    public boolean coincide(String contrasena, String contrasenaCodificada) {
        return codificador.matches(contrasena, contrasenaCodificada);
    }
}
