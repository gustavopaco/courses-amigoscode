package com.pacoprojects.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Cliente {

    @Id
    @SequenceGenerator(name = "cliente_sequence", sequenceName = "cliente_sequence", allocationSize = 1)
    @GeneratedValue(generator = "cliente_sequence", strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String nome;
    private String email;
    private Integer idade;

    public Cliente(Integer id,
                   String nome,
                   String email,
                   Integer idade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.idade = idade;
    }

    public Cliente() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) && Objects.equals(nome, cliente.nome) && Objects.equals(email, cliente.email) && Objects.equals(idade, cliente.idade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, idade);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", idade=" + idade +
                '}';
    }
}
