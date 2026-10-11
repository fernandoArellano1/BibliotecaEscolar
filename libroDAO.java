package dao;

import config.DatabaseConnection;
import model.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO implements GenericDAO<Libro> {

    @Override
    public boolean insertar(Libro l) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, l.getTitulo());
            stmt.setString(2, l.getAutor());
            stmt.setString(3, l.getIsbn());
            stmt.setString(4, l.getEditorial());
            stmt.setInt(5, l.getStock());
            stmt.setInt(6, l.getIdCategoria());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Libro l) {
        String sql = "UPDATE libros SET titulo=?, autor=?, isbn=?, editorial=?, stock=?, id_categoria=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, l.getTitulo());
            stmt.setString(2, l.getAutor());
            stmt.setString(3, l.getIsbn());
            stmt.setString(4, l.getEditorial());
            stmt.setInt(5, l.getStock());
            stmt.setInt(6, l.getIdCategoria());
            stmt.setInt(7, l.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Libro> obtenerTodos() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Libro(
                    rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
                    rs.getString("isbn"), rs.getString("editorial"), rs.getInt("stock"),
                    rs.getInt("id_categoria")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public synchronized boolean descontarStock(int idLibro) {
        String checkSql = "SELECT stock FROM libros WHERE id = ?";
        String updateSql = "UPDATE libros SET stock = stock - 1 WHERE id = ? AND stock > 0";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            
            checkStmt.setInt(1, idLibro);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt("stock") > 0) {
                updateStmt.setInt(1, idLibro);
                return updateStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}