package br.com.fiap.baitersburger_orders.infra.persistence;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDynamoRepository {


    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<OrderDynamoEntity> orderTable;

    public OrderDynamoRepository(DynamoDbClient dynamoDbClient) {
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.orderTable = enhancedClient.table("Orders",
                TableSchema.fromBean(OrderDynamoEntity.class));
    }

    public void save(OrderDynamoEntity order) {
        orderTable.putItem(order);
    }

    public OrderDynamoEntity getById(String orderId) {
        return orderTable.getItem(Key.builder()
                .partitionValue(orderId)
                .build());
    }

    public List<OrderDynamoEntity> getByStatus(String status) {
        DynamoDbIndex<OrderDynamoEntity> index = orderTable.index("status-createdAt-index");

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                        .partitionValue(status)
                        .build());

        return index.query(queryConditional)
                .iterator()
                .next()
                .items()
                .stream().
                toList();


    }

    public List<OrderDynamoEntity> getAll() {
        return  orderTable.scan().items().stream().collect(Collectors.toList());
    }
}
