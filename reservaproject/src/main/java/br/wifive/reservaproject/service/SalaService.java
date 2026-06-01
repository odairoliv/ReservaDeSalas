package br.wifive.reservaproject.service;

import br.wifive.reservaproject.model.Sala;
import br.wifive.reservaproject.model.ViaCepResponse;
import br.wifive.reservaproject.repository.SalaRepository;
import br.wifive.reservaproject.exception.SalaNaoEncontradaException;
import br.wifive.reservaproject.exception.CepForaException;
import java.util.List;
import java.util.UUID;

public class SalaService {
    private SalaRepository repo = new SalaRepository();
    private ViaCepService cepService = new ViaCepService();

    public Sala criarSala(Sala s) throws Exception {
        if (s.getNome() == null || s.getNome().trim().isEmpty()) {
            throw new Exception("Nome da sala obrigatório.");
        }
        
        if (s.getCep() != null && !s.getCep().isEmpty()) {
            ViaCepResponse endereco = cepService.buscarEndereco(s.getCep());
            
            if (!"PR".equalsIgnoreCase(endereco.getUf())) {
                throw new CepForaException("O sistema só permite cadastro de salas no estado do Paraná (PR).");
            }
            
            s.setLocalizacao(endereco.getLogradouro() + ", " + endereco.getLocalidade() + " - " + endereco.getUf());
        }

        s.setId(UUID.randomUUID().toString());
        repo.salvar(s);
        return s;
    }

    public List<Sala> listarSalas() throws Exception {
        return repo.listar();
    }

    public Sala buscarPorId(String id) throws Exception {
        Sala s = repo.buscarPorId(id);
        if (s == null) throw new SalaNaoEncontradaException("Sala não encontrada.");
        return s;
    }

    public void deletar(String id) throws Exception {
        buscarPorId(id);
        repo.deletar(id);
    }

    public Sala atualizar(Sala s) throws Exception {
        if (s.getId() == null) throw new Exception("ID obrigatório.");
        buscarPorId(s.getId());
        repo.atualizar(s);
        return s;
    }
}