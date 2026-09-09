/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static control.Globales.ENEMIES;
import java.awt.Image;
import java.net.URL;

/**
 *
 * @author User
 */
public class Enemy {

    // Removemos a inicialização direta com String daqui. Faremos no construtor.
    private Image carImage;
    private double x = 0;
    private double y = 0;
    private double speed = 9;
    private double acceleration = 0.1;
    private double MaximumSpeed = 15;
    private boolean overtake = false;
    private boolean isFuelCar = false;
    private boolean isAggressive = false;

    public Enemy(double x, double y, int img) {
        this.x = x;
        this.y = y;
        
        // Carregamento correto lendo de dentro do pacote/jar
        URL imgUrl = getClass().getResource(ENEMIES[img]);
        if (imgUrl != null) {
            this.carImage = new javax.swing.ImageIcon(imgUrl).getImage();
        } else {
            System.err.println("Erro ao carregar a imagem do inimigo: " + ENEMIES[img]);
        }
    }

    public boolean isOvertake() {
        return overtake;
    }

    public void setOvertake(boolean overtake) {
        this.overtake = overtake;
    }

    public Image getCarImage() {
        return carImage;
    }

    public void setCarImage(Image carImage) {
        this.carImage = carImage;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getMaximumSpeed() {
        return MaximumSpeed;
    }

    public void setMaximumSpeed(double MaximumSpeed) {
        this.MaximumSpeed = MaximumSpeed;
    }

    public boolean isFuelCar() {
        return isFuelCar;
    }

    public void setFuelCar(boolean isFuelCar) {
        this.isFuelCar = isFuelCar;
        if (isFuelCar) {
            // Este trecho você já tinha ajustado perfeitamente!
            java.net.URL imgUrl = getClass().getResource("/images/upcar.png");
            if (imgUrl != null) {
                this.carImage = new javax.swing.ImageIcon(imgUrl).getImage();
            }
        }
    }

    public boolean isAggressive() {
        return isAggressive;
    }

    public void setAggressive(boolean isAggressive) {
        this.isAggressive = isAggressive;
    }
}