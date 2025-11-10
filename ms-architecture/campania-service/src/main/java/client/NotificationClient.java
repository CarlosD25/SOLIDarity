/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ObjectMapperConfig;
import dto.NotificationRequestDTO;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * @author Carlo
 */
public class NotificationClient {

    private HttpClient client;
    private ObjectMapper objectMapper;

    private NotificationClient(){
        client = HttpClient.newHttpClient();
        objectMapper = ObjectMapperConfig.createObjectMapper();
    }
    
    public static class Holder{
        private static final NotificationClient CLIENT = new NotificationClient();
    }
    
    public static NotificationClient getInstance(){
        
        return Holder.CLIENT;
    }
    public HttpResponse<String> enviarNotificacion(NotificationRequestDTO notificationRequestDTO) throws JsonProcessingException, IOException, InterruptedException {

        String jsonBody = objectMapper.writeValueAsString(notificationRequestDTO);

        String url = String.format(
                "http://localhost:8080/notification-service/notifications"
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response;

    }

}
