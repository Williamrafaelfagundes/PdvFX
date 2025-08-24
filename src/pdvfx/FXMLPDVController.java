/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package pdvfx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author willi
 */
public class FXMLPDVController implements Initializable {

    @FXML
    private Button btnCadastro;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnPDV;

    @FXML
    private Button btnVenda;

    private Connection conexao;
    String nome;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conectarBanco();

    }

    private void conectarBanco() {
        try {
            conexao = ConexaoBanco.getConnection();
            if (conexao == null) {
                System.err.println("Erro: Conexão com o banco de dados não foi estabelecida.");
            } else {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
            }
        } catch (Exception e) {
            System.err.println("Exceção ao tentar conectar ao banco de dados:");
            e.printStackTrace();
        }
    }

    @FXML
    void irParaCadastro() {
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

            Stage stage = (Stage) btnCadastro.getScene().getWindow();
            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stage, newStage);

            // Fecha a tela atual
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
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
