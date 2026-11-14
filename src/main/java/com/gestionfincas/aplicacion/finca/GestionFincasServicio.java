package com.gestionfincas.aplicacion.finca;

import com.gestionfincas.aplicacion.usuario.RepositorioUsuarios;
import com.gestionfincas.dominio.Finca;
import com.gestionfincas.dominio.Usuario;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

public final class GestionFincasServicio implements GestionFincas {

    private final RepositorioFincas repositorioFincas;
    private final RepositorioUsuarios repositorioUsuarios;

    public GestionFincasServicio(RepositorioFincas repositorioFincas, RepositorioUsuarios repositorioUsuarios) {
        this.repositorioFincas = repositorioFincas;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    @Override
    public Finca crear(DatosFinca datos) {
        return repositorioFincas.guardar(crearFinca(UUID.randomUUID().toString(), datos));
    }

    @Override
    public Finca consultar(String fincaId) {
        return buscarFinca(fincaId);
    }

    @Override
    public List<Finca> listar() {
        return repositorioFincas.listar();
    }

    @Override
    public List<Finca> filtrar(
            String nombreFinca,
            String propietarioId,
            String pais,
            String departamento,
            String ciudad) {
        return repositorioFincas.listar().stream()
                .filter(finca -> coincide(finca.nombreFinca(), nombreFinca))
                .filter(finca -> propietarioId == null || propietarioId.isBlank()
                        || finca.propietario().id().equalsIgnoreCase(propietarioId.strip()))
                .filter(finca -> coincide(finca.pais(), pais))
                .filter(finca -> coincide(finca.departamento(), departamento))
                .filter(finca -> coincide(finca.ciudad(), ciudad))
                .toList();
    }

    @Override
    public Finca actualizar(String fincaId, DatosFinca datos) {
        buscarFinca(fincaId);
        return repositorioFincas.guardar(crearFinca(fincaId, datos));
    }

    @Override
    public void eliminar(String fincaId) {
        buscarFinca(fincaId);
        repositorioFincas.eliminar(fincaId);
    }

    private Finca buscarFinca(String fincaId) {
        return repositorioFincas.buscarPorId(fincaId)
                .orElseThrow(() -> new NoSuchElementException("No existe una finca con el identificador indicado."));
    }

    private Finca crearFinca(String fincaId, DatosFinca datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos de la finca son obligatorios.");
        }
        Usuario propietario = buscarUsuarioObligatorio(datos.propietarioId(), "propietario");
        Usuario capataz = buscarUsuarioOpcional(datos.capatazId());

        return new Finca(
                fincaId,
                datos.nombreFinca(),
                datos.areaHa(),
                datos.areaM2(),
                propietario,
                capataz,
                datos.pais(),
                datos.departamento(),
                datos.ciudad(),
                datos.produceLeche(),
                datos.produceCereales(),
                datos.produceFrutas(),
                datos.produceVerduras());
    }

    private Usuario buscarUsuarioObligatorio(String usuarioId, String rol) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new IllegalArgumentException("El usuario asignado como " + rol + " es obligatorio.");
        }
        return repositorioUsuarios.buscarPorId(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("No existe el usuario asignado como " + rol + "."));
    }

    private Usuario buscarUsuarioOpcional(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            return null;
        }
        return repositorioUsuarios.buscarPorId(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("No existe el usuario asignado como capataz."));
    }

    private boolean coincide(String valor, String filtro) {
        return filtro == null || filtro.isBlank()
                || (valor != null && valor.toLowerCase(Locale.ROOT).contains(filtro.strip().toLowerCase(Locale.ROOT)));
    }
}
