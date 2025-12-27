package br.com.fiap.baitersburger_orders.infra.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDynamoRepositoryTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Mock
    private DynamoDbTable<OrderDynamoEntity> orderTable;

    @Mock
    private DynamoDbIndex<OrderDynamoEntity> orderIndex;

    private OrderDynamoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new OrderDynamoRepository(dynamoDbClient);

        try {
            var field = OrderDynamoRepository.class.getDeclaredField("orderTable");
            field.setAccessible(true);
            field.set(repository, orderTable);
        } catch (Exception e) {
            fail("Erro ao configurar mocks de persistência");
        }
    }

    @Test
    void shouldSaveOrder() {
        OrderDynamoEntity entity = new OrderDynamoEntity();
        entity.setOrderId(UUID.randomUUID());

        repository.save(entity);

        verify(orderTable, times(1)).putItem(entity);
    }

    @Test
    void shouldGetOrderById() {
        String id = UUID.randomUUID().toString();
        OrderDynamoEntity expected = new OrderDynamoEntity();

        when(orderTable.getItem(any(Key.class))).thenReturn(expected);

        OrderDynamoEntity result = repository.getById(id);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(orderTable).getItem(any(Key.class));
    }

    @Test
    void shouldGetOrdersByStatus() {
        String status = "RECEIVED";
        OrderDynamoEntity entity = new OrderDynamoEntity();

        Page<OrderDynamoEntity> page = Page.create(List.of(entity));
        SdkIterable<Page<OrderDynamoEntity>> sdkIterable = () -> List.of(page).iterator();
        PageIterable<OrderDynamoEntity> pageIterable = PageIterable.create(sdkIterable);

        when(orderTable.index("status-createdAt-index")).thenReturn(orderIndex);
        when(orderIndex.query(any(QueryConditional.class))).thenReturn(pageIterable);

        List<OrderDynamoEntity> results = repository.getByStatus(status);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(orderIndex).query(any(QueryConditional.class));
    }

    @Test
    void shouldGetAllOrders() {
        OrderDynamoEntity entity = new OrderDynamoEntity();

        Page<OrderDynamoEntity> page = Page.create(List.of(entity));
        SdkIterable<Page<OrderDynamoEntity>> sdkIterable = () -> List.of(page).iterator();
        PageIterable<OrderDynamoEntity> pageIterable = PageIterable.create(sdkIterable);

        when(orderTable.scan()).thenReturn(pageIterable);

        List<OrderDynamoEntity> results = repository.getAll();

        assertEquals(1, results.size());
        verify(orderTable).scan();
    }
}