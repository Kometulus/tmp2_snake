import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class GameField extends JPanel implements ActionListener { //панель для действия игры
    private final int SIZE = 320; //размер поля
    private final int DOT_SIZE = 16; //размер в пикселях 1 части змейки
    private final int ALL_DOTS = 400;// сколько игровых единиц на поле
    private Image dot;
    private Image apple;
    private int appleX;//позиция яблока
    private int appleY;
    private int[] x = new int[ALL_DOTS];//массив для всех точек змейки
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;//
    private boolean left = false;//отвечают за текущее положение движения змейки
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;//при инициализации игры мы на поле
    int scores;

    public GameField() {
        setBackground(Color.black);
        loadImages();//вызываем метод для загрузки картинок
        initGame();// вызываем метод игры
        addKeyListener(new FieldKeyListener());
        setFocusable(true);//коннект клавиш и игрового поля
    }

    public void initGame() {//инициализирует начало игры
        dots = 3; //начальное колво точек
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE; //стартовое место змейки
            y[i] = 48;
        }
        timer = new Timer(250, this);//с какой частотой
        timer.start();
        createApple();//метод для создания яблока
    }

    public void createApple() {
        appleX = new Random().nextInt(20) * DOT_SIZE; //20 16пиксельных квадратов может поместиться в поле
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    public void loadImages() { //метод для загрузки картинок
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();//инициализация объекта

        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) { //перерисовыввает игровое поле
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);//сначала рисуем яблоко
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);//перерисовываем всю змейку
            }
        } else {
            String str = "GAME OVER!";
            String sc1="Score:";
            String sc=Integer.toString(scores);
            Font f = new Font("Arial", Font.BOLD, 20);
            g.setColor(Color.red);
            g.setFont(f);
            g.drawString(str, 125, SIZE / 2);
            g.drawString(sc, 165, 250);
            g.drawString(sc1, 125, 200);
            Path path = Paths.get("C:\\Users\\Admin\\Documents\\нужное\\6 семак\\Snake\\src\\score.txt");
            //Path path1 = Paths.get("C:\\Users\\Admin\\Documents\\нужное\\6 семак\\Snake\\src\\score.txt");
            String contents = Integer.toString(scores);
           // String contents1 = "YOUR SCORE: ";
            try {
              //  Files.writeString(path1, contents1, StandardCharsets.UTF_8);
                Files.writeString(path, contents, StandardCharsets.UTF_8);
            } catch (IOException ex) {

            }
        }
    }

    public void move() { //логическое передвижение точек
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];//перебираем по точкам, так двигается змея
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }

    }

    public void checkApple() {//проверяем столкновение
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            scores++;
            System.out.println(scores);
            if (dots % 5 == 0) {//увеличиваем скорость после 5 ябоок
                timer.setDelay(timer.getDelay() - 50);
            }
            createApple();
        }
    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 0 && x[0] == x[i] && y[0] == y[i]) {//проверка на столкновение с самой собой
                inGame = false;
            }
        }
        if (x[0] > SIZE) {//столкновение со стенками
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) { //должен вызываться каждый раз по тику таймера
        if (inGame) {
            checkApple();//встретили ли мы яблоко
            checkCollisions();//проверка столкновения с бортами
            move();//двигать змейку
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {//класс расширяющий киадаптер, для управления клавишами
        @Override
        public void keyPressed(KeyEvent e) {//метод нажатия клавиш
            super.keyPressed(e);
            int key = e.getKeyCode();
//в соответствие с нажатой клавишей меняется направление
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP && !down) {
                left = false;
                up = true;
                right = false;
            }

            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN && !up) {
                left = false;
                down = true;
                right = false;
            }

            if (key == KeyEvent.VK_P) {//пауза
                timer.stop();
            }

            if (key == KeyEvent.VK_O) {//продолжение
                timer.start();
            }
        }
    }
}
