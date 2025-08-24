/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdvfx;

import javafx.stage.Stage;

/**
 *
 * @author willi
 */
public class Configuracoes {

     void transferirEstadoDaJanela(Stage stageAntiga, Stage stageNova) {
        // Verifica se a janela antiga estava maximizada
        if (stageAntiga.isMaximized()) {
            // Se sim, maximiza a nova janela também
            stageNova.setMaximized(true);
        } else {
            // Se não, copia as dimensões e a posição
            stageNova.setX(stageAntiga.getX());
            stageNova.setY(stageAntiga.getY());
            stageNova.setWidth(stageAntiga.getWidth());
            stageNova.setHeight(stageAntiga.getHeight());
        }
    }

}
