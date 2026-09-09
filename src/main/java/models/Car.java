/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static control.Globales.CAR_IMG;
import static control.Globales.CRASH_CAR;
import java.awt.Image;
import java.net.URL;

/**
 *
 * @author User
 */
public class Car {

    // Removemos a inicialização direta. Faremos no construtor.
    private Image carImage;
    private double x = 0;
    private double y = 0;
    private double speed = 0;
    private double acceleration = 1;
    private double MaximumSpeed = 5;
    private boolean isCrahs = false;
    private boolean isHit = false;
    private int hitDirection;

    public Car(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        
        // Carregamento inicial do carro
        URL imgUrl = getClass().getResource(CAR_IMG);
        if (imgUrl != null) {
            this.carImage = new javax.swing.ImageIcon(imgUrl).getImage();
        } else {
            System.err.println("Erro ao carregar a imagem do carro: " + CAR_IMG);
        }
    }

    public int getHitDirection() {
        return hitDirection;
    }

    public void setHitDirection(int hitDirection) {
        this.hitDirection = hitDirection;
    }

    public boolean isIsCrahs() {
        return isCrahs;
    }

    public void setIsCrahs(boolean isCrahs) {
        this.isCrahs = isCrahs;
    }

    public boolean isIsHit() {
        return isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    public boolean isCrahs() {
        return isCrahs;
    }

    public void setCrahs(boolean parCrash) {
        this.isCrahs = parCrash;
        URL imgUrl;
        
        // Alterna entre a imagem de batida e a imagem normal usando getResource
        if (parCrash) {
            imgUrl = getClass().getResource(CRASH_CAR);
        } else {
            imgUrl = getClass().getResource(CAR_IMG);
        }
        
        if (imgUrl != null) {
            this.carImage = new javax.swing.ImageIcon(imgUrl).getImage();
        }
    }

    public double getMaximumSpeed() {
        return MaximumSpeed;
    }

    public void setMaximumSpeed(double MaximumSpeed) {
        this.MaximumSpeed = MaximumSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getX() {
        return x;
    }

    public Image getCarImage() {
        return carImage;
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
}