import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Hangman {
    private JPanel hangmanPanel;
    private JTextField inputField;
    private JLabel wordLabel;
    private JButton submitButton;
    private JLabel historyLabel;
    private JLabel guessesLabel;
    private JLabel imageLabel;
    private JPanel inputPanel;
    private JPanel historyCardPanel;
    private JPanel historyPanel;
    private JLabel mistakeLabel;

    private int victoryStreak = 0;
    private int mistakeLimit = 10;
    private Game game;

    private ArrayList<String> randomWords;

    public Hangman(JFrame frame){
        loadWordsFromFile();
        startNewGame();

        JMenuBar menuBar = getJMenuBar();
        frame.setJMenuBar(menuBar);

        submitButton.addActionListener(e -> {
            submitGuess(game);
        });

        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);

        hangmanPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        historyCardPanel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
        wordLabel.setBorder(BorderFactory.createEmptyBorder(25,0,0,0));

        historyCardPanel.setLayout(new CardLayout());
        JPanel emptyPanel = new JPanel();
        historyCardPanel.add(historyPanel, "history");
        historyCardPanel.add(emptyPanel, "empty");

        //nur ein Character und Uppercase
        inputField.setDocument(new javax.swing.text.PlainDocument() {
            @Override public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str != null && getLength() + str.length() <= 1)
                    super.insertString(offs, str.toUpperCase(), a);
            }
        });
        inputField.addActionListener(e -> {
            submitButton.doClick();
        });
    }

    private JMenuBar getJMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenu viewMenu = new JMenu("View");
        JMenuItem viewHistoryItem = new JCheckBoxMenuItem("Show Guess History", true);
        JMenuItem viewMistakeCounterItem = new JCheckBoxMenuItem("Show Mistake Counter");
        JMenuItem setLimitItem = new JMenuItem("Set Mistake Limit");
        JMenuItem addWordItem = new JMenuItem("Add Word");
        JMenuItem startNewGameItem = new JMenuItem("New Game");

        viewHistoryItem.addActionListener(e -> {
            viewHistory();
        });

        setLimitItem.addActionListener(e -> {
            setGuessLimit(mistakeLimit);
        });

        addWordItem.addActionListener(e -> {
            addWordToList();
        });

        viewMistakeCounterItem.addActionListener(e -> {
            showMistakeCounter();
        });

        startNewGameItem.addActionListener(e -> {
            startNewGame();
        });

        viewMenu.add(viewHistoryItem);
        viewMenu.add(viewMistakeCounterItem);
        gameMenu.add(startNewGameItem);
        gameMenu.add(addWordItem);
        gameMenu.add(setLimitItem);
        menuBar.add(gameMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }

    private void showMistakeCounter(){
        mistakeLabel.setVisible(!mistakeLabel.isVisible());
    }

    private void addWordToList(){
        String newWord = JOptionPane.showInputDialog(
                null,
                "Enter a new word:",
                "Add Word",
                JOptionPane.PLAIN_MESSAGE
        );

        if (newWord != null && !newWord.trim().isEmpty()) {
            if(randomWords.contains(newWord.trim())) {
                JOptionPane.showMessageDialog(null, "Word already exist.");
                return;
            }

            randomWords.add(newWord.trim());
            System.out.println(randomWords.getLast());
            JOptionPane.showMessageDialog(null, "Word added.");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/words.txt", true))) {
                writer.newLine();
                writer.write(newWord);
            } catch (IOException e) {
                System.out.println("Hat nicht gefunkt.");
            }
        }
    }

    private void viewHistory() {
        CardLayout cl = (CardLayout) historyCardPanel.getLayout();
        if (historyPanel.isVisible()) {
            cl.show(historyCardPanel, "empty");
        } else {
            cl.show(historyCardPanel, "history");
        }
    }

    private void setGuessLimit(int currentLimit){
        JRadioButton rb3 = new JRadioButton("3");
        JRadioButton rb5 = new JRadioButton("5");
        JRadioButton rb10 = new JRadioButton("10");

        switch (currentLimit) {
            case 3 -> rb3.setSelected(true);
            case 5 -> rb5.setSelected(true);
            default -> rb10.setSelected(true);
        }

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rb3);
        buttonGroup.add(rb5);
        buttonGroup.add(rb10);

        JPanel panel = new JPanel();
        panel.add(rb3);
        panel.add(rb5);
        panel.add(rb10);

        int dialog = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Set limit for wrong guesses",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (dialog == JOptionPane.OK_OPTION) {
            if (rb3.isSelected()) mistakeLimit = 3;
            else if (rb5.isSelected()) mistakeLimit = 5;
            else if (rb10.isSelected()) mistakeLimit = 10;

            System.out.println("New limit: " + mistakeLimit);
            startNewGame();
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Henkersknecht™ - Das Original");
        frame.setContentPane(new Hangman(frame).hangmanPanel);
        frame.setSize(510, 580);
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

        mistakeLabel.setText("Mistakes: " + game.getMistakeCounter());

        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);
        inputField.setText("");
        boolean gameOver = game.isGameOver();
        boolean victory = game.isVictory();

        if(gameOver){
            String message = "A measly " + game.getWord() + " has caused his demise...";
            Game.showDialog(message, "Game over");
            victoryStreak = 0;
            startNewGame();
        } else if(victory){
            victoryStreak++;
            if(victoryStreak > 2){
                String message = "A many hanged men's saviour...\nVictory Streak: " + victoryStreak;
                Game.showDialog(message, "Victory");
            } else {
                Game.showDialog("He is absolved of his sins - for now.", "Victory");
            }
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
        int index = (int) (Math.random() * randomWords.size());
        String word = randomWords.get(index);
        game = new Game(word, mistakeLimit);
        wordLabel.setText(game.getGameArray());
        historyLabel.setText("No guesses made.");
        ImageIcon icon = new ImageIcon(game.getCurrentImage());
        imageLabel.setIcon(icon);
        mistakeLabel.setText("Mistakes: 0");
    }

    private void loadWordsFromFile() {
        randomWords = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/" + "words.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                randomWords.add(line.trim());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            // fallback to default words
            randomWords.addAll(Arrays.asList("backyard","butterfly","snowflake","blueberry","mailbox"));
        }
    }
}
