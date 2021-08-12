package br.com.maiara.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class KafkaDispatcher implements Closeable {

    private final KafkaProducer<String, String> producer;

    KafkaDispatcher(){
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties(){
        var properties = new  Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

     void send(String topic, String key, String value) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<>(topic, key, value); //configurações de envio de mensagem

        Callback callback = (data, ex) -> {
            if (ex != null){
                ex.printStackTrace();
                return;
            }
            //Isso será um observer, quando terminar, irá printar as informações em console
            System.out.println("Sucesso" + data.topic() + ":::partition: " + data.partition() + "/offset: " + data.offset() + "/timestamp: " + data.timestamp());
        };

        producer.send(record, callback).get(); //método assíncrono, por isso o get() -> ele espera o método terminar.
    }

    @Override
    public void close(){
        producer.close();
    }
}
