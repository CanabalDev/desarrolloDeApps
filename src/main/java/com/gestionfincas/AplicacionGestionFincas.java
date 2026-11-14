package com.gestionfincas;

import com.gestionfincas.aplicacion.usuario.CodificadorContrasena;
import com.gestionfincas.aplicacion.usuario.GestionUsuarios;
import com.gestionfincas.aplicacion.usuario.GestionUsuariosServicio;
import com.gestionfincas.aplicacion.usuario.RepositorioUsuarios;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AplicacionGestionFincas {

    public static void main(String[] argumentos) {
        SpringApplication.run(AplicacionGestionFincas.class, argumentos);
    }

    @Bean
    public GestionUsuarios gestionUsuarios(
            RepositorioUsuarios repositorioUsuarios,
            CodificadorContrasena codificadorContrasena) {
        return new GestionUsuariosServicio(repositorioUsuarios, codificadorContrasena);
    }
}
