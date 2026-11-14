package com.gestionfincas.aplicacion.finca;

import com.gestionfincas.dominio.Finca;

import java.util.List;

public interface GestionFincas {

    Finca crear(DatosFinca datos);

    Finca consultar(String fincaId);

    List<Finca> listar();

    Finca actualizar(String fincaId, DatosFinca datos);

    void eliminar(String fincaId);
}
