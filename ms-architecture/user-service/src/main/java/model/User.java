/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author Carlo
 */
public class User {
    
    private int id;
    private String name;
    private String telefono;
    private String email;
    private String password;
    private String address;
    private boolean active;
    private List<Rol> roles;
    private String imagenUrl;

    public User() {
    }

    public User(int id, String name, String telefono, String email, String password, String address, boolean active, List<Rol> roles, String imagenUrl) {
        this.id = id;
        this.name = name;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.address = address;
        this.active = active;
        this.roles = roles;
        this.imagenUrl = imagenUrl;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    

    
    
    
}
