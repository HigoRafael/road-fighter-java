/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Higor
 */
import java.awt.Image;
import javax.swing.ImageIcon;

public class Bonus {

    private double x;
    private double y;
    private Image image;
    private boolean isVisible = false;

    public Bonus(String imagePath) {
        // Pede para o Java procurar a imagem dentro do projeto/jar
        java.net.URL imgUrl = getClass().getResource(imagePath);

        // Carrega a imagem a partir da URL encontrada
        Image imagemOriginal = new ImageIcon(imgUrl).getImage();

        this.image = imagemOriginal.getScaledInstance(50, 80, Image.SCALE_SMOOTH);
        this.x = 45;
        this.y = 800;
    }

    // Métodos Getters e Setters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

}
