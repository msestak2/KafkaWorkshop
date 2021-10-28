package um.si;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class KafkaStreamer {
    private KafkaStreams streams;


    public KafkaStreamer(KafkaStreams streams) {
        this.streams = streams;
    }

    public static Properties loadProperties(String fileName) throws IOException {
        final Properties envProps = new Properties();
        final FileInputStream input = new FileInputStream(fileName);
        envProps.load(input);
        input.close();

        return envProps;
    }


    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 1) {
            throw new IllegalArgumentException(
                    "This program takes the path to an environment configuration file as an argument");
        }

        final Properties props = KafkaStreamer.loadProperties(args[0]);
        props.put(
                StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
                Serdes.Integer().getClass().getName());
        props.put(
                StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                Serde.class);

        org.apache.kafka.common.serialization.Serde<Order> serdes = Serdes.serdeFrom(new OrderSerializer<Order>(), new OrderDeserializer());
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<Integer, Order> inputStream = streamsBuilder.stream(props.getProperty("input.topic.name"), Consumed.with(Serdes.Integer(), serdes));

        inputStream.map((k,v)->new KeyValue<>(v.getUser_id().intValue(),v.getOrder_no()))
                .groupByKey(Grouped.with(Serdes.Integer(), Serdes.String())).count().toStream().mapValues(value -> value.toString())
                .to("output-orders", Produced.with(Serdes.Integer(), Serdes.String()));

        KTable<Integer, String> orderCountsStream = streamsBuilder.table("output-orders", Consumed.with(Serdes.Integer(), Serdes.String()));
        orderCountsStream.filter((key, value) -> Integer.valueOf(value) > 5).toStream()
                .to("more-than-5-orders", Produced.with(Serdes.Integer(), Serdes.String()));

        inputStream
            .map((k,v)->new KeyValue<>(v.getOrder_no(),v.getItems()))
            .flatMapValues(value -> Arrays.asList(value.get("quantity")))
            .groupByKey(Grouped.with(Serdes.String(),Serdes.Integer()))
            .reduce(Integer::sum)
            .toStream()
            .mapValues(v -> v.toString())
            .to("order-quantities", Produced.with(Serdes.String(), Serdes.String()));

        Topology topology = streamsBuilder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();

    }
}
