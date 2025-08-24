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
public class FXMLPesquisaProdutoController implements Initializable {

    @FXML
    private Button btnPesquisar;

    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<?, ?> colunaAdicionar;

    @FXML
    private TextField inputCodProduto;

    @FXML
    private TextField inputNomeProduto;

    @FXML
    private Button btnAvancar;

    @FXML
    private Button btnCadastro;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnPDV;

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
    private String termoPesquisa;

    @FXML
    void voltarParaTelaDeVenda(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLVenda.fxml"));
            Parent root = loader.load();

            FXMLVendaController controller = loader.getController();
            controller.carregarProdutosSelecionados(); // método que você vai criar

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setTitle("Venda de Produtos");
            newStage.show();

            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stage, newStage);

            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TableView<Produto> tabelaListagemProdutosAdicionados;

    @FXML
    void avancar(ActionEvent event) {
        System.out.println("Avançar");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabela();
        adicionarColunaAdicionar();

    }

    private void adicionarColunaAdicionar() {
        TableColumn<Produto, Void> colunaBotao = new TableColumn<>("Adicionar");

        colunaBotao.setCellFactory(coluna -> new TableCell<Produto, Void>() {
            private final Button btn = new Button("➕");

            {
                btn.setOnAction((ActionEvent event) -> {
                    Produto produtoSelecionado = getTableView().getItems().get(getIndex());
                    SessaoProduto.adicionarProduto(produtoSelecionado);
                });
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
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

        tabelaListagemProdutosAdicionados.getColumns().add(colunaBotao);
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

    @FXML
    void pesquisarProduto(ActionEvent event) {
        String codigoTexto = inputCodProduto.getText().trim();
        String nomePesquisa = inputNomeProduto.getText().trim();

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

            tabelaListagemProdutosAdicionados.setItems(listaResultado);

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

            tabelaListagemProdutosAdicionados.setItems(listaProdutos);

        } catch (SQLException e) {
            System.err.println("Erro ao consultar o banco: " + e.getMessage());
        }
    }

    public void setTermoPesquisa(String termo) {
        this.termoPesquisa = termo;
        realizarPesquisa(); // dispara a pesquisa automaticamente
    }

    private void realizarPesquisa() {
        if (termoPesquisa == null || termoPesquisa.isEmpty()) {
            return;
        }

        // Faça a lógica para pesquisar no banco ou filtrar os dados usando o termoPesquisa
        System.out.println("Pesquisando por: " + termoPesquisa);

        // Exemplo: use uma query com WHERE nome LIKE ou id = ?
    }

}
