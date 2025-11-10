/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dto.NotificationRequestDTO;
import dto.NotificationResponseDTO;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import mapper.NotificationMapper;
import mapper.NotificationMapperImpl;
import model.Notification;
import persistencia.NotificationDao;
import persistencia.factory.NotificationDaoFactory;

/**
 *
 * @author Carlo
 */
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(){
        notificationDao = NotificationDaoFactory.getNotificationDao(NotificationDaoFactory.TipoDB.POSTGRES);
        notificationMapper = NotificationMapperImpl.getInstance();
    }
    
    @Override
    public NotificationResponseDTO create(NotificationRequestDTO requestDTO) {

        Notification notification = notificationMapper.toEntity(requestDTO);
        notification.setFecha(Timestamp.from(Instant.now()));
        return notificationMapper.toDto(notificationDao.create(notification));

    }

    @Override
    public List<NotificationResponseDTO> getByUserId(int id) {

        return notificationDao.findByUserId(id).stream().map(notificationMapper::toDto).toList();

    }

}
