/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdvfx;

/**
 *
 * @author willi
 */
public class Produto {

    private int id;
    private String nome;
    private Double valorVenda;
    private Double estoque;

    public Produto(int id, String nome, double valorVenda, double estoque) {
        this.id = id;
        this.nome = nome;
        this.valorVenda = valorVenda;
        this.estoque = estoque;

    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public Double getEstoque() {
        return estoque;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public void setEstoque(Double estoque) {
        this.estoque = estoque;
    }
    

    void mostrarDados() {
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Valor Venda: " + valorVenda);
        System.out.println("Estoque: " + estoque);
        System.out.println("---------------------------");
    }

}
