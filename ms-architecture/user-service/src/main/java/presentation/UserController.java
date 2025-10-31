/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import service.UserService;
import service.UserServiceImpl;

/**
 *
 * @author Carlo
 */
@WebServlet("/users/*")
public class UserController extends HttpServlet {

    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            BufferedReader br = req.getReader();
            UserRequestDTO userRequestDTO = objectMapper.readValue(br, UserRequestDTO.class);

            UserResponseDTO userResponseDTO = userService.create(userRequestDTO);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), userResponseDTO);
        }catch(Exception ex){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

}
