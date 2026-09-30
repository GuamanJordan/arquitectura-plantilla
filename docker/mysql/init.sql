CREATE TABLE IF NOT EXISTS productos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(40) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
('Laptop', 'Equipo de prueba', 850.00, 10),
('Mouse', 'Periferico de prueba', 15.50, 50),
('Teclado', 'Periferico de prueba', 28.90, 30);

INSERT INTO usuarios (username, password_hash, rol, activo) VALUES
('admin', 'admin', 'ADMIN', TRUE),
('cliente', 'cliente', 'CLIENTE', TRUE);
