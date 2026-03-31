package edu.pe.cibertec.infracciones.service.impl;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Vehiculo;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.VehiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InfractorServiceImplTest {
    @Mock
    private MultaRepository multaRepository;

    //P2 = Pregunta 2 xd
    @Mock
    private InfractorRepository infractorRepository;

    //P2
    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private InfractorServiceImpl infractorService;

    @Test
    void calcularDeuda_DebeRetornarSumaConRecargo() {
        Long idInfractor = 1L;

        Multa multaPendiente = new Multa();
        multaPendiente.setMonto(200.00);
        multaPendiente.setEstado(EstadoMulta.PENDIENTE);

        Multa multaVencida = new Multa();
        multaVencida.setMonto(300.00);
        multaVencida.setEstado(EstadoMulta.VENCIDA);

        List<Multa> multasDelInfractor = Arrays.asList(multaPendiente, multaVencida);

        Mockito.when(multaRepository.findByInfractor_Id(idInfractor)).thenReturn(multasDelInfractor);

        // este test falla intencionalmente(por el momento xd)
        Double deudaTotal = infractorService.calcularDeuda(idInfractor);

        assertEquals(545.00, deudaTotal, "La deuda debe ser de 545.00 aplicando el 15% a la vencida.");
    }

    //P2
    @Test
    void desasignarVehiculo_DebeRemoverVehiculoSiNoHayMultasPendientes() {
        Long idInfractor = 1L;
        Long idVehiculo = 1L;

        Infractor infractor = new Infractor();
        infractor.setId(idInfractor);
        infractor.setVehiculos(new ArrayList<>());

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(idVehiculo);

        infractor.getVehiculos().add(vehiculo);

        Mockito.when(infractorRepository.findById(idInfractor)).thenReturn(Optional.of(infractor));
        Mockito.when(vehiculoRepository.findById(idVehiculo)).thenReturn(Optional.of(vehiculo));

        Mockito.when(multaRepository.findByVehiculo_IdAndEstado(idVehiculo, EstadoMulta.PENDIENTE))
                .thenReturn(new ArrayList<>());

        // este test falla intencionalmente(por el momento xd) x2
        infractorService.desasignarVehiculo(idInfractor, idVehiculo);

        assertEquals(0, infractor.getVehiculos().size(), "El vehiculo debio ser removido de la lista del infractor");

        Mockito.verify(infractorRepository).save(infractor);
    }
}
