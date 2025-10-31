-- =====================================================
-- Tablas básicas: users, roles y roles_users
-- =====================================================

-- Tabla: roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla: users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(200),
    active BOOLEAN NOT NULL
);

-- Tabla intermedia: roles_users
CREATE TABLE roles_users (
    id SERIAL PRIMARY KEY,
    id_rol INT NOT NULL,
    id_user INT NOT NULL,
    CONSTRAINT fk_roles FOREIGN KEY (id_rol) REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_users FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_roles_users UNIQUE (id_rol, id_user)
);
