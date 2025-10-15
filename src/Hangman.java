import javax.swing.*;

public class Hangman {
    private JPanel hangmanPanel;
    private JTextField inputField;
    private JLabel wordLabel;
    private JButton submitButton;
    private JLabel historyLabel;
    private JLabel guessesLabel;
    private JLabel imageLabel;

    private int guessLimit;

    public Hangman(){
        guessLimit = 11;
        Game game = new Game("Sophie", guessLimit);
        wordLabel.setText(game.getGameArray());
        submitButton.addActionListener(e -> {
            submitGuess(game);
        });

        //ISSUE MIT IMAGE, WENN MAN GEWINNT ODER VERLIERT (TRIGGERT KEINEN GAMESTATE)
        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);

        hangmanPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

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
        frame.setSize(500, 500);
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
        }
        if(!game.getHistory().isEmpty()){
            historyLabel.setText("<html>"+game.getHistory()+"</html>");
        }

        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);
        inputField.setText("");
        game.checkGameState();
    }
}
