package com.gestionfincas.infraestructura.api;

import com.gestionfincas.aplicacion.finca.DatosFinca;
import com.gestionfincas.aplicacion.finca.GestionFincas;
import com.gestionfincas.dominio.Finca;
import com.gestionfincas.dominio.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
public class FincaControlador {

    private final GestionFincas gestionFincas;

    public FincaControlador(GestionFincas gestionFincas) {
        this.gestionFincas = gestionFincas;
    }

    @GetMapping("/api/fincas")
    public List<RespuestaFinca> listar(
            @RequestParam(name = "nombreFinca", required = false) String nombreFinca,
            @RequestParam(name = "propietarioId", required = false) String propietarioId,
            @RequestParam(name = "pais", required = false) String pais,
            @RequestParam(name = "departamento", required = false) String departamento,
            @RequestParam(name = "ciudad", required = false) String ciudad) {
        return gestionFincas.filtrar(nombreFinca, propietarioId, pais, departamento, ciudad)
                .stream().map(this::respuesta).toList();
    }

    @PostMapping("/api/fincas")
    public ResponseEntity<RespuestaFinca> crear(@Valid @RequestBody SolicitudFinca solicitud) {
        Finca finca = gestionFincas.crear(solicitud.datos());
        return ResponseEntity.created(URI.create("/api/finca/" + finca.id())).body(respuesta(finca));
    }

    @GetMapping("/api/finca/{fincaId}")
    public RespuestaFinca consultar(@PathVariable("fincaId") String fincaId) {
        return respuesta(gestionFincas.consultar(fincaId));
    }

    @PutMapping("/api/finca/{fincaId}")
    public RespuestaFinca actualizar(
            @PathVariable("fincaId") String fincaId,
            @Valid @RequestBody SolicitudFinca solicitud) {
        return respuesta(gestionFincas.actualizar(fincaId, solicitud.datos()));
    }

    @DeleteMapping("/api/finca/{fincaId}")
    public ResponseEntity<Void> eliminar(@PathVariable("fincaId") String fincaId) {
        gestionFincas.eliminar(fincaId);
        return ResponseEntity.noContent().build();
    }

    private RespuestaFinca respuesta(Finca finca) {
        return new RespuestaFinca(
                finca.id(),
                finca.nombreFinca(),
                finca.areaHa(),
                finca.areaM2(),
                referencia(finca.propietario()),
                finca.capataz() == null ? null : referencia(finca.capataz()),
                finca.pais(),
                finca.departamento(),
                finca.ciudad(),
                finca.produceLeche(),
                finca.produceCereales(),
                finca.produceFrutas(),
                finca.produceVerduras());
    }

    private ReferenciaUsuario referencia(Usuario usuario) {
        return new ReferenciaUsuario(usuario.id(), usuario.nombreUsuario(), usuario.nombre());
    }

    public record SolicitudFinca(
            @NotBlank @Size(max = 150) String nombreFinca,
            @NotNull @DecimalMin("0.00") @Digits(integer = 14, fraction = 2) BigDecimal areaHa,
            @NotNull @DecimalMin("0.00") @Digits(integer = 14, fraction = 2) BigDecimal areaM2,
            @NotBlank @Size(max = 36) String propietarioId,
            @Size(max = 36) String capatazId,
            @Size(max = 100) String pais,
            @Size(max = 100) String departamento,
            @Size(max = 100) String ciudad,
            boolean produceLeche,
            boolean produceCereales,
            boolean produceFrutas,
            boolean produceVerduras) {

        public DatosFinca datos() {
            return new DatosFinca(
                    nombreFinca,
                    areaHa,
                    areaM2,
                    propietarioId,
                    capatazId,
                    pais,
                    departamento,
                    ciudad,
                    produceLeche,
                    produceCereales,
                    produceFrutas,
                    produceVerduras);
        }
    }

    public record ReferenciaUsuario(String id, String nombreUsuario, String nombre) {
    }

    public record RespuestaFinca(
            String id,
            String nombreFinca,
            BigDecimal areaHa,
            BigDecimal areaM2,
            ReferenciaUsuario propietario,
            ReferenciaUsuario capataz,
            String pais,
            String departamento,
            String ciudad,
            boolean produceLeche,
            boolean produceCereales,
            boolean produceFrutas,
            boolean produceVerduras) {
    }
}
