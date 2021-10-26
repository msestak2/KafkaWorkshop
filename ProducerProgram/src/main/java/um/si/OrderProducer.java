package um.si;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OrderProducer {

    private Producer<String, Order> producer;
    private String outTopic;

    public OrderProducer(Producer<String, Order> producer, String outTopic) {
        this.producer = producer;
        this.outTopic = outTopic;
    }

    public Future<RecordMetadata> produce() {
        Random rand = new Random();
        String orderNo = String.valueOf((int)(Math.random()*(10000 - 0 + 1) + 1));
        Order order = new Order(orderNo,System.currentTimeMillis(), Integer.valueOf(rand.nextInt((9-0) + 1)),
                Integer.valueOf(rand.nextInt((9-0) + 1)), Integer.valueOf(rand.nextInt((9-0) + 1)),
                new HashMap<String, Integer>(){{
                    put("item_id", Integer.valueOf(rand.nextInt((9-0) + 1)));
                    put("quantity", Integer.valueOf(rand.nextInt((8-0) + 1)));
                }}
        );
        ProducerRecord<String, Order> producerRecord = new ProducerRecord<>(outTopic, orderNo, order);
        System.out.println("[ORDER] new order: " + order.toString() + " sent!");
        return producer.send(producerRecord);
    }

    public static Properties loadProperties(String fileName) throws IOException {
        final Properties envProps = new Properties();
        final FileInputStream input = new FileInputStream(fileName);
        envProps.load(input);
        input.close();

        return envProps;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException(
                    "This program takes the path to an environment configuration file as an argument");
        }

        final Properties props = OrderProducer.loadProperties(args[0]);
        final String topic = props.getProperty("output.topic.name");
        final Producer<String, Order> producer = new KafkaProducer<>(props);
        final OrderProducer producerApp = new OrderProducer(producer, topic);

        while(true){
            producerApp.produce();

            Thread.sleep(10000);
        }

    }

    /*public static void main(String[] args) throws InterruptedException {

        String topicName = "orders";

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9094");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "um.si.OrderSerializer");

        /*List<String[]> pricelist = CSVReader.readData();
        for(int j =0; j<5; j++){
            String[] elements = pricelist.get(j);
            System.out.println("Item: " + elements[0] + ", price: " + elements[1]);
        }

        Random rand = new Random();

        Producer producer = new
        while(true) {
            String orderNo = String.valueOf((Math.random()*(10000 - 0 + 1) + 1));
            Order order = new Order(orderNo,System.currentTimeMillis(), Integer.valueOf(rand.nextInt((9-0) + 1)),
                    Integer.valueOf(rand.nextInt((9-0) + 1)), Integer.valueOf(rand.nextInt((9-0) + 1)),
                    new HashMap<String, Integer>(){{
                        put("item_id", Integer.valueOf(rand.nextInt((9-0) + 1)));
                        put("quantity", Integer.valueOf(rand.nextInt((8-0) + 1)));
                    }}
                    );

            ProducerRecord<String, Order> producerRecord = new ProducerRecord<>(topicName, order);
            producer.send(producerRecord);
            System.out.println("[ORDER] new order: " + order.toString() + " sent!");

            Thread.sleep(10000);
            }
        }

    }

}

class CSVReader {
    public static List<String[]> readData() {
        int count = 0;
        String file = "restaurant-1-orders.csv";
        List<String[]> content = new ArrayList<>();
        List<String[]> pricelist = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                content.add(line.split(","));
            }
        } catch (IOException e) {
            //Some error logging
        }
        for (String[] line:
             content) {
            pricelist.add(new String[]{line[2],line[4]});
        }
        pricelist.remove(0);
        return pricelist;
    }

 */
}
