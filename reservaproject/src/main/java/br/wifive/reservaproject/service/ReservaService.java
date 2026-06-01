package br.wifive.reservaproject.service;

import br.wifive.reservaproject.model.Reserva;
import br.wifive.reservaproject.repository.ReservaRepository;
import br.wifive.reservaproject.repository.SalaRepository;
import br.wifive.reservaproject.rabbit.ReservaProducer;
import br.wifive.reservaproject.exception.*;
import java.util.List;
import java.util.UUID;

public class ReservaService {
    private ReservaRepository repo = new ReservaRepository();
    private SalaRepository salaRepo = new SalaRepository();
    private ReservaProducer producer = new ReservaProducer(); // Instância do Producer do RabbitMQ

    public Reserva criarReserva(Reserva r) throws Exception {
        // Validações obrigatórias
        if (r.getProfessor() == null || r.getSalaId() == null) {
            throw new Exception("Dados obrigatórios ausentes.");
        }
        if (salaRepo.buscarPorId(r.getSalaId()) == null) {
            throw new SalaNaoEncontradaException("A sala informada não existe.");
        }
        if (repo.verificarConflito(r.getSalaId(), r.getDataReserva(), r.getHorario())) {
            throw new ReservaConflitoException("Horário ocupado.");
        }
        
        // Definição do ID único e persistência no banco de dados via JDBC
        r.setId(UUID.randomUUID().toString());
        repo.salvar(r);
        
        // MENSAGERIA ASSÍNCRONA: Publica a mensagem de confirmação na fila do RabbitMQ
        String msgFila = "CONFIRMAÇÃO DE RESERVA | ID: " + r.getId() + " | Prof: " + r.getProfessor() + " | Data: " + r.getDataReserva();
        producer.enviarMensagem(msgFila);
        
        return r;
    }

    public List<Reserva> listarReservas() throws Exception {
        return repo.listar();
    }

    public Reserva buscarPorId(String id) throws Exception {
        Reserva r = repo.buscarPorId(id);
        if (r == null) {
            throw new ReservaNaoEncontradaException("Reserva não encontrada.");
        }
        return r;
    }

    public void deletar(String id) throws Exception {
        buscarPorId(id);
        repo.deletar(id);
    }

    public Reserva atualizar(Reserva r) throws Exception {
        if (r.getId() == null) {
            throw new Exception("ID obrigatório.");
        }
        if (salaRepo.buscarPorId(r.getSalaId()) == null) {
            throw new SalaNaoEncontradaException("Sala inexistente.");
        }
        
        Reserva antiga = buscarPorId(r.getId());	
        boolean mudouHorario = !antiga.getDataReserva().equals(r.getDataReserva()) || !antiga.getHorario().equals(r.getHorario());
        
        if (mudouHorario && repo.verificarConflito(r.getSalaId(), r.getDataReserva(), r.getHorario())) {
            throw new ReservaConflitoException("Novo horário ocupado.");
        }
        
        repo.atualizar(r);
        return r;
    }
}