package um.si;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class OrderSerializer<T> implements Serializer<T> {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        byte[] serializedValues = null;
        Order order = (Order)o;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            serializedValues = objectMapper.writeValueAsBytes(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return serializedValues;
    }

    @Override
    public void close() {

    }
}
