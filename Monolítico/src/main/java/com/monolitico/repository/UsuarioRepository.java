package com.monolitico.repository;

import com.milista.datos.MiLista;
import com.monolitico.conection.ConnectionDB;
import com.monolitico.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    public Usuario create(Usuario user) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, edad, nacionalidad) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getApellido());
            stmt.setInt(3, user.getEdad());
            stmt.setString(4, user.getNacionalidad());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al crear el usuario: No se insertaron filas.");
            }

            try (ResultSet result = stmt.getGeneratedKeys()) {
                if (result.next()) {
                    user.setId(result.getInt(1));
                } else {
                    throw new SQLException("Error al obtener el ID del usuario.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new MiLista<>();
        String sql = "SELECT * FROM usuarios";

        System.out.println("Consultando todos los usuarios de la base de datos...");

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario user = new Usuario();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setApellido(rs.getString("apellido"));
                user.setEdad(rs.getInt("edad"));
                user.setNacionalidad(rs.getString("nacionalidad"));

                usuarios.add(user);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return usuarios;
    }

    public boolean findById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Connection conn = ConnectionDB.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                return rs.next();
            }
        }
    }

    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConnectionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            boolean existe = findById(id);

            if (!existe) {
                throw new SQLException("No existe ese usuario en la base de datos");
            }
            stmt.setInt(1, id);

            stmt.executeUpdate();

        }

    }

}
