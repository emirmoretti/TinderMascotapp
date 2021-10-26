/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.tinder.controladores;

import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.repositorios.ZonaRepositorio;
import edu.egg.tinder.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Emir
 */
@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,@RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "usuario o clave incorrectos.");
        }
        if (logout != null) {
            modelo.put("logout", "Ha salido correctamente de la plataforma");
        }
        return "login.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        List<Zona> zonas = zonaRepositorio.findAll();
        modelo.put("zonas", zonas);
        return "registro.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2, @RequestParam MultipartFile archivo, @RequestParam String idZona) {
        try {
            usuarioServicio.registrar(nombre, apellido, mail, clave, archivo, clave2, idZona);
        } catch (Exception ex) {
            List<Zona> zonas = zonaRepositorio.findAll();
            modelo.put("error", ex.getMessage());
            modelo.put("zonas", zonas);
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave);
            modelo.put("clave2", clave2);

            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "registro.html";
        }
        modelo.put("titulo", "bienvenido al tinder de mascota");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
        return "exito.html";
    }
}
