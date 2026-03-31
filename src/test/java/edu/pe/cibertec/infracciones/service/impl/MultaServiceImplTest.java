package edu.pe.cibertec.infracciones.service.impl;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Vehiculo;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

//P3 = pregunta 3 xd
@ExtendWith(MockitoExtension.class)
public class MultaServiceImplTest {

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private InfractorRepository infractorRepository;

    @InjectMocks
    private MultaServiceImpl multaService;

    //P3
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

        assertEquals(infractorB,multa.getInfractor(), "La multa debe quedar asiganada al infractor B");

        Mockito.verify(multaRepository).save(multa);

    }



}
