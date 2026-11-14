package com.gestionfincas.infraestructura.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositorioFincasJpa extends JpaRepository<FincaEntidad, String> {

    @Query("SELECT COUNT(finca) FROM FincaEntidad finca WHERE finca.propietario.id = :usuarioId")
    long contarPorPropietario(@Param("usuarioId") String usuarioId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE fincas SET capataz_id = NULL WHERE capataz_id = :usuarioId", nativeQuery = true)
    int quitarCapataz(@Param("usuarioId") String usuarioId);
}
