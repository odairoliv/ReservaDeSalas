package br.wifive.reservaproject.handler;

import com.sun.net.httpserver.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.wifive.reservaproject.model.Sala;
import br.wifive.reservaproject.service.SalaService;
import br.wifive.reservaproject.exception.SalaNaoEncontradaException;
import br.wifive.reservaproject.exception.CepForaException;
import java.io.*;

public class SalaHandler implements HttpHandler {
	private ObjectMapper mapper = new ObjectMapper();
	private SalaService service = new SalaService();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String metodo = exchange.getRequestMethod();
		String path = exchange.getRequestURI().getPath();
		try {
			if (metodo.equals("GET") && path.matches("/salas/[^/]+"))
				buscarPorId(exchange);
			else if (metodo.equals("GET"))
				listar(exchange);
			else if (metodo.equals("POST"))
				criar(exchange);
			else if (metodo.equals("DELETE") && path.matches("/salas/[^/]+"))
				deletar(exchange);
			else if (metodo.equals("PUT"))
				atualizar(exchange);
			else
				exchange.sendResponseHeaders(405, -1);
		} catch (SalaNaoEncontradaException e) {
			enviar(exchange, 404, "{\"erro\":\"" + e.getMessage() + "\"}");
		} catch (CepForaException e) {
			enviar(exchange, 400, "{\"erro\":\"" + e.getMessage() + "\"}");
		} catch (Exception e) {
			enviar(exchange, 500, "{\"erro\":\"Erro no servidor.\"}");
		}
	}

	private void listar(HttpExchange exchange) throws Exception {
		enviar(exchange, 200, mapper.writeValueAsString(service.listarSalas()));
	}

	private void criar(HttpExchange exchange) throws Exception {
		enviar(exchange, 201,
				mapper.writeValueAsString(service.criarSala(mapper.readValue(exchange.getRequestBody(), Sala.class))));
	}

	private void buscarPorId(HttpExchange exchange) throws Exception {
		enviar(exchange, 200,
				mapper.writeValueAsString(service.buscarPorId(exchange.getRequestURI().getPath().split("/")[2])));
	}

	private void deletar(HttpExchange exchange) throws Exception {
		service.deletar(exchange.getRequestURI().getPath().split("/")[2]);
		enviar(exchange, 200, "{\"msg\":\"Removida\"}");
	}

	private void atualizar(HttpExchange exchange) throws Exception {
		enviar(exchange, 200,
				mapper.writeValueAsString(service.atualizar(mapper.readValue(exchange.getRequestBody(), Sala.class))));
	}

	private void enviar(HttpExchange exchange, int status, String resp) throws IOException {
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		exchange.sendResponseHeaders(status, resp.getBytes("UTF-8").length);
		OutputStream os = exchange.getResponseBody();
		os.write(resp.getBytes("UTF-8"));
		os.close();
	}
}