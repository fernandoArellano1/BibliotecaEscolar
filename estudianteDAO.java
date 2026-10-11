package dao;

import config.DatabaseConnection;
import model.Estudiante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements GenericDAO<Estudiante> {

    @Override
    public boolean insertar(Estudiante e) {
        String sql = "INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getRut());
            stmt.setString(3, e.getCurso());
            stmt.setString(4, e.getCorreo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException err) {
            err.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Estudiante e) {
        String sql = "UPDATE estudiantes SET nombre=?, rut=?, curso=?, correo=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getRut());
            stmt.setString(3, e.getCurso());
            stmt.setString(4, e.getCorreo());
            stmt.setInt(5, e.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException err) {
            err.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException err) {
            err.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Estudiante(
                    rs.getInt("id"), rs.getString("nombre"),
                    rs.getString("rut"), rs.getString("curso"), rs.getString("correo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}