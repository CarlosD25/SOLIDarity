/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import com.milista.datos.MiLista;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Campaña;
import model.Status;

/**
 *
 * @author Carlo
 */
public class CampañaDaoImpl implements CampañaDao {

    private Connection conn;

    public CampañaDaoImpl() {

        try {
            conn = ConnectionPostgresDB.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(CampañaDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Campaña save(Campaña campaña) {

        String sql = "insert into campañas (id_beneficiario,titulo,status,descripcion,fecha_inicio,monto_objetivo) values(?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, campaña.getIdBeneficiario());
            ps.setString(2, campaña.getTitulo());
            ps.setString(3, campaña.getStatus().name());
            ps.setString(4, campaña.getDescripcion());
            ps.setTimestamp(5, campaña.getFechaInicio());
            ps.setBigDecimal(6, campaña.getMontoObjetivo());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        campaña.setId(rs.getInt(1));
                        return campaña;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new Campaña();
    }

    @Override
    public String actualizarImagenCampaña(int id, String imagenUrl) {
        String sql = "update campañas set imagen_url = ? where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, imagenUrl);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return "Imagen actualizada correctamente";
            } else {
                return "No se encontró la campaña con id " + id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al actualizar la imagen: " + e.getMessage();
        }
    }

    @Override
    public Campaña findById(int id) {
        String sql = "select id, id_beneficiario,titulo, status, descripcion, fecha_inicio, fecha_finalizacion, monto_objetivo, monto_recaudado, imagen_url from campañas where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Campaña campaña = new Campaña();
                campaña.setId(rs.getInt("id"));
                campaña.setIdBeneficiario(rs.getInt("id_beneficiario"));
                campaña.setTitulo(rs.getString("titulo"));
                campaña.setStatus(Status.valueOf(rs.getString("status")));
                campaña.setDescripcion(rs.getString("descripcion"));
                campaña.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                campaña.setFechaFinalizacion(rs.getTimestamp("fecha_finalizacion"));
                campaña.setMontoObjetivo(rs.getBigDecimal("monto_objetivo"));
                campaña.setMontoRecaudado(rs.getBigDecimal("monto_recaudado"));
                campaña.setImagenUrl(rs.getString("imagen_url"));
                return campaña;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar campaña con id: " + id, e);
        }
    }

    @Override
    public List<Campaña> getAll() {

        List<Campaña> camapañas = new MiLista<>();

        String sql = "select * from campañas";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Campaña c = new Campaña();
                    c.setId(rs.getInt("id"));
                    c.setIdBeneficiario(rs.getInt("id_beneficiario"));
                    c.setTitulo(rs.getString("titulo"));
                    c.setStatus(Status.valueOf(rs.getString("status")));
                    c.setDescripcion(rs.getString("descripcion"));
                    c.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                    c.setFechaFinalizacion(rs.getTimestamp("fecha_finalizacion"));
                    c.setMontoObjetivo(rs.getBigDecimal("monto_objetivo"));
                    c.setMontoRecaudado(rs.getBigDecimal("monto_recaudado"));
                    c.setImagenUrl("imagen_url");
                    camapañas.add(c);
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return camapañas;

    }

    @Override
    public void actualizarMontoRecaudado(int id, BigDecimal montoRecaudado) {
        String sql = "update campañas set monto_recaudado = ? where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, montoRecaudado);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("No se encontró la campaña con id: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el monto recaudado de la campaña con id: " + id, e);
        }
    }

    @Override
    public boolean existById(int id) {

        String sql = "select 1 from campañas where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;

        }

    }

    public void updateEstado(int id, Status nuevoEstado) {
        String sql = "update campañas set status = ? where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el estado de la campaña con id: " + id, e);
        }
    }

    @Override
    public Campaña update(int id, Campaña campaña) {
        StringBuilder sql = new StringBuilder("update campañas set ");
        List<Object> params = new ArrayList<>();

        if (campaña.getTitulo() != null) {
            sql.append("titulo = ?, ");
            params.add(campaña.getTitulo());
        }

        if (campaña.getDescripcion() != null) {
            sql.append("descripcion = ?, ");
            params.add(campaña.getDescripcion());
        }

        if (campaña.getMontoObjetivo() != null) {
            sql.append("monto_objetivo = ?, ");
            params.add(campaña.getMontoObjetivo());
        }

        if (params.isEmpty()) {
            return findById(id);
        }

        sql.setLength(sql.length() - 2);
        sql.append(" where id = ?");
        params.add(id);

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return findById(id);
    }

}
