package um.si;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import um.si.Order;

import java.io.IOException;
import java.util.Map;

public class OrderDeserializer<T> implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public Object deserialize(String s, byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = null;
        try {
            order = objectMapper.readValue(bytes, Order.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public void close() {

    }
}
