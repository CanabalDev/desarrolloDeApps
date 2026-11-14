package com.gestionfincas.infraestructura.persistencia;

import com.gestionfincas.dominio.Finca;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "fincas")
public class FincaEntidad {

    @Id
    @Column(name = "id", nullable = false, length = Finca.LONGITUD_MAXIMA_ID)
    private String id;

    @Column(name = "nombre_finca", nullable = false, length = Finca.LONGITUD_MAXIMA_NOMBRE)
    private String nombreFinca;

    @Column(name = "area_ha", nullable = false, precision = Finca.PRECISION_AREA, scale = Finca.ESCALA_AREA)
    private BigDecimal areaHa;

    @Column(name = "area_m2", nullable = false, precision = Finca.PRECISION_AREA, scale = Finca.ESCALA_AREA)
    private BigDecimal areaM2;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propietario_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_fincas_propietario"))
    private UsuarioEntidad propietario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capataz_id", foreignKey = @ForeignKey(name = "fk_fincas_capataz"))
    private UsuarioEntidad capataz;

    @Column(name = "pais", length = Finca.LONGITUD_MAXIMA_UBICACION)
    private String pais;

    @Column(name = "departamento", length = Finca.LONGITUD_MAXIMA_UBICACION)
    private String departamento;

    @Column(name = "ciudad", length = Finca.LONGITUD_MAXIMA_UBICACION)
    private String ciudad;

    @Column(name = "produce_leche", nullable = false)
    private boolean produceLeche;

    @Column(name = "produce_cereales", nullable = false)
    private boolean produceCereales;

    @Column(name = "produce_frutas", nullable = false)
    private boolean produceFrutas;

    @Column(name = "produce_verduras", nullable = false)
    private boolean produceVerduras;

    protected FincaEntidad() {
    }

    public FincaEntidad(Finca finca, UsuarioEntidad propietario, UsuarioEntidad capataz) {
        id = finca.id();
        nombreFinca = finca.nombreFinca();
        areaHa = finca.areaHa();
        areaM2 = finca.areaM2();
        this.propietario = propietario;
        this.capataz = capataz;
        pais = finca.pais();
        departamento = finca.departamento();
        ciudad = finca.ciudad();
        produceLeche = finca.produceLeche();
        produceCereales = finca.produceCereales();
        produceFrutas = finca.produceFrutas();
        produceVerduras = finca.produceVerduras();
    }

    public Finca aDominio() {
        return new Finca(
                id,
                nombreFinca,
                areaHa,
                areaM2,
                propietario.aDominio(),
                capataz == null ? null : capataz.aDominio(),
                pais,
                departamento,
                ciudad,
                produceLeche,
                produceCereales,
                produceFrutas,
                produceVerduras);
    }
}
