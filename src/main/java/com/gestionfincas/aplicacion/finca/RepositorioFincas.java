package com.gestionfincas.aplicacion.finca;

import com.gestionfincas.dominio.Finca;

import java.util.List;
import java.util.Optional;

public interface RepositorioFincas {

    Optional<Finca> buscarPorId(String fincaId);

    List<Finca> listar();

    Finca guardar(Finca finca);

    void eliminar(String fincaId);

    boolean existePropietario(String usuarioId);

    void quitarCapataz(String usuarioId);
}
