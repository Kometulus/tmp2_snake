import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame { //основной класс, от него наследуется все что хочет быть окном

    public MainWindow(){ //далее свойства конструктора
     setTitle("Змеюга");
     setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //крестик нажимаешь, закрывется
     setSize(365,385);
     setLocation(400,200);
     add(new GameField());//добавление игрового поля
     setVisible(true);
    }

    public static void main(String[] args){ //создаем экземпляр предыдущего

        MainWindow mw = new MainWindow();
    }
}
