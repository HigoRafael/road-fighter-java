/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author User
 */
public class Enemies {

    public static ArrayList<Enemy> enemiesList = new ArrayList<>();
    private static boolean timeRespawn = false;
    private static boolean ready = true;

    public Enemies() {

    }

    public static ArrayList getList() {
        return enemiesList;
    }

    public static void addEnemy(Street street) {

        if (street.getMaximumSpeed() <= street.getSpeed() && street.getY() >= -400 && !timeRespawn && ready) {
            Random rand = new Random();
            int x = rand.nextInt((408 - 166) + 1) + 166;
            int y = rand.nextInt((400 - 0) + 1) + 0;
            int img = rand.nextInt((1 - 0) + 1) + 0;
            timeRespawn = true;
            Enemy car = new Enemy(x, -y, img);
            // --- NOVA LÓGICA DE SORTEIO ---
            // Sorteia um número de 0 a 100. Se for menor que 25 (25% de chance), vira carro de combustível!
            if (rand.nextInt(100) < 25) {
                car.setFuelCar(true);
            }else if (rand.nextInt(100) < 35) {
                car.setAggressive(true); // 35% de chance de ser um carro agressivo (te fecha)
            }
            // ------------------------------
            enemiesList.add(car);
        }
        if (timeRespawn) {
            timeRespawn = false;
            ready = false;
            CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(() -> {
                ready = true;
            });
        }
    }

    // Adicionamos o 'Car player' aqui no parâmetro
    public static void updateEnemy(Street street, Car player) {
        for (int i = 0; i < enemiesList.size(); i++) {
            Enemy elemento = enemiesList.get(i);
            
            // Movimento original do eixo Y (para frente)
            elemento.setY(street.getMaximumSpeed() <= street.getSpeed() ? elemento.getY() + elemento.getSpeed() : elemento.getY() - elemento.getSpeed());

            // --- INTELIGÊNCIA ARTIFICIAL (Eixo X) ---
            // Se ele for agressivo e você ainda não tiver ultrapassado ele
            if (elemento.isAggressive() && !elemento.isOvertake()) {
                
                // Calcula a distância entre você e o inimigo
                double distancia = player.getY() - elemento.getY();
                
                // Se você estiver logo atrás dele (menos de 250 pixels de distância)
                if (distancia > 0 && distancia < 250) {
                    
                    // Ele joga o carro na direção do seu eixo X!
                    if (player.getX() > elemento.getX()) {
                        elemento.setX(elemento.getX() + 2); // Vira para a Direita
                    } else if (player.getX() < elemento.getX()) {
                        elemento.setX(elemento.getX() - 2); // Vira para a Esquerda
                    }
                    
                    // Limites da pista para o inimigo não ir parar na grama
                    if (elemento.getX() < 168) elemento.setX(168);
                    if (elemento.getX() > 390) elemento.setX(390);
                }
            }
            // ----------------------------------------

            enemiesList.set(i, elemento);
            if (elemento.getY() >= 800 || elemento.getY() < -400) {
                enemiesList.remove(i);
            }
        }
    }
}
