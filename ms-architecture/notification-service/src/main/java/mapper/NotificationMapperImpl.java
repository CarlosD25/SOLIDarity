/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import dto.NotificationRequestDTO;
import dto.NotificationResponseDTO;
import model.Notification;

/**
 *
 * @author Carlo
 */
public class NotificationMapperImpl implements NotificationMapper {

    private static NotificationMapperImpl mapperImpl = new NotificationMapperImpl();

    private NotificationMapperImpl() {
    }

    private static class Holder {
        private static final NotificationMapperImpl INSTANCE = new NotificationMapperImpl();
    }

    public static NotificationMapperImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Notification toEntity(NotificationRequestDTO requestDTO) {

        Notification n = new Notification();
        n.setIdUser(requestDTO.getIdUser());
        n.setMessage(requestDTO.getMessage());
        return n;

    }

    @Override
    public NotificationResponseDTO toDto(Notification notification) {

        NotificationResponseDTO responseDTO = new NotificationResponseDTO();
        responseDTO.setId(notification.getId());
        responseDTO.setMessage(notification.getMessage());
        responseDTO.setFecha(notification.getFecha().toLocalDateTime());
        return responseDTO;

    }

}
