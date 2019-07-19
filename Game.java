import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JComponent {
    private MySpaceShip MyShip;
    private ArrayList<Ellipse2D.Double> Fires = new ArrayList<>();
    private Ellipse2D.Double Fire;
    private EnemySpaceShip EnemyShip;
    private ArrayList<Rectangle2D.Double> EnemySpaceShips = new ArrayList<>();
    private BufferedImage Buffer;
    private Random NoEnemy, EnemyPos;
    private Timer FireTimer;
    private int nF, ESize, Level = 1, Score = 0;
    public Game(){
        MyShip = new MySpaceShip();
        nF = 0;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                int k = 0;
                if(e.getID() == KeyEvent.KEY_PRESSED){
                    k = e.getKeyCode();
                }
                switch(k){
                    case KeyEvent.VK_UP:
                        System.out.println("Moved Up");
                        if(MyShip.getMyShip().y >= 0) {
                            MyShip.getMyShip().y = MyShip.getMyShip().y - 5;
                            repaint();
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        System.out.println("Moved Down");
                        if(MyShip.getMyShip().y <= getHeight() - 50) {
                            MyShip.getMyShip().y = MyShip.getMyShip().y + 5;
                            repaint();
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if(MyShip.getMyShip().x >= 50) {
                            MyShip.getMyShip().x = MyShip.getMyShip().x - 56;
                            System.out.println("Moved Left");
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(MyShip.getMyShip().x <= getWidth() - 100) {
                            MyShip.getMyShip().x = MyShip.getMyShip().x + 56;
                            System.out.println("Moved Right");
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        Fire = new Ellipse2D.Double(MyShip.getMyShip().x + 20, MyShip.getMyShip().y + 5, 10, 10);
                        Fires.add(Fire);
                        System.out.println("added");
                        break;
                }
                return false;
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        if(Buffer == null){
            Buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D g1 = (Graphics2D)Buffer.getGraphics();
        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g1.setColor(Color.BLACK);
        g1.fillRect(0,0,700,900);
        g1.setColor(Color.GRAY);
        for(int i=0;i<12;i++) {
            g1.drawLine(56*i, 0, 56*i, 900);
        }
        g1.setColor(Color.CYAN);
        g1.fill(MyShip.getMyShip());
        for(int i=0;i<EnemySpaceShips.size();i++) {
            g1.fill(EnemySpaceShips.get(i));
        }
        for(int i=0;i<Fires.size();i++) {
            g1.fill(Fires.get(i));
        }
        g1.setColor(Color.WHITE);
        g1.setFont(new Font("Century",Font.BOLD,30));
        g1.drawString("Score: " + Score, 10, 35);
        g1.drawString("Level: " + Level, 475, 35);
        g.drawImage(Buffer, 0, 0, null);
    }
    public void update(){
        if(Score >= 20){
            Level = 2;
        }
        if(Score >= 40){
            Level = 3;
        }
        if(Score >= 60){
            Level = 4;
        }
        if(Score >= 80){
            Level = 5;
        }
        for(int i=0;i<Fires.size();i++) {
            Fires.get(i).y = Fires.get(i).y - (2*Level);
            repaint();
        }
        for(int i=0;i<EnemySpaceShips.size();i++){
            EnemySpaceShips.get(i).y = EnemySpaceShips.get(i).y + Level;
            repaint();
        }
        for(int i=0;i<Fires.size();i++) {
            if(Fires.get(i).y < 0){
                Fires.remove(i);
                repaint();
            }
        }
        for(int i=0;i<EnemySpaceShips.size();i++){
            if(EnemySpaceShips.get(i).y > 900){
                EnemySpaceShips.remove(i);
            }
        }
        for(int i=0;i<EnemySpaceShips.size();i++){
            for(int j=0;j<Fires.size();j++) {
                if (Fires.get(j).intersects(EnemySpaceShips.get(i))) {
                    Fires.remove(j);
                    EnemySpaceShips.remove(i);
                    System.out.println("Target Eliminated");
                    Score++;
                    repaint();
                    break;
                }
            }
        }
        for(int i=0;i<EnemySpaceShips.size();i++){
            if(EnemySpaceShips.get(i).intersects(MyShip.getMyShip())){
                System.out.println("Game End");
                setVisible(false);
                repaint();
            }
        }
        if(EnemySpaceShips.size() == 0 || EnemySpaceShips.get(EnemySpaceShips.size()-1).y > 550 - (100*(Level-1))){
            ESize = EnemySpaceShips.size();
            NoEnemy = new Random();
            int n = NoEnemy.nextInt(11) + 1;
            System.out.println(n);
            for(int i=0;i<n;i++){
                EnemyRandomPosition();
//                EnemyShip = new EnemySpaceShip(i);
//                EnemySpaceShips.add(EnemyShip.getEnemyShip());
            }
        }
        repaint();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

        public void EnemyRandomPosition(){
            int flag = 0;
            EnemyPos = new Random();
            int n = EnemyPos.nextInt(11);
            if(ESize == 0) {
                for (int i = 0; i < EnemySpaceShips.size(); i++) {
                    double CPos = (EnemySpaceShips.get(i).x - 3) / 56;
                    if ((int) CPos == n) {
                        flag = 1;
                        break;
                    }
                }
            }
            else{
                for (int i = ESize; i < EnemySpaceShips.size(); i++) {
                    double CPos = (EnemySpaceShips.get(i).x - 3) / 56;
                    if ((int) CPos == n) {
                        flag = 1;
                        break;
                    }
                }
            }
            if(flag == 1){
                EnemyRandomPosition();
            }
            else{
                EnemyShip = new EnemySpaceShip(n);
                EnemySpaceShips.add(EnemyShip.getEnemyShip());
            }
        }
}
