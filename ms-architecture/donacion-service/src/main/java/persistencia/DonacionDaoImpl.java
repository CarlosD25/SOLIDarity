/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import com.milista.datos.MiLista;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Donacion;

/**
 *
 * @author Carlo
 */
public class DonacionDaoImpl implements DonacionDao {

    private Connection conn;

    public DonacionDaoImpl() {
        try {
            this.conn = ConnectionPostgresDB.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(DonacionDaoImpl.class.getName()).log(Level.SEVERE, "Ocurrió un error en UserDaoImpl", ex);
        }
    }

    @Override
    public Donacion save(Donacion donacion) {

        String sql = "insert into donaciones (id_user,id_campaña,monto,fecha) values (?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, donacion.getIdUser());
            ps.setInt(2, donacion.getIdCampaña());
            ps.setBigDecimal(3, donacion.getMonto());
            ps.setTimestamp(4, donacion.getFecha());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    donacion.setId(rs.getInt("id"));
                }

            }

            return donacion;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new Donacion();
    }

    @Override
    public List<Donacion> getByCampañaId(int id) {
        
        String sql = "select * from donaciones where id_campaña = ?";
        List<Donacion> donaciones = new MiLista<>();
        
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1, id);
            
            try(ResultSet rs = ps.executeQuery()){
                
                while(rs.next()){
                    Donacion d = new Donacion();
                    d.setId(rs.getInt("id"));
                    d.setIdUser(rs.getInt("id_user"));
                    d.setIdCampaña(rs.getInt("id_campaña"));
                    d.setMonto(rs.getBigDecimal("monto"));
                    d.setFecha(rs.getTimestamp("fecha"));
                    donaciones.add(d);
                }
                
            }
            
            
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        
        return donaciones;
        
    }

}
