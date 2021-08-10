package br.com.maiara.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
       var producer = new KafkaProducer<String, String>(properties()); //configurações iniciais de conexão com kafka
       var value = "123, 8787878788788, 123456"; //mensagem
       var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value); //configurações de envio de mensagem

       Callback  callback = (data, ex) -> {
            if (ex != null){
                ex.printStackTrace();
                return;
            }
            //Isso será um observer, quando terminar, irá printar as informações em console
            System.out.println("Sucesso" + data.topic() + ":::partition: " + data.partition() + "/offset: " + data.offset() + "/timestamp: " + data.timestamp());
        };

       var email = "welcome!";
       var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, email);

       producer.send(record, callback).get(); //método assíncrono, por isso o get() -> ele espera o método terminar.
       producer.send(emailRecord, callback).get();
    }

    private static Properties properties(){
     var properties = new  Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
     return properties;
    }
}
