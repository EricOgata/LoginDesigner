package com.example.bruno.logindesigner;

/**
 * Created by SENAI on 29/10/2016.
 */

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private Boolean ativo;
    private String login;
    private Integer idPropriedade;
    private Integer idPerfil;

    public Usuario(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getIdPropriedade() {
        return idPropriedade;
    }

    public void setIdPropriedade(Integer idPropriedade) {
        this.idPropriedade = idPropriedade;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }
}
