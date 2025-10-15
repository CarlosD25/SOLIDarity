/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.List;
import model.Domiciliario;
import model.Rol;
import model.Usuario;
import persistencia.DomiciliarioRepository;

/**
 *
 * @author Carlo
 */
public class DomiciliarioService {

    private final DomiciliarioRepository domiciliarioRepository = new DomiciliarioRepository();
    private final UsuarioService usuarioService = new UsuarioService();

    public Domiciliario create(Domiciliario domiciliario) {
        Usuario usuario = new Usuario();
        usuario.setNombre(domiciliario.getUsuario().getNombre());
        usuario.setTelefono(domiciliario.getUsuario().getTelefono());
        usuario.setEmail(domiciliario.getUsuario().getEmail());
        usuario.setPassword(domiciliario.getUsuario().getPassword());
        usuario.setActivo(true);
        usuario.setRoles(List.of(new Rol("ROL_DONANTE"), new Rol("ROL_DOMICILIARIO")));

        Usuario usuarioCreated = usuarioService.create(usuario);
        domiciliario.setUsuario(usuarioCreated);
        return domiciliarioRepository.create(domiciliario);
    }

    public List<Domiciliario> getAll() {
        return domiciliarioRepository.getAll();
    }

    public Domiciliario getById(int id) {
        Domiciliario domiciliario = domiciliarioRepository.getById(id);

        if (domiciliario == null) return null;

        int idUsuario = domiciliario.getUsuario().getId(); 
        Usuario usuario = usuarioService.getById(idUsuario); 

        domiciliario.setUsuario(usuario);

        return domiciliario;
    }

    public boolean delete(int id) {
        return domiciliarioRepository.delete(id);
    }

}
