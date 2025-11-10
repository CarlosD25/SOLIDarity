/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import client.CampañaClient;
import dto.DonacionRequestDTO;
import dto.DonacionResponseDTO;
import exception.DonationFailedException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
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
public class DonacionServiceImpl implements DonacionService {

    private final DonacionDao donacionDao;
    private final DonacionMapper donacionMapper;
    private final CampañaClient client;

    public DonacionServiceImpl(){
        donacionDao = new DonacionDaoImpl();
        donacionMapper = DonacionMapperImpl.getInstace();
        client = CampañaClient.getInstance();
    }
    
    @Override
    public DonacionResponseDTO save(DonacionRequestDTO requestDTO) {

        try {
            HttpResponse<String> response = client.actualizarMontoConDonacion(requestDTO);

            if (response.statusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                throw new DonationFailedException("No se pudo procesar la donación: Código " + response.statusCode());
            }

            Donacion d = donacionMapper.toEntity(requestDTO);
            d.setFecha(Timestamp.from(Instant.now()));

            Donacion saved = donacionDao.save(d);
            return donacionMapper.toDTO(saved);

        } catch (IOException | InterruptedException ex) {
            throw new DonationFailedException("Error de comunicación con el servicio de campañas");
        }

    }

    @Override
    public List<DonacionResponseDTO> getByCampañaId(int id) {

        return donacionDao.getByCampañaId(id).stream().map(donacionMapper::toDTO).toList();

    }

}
