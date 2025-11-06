/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

}
