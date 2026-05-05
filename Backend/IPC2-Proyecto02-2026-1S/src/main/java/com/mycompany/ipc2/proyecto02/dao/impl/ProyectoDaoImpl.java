package com.mycompany.ipc2.proyecto02.dao.impl;

import com.mycompany.ipc2.proyecto02.config.ConexionDB;
import com.mycompany.ipc2.proyecto02.dao.ProyectoDAO;
import com.mycompany.ipc2.proyecto02.model.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProyectoDaoImpl implements ProyectoDAO {
    
    

    @Override
    public Proyecto publicarProyectoConHabilidades(Proyecto proyecto, List<Integer> habilidades) {
        String sqlProyecto = "INSERT INTO proyecto (id_cliente, id_categoria, titulo, descripcion, presupuesto_maximo, fecha_limite, estado) VALUES (?, ?, ?, ?, ?, ?, 'ABIERTO')";
        String sqlHabilidades = "INSERT INTO proyecto_habilidad (id_proyecto, id_habilidad) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement stmtProyecto = conn.prepareStatement(sqlProyecto, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtHabilidades = conn.prepareStatement(sqlHabilidades)) {

                stmtProyecto.setInt(1, proyecto.getIdCliente());
                stmtProyecto.setInt(2, proyecto.getIdCategoria());
                stmtProyecto.setString(3, proyecto.getTitulo());
                stmtProyecto.setString(4, proyecto.getDescripcion());
                stmtProyecto.setDouble(5, proyecto.getPresupuestoMaximo());
                stmtProyecto.setDate(6, Date.valueOf(proyecto.getFechaLimite()));

                int filasAfectadas = stmtProyecto.executeUpdate();
                if (filasAfectadas == 0) {
                    conn.rollback();
                    return null;
                }

                int idGenerado;
                try (ResultSet rs = stmtProyecto.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        proyecto.setIdProyecto(idGenerado);
                        proyecto.setEstado("ABIERTO");
                    } else {
                        conn.rollback();
                        return null;
                    }
                }

                for (Integer idHabilidad : habilidades) {
                    stmtHabilidades.setInt(1, idGenerado);
                    stmtHabilidades.setInt(2, idHabilidad);
                    stmtHabilidades.addBatch();
                }
                stmtHabilidades.executeBatch();

                conn.commit(); 
                return proyecto;

            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public List<Map<String, Object>> obtenerPropuestasPorProyecto(int idProyecto) {
        List<Map<String, Object>> propuestas = new ArrayList<>();
        
        String sql = "SELECT p.id_propuesta, u.nombre_completo AS nombre_freelancer, " +
                     "p.monto_ofertado, p.plazo_dias, " +
                     "p.carta_presentacion, p.estado " +
                     "FROM propuesta p " +
                     "INNER JOIN usuario u ON p.id_freelancer = u.id_usuario " +
                     "WHERE p.id_proyecto = ? " +
                     "ORDER BY p.monto_ofertado ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProyecto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> prop = new HashMap<>();
                    prop.put("idPropuesta", rs.getInt("id_propuesta"));
                    prop.put("nombreFreelancer", rs.getString("nombre_freelancer"));
                    prop.put("montoOfertado", rs.getDouble("monto_ofertado"));
                    prop.put("plazoDias", rs.getInt("plazo_dias"));
                    prop.put("cartaPresentacion", rs.getString("carta_presentacion"));
                    prop.put("estado", rs.getString("estado"));
                    
                    propuestas.add(prop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return propuestas;
    }
    
    @Override
    public void aceptarPropuesta(int idProyecto, int idPropuesta, int idCliente) throws Exception {
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            // 1. Obtener el monto de la propuesta
            String sqlMonto = "SELECT monto_ofertado FROM propuesta WHERE id_propuesta = ?";
            double montoOfertado = 0;
            try (PreparedStatement stmt = conn.prepareStatement(sqlMonto)) {
                stmt.setInt(1, idPropuesta);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) montoOfertado = rs.getDouble("monto_ofertado");
                    else throw new Exception("La propuesta no existe.");
                }
            }

            // 2. Verificar el saldo del cliente
            String sqlSaldo = "SELECT saldo_disponible FROM cliente WHERE id_cliente = ?";
            double saldoCliente = 0;
            try (PreparedStatement stmt = conn.prepareStatement(sqlSaldo)) {
                stmt.setInt(1, idCliente);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) saldoCliente = rs.getDouble("saldo_disponible");
                    else throw new Exception("Cliente no encontrado.");
                }
            }

            if (saldoCliente < montoOfertado) {
                throw new Exception("Saldo insuficiente. Por favor, recarga tu cuenta.");
            }

            // 3. Descontar el saldo al cliente
            String sqlRestar = "UPDATE cliente SET saldo_disponible = saldo_disponible - ? WHERE id_cliente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlRestar)) {
                stmt.setDouble(1, montoOfertado);
                stmt.setInt(2, idCliente);
                stmt.executeUpdate();
            }

            // 4. Cambiar el proyecto a EN_PROGRESO
            String sqlProy = "UPDATE proyecto SET estado = 'EN_PROGRESO' WHERE id_proyecto = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlProy)) {
                stmt.setInt(1, idProyecto);
                stmt.executeUpdate();
            }

            // 5. Marcar esta propuesta como ACEPTADA y las demás como RECHAZADAS
            String sqlAceptar = "UPDATE propuesta SET estado = CASE WHEN id_propuesta = ? THEN 'ACEPTADA' ELSE 'RECHAZADA' END WHERE id_proyecto = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlAceptar)) {
                stmt.setInt(1, idPropuesta);
                stmt.setInt(2, idProyecto);
                stmt.executeUpdate();
            }

            // 6. ¡AQUÍ NACE EL CONTRATO!
            String sqlContrato = "INSERT INTO contrato (id_propuesta, monto_bloqueado, porcentaje_comision_aplicado, fecha_inicio) VALUES (?, ?, 10.00, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(sqlContrato)) {
                stmt.setInt(1, idPropuesta);
                stmt.setDouble(2, montoOfertado);
                stmt.executeUpdate();
            }

            conn.commit(); 
        } catch (Exception e) {
            if (conn != null) conn.rollback(); 
            throw e; 
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void rechazarPropuesta(int idPropuesta) throws Exception {
        String sql = "UPDATE propuesta SET estado = 'RECHAZADA' WHERE id_propuesta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPropuesta);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public Map<String, Object> obtenerDetalleEntrega(int idProyecto) throws Exception {
        Map<String, Object> respuesta = new HashMap<>();
        
        String sqlProyecto = "SELECT id_proyecto, titulo, descripcion, presupuesto_maximo, fecha_limite, estado FROM proyecto WHERE id_proyecto = ?";
        
        // CORREGIDO: Doble JOIN para llegar desde Entrega hasta Proyecto pasando por Contrato y Propuesta
        String sqlEntrega = "SELECT e.descripcion, e.url_archivo " +
                            "FROM entrega e " +
                            "INNER JOIN contrato c ON e.id_contrato = c.id_contrato " +
                            "INNER JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                            "WHERE p.id_proyecto = ? " +
                            "ORDER BY e.id_entrega DESC LIMIT 1";

        try (Connection conn = ConexionDB.getConnection()) {
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlProyecto)) {
                stmt.setInt(1, idProyecto);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Map<String, Object> proyecto = new HashMap<>();
                        proyecto.put("id", rs.getInt("id_proyecto"));
                        proyecto.put("titulo", rs.getString("titulo"));
                        proyecto.put("descripcion", rs.getString("descripcion"));
                        proyecto.put("presupuestoMaximo", rs.getDouble("presupuesto_maximo"));
                        proyecto.put("fechaLimite", rs.getString("fecha_limite"));
                        proyecto.put("estado", rs.getString("estado"));
                        respuesta.put("proyecto", proyecto);
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlEntrega)) {
                stmt.setInt(1, idProyecto);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Map<String, Object> entrega = new HashMap<>();
                        entrega.put("comentarios", rs.getString("descripcion")); 
                        entrega.put("archivoUrl", rs.getString("url_archivo"));
                        respuesta.put("entrega", entrega);
                    } else {
                        respuesta.put("entrega", null); 
                    }
                }
            }
        }
        return respuesta;
    }

    @Override
    public void aprobarEntrega(int idProyecto, int estrellas, String comentario) throws Exception {
        // 1. Estados correctos según tu esquema
        String sqlUpdateProyecto = "UPDATE proyecto SET estado = 'COMPLETADO' WHERE id_proyecto = ?";
        String sqlUpdateEntrega = "UPDATE entrega e " +
                                  "INNER JOIN contrato c ON e.id_contrato = c.id_contrato " +
                                  "INNER JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                                  "SET e.estado = 'APROBADA' WHERE p.id_proyecto = ?";

        // 2. Obtener datos del contrato para hacer cálculos
        String sqlDatosContrato = "SELECT c.id_contrato, c.monto_bloqueado, c.porcentaje_comision_aplicado, p.id_freelancer " +
                                  "FROM contrato c " +
                                  "INNER JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                                  "WHERE p.id_proyecto = ?";

        // 3. Pagos y Calificación
        String sqlPagarFreelancer = "UPDATE freelancer SET saldo_acumulado = saldo_acumulado + ? WHERE id_freelancer = ?";
        String sqlIngresoPlataforma = "INSERT INTO ingreso_plataforma (id_contrato, monto_comision_cobrada) VALUES (?, ?)";
        String sqlCalificacion = "INSERT INTO calificacion (id_contrato, estrellas, comentario) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            // Marcamos Proyecto y Entrega como terminados
            try (PreparedStatement stmt1 = conn.prepareStatement(sqlUpdateProyecto)) {
                stmt1.setInt(1, idProyecto);
                stmt1.executeUpdate();
            }
            try (PreparedStatement stmt2 = conn.prepareStatement(sqlUpdateEntrega)) {
                stmt2.setInt(1, idProyecto);
                stmt2.executeUpdate();
            }

            // Calculamos el dinero
            int idContrato = 0;
            int idFreelancer = 0;
            double montoBloqueado = 0;
            double porcentajeComision = 0;

            try (PreparedStatement stmtDatos = conn.prepareStatement(sqlDatosContrato)) {
                stmtDatos.setInt(1, idProyecto);
                try (ResultSet rs = stmtDatos.executeQuery()) {
                    if (rs.next()) {
                        idContrato = rs.getInt("id_contrato");
                        idFreelancer = rs.getInt("id_freelancer");
                        montoBloqueado = rs.getDouble("monto_bloqueado");
                        porcentajeComision = rs.getDouble("porcentaje_comision_aplicado");
                    } else {
                        throw new Exception("No se encontró el contrato activo para este proyecto.");
                    }
                }
            }

            // Matemática
            double comisionCobrada = montoBloqueado * (porcentajeComision / 100.0);
            double pagoFreelancer = montoBloqueado - comisionCobrada;

            // Pagar al Freelancer
            try (PreparedStatement stmtPago = conn.prepareStatement(sqlPagarFreelancer)) {
                stmtPago.setDouble(1, pagoFreelancer);
                stmtPago.setInt(2, idFreelancer);
                stmtPago.executeUpdate();
            }

            // Registrar ganancia de la Plataforma
            try (PreparedStatement stmtIngreso = conn.prepareStatement(sqlIngresoPlataforma)) {
                stmtIngreso.setInt(1, idContrato);
                stmtIngreso.setDouble(2, comisionCobrada);
                stmtIngreso.executeUpdate();
            }

            // Guardar la Calificación
            try (PreparedStatement stmtCalificacion = conn.prepareStatement(sqlCalificacion)) {
                stmtCalificacion.setInt(1, idContrato);
                stmtCalificacion.setInt(2, estrellas);
                stmtCalificacion.setString(3, comentario);
                stmtCalificacion.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error al aprobar, procesar el pago y calificar: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void rechazarEntrega(int idProyecto, String motivo) throws Exception {
        String sqlUpdateProyecto = "UPDATE proyecto SET estado = 'EN_PROGRESO' WHERE id_proyecto = ?";
        
        String sqlUpdateEntrega = "UPDATE entrega e " +
                                  "INNER JOIN contrato c ON e.id_contrato = c.id_contrato " +
                                  "INNER JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                                  "SET e.estado = 'RECHAZADA', e.motivo_rechazo = ? " +
                                  "WHERE p.id_proyecto = ? AND e.estado != 'RECHAZADA'";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(sqlUpdateProyecto)) {
                stmt1.setInt(1, idProyecto);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(sqlUpdateEntrega)) {
                stmt2.setString(1, motivo);
                stmt2.setInt(2, idProyecto);
                stmt2.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error al rechazar la entrega: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

   @Override
    public void cancelarContrato(int idProyecto, String motivo, int idCliente) throws Exception {
        String sqlUpdateProyecto = "UPDATE proyecto SET estado = 'CANCELADO' WHERE id_proyecto = ?";
        
        String sqlUpdateContrato = "UPDATE contrato c " +
                                   "INNER JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                                   "SET c.motivo_cancelacion = ? " +
                                   "WHERE p.id_proyecto = ?";
        
        String sqlDevolverDinero = "UPDATE cliente SET saldo_disponible = saldo_disponible + " +
                                   "(SELECT presupuesto_maximo FROM proyecto WHERE id_proyecto = ?) " +
                                   "WHERE id_cliente = ?";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement stmt1 = conn.prepareStatement(sqlUpdateProyecto)) {
                stmt1.setInt(1, idProyecto);
                stmt1.executeUpdate();
            }
            
            try (PreparedStatement stmtContrato = conn.prepareStatement(sqlUpdateContrato)) {
                stmtContrato.setString(1, motivo);
                stmtContrato.setInt(2, idProyecto);
                stmtContrato.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(sqlDevolverDinero)) {
                stmt2.setInt(1, idProyecto);
                stmt2.setInt(2, idCliente);
                stmt2.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new Exception("Error al cancelar el contrato y devolver fondos: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    
    @Override
    public void editarProyectoAbierto(int idProyecto, String titulo, String descripcion, double presupuesto, String fechaLimite) throws Exception {
        String sql = "UPDATE proyecto SET titulo = ?, descripcion = ?, presupuesto_maximo = ?, fecha_limite = ? WHERE id_proyecto = ? AND estado = 'ABIERTO'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, presupuesto);
            stmt.setDate(4, Date.valueOf(fechaLimite));
            stmt.setInt(5, idProyecto);
            
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new Exception("No se pudo editar. Asegúrate de que el proyecto siga ABIERTO.");
            }
        }
    }

    @Override
    public void eliminarProyectoAbierto(int idProyecto) throws Exception {
        String sql = "DELETE FROM proyecto WHERE id_proyecto = ? AND estado = 'ABIERTO'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProyecto);
            
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new Exception("No se pudo eliminar. El proyecto ya no está en estado ABIERTO.");
            }
        }
    }
    
    @Override
    public List<Map<String, Object>> obtenerCatalogoHabilidades() throws Exception {
        List<Map<String, Object>> habilidades = new ArrayList<>();
        // Traemos el ID y el nombre de la habilidad
        String sql = "SELECT id_habilidad, nombre FROM habilidad WHERE estado = 'ACTIVO' ORDER BY nombre ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> hab = new HashMap<>();
                hab.put("id_habilidad", rs.getInt("id_habilidad"));
                hab.put("nombre", rs.getString("nombre"));
                habilidades.add(hab);
            }
        }
        return habilidades;
    }
    
    
    @Override
    public List<Map<String, Object>> obtenerProyectosDisponibles(int idFreelancer) throws Exception {
        List<Map<String, Object>> proyectos = new ArrayList<>();
        
        String sql = 
            "SELECT p.id_proyecto, p.titulo, p.descripcion, p.presupuesto_maximo, p.fecha_limite, " +
            "       c.nombre AS categoria, " +
            "       GROUP_CONCAT(h.nombre SEPARATOR ', ') AS habilidades_requeridas, " +
            "       (SELECT id_propuesta FROM propuesta WHERE id_proyecto = p.id_proyecto AND id_freelancer = ?) AS id_propuesta_enviada " +
            "FROM proyecto p " +
            "JOIN categoria c ON p.id_categoria = c.id_categoria " +
            "LEFT JOIN proyecto_habilidad ph ON p.id_proyecto = ph.id_proyecto " +
            "LEFT JOIN habilidad h ON ph.id_habilidad = h.id_habilidad " +
            "WHERE p.estado = 'ABIERTO' " +
            "GROUP BY p.id_proyecto " +
            "ORDER BY p.fecha_publicacion DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer); // Pasamos el ID del freelancer
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> proyecto = new HashMap<>();
                    proyecto.put("id", rs.getInt("id_proyecto"));
                    proyecto.put("titulo", rs.getString("titulo"));
                    proyecto.put("descripcion", rs.getString("descripcion"));
                    proyecto.put("presupuestoMaximo", rs.getDouble("presupuesto_maximo"));
                    proyecto.put("fechaLimite", rs.getDate("fecha_limite").toString());
                    proyecto.put("categoria", rs.getString("categoria"));
                    proyecto.put("habilidades", rs.getString("habilidades_requeridas"));
                    
                    // Si id_propuesta_enviada no es 0, significa que ya aplicó
                    proyecto.put("yaAplico", rs.getInt("id_propuesta_enviada") > 0);
                    
                    proyectos.add(proyecto);
                }
            }
        }
        return proyectos;
    }
    
    @Override
    public List<Map<String, Object>> obtenerContratosActivosFreelancer(int idFreelancer) throws Exception {
        List<Map<String, Object>> contratos = new ArrayList<>();
        
        String sql = 
            "SELECT p.id_proyecto, p.titulo, p.estado, p.presupuesto_maximo, p.fecha_limite, " +
            "       pr.monto_ofertado, " +
            "       c.id_contrato " +
            "FROM proyecto p " +
            "JOIN propuesta pr ON p.id_proyecto = pr.id_proyecto " +
          
            "JOIN contrato c ON pr.id_propuesta = c.id_propuesta " + 
            "WHERE pr.id_freelancer = ? " +
            "AND pr.estado = 'ACEPTADA' " +
            "AND p.estado IN ('EN_PROGRESO', 'ENTREGA_PENDIENTE') " +
            "ORDER BY p.fecha_limite ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> contrato = new HashMap<>();
                    contrato.put("idProyecto", rs.getInt("id_proyecto"));
                    contrato.put("idContrato", rs.getInt("id_contrato")); 
                    contrato.put("titulo", rs.getString("titulo"));
                    contrato.put("estado", rs.getString("estado"));
                    contrato.put("montoOfertado", rs.getDouble("monto_ofertado"));
                    contrato.put("fechaLimite", rs.getDate("fecha_limite").toString());
                    contratos.add(contrato);
                }
            }
        }
        return contratos;
    }
    
    
    
    

    // Métodos CrudDao restantes omitidos por brevedad
    @Override public Proyecto crear(Proyecto entidad) { return null; }
    @Override public Optional<Proyecto> obtenerPorId(Integer integer) { return Optional.empty(); }
    @Override public List<Proyecto> obtenerTodos() { return null; }
    @Override public boolean actualizar(Proyecto entidad) { return false; }
    @Override public boolean eliminar(Integer integer) { return false; }
}