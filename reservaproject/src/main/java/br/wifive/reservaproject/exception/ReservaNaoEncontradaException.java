package br.wifive.reservaproject.exception;

public class ReservaNaoEncontradaException extends Exception {
    public ReservaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}