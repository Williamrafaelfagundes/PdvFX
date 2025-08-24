/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package pdvfx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static pdvfx.ConexaoBanco.getConnection;

/**
 *
 * @author willi
 */
public class FXMLEditarProdutoController implements Initializable {

    String nome;
    String nomeAtualDoProduto;

    @FXML
    private TextField codProduto;

    @FXML
    private TextField InputEstoqueInicial;

    @FXML
    private Button btnCadastro;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnPDV;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnVenda;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnVoltar;

    @FXML
    private TextField inputNomeProduto;

    @FXML
    private Label labelFeedBack;

    @FXML
    private TextField inputValorDeVenda;

    @FXML
    void salvarProduto(ActionEvent event) {

        nome = inputNomeProduto.getText();
        String idTexto = codProduto.getText();
        String valorTexto = inputValorDeVenda.getText().replace(",", ".");
        String estoqueTexto = InputEstoqueInicial.getText().replace(",", ".");

        if (idTexto.isEmpty()) {
            System.out.println("ID do produto não informado.");
            return;
        }

        int id = Integer.parseInt(idTexto);
        double valorVenda = 0;
        double estoque = 0;

        try {
            valorVenda = Double.parseDouble(valorTexto);
            estoque = Double.parseDouble(estoqueTexto);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter valor ou estoque: " + e.getMessage());
            labelFeedBack.setText("Erro: valor ou estoque inválido.");
            return;
        }

        String sql = "UPDATE produto SET nome = ?, valor_venda = ?, estoque = ? WHERE id = ?";

        try (Connection conexao = getConnection(); java.sql.PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setDouble(2, valorVenda);
            pstmt.setDouble(3, estoque);
            pstmt.setInt(4, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto atualizado com sucesso!");
                labelFeedBack.setText("Produto atualizado com sucesso!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null); // pode deixar nulo para evitar cabeçalho desnecessário
                alert.setContentText("Produto salvo com sucesso!");

                alert.showAndWait(); // mostra o alerta e aguarda o clique no botão "OK"

            } else {
                System.out.println("Erro ao editar produto.");
                labelFeedBack.setText("Erro ao salvar produto.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao editar produto: " + e.getMessage());
            labelFeedBack.setText("Erro ao salvar produto.");
        }
    }

    @FXML
    public void excluirProduto(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja realmente excluir o produto?");
        alert.setContentText("O produto será excluido permanentemente e não será possível reverter a situação.");

        // Exibe a caixa de diálogo e aguarda a resposta
        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Usuário confirmou, executa o cancelamento
            int id = Integer.parseInt(codProduto.getText());

            String sql = "DELETE FROM produto WHERE id = ?";

            try (Connection conexao = getConnection(); java.sql.PreparedStatement pstmt = conexao.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                int linhasAfetadas = pstmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Produto excluído com sucesso!");
                    voltarParaTelaCadastro(event);

                } else {
                    System.out.println("Produto com ID informado não foi encontrada.");
                }

            } catch (SQLException e) {
                System.err.println("Erro ao excluir Produto: " + e.getMessage());
            }

        }

    }

    public void setProdutoParaEditar(Produto produto) {
        nomeAtualDoProduto = produto.getNome();
        inputNomeProduto.setText(produto.getNome());
        codProduto.setText(String.valueOf(produto.getId()));
        inputValorDeVenda.setText(String.valueOf(produto.getValorVenda()).replace(".", ","));
        InputEstoqueInicial.setText(String.valueOf(produto.getEstoque()).replace(".", ","));
    }

    @FXML
    void irParaTelaPDV(ActionEvent event) {
        System.out.println("Entrou no método");
        try {
            System.out.println("entrou no try");
            // Carrega o arquivo FXML da nova tela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPDV.fxml"));
            Parent root = loader.load();

            // Cria uma nova Stage e Scene
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Configura e mostra a nova Stage
            newStage.setScene(scene);
            newStage.setTitle("Pdv");

            newStage.show();

            Stage stage = (Stage) btnPDV.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stage, newStage);
            // Fecha a tela atual
            stage.close();

        } catch (IOException e) {
        }
    }

    @FXML
    void voltarParaTelaCadastro(ActionEvent event) {
        try {
            // Carrega o arquivo FXML da nova tela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCadastro.fxml"));
            Parent root = loader.load();

            // Cria uma nova Stage e Scene
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Configura e mostra a nova Stage
            newStage.setScene(scene);
            newStage.setTitle("Cadastro");

            newStage.show();

            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stage, newStage);

            // Fecha a tela atual
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        inputValorDeVenda.textProperty().addListener((obs, oldVal, newVal) -> {
            // Permite apenas números com no máximo uma vírgula e duas casas decimais
            if (!newVal.matches("\\d{0,7}(,\\d{0,2})?")) {
                inputValorDeVenda.setText(oldVal);
            }
        });
        InputEstoqueInicial.textProperty().addListener((obs, oldVal, newVal) -> {
            // Permite apenas números com no máximo uma vírgula e duas casas decimais
            if (!newVal.matches("\\d{0,7}(,\\d{0,2})?")) {
                InputEstoqueInicial.setText(oldVal);
            }
        });
    }

    @FXML
    void abrirTelaVenda() {
        try {
            // Carrega o arquivo FXML da nova tela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLVenda.fxml"));
            Parent root = loader.load();

            // Cria uma nova Stage e Scene
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Configura e mostra a nova Stage
            newStage.setScene(scene);
            newStage.setTitle("Venda de Produtos");

            newStage.show();

            Stage stage = (Stage) btnVenda.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stage, newStage);

            // Fecha a tela atual
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
