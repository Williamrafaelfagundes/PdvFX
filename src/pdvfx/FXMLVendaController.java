/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package pdvfx;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import static pdvfx.ConexaoBanco.getConnection;

/**
 *
 * @author willi
 */
public class FXMLVendaController implements Initializable {

    @FXML
    private Button btnAvancar;

    @FXML
    private Button btnCadastro;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnPDV;

    @FXML
    private Button btnPesquisar;

    @FXML
    private Button btnVenda;

    @FXML
    private TableColumn<Produto, Integer> colunaCod;

    @FXML
    private TableColumn<Produto, Double> colunaEstoque;

    @FXML
    private TableColumn<Produto, String> colunaNomeProduto;

    @FXML
    private TableColumn<Produto, Double> colunaValorVenda;

    @FXML
    private TextField inputCliente;

    @FXML
    private TextField inputCodProduto;

    @FXML
    private TextField inputNomeProduto;

    @FXML
    private TableView<Produto> tabelaListagemProdutosAdicionados;

    @FXML
    void avancar(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabela();
        adicionarColunaRemover();

    }

    public void carregarProdutosSelecionados() {
        tabelaListagemProdutosAdicionados.setItems(SessaoProduto.getProdutos());
        // SessaoProdutos.limpar(); // só limpe se quiser esvaziar após a venda
    }

    void abrirTelaEdicao(Produto produto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEditarProduto.fxml"));
            Parent root = loader.load();

            FXMLEditarProdutoController controller = loader.getController();
            controller.setProdutoParaEditar(produto); // método que você cria no outro controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Produto");
            stage.show();

            Stage stageAtual = (Stage) tabelaListagemProdutosAdicionados.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stageAtual, stage);

            // Fecha a janela atual (Cadastro)
            stageAtual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adicionarColunaEditar() {

    }

    private void configurarTabela() {
        if (colunaCod != null && colunaNomeProduto != null && colunaValorVenda != null && colunaEstoque != null) {
            // 1. VINCULAR OS DADOS ÀS COLUNAS (ESSENCIAL)
            // Esta parte estava faltando no seu código atualizado.
            colunaCod.setCellValueFactory(new PropertyValueFactory<>("id"));
            colunaNomeProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colunaValorVenda.setCellValueFactory(new PropertyValueFactory<>("valorVenda"));
            colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));

            // 2. CONFIGURAR LARGURA PROPORCIONAL
            colunaCod.prefWidthProperty().bind(tabelaListagemProdutosAdicionados.widthProperty().multiply(0.10)); // 10%
            colunaNomeProduto.prefWidthProperty().bind(tabelaListagemProdutosAdicionados.widthProperty().multiply(0.40)); // 40%
            colunaValorVenda.prefWidthProperty().bind(tabelaListagemProdutosAdicionados.widthProperty().multiply(0.20)); // 20%
            colunaEstoque.prefWidthProperty().bind(tabelaListagemProdutosAdicionados.widthProperty().multiply(0.15)); // 15%

            // 3. FORMATAR A EXIBIÇÃO DAS CÉLULAS (SEU CÓDIGO ATUAL)
            // Formatação com vírgula como separador decimal
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            // ... o restante do seu código de formatação continua aqui, sem alterações ...

        } else {
            System.err.println("Erro: Colunas não foram injetadas corretamente pelo FXML.");
        }

        // Formatação com vírgula como separador decimal
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);

// Alinhar texto à esquerda
        colunaNomeProduto.setCellFactory(tc -> {
            TableCell<Produto, String> cell = new TableCell<Produto, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        setStyle("-fx-alignment: CENTER-LEFT;");
                    }
                }
            };
            return cell;
        });

// Alinhar código à direita
        colunaCod.setCellFactory(tc -> new TableCell<Produto, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

// Alinhar valor de venda à direita com "R$" e vírgula
        colunaValorVenda.setCellFactory(tc -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String valorFormatado = "R$ " + decimalFormat.format(item);
                    setText(valorFormatado);
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

// Alinhar estoque à direita com vírgula
        colunaEstoque.setCellFactory(tc -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(decimalFormat.format(item));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });
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

    void carregarDados() {
        System.out.println("Pesquisar produto...");
        ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

        String sql = "SELECT id, nome, valor_venda, estoque FROM produto";

        try (Connection conexao = getConnection(); Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                double valorVenda = rs.getDouble("valor_venda");
                if (rs.wasNull()) {
                    valorVenda = 0.0;
                }

                double estoque = rs.getDouble("estoque");
                if (rs.wasNull()) {
                    estoque = 0.0;
                }

                Produto produto = new Produto(id, nome, valorVenda, estoque);
                listaProdutos.add(produto);
            }

            tabelaListagemProdutosAdicionados.setItems(listaProdutos);

        } catch (SQLException e) {
            System.err.println("Erro ao consultar o banco: " + e.getMessage());
        }
    }


    public void receberProdutoSelecionado(Produto produto) {
        if (produto != null) {
            tabelaListagemProdutosAdicionados.getItems().add(produto);
        }
    }

    private void adicionarColunaRemover() {
        TableColumn<Produto, Void> colunaRemover = new TableColumn<>("Remover");

        colunaRemover.setCellFactory(param -> new TableCell<Produto, Void>() {
            private final Button btnRemover = new Button("Remover");

            {
                btnRemover.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-font-weight: bold;");
                btnRemover.setOnAction((ActionEvent event) -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    tabelaListagemProdutosAdicionados.getItems().remove(produto);
                    SessaoProduto.getProdutos().remove(produto); // opcional, se você usa essa lista global
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnRemover);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        colunaRemover.prefWidthProperty().bind(tabelaListagemProdutosAdicionados.widthProperty().multiply(0.15));
        tabelaListagemProdutosAdicionados.getColumns().add(colunaRemover);
    }

    @FXML
    void pesquisarProduto(ActionEvent event) {
        try {
            // Carrega o arquivo FXML da nova tela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPesquisaProduto.fxml"));
            Parent root = loader.load();

            // Cria uma nova Stage e Scene
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Pegue o valor dos campos de pesquisa (código ou nome)
            String termoPesquisa = inputCodProduto.getText().isEmpty()
                    ? inputNomeProduto.getText()
                    : inputCodProduto.getText();

            // Obtenha o controller da tela de pesquisa
            FXMLPesquisaProdutoController controller = loader.getController();

            // Passe o termo para o controller
            controller.setTermoPesquisa(termoPesquisa);

            // Configura e mostra a nova Stage
            newStage.setScene(scene);
            newStage.setTitle("Pesquisa Produto");

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
