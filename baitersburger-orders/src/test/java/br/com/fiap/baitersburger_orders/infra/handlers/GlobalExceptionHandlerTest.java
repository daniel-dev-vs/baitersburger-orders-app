package br.com.fiap.baitersburger_orders.infra.handlers;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleNotFoundException() {
        NotFoundException ex = new NotFoundException("Pedido não encontrado");

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pedido não encontrado", response.getBody().getBody().getDetail());
    }

    @Test
    void shouldHandleFeignException() {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(402); // Ex: Payment Required
        when(ex.getMessage()).thenReturn("Erro na integração");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, request);

        assertEquals(HttpStatus.PAYMENT_REQUIRED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("Erro interno inesperado");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}