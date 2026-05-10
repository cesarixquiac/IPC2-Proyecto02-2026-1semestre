-- INSERTS TABLA categoria
INSERT INTO categoria (id_categoria, nombre, descripcion, estado) VALUES
(1, 'Desarrollo Web', 'Creación de sitios web y aplicaciones web a medida.', 'ACTIVO'),
(2, 'Diseño Gráfico', 'Creación de logotipos, UI/UX, e ilustraciones.', 'ACTIVO'),
(3, 'Marketing Digital', 'Gestión de redes sociales, SEO y campañas publicitarias.', 'ACTIVO'),
(4, 'Musica y arteeeee', 'musica y arteeeee', 'ACTIVO'),
(9, 'Cyber seguridad', 'seguridad', 'ACTIVO');

-- INSERTS TABLA usuario
INSERT INTO usuario (id_usuario, nombre_completo, username, password, email, telefono, direccion, cui, fecha_nacimiento, rol, estado_cuenta) VALUES
(1, 'Administrador Principal', 'admin', '$2a$10$9FrjcexY8gdlWRezWomYQuxStYQzCLTAd0Z8wlQRG9bkJjKCaokOa', 'admin@connectwork.com', '11112222', 'Oficinas Centrales', '1000000000000', '1990-01-01', 'ADMIN', 'ACTIVO'),
(2, 'Tech Solutions SA', 'tech_client', 'hashed_pass_c1', 'contacto@techsolutions.com', '33334444', 'Zona 4, Edificio TEC', '2000000000000', '1985-05-15', 'CLIENTE', 'ACTIVO'),
(3, 'Marketing Global', 'mkt_client', 'hashed_pass_c2', 'info@mktglobal.com', '55556666', 'Zona 10', '3000000000000', '1992-08-20', 'CLIENTE', 'ACTIVO'),
(4, 'Juan Desarrollador', 'juan_dev', 'hashed_pass_f1', 'juan.dev@email.com', '77778888', 'Zona 1', '4000000000000', '1998-11-10', 'FREELANCER', 'ACTIVO'),
(5, 'Maria Diseñadora', 'maria_design', 'hashed_pass_f2', 'maria.dsgn@email.com', '99990000', 'Zona 15', '5000000000000', '1995-03-25', 'FREELANCER', 'ACTIVO'),
(6, 'Carlos Cliente', 'carlos_test', '$2a$12$41B0PTYE7g.7sfgRYBPXpO1zq2405SGPn1qo/1lGaW6oeUU2CMB1m', 'carlos@test.com', '12345678', 'Xela', '1234567890123', '2000-01-01', 'CLIENTE', 'ACTIVO'),
(7, 'Pedro FREELANCER', 'predro_f_test', '$2a$12$Z3kJqSCB5sdVNKoH9ly79OYY8ODjsvco9ta0mDUSJbs.cBoz1/lVm', 'pedro@test.com', '12345678', 'Xela', '1234567890012', '2000-01-01', 'FREELANCER', 'ACTIVO'),
(8, 'Juan', 'juan_test', '$2a$12$Jmn9.TLP1rpc0ncJmDQo0OqNFTs1tn.N32n8XsWuDnSYsNgnKtnFC', 'juanprueba@test.com', '12345678', 'xela', '1234567890', '2026-05-08', 'CLIENTE', 'ACTIVO'),
(9, 'Juan freelancer', 'juan_f_test', '$2a$12$zoPkW2TsY8kz6L/eqlEu5.9oBZ8S0YbKKuKMfuAQbsmiv7MjoXxGe', 'juanfreelancer@test.com', '12345678', 'xela', '2234567890', '2026-05-31', 'FREELANCER', 'ACTIVO'),
(10, 'melvin', 'melvin_cliente', '$2a$12$W7o1GDX4vtW4OefNHqQ/4OcmZT4BK2xZDUzjBkpsZOBRriN/MFI4u', 'melvien@test.com', '12345678', 'San Cris', '1134567890', '2026-05-08', 'CLIENTE', 'ACTIVO'),
(11, 'kike freelancer', 'kike_freelancer', '$2a$12$66WmDs6HjlqIBRmFRrqhv.6WArHS/HhIIw/pfoM/Q0ghuWuHh6Xmq', 'kike@test.com', '12345678', 'xela', '1114567890', '2026-05-31', 'FREELANCER', 'ACTIVO'),
(12, 'wicho', 'wicho', '$2a$12$r.u6SbAmvZ/lw29H3ya9U.Kc7VamBh.Q.hwc.RDCbiPv2KaVlbv5u', 'wicho@wicho.com', '12345678', 'Xela', '123456', '2026-05-12', 'CLIENTE', 'ACTIVO');

-- INSERTS TABLA cliente
INSERT INTO cliente (id_cliente, descripcion_empresa, sector_industria, sitio_web, saldo_disponible) VALUES
(2, 'Agencia de desarrollo de software a medida.', 'Tecnología', 'www.techsolutions.com', 750.00),
(3, 'Agencia de marketing digital y publicidad.', 'Publicidad', 'www.mktglobal.com', 500.00),
(6, 'Agencia de Marketing Digital', 'Publicidad', 'www.agenciatest.com', 300.00),
(8, 'juan tech', 'tecnologÃ­a ', 'juantech.com', 0.00),
(10, 'melvin tech', 'tecnologÃ­a ', 'mlevintech.com', 0.00),
(12, 'wicho entreprise ', 'musica', 'wicho.wicho.com', 0.00);

-- INSERTS TABLA freelancer
INSERT INTO freelancer (id_freelancer, biografia, nivel_experiencia, tarifa_hora, saldo_acumulado) VALUES
(4, 'Desarrollador Full Stack con 5 años de experiencia en Angular y Java.', 'SENIOR', 25.00, 675.00),
(5, 'Diseñadora gráfica especializada en UI/UX e identidad corporativa.', 'SEMI_SENIOR', 18.00, 0.00),
(7, 'Desarrollador backend especialista en Java', 'SENIOR', 25.50, 1755.00),
(11, 'desarrollador full stack', 'SEMI_SENIOR', 25.50, 855.00);

-- INSERTS TABLA habilidad
INSERT INTO habilidad (id_habilidad, id_categoria, nombre, estado) VALUES
(1, 1, 'Angular', 'ACTIVO'),
(2, 1, 'Java EE', 'ACTIVO'),
(3, 2, 'Figma', 'ACTIVO'),
(4, 2, 'Adobe Illustrator', 'ACTIVO'),
(5, 3, 'Google Ads', 'ACTIVO'),
(6, 1, 'docker', 'ACTIVO');

-- INSERTS TABLA freelancer_habilidad
INSERT INTO freelancer_habilidad (id_freelancer, id_habilidad) VALUES
(4, 1),
(4, 2),
(5, 3),
(5, 4),
(7, 1),
(7, 2),
(11, 1),
(11, 2);

-- INSERTS TABLA proyecto
INSERT INTO proyecto (id_proyecto, id_cliente, id_categoria, titulo, descripcion, presupuesto_maximo, fecha_limite, fecha_publicacion, estado) VALUES
(1, 2, 1, 'Sistema de Inventario Web', 'Necesitamos un sistema de inventario usando Angular y API REST.', 800.00, '2026-06-30', '2026-05-03 23:53:45', 'COMPLETADO'),
(2, 3, 2, 'Rediseño de Marca', 'Buscamos rediseñar nuestro logo y paleta de colores corporativa.', 300.00, '2026-05-20', '2026-05-03 23:53:45', 'ABIERTO'),
(3, 6, 1, 'E-commerce para venta minorista', 'Necesito un carrito de compras seguro.', 1200.00, '2026-07-15', '2026-05-06 23:38:26', 'EN_PROGRESO'),
(4, 6, 1, 'E-commerce para venta mayorista', 'Necesito un carrito de compras seguro y amplio para manejar grandes cantidades de productos', 1200.00, '2026-07-15', '2026-05-07 13:36:24', 'COMPLETADO'),
(5, 6, 1, 'Sistema de manejo de pizza a domicilio ', 'pizza ', 500.00, '2026-05-31', '2026-05-08 14:54:52', 'EN_PROGRESO'),
(6, 6, 1, 'sistema de bodega', 'cajas', 800.00, '2026-05-31', '2026-05-11 03:56:33', 'COMPLETADO'),
(8, 6, 1, 'sistema de bodega despensa familiar', 'familia', 800.00, '2026-05-31', '2026-05-11 10:53:52', 'COMPLETADO'),
(9, 6, 1, 'sistema de bodega 2', 'zzzz', 500.00, '2026-05-31', '2026-05-12 01:34:28', 'COMPLETADO'),
(10, 6, 1, 'proyecto para plataforma de videojuegos', 'juegos', 1200.00, '2026-05-31', '2026-05-12 18:54:30', 'CANCELADO'),
(11, 6, 1, 'Manejo de pedidos Macdonals', 'mac', 1500.00, '2026-05-31', '2026-05-13 13:28:55', 'ENTREGA_PENDIENTE'),
(12, 6, 1, 'Manejo de pedidos Shein', 'zzzzzz', 250.00, '2026-05-13', '2026-05-13 17:16:31', 'EN_PROGRESO');

-- INSERTS TABLA proyecto_habilidad
INSERT INTO proyecto_habilidad (id_proyecto, id_habilidad) VALUES
(1, 1),
(1, 2),
(2, 4),
(3, 1),
(3, 2),
(4, 1),
(4, 2),
(5, 1),
(5, 2),
(6, 1),
(6, 2),
(8, 1),
(8, 2),
(9, 1),
(9, 2),
(10, 1),
(10, 2),
(11, 1),
(11, 2),
(12, 1),
(12, 2);

-- INSERTS TABLA propuesta
INSERT INTO propuesta (id_propuesta, id_proyecto, id_freelancer, monto_ofertado, plazo_dias, carta_presentacion, estado, fecha_envio) VALUES
(1, 1, 4, 750.00, 30, 'Hola, tengo mucha experiencia en Angular y Java EE. Puedo tenerlo listo en un mes.', 'ACEPTADA', '2026-05-03 23:53:45'),
(2, 2, 5, 280.00, 15, 'Me encanta el diseño corporativo. Te adjunto mi portafolio.', 'PENDIENTE', '2026-05-03 23:53:45'),
(3, 3, 7, 1150.00, 20, 'Tengo experiencia desarrollando carritos de compra seguros. Adjunto referencias.', 'ACEPTADA', '2026-05-07 00:12:34'),
(4, 4, 7, 1150.00, 20, 'Tengo experiencia desarrollando carritos de compra seguros. Adjunto referencias.', 'ACEPTADA', '2026-05-07 13:37:04'),
(5, 2, 5, 850.00, 3, '', 'PENDIENTE', '2026-05-08 18:57:25'),
(7, 2, 5, 850.00, 3, '', 'PENDIENTE', '2026-05-08 19:00:21'),
(8, 2, 4, 1200.00, 5, '', 'PENDIENTE', '2026-05-08 19:00:21'),
(9, 2, 7, 1000.00, 7, '', 'PENDIENTE', '2026-05-08 19:00:21'),
(11, 5, 5, 850.00, 3, '¡Hola! Me encantaría trabajar en este proyecto. Tengo más de 3 años de experiencia en estas tecnologías y puedo empezar de inmediato.', 'ACEPTADA', '2026-05-08 19:06:13'),
(12, 5, 7, 1200.00, 5, 'Soy desarrollador Full-Stack. Garantizo un código limpio, seguro y escalable. Te entregaré la documentación completa al finalizar.', 'RECHAZADA', '2026-05-08 19:06:13'),
(14, 6, 7, 800.00, 7, '¡Hola! Soy experto en este tema y puedo empezar hoy mismo.', 'ACEPTADA', '2026-05-11 04:04:05'),
(15, 8, 11, 500.00, 25, 'soy la mera vrg', 'ACEPTADA', '2026-05-11 10:54:18'),
(16, 9, 11, 450.00, 25, 'zzzzz', 'ACEPTADA', '2026-05-12 01:34:54'),
(17, 10, 11, 1000.00, 25, 'soy cabron', 'ACEPTADA', '2026-05-12 18:55:22'),
(18, 11, 11, 1150.00, 35, 'soy bueno', 'ACEPTADA', '2026-05-13 13:29:22'),
(19, 12, 11, 150.00, 20, 'soy bueno', 'ACEPTADA', '2026-05-13 17:17:05');

-- INSERTS TABLA contrato
INSERT INTO contrato (id_contrato, id_propuesta, monto_bloqueado, porcentaje_comision_aplicado, fecha_inicio, motivo_cancelacion) VALUES
(1, 1, 750.00, 10.00, '2026-05-03 23:53:45', NULL),
(2, 3, 1150.00, 10.00, '2026-05-07 13:21:10', NULL),
(3, 4, 1150.00, 10.00, '2026-05-07 13:37:59', NULL),
(4, 14, 800.00, 10.00, '2026-05-11 04:05:01', NULL),
(5, 15, 500.00, 10.00, '2026-05-11 10:54:42', NULL),
(6, 16, 450.00, 10.00, '2026-05-12 01:35:58', NULL),
(7, 17, 1000.00, 10.00, '2026-05-12 18:55:58', 'usted es pendejo'),
(8, 18, 1150.00, 10.00, '2026-05-13 13:29:50', NULL),
(9, 19, 150.00, 10.00, '2026-05-13 17:35:06', NULL);

-- INSERTS TABLA entrega
INSERT INTO entrega (id_entrega, id_contrato, descripcion, url_archivo, estado, motivo_rechazo, fecha_subida) VALUES
(1, 1, 'Primera versión del backend y frontend terminada.', 'https://github.com/juandev/repo-inventario', 'APROBADA', NULL, '2026-05-03 23:53:45'),
(2, 3, 'Aquí está el proyecto terminado.', 'https://github.com/tu-repo/proyecto-final', 'APROBADA', NULL, '2026-05-07 15:22:30'),
(3, 4, 'Aquí está la App de Delivery terminada y funcionando.', 'https://github.com/app-delivery', 'APROBADA', NULL, '2026-05-11 04:06:16'),
(5, 5, 'zzzzzz', 'url', 'APROBADA', 'no funciona el link', '2026-05-11 15:54:27'),
(6, 6, 'zzz', 'url', 'APROBADA', NULL, '2026-05-12 01:37:34'),
(7, 5, 'zzz', 'https://github.com/cesarixquiac', 'APROBADA', NULL, '2026-05-12 01:39:48'),
(8, 7, 'aqui esta su mrd', 'https://github.com/cesarixquiac', 'PENDIENTE', NULL, '2026-05-12 18:56:39'),
(9, 8, 'ahuevo', 'https://github.com/cesarixquiac', 'PENDIENTE', NULL, '2026-05-13 13:30:23');

-- INSERTS TABLA calificacion
INSERT INTO calificacion (id_calificacion, id_contrato, estrellas, comentario, fecha_calificacion) VALUES
(1, 1, 5, 'Excelente trabajo, entregó a tiempo y el código es muy limpio.', '2026-05-03 23:53:45'),
(2, 4, 5, 'excelente', '2026-05-11 04:10:53'),
(3, 5, 5, '', '2026-05-12 01:40:48'),
(4, 6, 5, '', '2026-05-12 03:29:30');

-- INSERTS TABLA historial_comision
INSERT INTO historial_comision (id_historial_comision, porcentaje, fecha_inicio, fecha_fin) VALUES
(1, 10.00, '2026-01-01 00:00:00', '2026-05-12 00:00:00'),
(2, 20.00, '2026-05-12 00:00:00', NULL);

-- INSERTS TABLA ingreso_plataforma
INSERT INTO ingreso_plataforma (id_ingreso, id_contrato, monto_comision_cobrada, fecha_ingreso) VALUES
(1, 1, 75.00, '2026-05-03 23:53:45'),
(2, 4, 80.00, '2026-05-11 04:10:53'),
(3, 5, 50.00, '2026-05-12 01:40:48'),
(4, 6, 45.00, '2026-05-12 03:29:30');

-- INSERTS TABLA recarga_saldo
INSERT INTO recarga_saldo (id_recarga, id_cliente, monto, fecha_recarga) VALUES
(1, 2, 1000.00, '2026-05-03 23:53:45'),
(2, 2, 500.00, '2026-05-03 23:53:45'),
(3, 3, 500.00, '2026-05-03 23:53:45'),
(4, 6, 500.00, '2026-05-06 13:34:26'),
(5, 6, 1000.00, '2026-05-07 13:20:52'),
(6, 6, 1000.00, '2026-05-07 13:37:35'),
(7, 6, 1000.00, '2026-05-07 13:37:38'),
(8, 6, 500.00, '2026-05-08 20:07:35'),
(9, 6, 1500.00, '2026-05-11 02:29:36'),
(10, 6, 500.00, '2026-05-12 01:35:50'),
(11, 6, 150.00, '2026-05-12 04:41:28'),
(12, 6, 1000.00, '2026-05-12 18:55:55');

-- INSERTS TABLA solicitud_catalogo
INSERT INTO solicitud_catalogo (id_solicitud, id_usuario_solicitante, tipo_solicitud, nombre_sugerido, descripcion, estado) VALUES
(1, 11, 'HABILIDAD', 'docker', 'imagen docker', 'RECHAZADA'),
(2, 11, 'HABILIDAD', 'docker', 'imagen docker', 'RECHAZADA'),
(3, 11, 'HABILIDAD', 'docker', 'img docker', 'ACEPTADA'),
(4, 6, 'CATEGORIA', 'inteligencia artificial', 'IA', 'ACEPTADA'),
(5, 6, 'CATEGORIA', 'Ciberseguridad', 'seguridad', 'ACEPTADA'),
(6, 6, 'CATEGORIA', 'Cyber seguridad', 'seguridad', 'ACEPTADA'),
(7, 11, 'HABILIDAD', 'SAP', 'sapoooo', 'PENDIENTE');