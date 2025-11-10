/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import dto.DonacionRequestDTO;
import dto.DonacionResponseDTO;
import java.time.LocalDateTime;
import model.Donacion;

/**
 *
 * @author Carlo
 */
public class DonacionMapperImpl implements DonacionMapper{

    private DonacionMapperImpl(){
        
    }
    
    public static class Holder{
        private static final DonacionMapperImpl DONACION_MAPPER_IMPL = new DonacionMapperImpl();
    }
    
    public static DonacionMapperImpl getInstace(){
        return Holder.DONACION_MAPPER_IMPL;
    }
    
    
    @Override
    public Donacion toEntity(DonacionRequestDTO requestDTO) {
        Donacion d = new Donacion();
        d.setIdUser(requestDTO.getIdUser());
        d.setIdCampaña(requestDTO.getIdCampaña());
        d.setMonto(requestDTO.getMonto());
        return d;
    }

    @Override
    public DonacionResponseDTO toDTO(Donacion donacion) {
        
        DonacionResponseDTO responseDTO = new DonacionResponseDTO();
        responseDTO.setId(donacion.getId());
        responseDTO.setMonto(donacion.getMonto());
        responseDTO.setIdUser(donacion.getIdUser());
        responseDTO.setIdCampaña(donacion.getIdCampaña());
        LocalDateTime dateTime = donacion.getFecha().toLocalDateTime();
        responseDTO.setFecha(dateTime);
        return responseDTO;
    }
    
}
