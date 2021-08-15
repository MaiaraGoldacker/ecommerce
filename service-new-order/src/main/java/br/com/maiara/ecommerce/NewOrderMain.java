package br.com.maiara.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

       var orderDispatcher = new KafkaDispatcher<Order>();
       var emailDispatcher = new KafkaDispatcher<String>();

       var userId = UUID.randomUUID().toString(); //UUID faz com que a mensagem seja diferente, então o costumer será uma partição distinta
       var orderId = UUID.randomUUID().toString();
       var amount = new BigDecimal(Math.random() * 5000 + 1);
       var order = new Order(userId, orderId, amount);


        orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

       var emailKey = "welcome to email service!";
       //var email = new Email(emailKey, emailKey);
       emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, emailKey);
    }
}
