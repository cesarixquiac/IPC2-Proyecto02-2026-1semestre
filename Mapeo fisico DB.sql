CREATE TABLE categoria (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO'
);

CREATE TABLE usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    cui VARCHAR(13) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    rol ENUM('ADMIN','CLIENTE','FREELANCER') NOT NULL,
    estado_cuenta ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO'
);

CREATE TABLE cliente (
    id_cliente INT PRIMARY KEY,
    descripcion_empresa TEXT,
    sector_industria VARCHAR(100),
    sitio_web VARCHAR(255),
    saldo_disponible DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE freelancer (
    id_freelancer INT PRIMARY KEY,
    biografia TEXT,
    nivel_experiencia ENUM('JUNIOR','SEMI_SENIOR','SENIOR'),
    tarifa_hora DECIMAL(10,2),
    saldo_acumulado DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (id_freelancer) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE habilidad (
    id_habilidad INT PRIMARY KEY AUTO_INCREMENT,
    id_categoria INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    estado ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE CASCADE
);

CREATE TABLE freelancer_habilidad (
    id_freelancer INT,
    id_habilidad INT,
    PRIMARY KEY (id_freelancer, id_habilidad),
    FOREIGN KEY (id_freelancer) REFERENCES freelancer(id_freelancer) ON DELETE CASCADE,
    FOREIGN KEY (id_habilidad) REFERENCES habilidad(id_habilidad) ON DELETE CASCADE
);

CREATE TABLE proyecto (
    id_proyecto INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    id_categoria INT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    presupuesto_maximo DECIMAL(10,2) NOT NULL,
    fecha_limite DATE NOT NULL,
    fecha_publicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM(
        'ABIERTO',
        'EN_REVISION',
        'EN_PROGRESO',
        'ENTREGA_PENDIENTE',
        'COMPLETADO',
        'CANCELADO'
    ) DEFAULT 'ABIERTO',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE proyecto_habilidad (
    id_proyecto INT,
    id_habilidad INT,
    PRIMARY KEY (id_proyecto, id_habilidad),
    FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto) ON DELETE CASCADE,
    FOREIGN KEY (id_habilidad) REFERENCES habilidad(id_habilidad) ON DELETE CASCADE
);

CREATE TABLE propuesta (
    id_propuesta INT PRIMARY KEY AUTO_INCREMENT,
    id_proyecto INT NOT NULL,
    id_freelancer INT NOT NULL,
    monto_ofertado DECIMAL(10,2) NOT NULL,
    plazo_dias INT NOT NULL,
    carta_presentacion TEXT NOT NULL,
    estado ENUM(
        'PENDIENTE',
        'ACEPTADA',
        'RECHAZADA',
        'RETIRADA'
    ) DEFAULT 'PENDIENTE',
    fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto) ON DELETE CASCADE,
    FOREIGN KEY (id_freelancer) REFERENCES freelancer(id_freelancer) ON DELETE CASCADE
);

CREATE TABLE contrato (
    id_contrato INT PRIMARY KEY AUTO_INCREMENT,
    id_propuesta INT NOT NULL UNIQUE,
    monto_bloqueado DECIMAL(10,2) NOT NULL,
    porcentaje_comision_aplicado DECIMAL(5,2) NOT NULL,
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    motivo_cancelacion TEXT,
    FOREIGN KEY (id_propuesta) REFERENCES propuesta(id_propuesta) ON DELETE CASCADE
);

CREATE TABLE entrega (
    id_entrega INT PRIMARY KEY AUTO_INCREMENT,
    id_contrato INT NOT NULL,
    descripcion TEXT NOT NULL,
    url_archivo VARCHAR(255),
    estado ENUM('PENDIENTE','APROBADA','RECHAZADA') DEFAULT 'PENDIENTE',
    motivo_rechazo TEXT,
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_contrato) REFERENCES contrato(id_contrato) ON DELETE CASCADE
);

CREATE TABLE calificacion (
    id_calificacion INT PRIMARY KEY AUTO_INCREMENT,
    id_contrato INT NOT NULL UNIQUE,
    estrellas INT CHECK (estrellas >= 1 AND estrellas <= 5),
    comentario TEXT,
    fecha_calificacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_contrato) REFERENCES contrato(id_contrato) ON DELETE CASCADE
);

CREATE TABLE historial_comision (
    id_historial_comision INT PRIMARY KEY AUTO_INCREMENT,
    porcentaje DECIMAL(5,2) NOT NULL,
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_fin DATETIME
);

CREATE TABLE ingreso_plataforma (
    id_ingreso INT PRIMARY KEY AUTO_INCREMENT,
    id_contrato INT NOT NULL,
    monto_comision_cobrada DECIMAL(10,2) NOT NULL,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_contrato) REFERENCES contrato(id_contrato) ON DELETE CASCADE
);

CREATE TABLE recarga_saldo (
    id_recarga INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_recarga DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

CREATE TABLE solicitud_catalogo (
    id_solicitud INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario_solicitante INT NOT NULL,
    tipo_solicitud ENUM('CATEGORIA','HABILIDAD') NOT NULL,
    nombre_sugerido VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado ENUM('PENDIENTE','ACEPTADA','RECHAZADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_usuario_solicitante)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
);