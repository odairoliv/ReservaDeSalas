package br.wifive.reservaproject.repository;

import br.wifive.reservaproject.model.Sala;
import br.wifive.reservaproject.config.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaRepository {

	public void salvar(Sala s) throws SQLException {
		String sql = "INSERT INTO sala (id, nome, capacidade, cep, localizacao) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, s.getId());
			stmt.setString(2, s.getNome());
			stmt.setInt(3, s.getCapacidade());
			stmt.setString(4, s.getCep());
			stmt.setString(5, s.getLocalizacao());
			stmt.executeUpdate();
		}
	}

	public List<Sala> listar() throws SQLException {
		List<Sala> lista = new ArrayList<>();
		String sql = "SELECT * FROM sala";
		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				lista.add(
						new Sala(rs.getString("id"), rs.getString("nome"), rs.getInt("capacidade"), rs.getString("cep"), // Novo
								rs.getString("localizacao")
						));
			}
		}
		return lista;
	}

	public Sala buscarPorId(String id) throws SQLException {
		String sql = "SELECT * FROM sala WHERE id = ?";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Sala(rs.getString("id"), rs.getString("nome"), rs.getInt("capacidade"), rs.getString("cep"), // Novo
						rs.getString("localizacao")
				);
			}
		}
		return null;
	}

	public void deletar(String id) throws SQLException {
		String sql = "DELETE FROM sala WHERE id = ?";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
		}
	}

	public void atualizar(Sala s) throws SQLException {
		String sql = "UPDATE sala SET nome = ?, capacidade = ?, cep = ?, localizacao = ? WHERE id = ?";
		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, s.getNome());
			stmt.setInt(2, s.getCapacidade());
			stmt.setString(3, s.getCep());
			stmt.setString(4, s.getLocalizacao());	
			stmt.setString(5, s.getId());
			stmt.executeUpdate();
		}
	}
}