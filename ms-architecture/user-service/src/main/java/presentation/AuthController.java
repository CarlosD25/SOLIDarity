/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.LoginRequestDTO;
import dto.UserRequestDTO;
import dto.UserResponseDTO;
import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import service.UserService;
import service.UserServiceImpl;

/**
 *
 * @author Carlo
 */
@WebServlet("/auth/*")
public class AuthController extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public AuthController() {
        userService = new UserServiceImpl();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo(); 
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (BufferedReader br = req.getReader()) {

            if ("/register".equalsIgnoreCase(path)) {
                
                UserRequestDTO userRequestDTO = objectMapper.readValue(br, UserRequestDTO.class);
                UserResponseDTO userResponseDTO = userService.create(userRequestDTO);

                resp.setStatus(HttpServletResponse.SC_CREATED);
                objectMapper.writeValue(resp.getWriter(), userResponseDTO);
                return; 
            }

            if ("/login".equalsIgnoreCase(path)) {
               
                LoginRequestDTO loginRequestDTO = objectMapper.readValue(br, LoginRequestDTO.class);
                UserResponseDTO userResponseDTO = userService.login(loginRequestDTO);

                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), userResponseDTO);
                return; 
            }

            
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            

        } catch (EmailAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());

        } catch (InvalidCredentialsException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(e.getMessage());
            

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);            
        }
    }
}
