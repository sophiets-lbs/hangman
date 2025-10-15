import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Hangman {
    private JPanel hangmanPanel;
    private JTextField inputField;
    private JLabel wordLabel;
    private JButton submitButton;
    private JLabel historyLabel;
    private JLabel guessesLabel;
    private JLabel imageLabel;
    private JPanel inputPanel;
    private JPanel historyPanel;

    private int guessLimit;
    private Game game;
    private String[] randomWords = {
            "backyard",
            "toothbrush",
            "snowflake",
            "blueberry",
            "mailbox",
            "firefly",
            "bookshelf",
            "cupcake",
            "lighthouse",
            "pocketwatch",
            "raincoat",
            "sunflower",
            "skateboard",
            "waterfall",
            "teacup",
            "butterfly",
            "headphones",
            "sandbox",
            "streetlamp",
            "typewriter",
            "windmill",
            "sunglasses",
            "strawberry",
            "pineapple"
    };

    public Hangman(){
        guessLimit = 10;

        startNewGame();

        submitButton.addActionListener(e -> {
            submitGuess(game);
        });

        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);

        hangmanPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
        wordLabel.setBorder(BorderFactory.createEmptyBorder(25,0,0,0));

        //nur ein Character und Uppercase
        inputField.setDocument(new javax.swing.text.PlainDocument() {
            @Override public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str != null && getLength() + str.length() <= 1)
                    super.insertString(offs, str.toUpperCase(), a);
            }
        });
        inputField.addActionListener(e -> {
            submitGuess(game);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hangman");
        frame.setContentPane(new Hangman().hangmanPanel);
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public boolean isInputValid(JTextField textField){
        String input = textField.getText();
        if(input.length() > 1) return false;
        if(input.isEmpty()) return false;
        char characterInput = input.charAt(0);
        return Character.isLetter(characterInput);
    }

    public void submitGuess(Game game){
        if(isInputValid(inputField)){
            char currentGuess = inputField.getText().charAt(0);
            game.handleGuess(currentGuess);
            wordLabel.setText(game.getGameArray());
        } else {
            wiggle(inputField, 5, 3);
        }
        if(!game.getHistory().isEmpty()){
            historyLabel.setText("<html>"+game.getHistory()+"</html>");
        }

        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);
        inputField.setText("");
        boolean gameOver = game.isGameOver();
        boolean victory = game.isVictory();

        if(gameOver){
            Game.showDialog("You lost!\nThe word is: " + game.getWord(), "Game over");
            startNewGame();
        } else if(victory){
            Game.showDialog("You won!", "Victory");
            startNewGame();
        }
    }

    //Wiggle-Effekt bei invalidem Input:
    public static void wiggle(JTextField field, int distance, int moves) {
        final int originalX = field.getX();
        Timer timer = new Timer(30, null);
        timer.addActionListener(new ActionListener() {
            int count = 0;
            public void actionPerformed(ActionEvent e) {
                int offset = (count % 2 == 0 ? distance : -distance);
                field.setLocation(originalX + offset, field.getY());
                count++;
                if (count >= moves) {
                    field.setLocation(originalX, field.getY()); // reset
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    public void startNewGame(){
        int index = (int) (Math.random() * randomWords.length);
        String word = randomWords[index];
        game = new Game(word, guessLimit);
        wordLabel.setText(game.getGameArray());
        historyLabel.setText("No guesses made.");
        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);

        System.out.println(game.getHistory());
        System.out.println(game.getWord());
        System.out.println(game.getGameArray());
    }
}
