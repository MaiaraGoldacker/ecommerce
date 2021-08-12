package br.com.maiara.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
       var dispatcher = new KafkaDispatcher();
       var key = UUID.randomUUID().toString(); //UUID faz com que a mensagem seja diferente, então o costumer será uma partição distinta
       var value = key + ", 8787878788788, 123456"; //mensagem
        dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

        var email = "welcome to email service!";
        dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
    }
}
