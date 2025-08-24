/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package pdvfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author willi
 *
 *
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private Button btnEntrar;

    @FXML
    private TextField inputSenha;

    @FXML
    private TextField inputUser;

    @FXML
    void entrar(ActionEvent event) {

        try {
            // Carrega o arquivo FXML da nova tela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPDV.fxml"));
            Parent root = loader.load();

            // Cria uma nova Stage e Scene
            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            Stage stage = (Stage) btnEntrar.getScene().getWindow();

            Configuracoes config = new Configuracoes();
            config.transferirEstadoDaJanela(stage, newStage);
            // 3. CHAMA O MÉTODO AUXILIAR para copiar o estado
            // Configura e mostra a nova Stage
            newStage.setScene(scene);
            newStage.setTitle("PDV");

            newStage.show();

            // Fecha a tela atual
            stage.close();

        } catch (IOException e) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
