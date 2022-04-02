import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main extends JFrame {
    private int gameWidth;
    private final int gameHeight;
    private final Font defaultFont = new Font("Baghdad", Font.BOLD,20);
    private JTextField nameTxt;
    private final Main me;
    static String name;
    private static int pchoice;


    public static String getname() {
        return name;
    }

    public static int getchoice() {
        return pchoice;
    }

    public Main() throws IOException {
        me = this;
        setSize(600,600);
        setLocationRelativeTo(null);
        setTitle("Tick Tack Toe");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        gameWidth = 3;
        gameHeight = 3;
        initTopPnl();
        initCenterPnl();
        initBottomPnl();
        setVisible(true);
    }

    private void initBottomPnl() {
        JButton start = new JButton("Start");
        start.setFont(defaultFont);
        add(start, BorderLayout.PAGE_END);
        start.addActionListener(e -> {
            if(checkSize()){
                String[] options = new String[]{"Yes","No"};
                int sw = -1;
                while (sw == -1) {
                    sw = JOptionPane.showOptionDialog(me, "Would you like to start the game as X?",
                            "Game Starter", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, options, options[0]);
                }
                pchoice = sw;
                TicTacToe game = new TicTacToe();
                setVisible(false);
                game.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true);
                    }
                });
            } else {
                JOptionPane.showMessageDialog(me,
                        "Name Field Cant Be Empty!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean checkSize() {
        gameWidth = 3;
        if (nameTxt.getText().equals("")) {
            gameWidth = -1;
            nameTxt.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        }
        else
            nameTxt.setBorder(new JTextField().getBorder());
        name = nameTxt.getText();
        return gameWidth != -1 && gameHeight != -1;
    }

    private void initCenterPnl() {
        JPanel centerPnl = new JPanel(new GridLayout(1, 1, 5, 5));

        JLabel nameLbl = new JLabel("Enter A Name:");
        nameLbl.setFont(defaultFont);
        nameLbl.setHorizontalAlignment(SwingConstants.CENTER);

        nameTxt = new JTextField("");
        nameTxt.setFont(defaultFont);
        nameTxt.setHorizontalAlignment(SwingConstants.CENTER);
        nameTxt.getDocument().putProperty("owner","name");

        nameTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        centerPnl.add(nameLbl);
        centerPnl.add(nameTxt);

        centerPnl.setBorder(new EmptyBorder(50,50,50,50));

        add(centerPnl, BorderLayout.CENTER);

    }

    private void initTopPnl() throws IOException {
        JPanel topPnl = new JPanel(new FlowLayout());
        BufferedImage pic = ImageIO.read(new File("unnamed.png"));
        JLabel gameTxt = new JLabel(new ImageIcon(pic));
        topPnl.add(gameTxt);
        add(topPnl,BorderLayout.PAGE_START);
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    public static class TicTacToe extends JFrame implements ChangeListener, ActionListener {
        enum Room { empty , O , X }
        private final Room[] cells;
        private Room Turn;
        private int wins, losses, draws;
        int whoStarts = 0;

        public TicTacToe() {
            setTitle("Tick Tack Toe");
            JPanel topPanel=new JPanel();
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
                int lineThickness = 5;
                g2d.setStroke(new BasicStroke(lineThickness));
                g2d.draw(new Line2D.Double(0, h / 3, w, h / 3));
                g2d.draw(new Line2D.Double(0, h * 2 / 3, w, h * 2 / 3));
                g2d.draw(new Line2D.Double(w / 3, 0, w / 3, h));
                g2d.draw(new Line2D.Double(w * 2 / 3, 0, w * 2 / 3, h));

                for (int i=0; i<9; i++) {
                    double X1 = (i % 3 + 0.5) * w / 3.0;
                    double Y1 = (i / 3 + 0.5) * h / 3.0;
                    double X2 = w / 8.0;
                    double Y2 = h / 8.0;
                    if (cells[i]== Room.O) {
                        g2d.setPaint(new Color(0xB70202));
                        g2d.draw(new Ellipse2D.Double(X1-X2, Y1-Y2, X2*2, Y2*2));
                    }
                    else if (cells[i]== Room.X) {
                        g2d.setPaint(new Color(0x00008C));
                        g2d.draw(new Line2D.Double(X1-X2, Y1-Y2, X1+X2, Y1+Y2));
                        g2d.draw(new Line2D.Double(X1-X2, Y1+Y2, X1+X2, Y1-Y2));
                    }
                }
            }


            public void mouseClicked(MouseEvent e) {
                int X = e.getX()*3/getWidth();
                int Y = e.getY()*3/getHeight();
                int pos = X + 3 * Y;
                if (pos >= 0 && pos <= 8 && cells[pos]== Room.empty) {
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
                if (num < 0)
                    if (Main.getchoice()==1) num = findRow(Room.O);
                    else num = findRow(Room.X);
                if (num < 0) {
                    do
                        num=random.nextInt(9);
                    while (cells[num] != Room.empty);
                }
                if (Main.getchoice()==1) cells[num]= Room.X;
                else cells[num] = Room.O;
            }

            int findRow(Room player) {
                for (int i = 0; i < 8; i++) {
                    int result = findCrossWays(player, rows[i][0], rows[i][1]);
                    if (result >= 0)
                        return result;
                }
                return -1;
            }


            int findCrossWays(Room player, int a, int b) {
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
                if (winner == Room.X && Main.getchoice()==0) {
                    wins++;
                    result = Main.getname() +" Wins!";
                }
                else if (winner == Room.X && Main.getchoice()==1) {
                    losses++;
                    result = "PC Wins!";
                }            else if (winner == Room.O && Main.getchoice()==0) {
                    losses++;
                    result = "PC Wins!";
                }            else if (winner == Room.O && Main.getchoice()==1) {
                    wins++;
                    result = Main.getname() + " Wins!";
                }
                else {
                    result = "Draw";
                    draws++;
                }
                ++whoStarts;
                if (JOptionPane.showConfirmDialog(null,
                        "Here Is Your Status: "+wins+ " Wins, "+losses+" Losses, "+draws+" Draws\n"
                                +"Play Again?", result, JOptionPane.YES_NO_OPTION)
                        !=JOptionPane.YES_OPTION) {
                    System.exit(0);
                }

                for (int j=0; j<9; ++j)
                    cells[j]= Room.empty;

                if (Main.getchoice() == 1 && whoStarts%2==0)
                    cells[4] = Room.X;
                if (whoStarts%2==1 && Main.getchoice()==0)
                    nextMove();
            }
        }
    }

}