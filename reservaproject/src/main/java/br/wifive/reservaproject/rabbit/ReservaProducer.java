package br.wifive.reservaproject.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ReservaProducer {
    private final static String QUEUE = "fila-reservas";

    public void enviarMensagem(String mensagem) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            
            channel.queueDeclare(QUEUE, true, false, false, null);
            channel.basicPublish("", QUEUE, null, mensagem.getBytes("UTF-8"));
            System.out.println(" [RabbitMQ - PRODUCER] Nova reserva postada na fila: '" + mensagem + "'");
            
        } catch (Exception e) {
            System.err.println("Erro ao conectar no RabbitMQ: " + e.getMessage());
        }
    }
}