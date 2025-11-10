/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ObjectMapperConfig;
import dto.DonacionRequestDTO;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * @author Carlo
 */
public class CampañaClient{

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    
    private CampañaClient(){
        client = HttpClient.newHttpClient();
        objectMapper = ObjectMapperConfig.createObjectMapper();
    }
    
    public static class Holder{
        private static final CampañaClient CAMPAÑA_CLIENT = new CampañaClient();
    }
    
    public static CampañaClient getInstance(){
        return Holder.CAMPAÑA_CLIENT;
    }

    public HttpResponse<String> actualizarMontoConDonacion(DonacionRequestDTO donacionRequestDTO) throws JsonProcessingException, IOException, InterruptedException {

        String jsonBody = objectMapper.writeValueAsString(donacionRequestDTO);

        String url = String.format(
                "http://localhost:8080/campania-service/campañas/%d/donacion",
                donacionRequestDTO.getIdCampaña()
        );

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse;
    }

}
