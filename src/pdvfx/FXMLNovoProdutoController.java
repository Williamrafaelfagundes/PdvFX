/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package pdvfx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
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
public class FXMLNovoProdutoController implements Initializable {

    String nome;
    double valorDeVenda;
    double estoque;

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
    private Button btnVoltar;

    @FXML
    private TextField inputNomeProduto;

    @FXML
    private TextField inputValorDeVenda;

    @FXML
    private Label labelObrigacaoEstoqueInicial;

    @FXML
    private Label labelObrigacaoNomeProduto;

    @FXML
    private Label labelObrigacaoValorDeVenda;

    @FXML
    void cancelarOperacao(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja realmente cancelar a operação?");
        alert.setContentText("As alterações não salvas serão perdidas.");

        // Exibe a caixa de diálogo e aguarda a resposta
        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Usuário confirmou, executa o cancelamento
            voltarParaTelaCadastro(event);
        }
        // Se cancelar ou fechar o alerta, não faz nada
    }

    @FXML
    void salvarProduto(ActionEvent event) {
        boolean dadosValidos = true;

        labelObrigacaoNomeProduto.setVisible(false);
        labelObrigacaoValorDeVenda.setVisible(false);
        labelObrigacaoEstoqueInicial.setVisible(false);

        String nome = inputNomeProduto.getText();
        String valorStr = inputValorDeVenda.getText().replace(",", ".");
        String estoqueStr = InputEstoqueInicial.getText().replace(",", ".");

        double valorDeVenda = 0;
        double estoque = 0;

        // Valida nome
        if (nome == null || nome.trim().isEmpty()) {
            labelObrigacaoNomeProduto.setVisible(true);
            dadosValidos = false;
        }

        // Valida valorDeVenda
        try {
            valorDeVenda = Double.parseDouble(valorStr);
            if (valorDeVenda < 0) {
                labelObrigacaoValorDeVenda.setVisible(true);
                dadosValidos = false;
            }
        } catch (NumberFormatException e) {
            labelObrigacaoValorDeVenda.setVisible(true);
            dadosValidos = false;
        }

        // Valida estoque
        try {
            estoque = Double.parseDouble(estoqueStr);
            if (estoque < 0) {
                labelObrigacaoEstoqueInicial.setVisible(true);
                dadosValidos = false;
            }
        } catch (NumberFormatException e) {
            labelObrigacaoEstoqueInicial.setVisible(true);
            dadosValidos = false;
        }

        if (dadosValidos) {
            String sql = "INSERT INTO produto (nome, valor_venda, estoque) VALUES (?, ?, ?)";

            try (Connection conexao = getConnection(); java.sql.PreparedStatement pstmt = conexao.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, nome);
                pstmt.setDouble(2, valorDeVenda);
                pstmt.setDouble(3, estoque);

                int linhasAfetadas = pstmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    // Recupera o ID gerado
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int idGerado = generatedKeys.getInt(1);

                            // Mostra mensagem de sucesso
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Sucesso");
                            alert.setHeaderText(null);
                            alert.setContentText("Produto salvo com sucesso!");
                            alert.showAndWait();

                            Stage stageAtual = (Stage) btnSalvar.getScene().getWindow();
                            // Abre a tela de edição do produto criado
                            abrirTelaEdicao(new Produto(idGerado, nome, valorDeVenda, estoque), stageAtual);

                            // Fecha a tela atual
                            
                            stageAtual.close();
                            

                        } else {
                            System.err.println("Falha ao obter ID do produto inserido.");
                        }
                    }

                } else {
                    System.out.println("Falha ao adicionar produto.");
                }

            } catch (SQLException e) {
                System.err.println("Erro ao adicionar produto: " + e.getMessage());
            }
        }
    }

    private void abrirTelaEdicao(Produto produto, Stage stageAtual) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEditarProduto.fxml"));
            Parent root = loader.load();

            FXMLEditarProdutoController controller = loader.getController();
            controller.setProdutoParaEditar(produto);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Produto");
            stage.show();
            
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stageAtual, stage);

        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        labelObrigacaoNomeProduto.setVisible(false);
        labelObrigacaoValorDeVenda.setVisible(false);
        labelObrigacaoEstoqueInicial.setVisible(false);

        InputEstoqueInicial.textProperty().addListener((obs, oldVal, newVal) -> {
            // Permite apenas números com no máximo uma vírgula e duas casas decimais
            if (!newVal.matches("\\d{0,7}(,\\d{0,2})?")) {
                InputEstoqueInicial.setText(oldVal);
            }
        });
        inputValorDeVenda.textProperty().addListener((obs, oldVal, newVal) -> {
            // Permite apenas números com no máximo uma vírgula e duas casas decimais
            if (!newVal.matches("\\d{0,7}(,\\d{0,2})?")) {
                inputValorDeVenda.setText(oldVal);
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
