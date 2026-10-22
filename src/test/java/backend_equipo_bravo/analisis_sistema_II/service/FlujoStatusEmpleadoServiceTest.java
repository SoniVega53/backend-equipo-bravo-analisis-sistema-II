package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.flujostatusempleado.FlujoStatusEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.FlujoStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.RoleOpcion;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.FlujoStatusEmpleadoError;
import backend_equipo_bravo.analisis_sistema_II.repository.FlujoStatusEmpleadoRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.RoleOpcionRepository;
import backend_equipo_bravo.analisis_sistema_II.repository.StatusEmpleadoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlujoStatusEmpleadoServiceTest {

    @Mock
    private FlujoStatusEmpleadoRepository flujoStatusEmpleadoRepository;

    @Mock
    private StatusEmpleadoRepository statusEmpleadoRepository;

    @Mock
    private RoleOpcionRepository roleOpcionRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private FlujoStatusEmpleadoService flujoStatusEmpleadoService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(flujoStatusEmpleadoService, "roleOpcionRepository", roleOpcionRepository);
        ReflectionTestUtils.setField(flujoStatusEmpleadoService, "jwtService", jwtService);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("usuario_prueba", null, Collections.emptyList())
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer test_token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        lenient().when(jwtService.obtenerRolDelToken("test_token")).thenReturn(1);

        RoleOpcion roleOpcion = new RoleOpcion();
        roleOpcion.setAlta(1);
        roleOpcion.setBaja(1);
        roleOpcion.setCambio(1);
        roleOpcion.setConsultar(1);
        lenient().when(roleOpcionRepository.findByIdRoleAndIdOpcion(any(), any())).thenReturn(Optional.of(roleOpcion));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testCrearExitoso() {
        FlujoStatusEmpleadoRequest request = FlujoStatusEmpleadoRequest.builder()
                .idStatusActual(1)
                .idStatusNuevo(2)
                .nombreEvento("Suspensión IGSS")
                .build();

        when(statusEmpleadoRepository.existsById(1)).thenReturn(true);
        when(statusEmpleadoRepository.existsById(2)).thenReturn(true);
        when(flujoStatusEmpleadoRepository.existsById(any(FlujoStatusEmpleadoId.class))).thenReturn(false);
        when(flujoStatusEmpleadoRepository.save(any(FlujoStatusEmpleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FlujoStatusEmpleado resultado = flujoStatusEmpleadoService.crear(request);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdStatusActual());
        assertEquals(2, resultado.getIdStatusNuevo());
        assertEquals("Suspensión IGSS", resultado.getNombreEvento());
        assertEquals("usuario_prueba", resultado.getUsuarioCreacion());
        assertNotNull(resultado.getFechaCreacion());
    }

    @Test
    void testCrearMismoStatusFalla() {
        FlujoStatusEmpleadoRequest request = FlujoStatusEmpleadoRequest.builder()
                .idStatusActual(1)
                .idStatusNuevo(1)
                .nombreEvento("Mismo estado")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> flujoStatusEmpleadoService.crear(request));
        assertEquals(FlujoStatusEmpleadoError.MISMO_STATUS.getCodigoTexto(), exception.getCodigoTexto());
    }

    @Test
    void testCrearYaExisteFalla() {
        FlujoStatusEmpleadoRequest request = FlujoStatusEmpleadoRequest.builder()
                .idStatusActual(1)
                .idStatusNuevo(2)
                .nombreEvento("Transición existente")
                .build();

        when(statusEmpleadoRepository.existsById(1)).thenReturn(true);
        when(statusEmpleadoRepository.existsById(2)).thenReturn(true);
        when(flujoStatusEmpleadoRepository.existsById(new FlujoStatusEmpleadoId(1, 2))).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> flujoStatusEmpleadoService.crear(request));
        assertEquals(FlujoStatusEmpleadoError.FLUJO_ALREADY_EXISTS.getCodigoTexto(), exception.getCodigoTexto());
    }

    @Test
    void testActualizarExitoso() {
        FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(1, 2);
        FlujoStatusEmpleado existente = new FlujoStatusEmpleado();
        existente.setIdStatusActual(1);
        existente.setIdStatusNuevo(2);
        existente.setNombreEvento("Evento Anterior");

        when(flujoStatusEmpleadoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(flujoStatusEmpleadoRepository.save(any(FlujoStatusEmpleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FlujoStatusEmpleadoRequest request = FlujoStatusEmpleadoRequest.builder()
                .idStatusActual(1)
                .idStatusNuevo(2)
                .nombreEvento("Evento Actualizado")
                .build();

        FlujoStatusEmpleado resultado = flujoStatusEmpleadoService.actualizar(id, request);

        assertNotNull(resultado);
        assertEquals("Evento Actualizado", resultado.getNombreEvento());
        assertEquals("usuario_prueba", resultado.getUsuarioModificacion());
        assertNotNull(resultado.getFechaModificacion());
    }

    @Test
    void testNotFoundException() {
        FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(99, 99);
        when(flujoStatusEmpleadoRepository.findById(id)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> flujoStatusEmpleadoService.buscarPorId(id));
        assertEquals(FlujoStatusEmpleadoError.FLUJO_NOT_FOUND.getCodigoTexto(), exception.getCodigoTexto());
    }
}

