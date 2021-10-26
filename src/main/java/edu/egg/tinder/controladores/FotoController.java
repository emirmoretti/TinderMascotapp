/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.tinder.controladores;

import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.excepciones.ErrorServicio;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import edu.egg.tinder.servicios.MascotaServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Emir
 */
@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private MascotaServicio mascotaServicio;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) throws ErrorServicio {

        try {
            Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
            Usuario usuario = respuesta.get();
            if (usuario.getFoto() == null) {
                throw new ErrorServicio("El usuario no tiene una foto");
            }
            byte[] foto = usuario.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) throws ErrorServicio {

        try {
            Mascota mascota = mascotaServicio.buscarPorId(id);
            if (mascota.getFoto() == null) {
                throw new ErrorServicio("La mascota no tiene una foto");
            }
            byte[] foto = mascota.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
