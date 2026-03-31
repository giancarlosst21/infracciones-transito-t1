package edu.pe.cibertec.infracciones.service.impl;

import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfractorServiceImplTest {

    @Mock
    private InfractorRepository infractorRepository;

    @InjectMocks
    private InfractorServiceImpl infractorService;

    @Test
    void verificarBloqueo_noDebeBloquear() {
        Infractor infractor = new Infractor();
        infractor.setId(1L);

        when(infractorRepository.findById(1L))
                .thenReturn(Optional.of(infractor));

        when(infractorRepository.contarMultasVencidas(1L))
                .thenReturn(2L);

        infractorService.verificarBloqueo(1L);
        verify(infractorRepository, never()).save(any());
    }
}
