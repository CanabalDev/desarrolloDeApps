CREATE TABLE fincas (
    id VARCHAR(36) NOT NULL,
    nombre_finca VARCHAR(150) NOT NULL,
    area_ha NUMERIC(16, 2) NOT NULL,
    area_m2 NUMERIC(16, 2) NOT NULL,
    propietario_id VARCHAR(36) NOT NULL,
    capataz_id VARCHAR(36),
    pais VARCHAR(100),
    departamento VARCHAR(100),
    ciudad VARCHAR(100),
    produce_leche BOOLEAN NOT NULL,
    produce_cereales BOOLEAN NOT NULL,
    produce_frutas BOOLEAN NOT NULL,
    produce_verduras BOOLEAN NOT NULL,
    CONSTRAINT pk_fincas PRIMARY KEY (id),
    CONSTRAINT fk_fincas_propietario FOREIGN KEY (propietario_id)
        REFERENCES usuarios (id) ON DELETE RESTRICT,
    CONSTRAINT fk_fincas_capataz FOREIGN KEY (capataz_id)
        REFERENCES usuarios (id) ON DELETE SET NULL
);
