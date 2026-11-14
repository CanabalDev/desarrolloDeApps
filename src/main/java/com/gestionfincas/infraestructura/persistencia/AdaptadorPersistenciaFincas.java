package com.gestionfincas.infraestructura.persistencia;

import com.gestionfincas.aplicacion.finca.RepositorioFincas;
import com.gestionfincas.dominio.Finca;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AdaptadorPersistenciaFincas implements RepositorioFincas {

    private final RepositorioFincasJpa repositorioFincas;
    private final RepositorioUsuariosJpa repositorioUsuarios;

    public AdaptadorPersistenciaFincas(
            RepositorioFincasJpa repositorioFincas,
            RepositorioUsuariosJpa repositorioUsuarios) {
        this.repositorioFincas = repositorioFincas;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    @Override
    public Optional<Finca> buscarPorId(String fincaId) {
        return repositorioFincas.findById(fincaId).map(FincaEntidad::aDominio);
    }

    @Override
    public List<Finca> listar() {
        return repositorioFincas.findAll().stream().map(FincaEntidad::aDominio).toList();
    }

    @Override
    public Finca guardar(Finca finca) {
        UsuarioEntidad propietario = repositorioUsuarios.getReferenceById(finca.propietario().id());
        UsuarioEntidad capataz = finca.capataz() == null
                ? null
                : repositorioUsuarios.getReferenceById(finca.capataz().id());
        return repositorioFincas.save(new FincaEntidad(finca, propietario, capataz)).aDominio();
    }

    @Override
    public void eliminar(String fincaId) {
        repositorioFincas.deleteById(fincaId);
    }

    @Override
    public boolean existePropietario(String usuarioId) {
        return repositorioFincas.contarPorPropietario(usuarioId) > 0;
    }

    @Override
    public void quitarCapataz(String usuarioId) {
        repositorioFincas.quitarCapataz(usuarioId);
    }
}
