package br.wifive.reservaproject.model;

public class Sala {
	private String id;
	private String nome;
	private int capacidade;
	private String cep;
	private String localizacao;
	public Sala() {
	}

	public Sala(String id, String nome, int capacidade, String cep, String localizacao) {
		this.id = id;
		this.nome = nome;
		this.capacidade = capacidade;
		this.cep = cep;
		this.localizacao = localizacao;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
}