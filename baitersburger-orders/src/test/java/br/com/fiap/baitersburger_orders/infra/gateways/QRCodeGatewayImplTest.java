package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.infra.apis.MercadoPagoApi;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.ResponseQRCodeDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.TypeResponse;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QRCodeGatewayImplTest {

    @Mock
    private MercadoPagoApi mercadoPagoApi;

    @InjectMocks
    private QRCodeGatewayImpl qrCodeGateway;

    @Test
    void shouldReturnQrDataSuccessfully() {
        String orderId = "123";
        String amount = "50.00";
        String expectedQrData = "000201010212432490...";

        var typeResponse = mock(TypeResponse.class);
        when(typeResponse.qrData()).thenReturn(expectedQrData);

        var responseDto = mock(ResponseQRCodeDTO.class);
        when(responseDto.typeResponse()).thenReturn(typeResponse);

        when(mercadoPagoApi.generateQr(orderId, amount)).thenReturn(responseDto);

        String result = qrCodeGateway.createQRCode(orderId, amount);

        assertEquals(expectedQrData, result);
        verify(mercadoPagoApi, times(1)).generateQr(orderId, amount);
    }

    @Test
    void shouldPropagateErrorWhenApiFails() {
        String orderId = "123";
        String amount = "50.00";

        when(mercadoPagoApi.generateQr(anyString(), anyString()))
                .thenThrow(FeignException.BadRequest.class);

        assertThrows(FeignException.class, () -> qrCodeGateway.createQRCode(orderId, amount));
    }

    @Test
    void shouldThrowExceptionWhenApiResponseIsNull() {
        String orderId = "123";
        String amount = "50.00";

        when(mercadoPagoApi.generateQr(orderId, amount)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> qrCodeGateway.createQRCode(orderId, amount));
    }
}