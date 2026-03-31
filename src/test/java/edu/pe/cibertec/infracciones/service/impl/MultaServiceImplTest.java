package edu.pe.cibertec.infracciones.service.impl;
import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Vehiculo;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MultaServiceImplTest {

    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private MultaServiceImpl multaService;

    @Test
    void actualizarEstados_debeCambiarAVencida() {
        Multa multa = new Multa();
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        when(multaRepository.findMultasPendientes())
                .thenReturn(List.of(multa));

        multaService.actualizarEstados();

        assertEquals(EstadoMulta.VENCIDA, multa.getEstado());
        verify(multaRepository).save(multa);
    }

    @Test
    void obtenerMultasPorPlaca_debeRetornarLista() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca("ABC123");

        Multa multa = new Multa();
        multa.setVehiculo(vehiculo);

        when(multaRepository.findByVehiculo_Placa("ABC123"))
                .thenReturn(List.of(multa));

        List<Multa> resultado = multaService.obtenerMultasPorPlaca("ABC123");

        assertEquals(1, resultado.size());
        verify(multaRepository).findByVehiculo_Placa("ABC123");
    }
}
