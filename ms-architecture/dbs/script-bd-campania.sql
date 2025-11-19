CREATE TABLE campañas (
    id SERIAL PRIMARY KEY,
    id_beneficiario INTEGER NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    descripcion TEXT,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_finalizacion TIMESTAMP,
    monto_objetivo NUMERIC(15,2) NOT NULL,
    monto_recaudado NUMERIC(15,2) DEFAULT 0,
    imagen_url TEXT
);
