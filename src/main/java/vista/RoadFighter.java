/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import control.Globales;
import static control.Globales.ALTO_FRAME;
import static control.Globales.ANCHO_FRAME;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import models.Bonus;
import models.Car;
import models.Enemies;
import models.Enemy;
import models.Guardrail;
import models.LeftArea;
import models.Oil;
import models.Sounds;
import models.Street;

public class RoadFighter extends JFrame implements Globales, KeyListener {

    private BufferedImage imgBuffered;
    private Image backGroundImage = null;
    private Graphics graficos;
    private Stroke defaultStroke;
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean isStop = false;
    private int points = 0;
    private int lives = 2;
    private boolean isCity = true;
    private boolean updateHit = false;
    private double fuel = 100.0;
    private int comboPerfeito = 0;
    private Car car = new Car(286, 730, 4);
    private Street street = new Street(301, -800, 0);
    private LeftArea leftArea = new LeftArea(0);
    private Guardrail guardrail = new Guardrail(160, -800, 0);
    private Enemies enemies = new Enemies();
    private Sounds car_sounds = new Sounds();
    private ArrayList<Oil> oils = new ArrayList<>();
    private Bonus superman = new Bonus("/images/superman.png");

    public RoadFighter() {
        addKeyListener(this);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.imgBuffered = new BufferedImage(ANCHO_FRAME, ALTO_FRAME, BufferedImage.TYPE_INT_RGB);
        graficos = imgBuffered.createGraphics();
        this.setBackground(Color.black);
        this.setSize(ANCHO_FRAME, ALTO_FRAME);
        this.setLocationRelativeTo(this);
        this.setVisible(true);
        paint(graficos);
    }

    public void startGame() {
        CompletableFuture.delayedExecutor(1000 / 60, TimeUnit.MILLISECONDS).execute(() -> {
            if (this.lives > 0) {
                this.update();
                this.startGame();
            } else {
                this.update();
                int resposta = JOptionPane.showConfirmDialog(this,
                        "GAME OVER!\nSua pontuação: " + this.points + "\nDeseja jogar novamente?",
                        "Fim de Jogo",
                        JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    resetGame();
                } else {
                    System.exit(0);
                }
            }
        });
    }

    public void resetGame() {
        this.lives = 2;
        this.points = 0;
        this.fuel = 100.0;
        this.comboPerfeito = 0;
        
        // 2. Destrava os controles
        this.isStop = false;
        this.updateHit = false;
        this.left = false;
        this.right = false;
        this.up = false;

        // 3. Recria o cenário e o carro nas posições originais de largada
        this.car = new Car(286, 730, 4);
        this.street = new Street(301, -800, 0);
        this.leftArea = new LeftArea(0);
        this.guardrail = new Guardrail(160, -800, 0);

        // 4. Limpa as listas de inimigos e óleo da pista
        Enemies.enemiesList.clear();
        this.oils.clear();

        // 5. Esconde o Superman caso ele estivesse na tela na hora da batida
        this.superman.setVisible(false);
        this.superman.setY(800);

        // 6. Dá a partida no motor de novo
        this.startGame();
    }

    public void update() {
        repaint();
    }

    public void paint(Graphics g) {
        imgBuffered = new BufferedImage(ANCHO_FRAME, ALTO_FRAME,
                BufferedImage.TYPE_INT_RGB);
        graficos = imgBuffered.createGraphics();

        Graphics2D g2d = (Graphics2D) graficos;
        this.defaultStroke = g2d.getStroke();
        g2d.getStroke();
        drawLayouts();
        drawLines();
        drawCar();
        drawLeftArea();

        drawGuardrail();
        if (!this.isStop) {
            speedControl();
        }

        verifyFuel();
        verifyCrahs();
        drawEnemies();
        drawOils();
        // --- DESENHA O BÔNUS ---
        verificarBonus();
        if (this.superman.isVisible()) {
            graficos.drawImage(this.superman.getImage(), (int) this.superman.getX(), (int) this.superman.getY(), null);
        }

        checkOvertake();
        drawPointsArea();
        verifyHit();
        updateHit();
        g.drawImage(imgBuffered, 0, 0, this);

    }

    public void drawLayouts() {
        graficos.setColor(Color.decode(STREET_COLOR));
        graficos.fillRect(0, 70, 28, ALTO_FRAME + 70);

        graficos.setColor(Color.decode(this.isCity ? GREEN : BEACH));
        graficos.fillRect(28, 70, 138, ALTO_FRAME + 70);

        graficos.setColor(Color.decode(STREET_COLOR));
        graficos.fillRect(166, 70, 270, ALTO_FRAME + 70);

        graficos.setColor(Color.decode(this.isCity ? GREEN : BLUE));
        graficos.fillRect(436, 70, 110, ALTO_FRAME + 70);

    }

    public void drawLines() {
        float dash[] = {50, 110};
        Graphics2D g2 = (Graphics2D) graficos;

        g2.setPaint(Color.decode(STREET_LINE));
        g2.setStroke(new BasicStroke(9, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
        g2.drawLine(301, (int) this.street.getY(), 301, 800);
        this.street.setY(this.street.getY() + this.street.getSpeed() >= 0 ? -800 : this.street.getY() + this.street.getSpeed());

    }

    void drawCar() {

        if (this.left) {
            this.car.setX(this.car.getX() - this.car.getSpeed());
        }
        if (this.right) {
            this.car.setX(this.car.getX() + this.car.getSpeed());
        }
        graficos.drawImage(this.car.getCarImage(), (int) this.car.getX(), (int) this.car.getY(), null);
    }

    public void drawLeftArea() {
        graficos.drawImage(this.leftArea.getTrees(), (int) this.leftArea.getTreesX(), (int) this.leftArea.getTreesY(), null);
        graficos.drawImage(this.leftArea.getTree(), (int) this.leftArea.getTreeX(), (int) this.leftArea.getTreeY(), null);
        graficos.drawImage(this.leftArea.getHouse(), (int) this.leftArea.getHouseX(), (int) this.leftArea.getHouseY(), null);
        graficos.drawImage(this.leftArea.getTree(), (int) this.leftArea.getTree2X(), (int) this.leftArea.getTree2Y(), null);

        if (this.leftArea.getTreesY() < 800) {
            this.leftArea.setTreesY(this.leftArea.getTreesY() + this.leftArea.getSpeed());
            this.leftArea.setTreeY(this.leftArea.getTreeY() + this.leftArea.getSpeed());
            this.leftArea.setHouseY(this.leftArea.getHouseY() + this.leftArea.getSpeed());
            this.leftArea.setTree2Y(this.leftArea.getTree2Y() + this.leftArea.getSpeed());
        } else {
            this.leftArea.setTreesY(-800);
            this.leftArea.setTreeY(-350);
            this.leftArea.setHouseY(-250);
            this.leftArea.setTree2Y(0);
        }

    }

    public void drawGuardrail() {
        float dash[] = {8, 20};
        Graphics2D g2 = (Graphics2D) graficos;

        g2.setPaint(Color.decode(STREET_LINE));
        g2.setStroke(defaultStroke);
        g2.setStroke(new BasicStroke(4));
        g2.drawLine((int) this.guardrail.getX(), (int) this.guardrail.getY(), (int) this.guardrail.getX(), 800);
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
        g2.drawLine(156, (int) this.guardrail.getY(), (int) 156, 800);
        g2.setPaint(Color.BLACK);
        g2.drawLine(164, (int) this.guardrail.getY(), (int) 164, 800);
        this.guardrail.setY(this.guardrail.getY() + this.guardrail.getSpeed() >= this.guardrail.getSpeed() ? -800 : this.guardrail.getY() + this.guardrail.getSpeed());
    }

    public void speedControl() {
        // Se a pista está rolando (ou seja, o carro está andando), gasta combustível
        if (this.street.getSpeed() > 0) {
            this.fuel -= 0.05; // Ajuste este número para gastar mais rápido ou mais devagar
        }
        if (this.up) {

            car_sounds.playCarSound();
            car_sounds.setGeneralVolume((float) (this.street.getSpeed() >= this.street.getMaximumSpeed() ? 0.8 : this.car_sounds.getVolume() + this.car_sounds.getVolumeToIncrease() > 0.8 ? 0.8 : this.car_sounds.getVolume() + this.car_sounds.getVolumeToIncrease()));

            this.street.setSpeed(this.street.getSpeed() >= this.street.getMaximumSpeed() ? this.street.getSpeed() : this.street.getSpeed() + this.street.getAcceleration());
            this.leftArea.setSpeed(this.leftArea.getSpeed() >= this.leftArea.getMaximumSpeed() ? this.leftArea.getSpeed() : this.leftArea.getSpeed() + this.leftArea.getAcceleration());
            this.guardrail.setSpeed(this.guardrail.getSpeed() >= this.guardrail.getMaximumSpeed() ? this.guardrail.getSpeed() : this.guardrail.getSpeed() + this.guardrail.getAcceleration());

        } else {

            this.street.setSpeed(this.street.getSpeed() <= 0 ? 0 : this.street.getSpeed() - this.street.getAcceleration());
            this.leftArea.setSpeed(this.leftArea.getSpeed() <= 0 ? 0 : this.leftArea.getSpeed() - this.leftArea.getAcceleration());
            this.guardrail.setSpeed(this.guardrail.getSpeed() <= 0 ? 0 : this.guardrail.getSpeed() - this.guardrail.getAcceleration());
            car_sounds.setGeneralVolume((float) (this.car_sounds.getVolume() - this.car_sounds.getVolumeToIncrease() <= 0 || this.street.getSpeed() <= 0 ? 0 : this.car_sounds.getVolume() - this.car_sounds.getVolumeToIncrease()));
        }
    }

    public void verifyCrahs() {

        if ((this.car.getX() <= 166 || this.car.getX() >= 408) && !this.car.isCrahs()) {
            this.street.setSpeed(0);
            this.leftArea.setSpeed(0);
            this.guardrail.setSpeed(0);
            this.car.setCrahs(true);
            this.isStop = true;
            this.left = false;
            this.right = false;
            this.up = false;
            this.car_sounds.stopCarSound();
            this.car_sounds.stopCarDriftSound();
            this.car_sounds.stopCarCrashSound();
            this.car_sounds.playCarCrashSound();
            this.lives = lives - 1;
            this.comboPerfeito = 0;
        }
        if (this.isStop) {
            restartGame();
        }
    }

    public void verifyFuel() {
        if (this.fuel <= 0 && !this.car.isCrahs()) {
            this.fuel = 0;
            this.street.setSpeed(0);
            this.leftArea.setSpeed(0);
            this.guardrail.setSpeed(0);
            this.car.setCrahs(true);
            this.isStop = true;
            this.left = false;
            this.right = false;
            this.up = false;
            this.car_sounds.stopCarSound();

            this.lives = lives - 1;
        }

        if (this.isStop && this.fuel == 0) {
            this.fuel = 100.0;
        }
    }

    public void restartGame() {
        this.isStop = false;
        CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(() -> {
            this.car_sounds.stopCarCrashSound();
            this.car.setCrahs(false);
            this.car.setX(286);
        });

    }

    public void drawEnemies() {
        Enemies.addEnemy(this.street);
        Enemies.enemiesList.forEach((enemy) -> {
            // O jogo vai desenhar a imagem correta automaticamente (carro comum ou upcar)
            graficos.drawImage(enemy.getCarImage(), (int) enemy.getX(), (int) enemy.getY(), null);
        });
        Enemies.updateEnemy(this.street, this.car);
    }

    public void drawOils() {
        // 1. Gera novas manchas de óleo aleatoriamente (apenas se o carro estiver andando)
        if (this.street.getSpeed() > 0 && this.points >= 1500) {
            // Sorteio: 2% de chance de gerar uma mancha a cada frame
            if (Math.random() < 0.02) {
                int x = (int) (Math.random() * (380 - 180 + 1) + 180); // Sorteia a posição X na pista
                oils.add(new Oil(x, -50)); // Nasce lá em cima, fora da tela
            }
        }

        // 2. Desenha e move as manchas
        graficos.setColor(Color.BLACK); // Cor do óleo
        for (int i = 0; i < oils.size(); i++) {
            Oil oleo = oils.get(i);

            // Desenha uma forma oval preta para simular a poça de óleo
            graficos.fillOval((int) oleo.getX(), (int) oleo.getY(), 40, 25);

            // Move a poça para baixo na mesma velocidade da pista
            oleo.setY(oleo.getY() + this.street.getSpeed());

            // Remove da lista se saiu da tela inferior para não pesar a memória
            if (oleo.getY() > 800) {
                oils.remove(i);
                i--; // Ajusta o índice da lista após remover
            }
        }
    }

    public void verificarBonus() {
        // 1. O contador de combo só sobe se o carro estiver acelerando
        if (this.street.getSpeed() > 0 && !this.car.isCrahs() && !this.car.isIsHit()) {
            this.comboPerfeito++;
        }

        // 2. Se você dirigir perfeitamente por aprox. 10 segundos
        if (this.comboPerfeito == 600) {
            this.superman.setVisible(true); // O Superman aparece!
        }

        // 3. O voo do bônus
        if (this.superman.isVisible()) {
            // Ele sobe rápido pela tela (eixo Y diminui)
            this.superman.setY(this.superman.getY() - 10);

            // Se ele cruzou a tela inteira e passou do topo
            if (this.superman.getY() < -50) {
                this.superman.setVisible(false); // Fica invisível de novo
                this.superman.setY(800); // Volta lá para baixo
                this.comboPerfeito = 0; // Zera o combo para você tentar de novo
                this.points += 1000; // GANHOU 1000 PONTOS!
            }
        }
    }

    public void checkOvertake() {
        for (int i = 0; i < Enemies.enemiesList.size(); i++) {
            Enemy enemy = Enemies.enemiesList.get(i);
            if ((enemy.getY() > this.car.getY()) && (!enemy.isOvertake())) {
                this.points = this.points + 50;
                enemy.setOvertake(true);
                Enemies.enemiesList.set(i, enemy);

            }
        }

    }

    public void drawPointsArea() {

        int cifras = 0;
        int n = this.points;
        while (n != 0) {
            n = n / 10;
            cifras++;
        }

        int total = 6 - cifras;
        String strpoints = "";
        for (int i = 0; i < total; i++) {
            strpoints = strpoints + "0";
        }
        strpoints = strpoints + String.valueOf(this.points);
        graficos.setColor(Color.BLACK);
        graficos.fillRect(0, 0, ANCHO_FRAME, 70);

        graficos.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        graficos.setColor(Color.WHITE); // Here
        graficos.drawString(strpoints, 446, 50);
        graficos.setFont(new Font("TimesRoman", Font.PLAIN, 19));
        graficos.drawString(String.valueOf(this.lives), 496, 67);

        graficos.drawString(String.valueOf((int) this.street.getSpeed() * 20) + " km/h", 20, 67);

        // --- DESENHANDO A BARRA DE COMBUSTÍVEL ---
        graficos.setFont(new Font("TimesRoman", Font.BOLD, 16));
        graficos.setColor(Color.WHITE);
        graficos.drawString("GASOLINA", 110, 65); // Texto

        // Fundo da barra (cinza escuro)
        graficos.setColor(Color.DARK_GRAY);
        graficos.fillRect(200, 52, 100, 15);

        // Barra de nível (Muda para vermelho se estiver acabando, senão é verde)
        if (this.fuel < 25) {
            graficos.setColor(Color.RED);
        } else {
            graficos.setColor(Color.GREEN);
        }
        
        graficos.fillRect(200, 52, (int) this.fuel, 15);
    }

    public void verifyHit() {
        double carx0 = this.car.getX();
        double cary0 = this.car.getY();
        double carx1 = carx0 + 30;
        double cary1 = this.car.getY();

        if (!this.car.isIsHit()) {

            // --- 1. CHECAGEM DE COLISÃO COM OS CARROS INIMIGOS ---
            for (int i = 0; i < Enemies.enemiesList.size(); i++) {
                Enemy enemy = Enemies.enemiesList.get(i);
                double x0 = enemy.getX();
                double y0 = enemy.getY();
                double x1 = x0 + 29;
                double y1 = y0 + 40;

                boolean bateu = ((carx0 > x0 && carx0 < x1) && (cary0 > y0 && cary0 < y1))
                        || ((carx1 > x0 && carx1 < x1) && (cary1 > y0 && cary1 < y1))
                        || ((carx0 + 15 > x0 && carx0 + 15 < x1) && (cary0 > y0 && cary0 < y1));

                if (bateu) {
                    if (enemy.isFuelCar()) {
                        // É O CARRO DE COMBUSTÍVEL!
                        this.fuel += 20;
                        if (this.fuel > 100) {
                            this.fuel = 100;
                        }
                        enemy.setY(2000);
                    } else {
                        // É UM INIMIGO COMUM (O carro derrapa)
                        this.comboPerfeito = 0; // A PUNIÇÃO ENTRA AQUI!
                        this.car.setIsHit(true);
                        this.updateHit = true;
                        this.car.setHitDirection(2);
                        this.street.setSpeed(this.street.getSpeed() - 5);
                        this.car_sounds.playCarDriftSound();
                    }
                }
            }

            // --- 2. CHECAGEM DE COLISÃO COM AS MANCHAS DE ÓLEO ---
            for (int i = 0; i < oils.size(); i++) {
                Oil oleo = oils.get(i);

                if ((carx0 + 30 > oleo.getX() && carx0 < oleo.getX() + 40)
                        && (cary0 + 40 > oleo.getY() && cary0 < oleo.getY() + 25)) {

                    this.comboPerfeito = 0; // A PUNIÇÃO ENTRA AQUI TAMBÉM!
                    this.car.setIsHit(true);
                    this.updateHit = true;
                    this.car.setHitDirection(Math.random() > 0.5 ? 2 : -2);
                    this.street.setSpeed(this.street.getSpeed() - 3);
                    this.car_sounds.playCarDriftSound();
                }
            }
        }
    }

    public void updateHit() {
        if (this.car.isIsHit()) {
            this.car.setX(this.car.getX() + this.car.getHitDirection());
        }
        if (this.updateHit) {
            this.updateHit = false;
            CompletableFuture.delayedExecutor(700, TimeUnit.MILLISECONDS).execute(() -> {
                this.car_sounds.stopCarDriftSound();
                this.car.setIsHit(false);
            });
        }
    }

    public static void main(String[] args) {
        RoadFighter rf = new RoadFighter();
        rf.startGame();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.isCity = this.isCity ? false : true;
        }
        if (!this.car.isCrahs() && !this.car.isIsHit()) {

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                this.left = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                this.right = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.up = true;

            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!this.car.isCrahs()) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                this.left = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                this.right = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.up = false;
            }
        }
    }
}
