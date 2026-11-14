CREATE TABLE usuarios (
    id VARCHAR(36) NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    correo VARCHAR(254) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uq_usuarios_nombre_usuario UNIQUE (nombre_usuario),
    CONSTRAINT uq_usuarios_correo UNIQUE (correo)
);
