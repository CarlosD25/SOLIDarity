/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.List;
import model.Rol;
import model.Usuario;
import persistencia.RolRepository;
import persistencia.UsuarioRepository;

/**
 *
 * @author Carlo
 */
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final RolRepository rolRepository = new RolRepository();
    
    
    public Usuario create(Usuario usuario){
        
        List<Rol> newRoles = new ArrayList<>();
        
        for (Rol rol : usuario.getRoles()) {
            if(!rolRepository.existRol(rol.getNombre())){
                newRoles.add(rolRepository.save(rol));
            }else{
                newRoles.add(rolRepository.findByName(rol.getNombre()));
            }
        }
        
        usuario.setRoles(newRoles);
        
        return usuarioRepository.create(usuario);

        
    }

    Usuario getById(int id) {
        Usuario usuario = usuarioRepository.getById(id);
        usuario.setRoles(rolRepository.getRolesByUsuarioId(id));
        return usuario;
    }
    
}
