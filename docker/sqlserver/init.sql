IF DB_ID('arquitectura_sqlserver') IS NULL
BEGIN
    CREATE DATABASE arquitectura_sqlserver;
END;
GO

USE arquitectura_sqlserver;
GO

IF OBJECT_ID('productos', 'U') IS NULL
BEGIN
    CREATE TABLE productos (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        nombre NVARCHAR(120) NOT NULL,
        descripcion NVARCHAR(255),
        precio DECIMAL(10,2) NOT NULL,
        stock INT NOT NULL
    );
END;
GO

IF OBJECT_ID('usuarios', 'U') IS NULL
BEGIN
    CREATE TABLE usuarios (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(80) NOT NULL UNIQUE,
        password_hash NVARCHAR(255) NOT NULL,
        rol NVARCHAR(40) NOT NULL,
        activo BIT NOT NULL DEFAULT 1
    );
END;
GO

IF NOT EXISTS (SELECT 1 FROM productos)
BEGIN
    INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
    ('Laptop', 'Equipo de prueba', 850.00, 10),
    ('Mouse', 'Periferico de prueba', 15.50, 50),
    ('Teclado', 'Periferico de prueba', 28.90, 30);
END;
GO
