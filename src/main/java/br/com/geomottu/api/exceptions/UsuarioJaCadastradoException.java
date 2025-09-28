package br.com.geomottu.api.exceptions;

public class UsuarioJaCadastradoException extends Exception{

    public UsuarioJaCadastradoException(){
        super("Nome de usuário já está em uso");
    }

    public UsuarioJaCadastradoException(String message){
        super(message);
    }
}
