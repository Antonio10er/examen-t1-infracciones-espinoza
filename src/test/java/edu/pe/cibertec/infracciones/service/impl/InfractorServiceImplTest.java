package edu.pe.cibertec.infracciones.service.impl;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InfractorServiceImplTest {
    @Mock
    private MultaRepository multaRepository;

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
}
