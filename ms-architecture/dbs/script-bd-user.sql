-- ==========================================
-- TABLA: roles
-- ==========================================
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ==========================================
-- TABLA: users
-- ==========================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    telefono VARCHAR(50),
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    imagen_url VARCHAR(500)
);

-- ==========================================
-- TABLA INTERMEDIA: roles_users
-- Relación muchos-a-muchos entre users y roles
-- ==========================================
CREATE TABLE roles_users (
    id_rol INT NOT NULL,
    id_user INT NOT NULL,

    PRIMARY KEY (id_rol, id_user),

    FOREIGN KEY (id_rol) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE
);
