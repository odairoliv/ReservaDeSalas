package br.wifive.reservaproject.repository;

import br.wifive.reservaproject.model.Reserva;
import br.wifive.reservaproject.config.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaRepository {

    public void salvar(Reserva r) throws SQLException {
        String sql = "INSERT INTO reserva (id, sala_id, professor, data_reserva, horario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getId());
            stmt.setString(2, r.getSalaId());
            stmt.setString(3, r.getProfessor());
            stmt.setString(4, r.getDataReserva());
            stmt.setString(5, r.getHorario());
            stmt.executeUpdate();
        }
    }

    public List<Reserva> listar() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reserva";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Reserva(
                        rs.getString("id"), rs.getString("sala_id"), rs.getString("professor"),
                        rs.getString("data_reserva"), rs.getString("horario")));
            }
        }
        return lista;
    }

    public Reserva buscarPorId(String id) throws SQLException {
        String sql = "SELECT * FROM reserva WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Reserva(rs.getString("id"), rs.getString("sala_id"), rs.getString("professor"), rs.getString("data_reserva"), rs.getString("horario"));
            }
        }
        return null;
    }

    public void deletar(String id) throws SQLException {
        String sql = "DELETE FROM reserva WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Reserva r) throws SQLException {
        String sql = "UPDATE reserva SET sala_id = ?, professor = ?, data_reserva = ?, horario = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getSalaId());
            stmt.setString(2, r.getProfessor());
            stmt.setString(3, r.getDataReserva());
            stmt.setString(4, r.getHorario());
            stmt.setString(5, r.getId());
            stmt.executeUpdate();
        }
    }

    public boolean verificarConflito(String salaId, String data, String horario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reserva WHERE sala_id = ? AND data_reserva = ? AND horario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, salaId);
            stmt.setString(2, data);
            stmt.setString(3, horario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}