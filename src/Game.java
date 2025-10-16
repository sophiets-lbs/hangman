import javax.swing.*;
import java.util.ArrayList;

public class Game {
    private String word;
    private char[] characterArray;
    private ArrayList<Character> gameArray;
    private ArrayList<Character> history;
    private int mistakeLimit;
    private int mistakeCounter = 0;

    public Game(String word, int mistakeLimit){
        this.word = word.toUpperCase();
        this.mistakeLimit = mistakeLimit;
        history = new ArrayList<>();
        characterArray = this.word.toCharArray();
        createInitialUnderscores();
    }

    private void createInitialUnderscores(){
        gameArray = new ArrayList<>();
        for (char c : characterArray) {
            if(c == ' ') gameArray.add(' ');
            else gameArray.add('_');
        }
    }

    public boolean checkCharacter(char input){
        return word.indexOf(input) >= 0;
    }

    public void handleGuess(char input){
        input = Character.toUpperCase(input);
        if(checkCharacter(input)){
            for (int i = 0; i < gameArray.size(); i++) {
                if(characterArray[i] == input){
                    gameArray.set(i, input);
                }
            }
            if(history.contains(input)){
                mistakeCounter++;
            }
        } else {
            mistakeCounter++;
        }
        history.add(input);
    }

    public int getMistakeCounter(){
        return mistakeCounter;
    }

    public boolean isGameOver() {
        return mistakeCounter >= mistakeLimit;
    }

    public boolean isVictory() {
        return !gameArray.contains('_');
    }


    public static void showDialog(String message, String title) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public String getGameArray() {
        return getStringFromArray(gameArray);
    }

    public String getHistory() {
        return getStringFromArray(history);
    }

    private String getStringFromArray(ArrayList<Character> arrayList){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            sb.append(arrayList.get(i));
            if (i < arrayList.size() - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public String getCurrentImage(){
        int totalImages = 11;
        if (mistakeCounter <= 0) return "images/image_0.png";

        // Calculate image number proportionally
        double fraction = (double) mistakeCounter / mistakeLimit;
        int imageNr = (int) Math.round(fraction * totalImages);

        if (imageNr > totalImages) imageNr = totalImages;
        if (imageNr < 0) imageNr = 0;
        return "images/image_" + imageNr + ".png";
    }

    public String getWord() {
        return word;
    }
}