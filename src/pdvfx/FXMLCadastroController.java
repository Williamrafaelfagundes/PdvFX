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
public class FXMLCadastroController implements Initializable {

    @FXML
    private Button btnCadastro;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnNovoProduto;

    @FXML
    private Button btnPDV;

    @FXML
    private Button btnVenda;

    @FXML
    private TextField pesquisaPorCodProduto;

    @FXML
    private TextField pesquisaPorNomeProduto;

    @FXML
    private TableView<Produto> tabelaListagemProdutos;

    @FXML
    private TableColumn<Produto, Integer> colunaCod;

    @FXML
    private TableColumn<Produto, String> colunaNomeProduto;

    @FXML
    private TableColumn<Produto, Double> colunaValorVenda;

    @FXML
    private TableColumn<Produto, Double> colunaEstoque;

    @FXML
    private TableColumn<Produto, Void> colunaEditar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabela();
        adicionarColunaEditar();

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

            Stage stageAtual = (Stage) tabelaListagemProdutos.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stageAtual, stage);

            // Fecha a janela atual (Cadastro)
            stageAtual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adicionarColunaEditar() {
        TableColumn<Produto, Void> colBtn = new TableColumn<>("Editar");

        colBtn.prefWidthProperty().bind(tabelaListagemProdutos.widthProperty().multiply(0.15)); // 15%

        colBtn.setCellFactory(param -> new TableCell<Produto, Void>() {
            private final Button btn = new Button("Editar");

            {
                btn.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    abrirTelaEdicao(produto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        tabelaListagemProdutos.getColumns().add(colBtn);
    }

    @FXML
    void irParaNovoProduto(ActionEvent event) {
        try {
            //Carrega o arquivo FXML da nova tela 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNovoProduto.fxml"));
            Parent root = loader.load();

            // Cria uma nova Stage e Scene
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            // Configura e mostra a nova Stage
            newStage.setScene(scene);
            newStage.setTitle("Cadastro");

            newStage.show();

            Stage stage = (Stage) btnNovoProduto.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stage, newStage);

            stage.close();

        } catch (IOException e) {
        }
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
            colunaCod.prefWidthProperty().bind(tabelaListagemProdutos.widthProperty().multiply(0.10)); // 10%
            colunaNomeProduto.prefWidthProperty().bind(tabelaListagemProdutos.widthProperty().multiply(0.40)); // 40%
            colunaValorVenda.prefWidthProperty().bind(tabelaListagemProdutos.widthProperty().multiply(0.20)); // 20%
            colunaEstoque.prefWidthProperty().bind(tabelaListagemProdutos.widthProperty().multiply(0.15)); // 15%

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

    @FXML
    void pesquisarProduto(ActionEvent event) {
        String codigoTexto = pesquisaPorCodProduto.getText().trim();
        String nomePesquisa = pesquisaPorNomeProduto.getText().trim();

        ObservableList<Produto> listaResultado = FXCollections.observableArrayList();

        String sql;
        boolean buscarPorCodigo = !codigoTexto.isEmpty();

        try (Connection conexao = getConnection()) {
            PreparedStatement pstmt;

            if (buscarPorCodigo) {
                sql = "SELECT * FROM produto WHERE id = ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(codigoTexto));
            } else {
                sql = "SELECT * FROM produto WHERE nome LIKE ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setString(1, "%" + nomePesquisa + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double valorVenda = rs.getDouble("valor_venda");
                double estoque = rs.getDouble("estoque");

                Produto p = new Produto(id, nome, valorVenda, estoque);
                listaResultado.add(p);
            }

            tabelaListagemProdutos.setItems(listaResultado);

        } catch (NumberFormatException e) {
            System.err.println("Código inválido: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar produto: " + e.getMessage());
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

            tabelaListagemProdutos.setItems(listaProdutos);

        } catch (SQLException e) {
            System.err.println("Erro ao consultar o banco: " + e.getMessage());
        }
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
