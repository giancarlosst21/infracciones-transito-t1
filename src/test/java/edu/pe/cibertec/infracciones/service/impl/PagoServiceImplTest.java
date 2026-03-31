package edu.pe.cibertec.infracciones.service.impl;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Pago;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PagoServiceImplTest {

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Captor
    private ArgumentCaptor<Pago> pagoCaptor;

    @Test
    void cuandoMultaEstaVencida_EntoncesAplicaRecargoYGuardaPago() {
        Long multaId = 1L;
        LocalDate hoy = LocalDate.now();

        Multa multaVencida = new Multa();
        multaVencida.setId(multaId);
        multaVencida.setMonto(500.00);
        multaVencida.setFechaEmision(hoy.minusDays(12));
        multaVencida.setFechaVencimiento(hoy.minusDays(2));
        multaVencida.setEstado(EstadoMulta.PENDIENTE);

        when(multaRepository.findById(multaId)).thenReturn(Optional.of(multaVencida));
        pagoService.procesarPago(multaId);
        verify(pagoRepository, times(1)).save(pagoCaptor.capture());

        Pago pagoRealizado = pagoCaptor.getValue();
        assertEquals(75.00, pagoRealizado.getRecargo(), "El recargo debe ser del 15%");
        assertEquals(0.00, pagoRealizado.getDescuentoAplicado(), "No debe tener descuento");
        assertEquals(575.00, pagoRealizado.getMontoPagado(), "El monto total debe incluir el recargo");
        assertEquals(EstadoMulta.PAGADA, multaVencida.getEstado());
    }
}
