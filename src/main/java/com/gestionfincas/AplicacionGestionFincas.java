package com.gestionfincas;

import com.gestionfincas.aplicacion.finca.GestionFincas;
import com.gestionfincas.aplicacion.finca.GestionFincasServicio;
import com.gestionfincas.aplicacion.finca.RepositorioFincas;
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
            RepositorioFincas repositorioFincas,
            CodificadorContrasena codificadorContrasena) {
        return new GestionUsuariosServicio(repositorioUsuarios, repositorioFincas, codificadorContrasena);
    }

    @Bean
    public GestionFincas gestionFincas(RepositorioFincas repositorioFincas, RepositorioUsuarios repositorioUsuarios) {
        return new GestionFincasServicio(repositorioFincas, repositorioUsuarios);
    }
}
