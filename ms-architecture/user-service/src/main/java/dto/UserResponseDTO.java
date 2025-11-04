/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;
import model.Rol;

/**
 *
 * @author Carlo
 */
public class UserResponseDTO {
    
    private int id;
    private String name;
    private String telefono;
    private String address;
    private String email;

    public UserResponseDTO() {
    }

    public UserResponseDTO(int id, String name, String telefono, String address, String email) {
        this.id = id;
        this.name = name;
        this.telefono = telefono;
        this.address = address;
        this.email = email;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   


    
    
}
