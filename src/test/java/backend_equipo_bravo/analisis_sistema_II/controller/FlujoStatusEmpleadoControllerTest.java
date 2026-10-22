package backend_equipo_bravo.analisis_sistema_II.controller;

import backend_equipo_bravo.analisis_sistema_II.dto.flujostatusempleado.FlujoStatusEmpleadoRequest;
import backend_equipo_bravo.analisis_sistema_II.entity.FlujoStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.FlujoStatusEmpleadoError;
import backend_equipo_bravo.analisis_sistema_II.service.FlujoStatusEmpleadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlujoStatusEmpleadoControllerTest {

    @Mock
    private FlujoStatusEmpleadoService flujoStatusEmpleadoService;

    @InjectMocks
    private FlujoStatusEmpleadoController flujoStatusEmpleadoController;

    @Test
    void testListarTodos() {
        when(flujoStatusEmpleadoService.buscarTodosPermisos()).thenReturn(List.of(new FlujoStatusEmpleado()));

        ResponseEntity<?> response = flujoStatusEmpleadoController.listarTodos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(flujoStatusEmpleadoService, times(1)).buscarTodosPermisos();
    }

    @Test
    void testObtenerPorIdExitoso() {
        FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(1, 2);
        FlujoStatusEmpleado entity = new FlujoStatusEmpleado();
        entity.setIdStatusActual(1);
        entity.setIdStatusNuevo(2);

        when(flujoStatusEmpleadoService.buscarPorIdPermisos(eq(id))).thenReturn(entity);

        ResponseEntity<?> response = flujoStatusEmpleadoController.obtenerPorId(1, 2);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testObtenerPorIdNotFound() {
        FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(99, 99);
        when(flujoStatusEmpleadoService.buscarPorIdPermisos(eq(id)))
                .thenThrow(new BusinessException(FlujoStatusEmpleadoError.FLUJO_NOT_FOUND));

        ResponseEntity<?> response = flujoStatusEmpleadoController.obtenerPorId(99, 99);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCrearExitoso() {
        FlujoStatusEmpleadoRequest request = FlujoStatusEmpleadoRequest.builder()
                .idStatusActual(1)
                .idStatusNuevo(2)
                .nombreEvento("Suspensión")
                .build();

        FlujoStatusEmpleado entity = new FlujoStatusEmpleado();
        entity.setIdStatusActual(1);
        entity.setIdStatusNuevo(2);

        when(flujoStatusEmpleadoService.crear(any(FlujoStatusEmpleadoRequest.class))).thenReturn(entity);

        ResponseEntity<?> response = flujoStatusEmpleadoController.crear(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testActualizarExitoso() {
        FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(1, 2);
        FlujoStatusEmpleadoRequest request = FlujoStatusEmpleadoRequest.builder()
                .idStatusActual(1)
                .idStatusNuevo(2)
                .nombreEvento("Actualizado")
                .build();

        FlujoStatusEmpleado entity = new FlujoStatusEmpleado();
        entity.setIdStatusActual(1);
        entity.setIdStatusNuevo(2);

        when(flujoStatusEmpleadoService.actualizar(eq(id), any(FlujoStatusEmpleadoRequest.class))).thenReturn(entity);

        ResponseEntity<?> response = flujoStatusEmpleadoController.actualizar(1, 2, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testEliminarExitoso() {
        FlujoStatusEmpleadoId id = new FlujoStatusEmpleadoId(1, 2);
        when(flujoStatusEmpleadoService.eliminarBasePermisos(eq(id))).thenReturn("Eliminado Correctamente");

        ResponseEntity<?> response = flujoStatusEmpleadoController.eliminar(1, 2);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(flujoStatusEmpleadoService, times(1)).eliminarBasePermisos(eq(id));
    }
}

