/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import dto.CampañaRequestDTO;
import dto.CampañaResponseDTO;
import dto.CampañaResumenDTO;
import model.Campaña;

/**
 *
 * @author Carlo
 */
public class CampañaMapperImpl implements CampañaMapper {

    private CampañaMapperImpl(){
        
    }
    
    public static class Holder{
        private static final CampañaMapperImpl CAMPAÑA_MAPPER_IMPL = new CampañaMapperImpl();
    }
    
    public static CampañaMapperImpl getInstance(){
        return Holder.CAMPAÑA_MAPPER_IMPL;
    }
    
    @Override
    public CampañaResponseDTO toResponseDto(Campaña campaña) {

        CampañaResponseDTO campañaResponseDTO = new CampañaResponseDTO();
        campañaResponseDTO.setId(campaña.getId());
        campañaResponseDTO.setIdBeneficiario(campaña.getIdBeneficiario());
        campañaResponseDTO.setTitulo(campaña.getTitulo());
        campañaResponseDTO.setDescripcion(campaña.getDescripcion());
        campañaResponseDTO.setFechaInicio(campaña.getFechaInicio().toLocalDateTime());
        if (campaña.getFechaFinalizacion() != null) {
            campañaResponseDTO.setFechaFinalizacion(campaña.getFechaFinalizacion().toLocalDateTime());
        } else {
            campañaResponseDTO.setFechaFinalizacion(null);
        }

        campañaResponseDTO.setStatus(campaña.getStatus());
        campañaResponseDTO.setMontoObjetivo(campaña.getMontoObjetivo());
        campañaResponseDTO.setMontoRecaudado(campaña.getMontoRecaudado());
        campañaResponseDTO.setImagenUrl(campaña.getImagenUrl());
        return campañaResponseDTO;

    }

    @Override
    public Campaña toEntity(CampañaRequestDTO campañaRequestDTO) {
        Campaña campaña = new Campaña();
        campaña.setIdBeneficiario(campañaRequestDTO.getIdBeneficiario());
        campaña.setTitulo(campañaRequestDTO.getTitulo());
        campaña.setDescripcion(campañaRequestDTO.getDescripcion());
        campaña.setMontoObjetivo(campañaRequestDTO.getMontoObjetivo());
        return campaña;
    }

    @Override
    public CampañaResumenDTO toResumenDto(Campaña campaña) {
        CampañaResumenDTO campañaResumenDTO = new CampañaResumenDTO();
        campañaResumenDTO.setId(campaña.getId());
        campañaResumenDTO.setIdBeneficiario(campaña.getIdBeneficiario());
        campañaResumenDTO.setStatus(campaña.getStatus());
        campañaResumenDTO.setTitulo(campaña.getTitulo());
        campañaResumenDTO.setMontoObjetivo(campaña.getMontoObjetivo());
        campañaResumenDTO.setMontoRecaudado(campaña.getMontoRecaudado());
        campañaResumenDTO.setImagenUrl(campaña.getImagenUrl());
        return campañaResumenDTO;
    }

}
