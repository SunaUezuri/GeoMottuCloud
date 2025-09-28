package br.com.geomottu.api.exceptions;

public class PlacaNaoEncontradaException extends Exception{

    public PlacaNaoEncontradaException(){
        super("Não foi possível localizar um objeto com esta placa");
    }

    public PlacaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
