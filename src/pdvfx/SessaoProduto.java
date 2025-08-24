/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdvfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author willi
 */
public class SessaoProduto {

    private static ObservableList<Produto> produtosSelecionados = FXCollections.observableArrayList();

    public static void adicionarProduto(Produto produto) {
        if (!produtosSelecionados.contains(produto)) {
            produtosSelecionados.add(produto);
        }
    }

    public static ObservableList<Produto> getProdutos() {
        return produtosSelecionados;
    }

    public static void limpar() {
        produtosSelecionados.clear();
    }
}
