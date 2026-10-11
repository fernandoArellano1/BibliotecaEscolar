package dao;

import config.DatabaseConnection;
import model.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public boolean registrarPrestamo(int idEstudiante, int idLibro) {
        String sql = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, devuelto) VALUES (?, ?, CURDATE(), FALSE)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEstudiante);
            stmt.setInt(2, idLibro);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prestamo> obtenerTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Prestamo(
                    rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"),
                    rs.getDate("fecha_prestamo"), rs.getDate("fecha_devolucion"), rs.getBoolean("devuelto")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}