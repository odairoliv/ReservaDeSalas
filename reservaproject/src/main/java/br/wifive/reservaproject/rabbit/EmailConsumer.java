package br.wifive.reservaproject.rabbit;

import com.rabbitmq.client.*;

public class EmailConsumer {
    private final static String QUEUE = "fila-reservas";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE, true, false, false, null);
        System.out.println(" [*] Serviço de E-mail iniciado.");
        System.out.println(" [*] Aguardando mensagens na fila. Para sair pressione CTRL+C");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), "UTF-8");
            System.out.println("\n [RabbitMQ - CONSUMER] Mensagem recebida!");
            System.out.println(" [x] Processando disparo de e-mail...");
         
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            
            System.out.println(" [x] E-mail enviado com sucesso! -> " + mensagem);
        };

        channel.basicConsume(QUEUE, true, callback, consumerTag -> { });
    }
}