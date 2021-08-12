package br.com.maiara.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

class KafkaService implements Closeable {

    private final KafkaConsumer<String, String> consumer;
    private final ConsumerFunction parse;

     KafkaService(String groupId, String topic, ConsumerFunction parse){
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(groupId));
        consumer.subscribe(Collections.singletonList(topic));
    }

     void run() {
        while(true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("encontrou " + records.count() + " registros");

                for (var record : records) {
                    parse.consume(record);
                }
                System.out.println("Sent email");
            }
        }
    }

     static Properties properties(String groupId){
        var properties = new  Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
         properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"); //Processando poll de registros de um em um, fazendo q os registros recebidos, sejam recebidos e commitados em sequencia.
         // Caso seja um valor muito grande, pode ser que haja uma mudança de estado no kafka(recompilado por exemplo), e erro entre o processo de recebimento e commit de varios registros.
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
