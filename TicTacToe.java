import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class TicTacToe extends JFrame implements ChangeListener, ActionListener {
    enum Room { empty , O , X }
    private final Room[] cells;
    private Room Turn;
    private int wins, losses, draws;
    int whoStarts = 0;

    public static void main(String[] args) {
        new TicTacToe();
    }

    public TicTacToe() {
        setTitle("Tick Tack Toe");
        JPanel topPanel=new JPanel();
        topPanel.setLayout(new FlowLayout());
        JLabel turnWho = new JLabel();
        add(topPanel, BorderLayout.NORTH);
        add(new Board(), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        cells = new Room[9];
        for (int i = 0; i < 9; i++) {
            cells[i] = Room.empty;
        }
        Turn = Room.X;
        if (Main.getchoice()==1)
            cells[4] = Room.X;
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }


    private class Board extends JPanel implements MouseListener {
        private final Random random=new Random();
        private final int[][] rows ={{0,2},{3,5},{6,8},{0,6},{1,7},{2,8},{0,8},{2,6}};

        public Board() {
            addMouseListener(this);
        }

        public void paint(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            Graphics2D g2d = (Graphics2D) g;

            g2d.setPaint(Color.WHITE);
            g2d.fill(new Rectangle2D.Double(0, 0, w, h));
            g2d.setPaint(Color.BLACK);
            int lineThickness = 4;
            g2d.setStroke(new BasicStroke(lineThickness));
            g2d.draw(new Line2D.Double(0, h/3, w, h/3));
            g2d.draw(new Line2D.Double(0, h*2/3, w, h*2/3));
            g2d.draw(new Line2D.Double(w/3, 0, w/3, h));
            g2d.draw(new Line2D.Double(w*2/3, 0, w*2/3, h));

            for (int i=0; i<9; i++) {
                double xpos=(i%3+0.5)*w/3.0;
                double ypos=(i/3+0.5)*h/3.0;
                double xr=w/8.0;
                double yr=h/8.0;
                if (cells[i]==Room.O) {
                    g2d.setPaint(new Color(0xB70202));
                    g2d.draw(new Ellipse2D.Double(xpos-xr, ypos-yr, xr*2, yr*2));
                }
                else if (cells[i]==Room.X) {
                    g2d.setPaint(new Color(0x00008C));
                    g2d.draw(new Line2D.Double(xpos-xr, ypos-yr, xpos+xr, ypos+yr));
                    g2d.draw(new Line2D.Double(xpos-xr, ypos+yr, xpos+xr, ypos-yr));
                }
            }
        }


        public void mouseClicked(MouseEvent e) {
            int X = e.getX()*3/getWidth();
            int Y = e.getY()*3/getHeight();
            int pos= X+3*Y;
            if (pos>=0 && pos<9 && cells[pos]==Room.empty) {
                if (Main.getchoice()==0) Turn = Room.X;
                else Turn = Room.O;
                cells[pos] = Turn;
                repaint();
                EI_Turn();
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        void EI_Turn() {
            Room turn;
            Room otherTurn;
            if (Main.getchoice()==0) {
                otherTurn = Room.O;
                turn = Room.X;
            }
            else {
                otherTurn = Room.X;
                turn = Room.O;
            }
            if (won(turn))
                newGame(turn);
            else if (isDraw())
                newGame(Room.empty);
            else {
                nextMove();
                if (won(otherTurn))
                    newGame(otherTurn);
                else if (isDraw())
                    newGame(Room.empty);
            }
        }


        boolean won(Room player) {
            for (int i=0; i<8; ++i)
                if (ColAndPillCheck(player, rows[i][0], rows[i][1]))
                    return true;
            return false;
        }

        boolean ColAndPillCheck(Room player, int a, int b) {
            return cells[a] == player && cells[b] == player
                    && cells[(a+b)/2] == player;
        }

        void nextMove() {
            int num;
            if (Main.getchoice()==1) num = findRow(Room.X);
            else num = findRow(Room.O);
            if (num<0)
                if (Main.getchoice()==1) num = findRow(Room.O);
                else num = findRow(Room.X);
            if (num<0) {
                do
                    num=random.nextInt(9);
                while (cells[num]!=Room.empty);
            }
            if (Main.getchoice()==1) cells[num]=Room.X;
            else cells[num]=Room.O;
        }

        int findRow(Room player) {
            for (int i=0; i<8; ++i) {
                int result=findXWays(player, rows[i][0], rows[i][1]);
                if (result>=0)
                    return result;
            }
            return -1;
        }


        int findXWays(Room player, int a, int b) {
            int middlePoint = (a+b)/2;
            if (cells[a] == player && cells[b] == player && cells[middlePoint] == Room.empty)
                return middlePoint;
            if (cells[a] == player && cells[middlePoint] == player && cells[b] == Room.empty)
                return b;
            if (cells[b] == player && cells[middlePoint] == player && cells[a] == Room.empty)
                return a;
            return -1;
        }

        boolean isDraw() {
            for (int i=0; i<9; i++)
                if (cells[i] == Room.empty)
                    return false;
            return true;
        }

        void newGame(Room winner) {
            repaint();

            String result;
            if (winner==Room.X && Main.getchoice()==0) {
                ++wins;
                result = Main.getname() +" Wins!";
            }
            else if (winner==Room.X && Main.getchoice()==1) {
                ++losses;
                result = "PC Wins!";
            }            else if (winner==Room.O && Main.getchoice()==0) {
                ++losses;
                result = "PC Wins!";
            }            else if (winner==Room.O && Main.getchoice()==1) {
                ++wins;
                result = Main.getname() + " Wins!";
            }
            else {
                result = "Draw";
                ++draws;
            }
            ++whoStarts;
            if (JOptionPane.showConfirmDialog(null,
                    "Here Is Your Status "+wins+ " Wins, "+losses+" Losses, "+draws+" Draws\n"
                            +"Play Again?", result, JOptionPane.YES_NO_OPTION)
                    !=JOptionPane.YES_OPTION) {
                System.exit(0);
            }

            for (int j=0; j<9; ++j)
                cells[j]=Room.empty;

            if (Main.getchoice() == 1 && whoStarts%2==0)
                cells[4] = Room.X;
            if (whoStarts%2==1 && Main.getchoice()==0)
                nextMove();
        }
    }
}