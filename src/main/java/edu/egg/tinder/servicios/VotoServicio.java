package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Voto;
import edu.egg.tinder.excepciones.ErrorServicio;
import edu.egg.tinder.repositorios.MascotaRepositorio;
import edu.egg.tinder.repositorios.VotoRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoServicio {

    @Autowired
    private MascotaRepositorio mascotaRepositorio;
    @Autowired
    private VotoRepositorio votoRepositorio;
    @Autowired
    private NotificacionServicio notificacionServicio;

    public void votar(String idUsuario, String idMascota1, String idMascota2) throws ErrorServicio {
        Voto voto = new Voto();
        voto.setFecha(new Date());

        if (idMascota1.equals(idMascota2)) {
            throw new ErrorServicio("Auto voto papi?");
        }

        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota1);
        if (respuesta.isPresent()) {
            Mascota mascota1 = respuesta.get();
            if (mascota1.getUsuario().getId().equals(idUsuario)) {
                voto.setMascota1(mascota1);
            } else {
                throw new ErrorServicio("No tiene permisos para realizar la op");
            }
        } else {
            throw new ErrorServicio("No existe una mascota vinculada a ese ID");
        }
        Optional<Mascota> respuesta2 = mascotaRepositorio.findById(idMascota2);
        if (respuesta2.isPresent()) {
            Mascota mascota2 = respuesta2.get();
            voto.setMascota2(mascota2);
            notificacionServicio.enviar("tu mascota ha sido votada", "tinder-mascotapp", mascota2.getUsuario().getMail());
        } else {
            throw new ErrorServicio("No existe una mascota vinculada a ese ID");
        }
        votoRepositorio.save(voto);
    }

    public void responder(String idUsuario, String idVoto) throws ErrorServicio {
        Optional<Voto> respuesta = votoRepositorio.findById(idVoto);
        if (respuesta.isPresent()) {
            Voto voto = respuesta.get();
            voto.setRespuesta(new Date());
            if (voto.getMascota2().getUsuario().getId().equals(idUsuario)) {
                notificacionServicio.enviar("tu voto fue respondido", "tinder-mascotapp", voto.getMascota1().getUsuario().getMail());
                votoRepositorio.save(voto);
            } else {
                throw new ErrorServicio("no tiene permisos para realizar la op");
            }
        } else {
            throw new ErrorServicio("no existe el voto");
        }
    }
}
