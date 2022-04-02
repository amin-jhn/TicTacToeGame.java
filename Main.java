import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

    public Main() {
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

    private void initTopPnl() {
        JPanel topPnl = new JPanel(new FlowLayout());
        JLabel gameTxt = new JLabel("TICK TACK TOE");
        gameTxt.setFont(new Font("Ink Free",Font.BOLD,60));
        topPnl.add(gameTxt);
        add(topPnl,BorderLayout.PAGE_START);
    }

    public static void main(String[] args) {
        new Main();
    }

}