package br.wifive.reservaproject;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import br.wifive.reservaproject.handler.ReservaHandler;
import br.wifive.reservaproject.handler.SalaHandler;

public class ApiServer {
	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/reservas", new ReservaHandler());
		server.createContext("/salas", new SalaHandler());
		server.setExecutor(null);
        server.start();
        System.out.println("Servidor rodando na porta 8000...");
	}
}