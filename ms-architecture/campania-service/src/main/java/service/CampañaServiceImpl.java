/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import client.NotificationClient;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import config.CloudinaryConfig;
import dto.CampañaEstadoRequestDTO;
import dto.CampañaRequestDTO;
import dto.CampañaResponseDTO;
import dto.CampañaImagenDTO;
import dto.CampañaResumenDTO;
import dto.CampañaUpdateDTO;
import dto.DonacionRequestDTO;
import dto.NotificationRequestDTO;
import exception.CampañaNotFoundException;
import exception.NotificationServiceException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import mapper.CampañaMapper;
import mapper.CampañaMapperImpl;
import model.Campaña;
import model.Status;
import persistencia.CampañaDao;
import persistencia.CampañaDaoImpl;

/**
 *
 * @author Carlo
 */
public class CampañaServiceImpl implements CampañaService {

    private final CampañaDao campañaDao;
    private final CampañaMapper campañaMapper;
    private final Cloudinary cloudinary;
    private final NotificationClient client;

    
    public CampañaServiceImpl(){
        campañaDao = new CampañaDaoImpl();
        campañaMapper = CampañaMapperImpl.getInstance();
        cloudinary = CloudinaryConfig.getCloudinary();
        client = NotificationClient.getInstance();
    }
    
    @Override
    public CampañaResponseDTO create(CampañaRequestDTO campañaRequestDTO) {

        Campaña campaña = campañaMapper.toEntity(campañaRequestDTO);
        campaña.setFechaInicio(Timestamp.from(Instant.now()));
        campaña.setStatus(Status.PENDIENTE);
        return campañaMapper.toResponseDto(campañaDao.save(campaña));

    }

    @Override
    public CampañaImagenDTO actualizarImagenCampaña(int id, InputStream imagen) {
        try {

            Campaña campaña = campañaDao.findById(id);
            if (campaña == null) {
                throw new RuntimeException("No existe la campaña con id: " + id);
            }

            byte[] bytes = imagen.readAllBytes();

            String publicId = "campaña_" + id;

            Map uploadResult = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", "campañas",
                            "overwrite", true
                    )
            );

            if (!uploadResult.containsKey("secure_url")) {
                throw new RuntimeException("Error al subir la imagen a Cloudinary: no se obtuvo secure_url");
            }

            String url = (String) uploadResult.get("secure_url");

            campañaDao.actualizarImagenCampaña(id, url);

            return new CampañaImagenDTO(url);

        } catch (IOException e) {
            throw new RuntimeException("Error de IO al procesar la imagen: " + e.getMessage(), e);
        }

    }

    @Override
    public List<CampañaResumenDTO> getAll() {

        return campañaDao.getAll().stream().map(campañaMapper::toResumenDto).toList();

    }

    @Override
    public CampañaResponseDTO getById(int id) {

        Campaña campaña = campañaDao.findById(id);

        if (campaña == null) {
            throw new CampañaNotFoundException("Camapaña con ID " + id + " no encontrada");
        }

        return campañaMapper.toResponseDto(campaña);

    }

    @Override
    public void actualizarMontoRecaudado(DonacionRequestDTO donacionRequestDTO) {

        Campaña campaña = campañaDao.findById(donacionRequestDTO.getIdCampaña());

        if (campaña == null) {
            throw new CampañaNotFoundException("Camapaña con ID " + donacionRequestDTO.getIdCampaña() + " no encontrada");

        }

        BigDecimal newMontoRecaudado = campaña.getMontoRecaudado().add(donacionRequestDTO.getMonto());

        if (newMontoRecaudado.compareTo(campaña.getMontoObjetivo()) >= 0) {
            campañaDao.actualizarMontoRecaudado(campaña.getId(), newMontoRecaudado);
            campañaDao.updateEstado(campaña.getId(), Status.COMPLETADA);
            campañaDao.actualizarFechaFinalizacion(campaña.getId(), Timestamp.from(Instant.now()));
        } else {
            campañaDao.actualizarMontoRecaudado(campaña.getId(), newMontoRecaudado);
        }

    }

    @Override
    public void actualizarEstado(int id, CampañaEstadoRequestDTO request) {

        Campaña campaña = campañaDao.findById(id);

        if (campaña == null) {
            throw new CampañaNotFoundException("Campaña con ID " + id + " no encontrada");
        }

        campañaDao.updateEstado(id, request.getStatus());

        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO();
        notificationRequestDTO.setIdUser(campaña.getIdBeneficiario());
        notificationRequestDTO.setMessage("Tu campaña '" + campaña.getTitulo() + "' ha sido " + request.getStatus().name().toLowerCase() + ".");

        try {
            HttpResponse<String> response = client.enviarNotificacion(notificationRequestDTO);
            if (response.statusCode() != HttpURLConnection.HTTP_CREATED) {
                throw new NotificationServiceException(
                        "Error de comunicación con el servicio de notificaciones, código: " + response.statusCode()
                );
            }
        } catch (IOException | InterruptedException e) {
            throw new NotificationServiceException("Error de comunicación con el servicio de notificaciones");
        }

    }

    @Override
    public CampañaResponseDTO update(int id, CampañaUpdateDTO campañaUpdateDTO) {
        
        Campaña c = campañaDao.findById(id);
        if(c==null) throw new CampañaNotFoundException("Campaña con ID " + id + " no encontrada");
        
        if(campañaUpdateDTO.getTitulo()!=null){
            c.setTitulo(campañaUpdateDTO.getTitulo());
        }
        
        if(campañaUpdateDTO.getDescripcion()!=null){
            c.setDescripcion(campañaUpdateDTO.getDescripcion());
        }
        
        if(campañaUpdateDTO.getMontoObjetivo()!=null){
            c.setMontoObjetivo(campañaUpdateDTO.getMontoObjetivo());
        }
        
        return campañaMapper.toResponseDto(campañaDao.update(id, c));
        
    }

    @Override
    public List<CampañaResumenDTO> getByStatus(String status) {
        return campañaDao.findByStatus(status).stream().map(campañaMapper::toResumenDto).toList();
    }
}
