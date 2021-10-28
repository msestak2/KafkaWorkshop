package um.si;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class Serde implements org.apache.kafka.common.serialization.Serde<Order> {
    private OrderSerializer serializer = new OrderSerializer();
    private OrderDeserializer deserializer = new OrderDeserializer<>();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer<Order> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<Order> deserializer() {
        return deserializer;
    }
}
