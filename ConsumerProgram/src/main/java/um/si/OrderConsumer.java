package um.si;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import um.si.Order;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Future;

public class OrderConsumer {
    private volatile boolean keepConsuming = true;
    private Consumer<String, Order> consumer;

    public OrderConsumer(Consumer<String, Order> consumer) {
        this.consumer = consumer;
    }

    public void runConsume(final Properties consumerProps) {
        try {
            consumer.subscribe(Collections.singletonList(consumerProps.getProperty("input.topic.name")));
            while (keepConsuming) {
                final ConsumerRecords<String, Order> consumerRecords = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, Order> record:
                     consumerRecords) {
                    System.out.println("Received order: " + record.value().toString());
                }
            }
        } finally {
            consumer.close();
        }
    }

    public static Properties loadProperties(String fileName) throws IOException {
        final Properties envProps = new Properties();
        final FileInputStream input = new FileInputStream(fileName);
        envProps.load(input);
        input.close();

        return envProps;
    }

    public void shutdown() {
        keepConsuming = false;
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 1) {
            throw new IllegalArgumentException(
                    "This program takes the path to an environment configuration file as an argument");
        }

        final Properties props = OrderConsumer.loadProperties(args[0]);
        final String topic = props.getProperty("input.topic.name");

        final Consumer<String, Order> consumer = new KafkaConsumer<>(props);
        final OrderConsumer consumerApplication = new OrderConsumer(consumer);

        Runtime.getRuntime().addShutdownHook(new Thread(consumerApplication::shutdown));

        consumerApplication.runConsume(props);
    }
}
