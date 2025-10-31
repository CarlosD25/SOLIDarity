/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dto.DonacionRequestDTO;
import dto.DonacionResponseDTO;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import mapper.DonacionMapper;
import mapper.DonacionMapperImpl;
import model.Donacion;
import persistencia.DonacionDao;
import persistencia.DonacionDaoImpl;

/**
 *
 * @author Carlo
 */
public class DonacionServiceImpl implements DonacionService{
    
    private final DonacionDao donacionDao = new DonacionDaoImpl();
    private final DonacionMapper donacionMapper = new DonacionMapperImpl();

    @Override
    public DonacionResponseDTO save(DonacionRequestDTO requestDTO) {
        
        Donacion d = donacionMapper.toEntity(requestDTO);
        d.setFecha(Timestamp.from(Instant.now()));
        return donacionMapper.toDTO(donacionDao.save(d));
        
    }

    @Override
    public List<DonacionResponseDTO> getByCampañaId(int id) {
        
        return donacionDao.getByCampañaId(id).stream().map(donacionMapper::toDTO).toList();
        
    }
    
}
