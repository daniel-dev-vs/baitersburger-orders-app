
package br.com.fiap.baitersburger_orders.infra.apis;

import br.com.fiap.baitersburger_orders.infra.apis.feign.MercadoPagoClient;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MercadoPagoApiTest {

    private MercadoPagoClient client;
    private MercadoPagoApi api;

    @BeforeEach
    void setUp() {
        client = mock(MercadoPagoClient.class);
        api = new MercadoPagoApi(client);
        ReflectionTestUtils.setField(api, "mercadoPagoExternalPosId", "POS-123");
        ReflectionTestUtils.setField(api, "mercadoPagadoAccessToken", "ACCESS-TOKEN");
    }

    @Test
    void generateQr_shouldDelegateToClientWithBuiltRequest() {
        String orderId = "order-1";
        String amount = "100.00";
        ResponseQRCodeDTO expectedResponse = new ResponseQRCodeDTO(
                "id-mercado-pago",
                "approved",
                "order-1",
                new TypeResponse("qr"));

        when(client.createOrderQRCode(anyString(), anyString(), any(RequestQRCodeDTO.class)))
                .thenReturn(expectedResponse);

        ResponseQRCodeDTO result = api.generateQr(orderId, amount);

        assertSame(expectedResponse, result);

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RequestQRCodeDTO> dtoCaptor = ArgumentCaptor.forClass(RequestQRCodeDTO.class);

        verify(client).createOrderQRCode(idCaptor.capture(), tokenCaptor.capture(), dtoCaptor.capture());

        assertNotNull(idCaptor.getValue());
        assertEquals("Bearer ACCESS-TOKEN", tokenCaptor.getValue());

        RequestQRCodeDTO dto = dtoCaptor.getValue();
        assertEquals("qr", dto.type());
        assertEquals(amount, dto.totalAmount());
        assertEquals(orderId, dto.externalReference());
        assertNotNull(dto.configQRCode());
        assertNotNull(dto.transactions());
    }

    @Test
    void createDto_shouldBuildProperRequestBody() {
        String orderId = "order-2";
        String amount = "50.00";

        RequestQRCodeDTO dto = api.createDto(orderId, amount);

        assertEquals("qr", dto.type());
        assertEquals(amount, dto.totalAmount());
        assertEquals(orderId, dto.externalReference());

        ConfigQRCode config = dto.configQRCode();
        assertNotNull(config);
        QR qr = config.qr();
        assertNotNull(qr);
        assertEquals("POS-123", qr.externalPosId());
        assertEquals("dynamic", qr.mode());

        Transaction transaction = dto.transactions();
        assertNotNull(transaction);
        List<Payment> payments = transaction.payments().stream().toList();
        assertNotNull(payments);
        assertEquals(1, payments.size());
        assertEquals(amount, payments.get(0).amount());
    }
}
