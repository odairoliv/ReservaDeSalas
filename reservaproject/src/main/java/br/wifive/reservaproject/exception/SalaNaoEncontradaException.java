package br.wifive.reservaproject.exception;

public class SalaNaoEncontradaException extends Exception {
    public SalaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}