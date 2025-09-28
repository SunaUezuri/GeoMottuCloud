package br.com.geomottu.api.exceptions;

public class IdNaoEncontradoException extends Exception{

    public IdNaoEncontradoException(){
        super("Não foi possível localizar um objeto com este Id");
    }

    public IdNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
