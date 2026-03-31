package edu.pe.cibertec.infracciones.service.impl;

import edu.pe.cibertec.infracciones.exception.InfractorBloqueadoException; //P4
import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Vehiculo;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor; //P4
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows; //P4

//P3 y P4= preguntas 3 y 4 xd
@ExtendWith(MockitoExtension.class)
public class MultaServiceImplTest {

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private InfractorRepository infractorRepository;

    @InjectMocks
    private MultaServiceImpl multaService;

    //P3 (actualizado con ArgumentCaptor para cumplir la P4)
    @Test
    void transferirMulta_DebeAsignarMultaAlNuevoInfractor() {
        Long idMulta = 1L;
        Long idInfractorA = 1L;
        Long idInfractorB = 2L;
        Long idVehiculo = 99L;

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(idVehiculo);

        // dueño original
        Infractor infractorA = new Infractor();
        infractorA.setId(idInfractorA);

        // el nuevo dueño
        Infractor infractorB = new Infractor();
        infractorB.setId(idInfractorB);
        infractorB.setBloqueado(false); //no debe estar bloquedo por eso el "false" :D
        infractorB.setVehiculos(new ArrayList<>());
        infractorB.getVehiculos().add(vehiculo);

        // cracion de la multa a transferir
        Multa multa = new Multa();
        multa.setId(idMulta);
        multa.setEstado(EstadoMulta.PENDIENTE); //lo ponemos en pendiente :D
        multa.setVehiculo(vehiculo);
        multa.setInfractor(infractorA);

        // A educar a esos mocks 🗣️🔥
        Mockito.when(multaRepository.findById(idMulta)).thenReturn(Optional.of(multa));
        Mockito.when(infractorRepository.findById(idInfractorB)).thenReturn(Optional.of(infractorB));

        multaService.transferirMulta(idMulta, idInfractorB);

        // P4: usando ArgumentCaptor para capturar el objeto que se intento guardar :D
        ArgumentCaptor<Multa> multaCaptor = ArgumentCaptor.forClass(Multa.class);

        // P4: verificamos que el save() fue llamado solo una vez :D
        Mockito.verify(multaRepository, Mockito.times(1)).save(multaCaptor.capture());

        // P4: con getValue() extraemos el valor capturado y luego lo verificamos :D
        Multa multaGuardada = multaCaptor.getValue();
        assertEquals(infractorB, multaGuardada.getInfractor(), "La multa capturada debe tener asignado al infractor B");
    }

    //P4
    @Test
    void transferirMulta_DebeLanzarExceptionSiInfractorBloqueado(){
        Long idMulta = 1L;
        Long idInfractorB = 2L;

        Multa multa = new Multa();
        multa.setId(idMulta);
        multa.setEstado(EstadoMulta.PENDIENTE);

        // el nuevo dueño esta BLOQUEADO :(
        Infractor infractorB = new Infractor();
        infractorB.setId(idInfractorB);
        infractorB.setBloqueado(true); //ahora si :D

        // A educar a esos mocks 🗣️🔥 x2
        Mockito.when(multaRepository.findById(idMulta)).thenReturn(Optional.of(multa));
        Mockito.when(infractorRepository.findById(idInfractorB)).thenReturn(Optional.of(infractorB));

        // Verificamos que DETONE 🗣️🔥 con la excepcion correcta :D
        assertThrows(InfractorBloqueadoException.class, () -> {
            multaService.transferirMulta(idMulta,idInfractorB);
        }, "Debe lanzar InfractorBloqueadoException porque el infractor esta bloqueado");

        // Le metemos su real verificada de que NUNCA se llamo al metodo save() :D
        Mockito.verify(multaRepository, Mockito.never()).save(Mockito.any(Multa.class));
    }
}
