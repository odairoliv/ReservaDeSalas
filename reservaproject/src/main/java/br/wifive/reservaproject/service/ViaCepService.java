package br.wifive.reservaproject.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.wifive.reservaproject.model.ViaCepResponse;

public class ViaCepService {
	private HttpClient client = HttpClient.newHttpClient();
	private ObjectMapper mapper = new ObjectMapper();

	public ViaCepResponse buscarEndereco(String cep) throws Exception {
		String cepLimpo = cep.replace("-", "").replace(".", "");
		String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ViaCepResponse viaCep = mapper.readValue(response.body(), ViaCepResponse.class);

		if (viaCep.getCep() == null) {
			throw new Exception("CEP inválido ou não encontrado.");
		}

		return viaCep;
	}
}