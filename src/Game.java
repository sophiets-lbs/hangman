import javax.swing.*;
import java.util.ArrayList;

public class Game {
    private String word;
    private char[] characterArray;
    private ArrayList<Character> gameArray;
    private ArrayList<Character> history;
    private int guessLimit;
    private int guessCounter = 0;

    public Game(String word, int guessLimit){
        this.word = word.toUpperCase();
        this.guessLimit = guessLimit;
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
                guessCounter++;
            }
        } else {
            guessCounter++;
        }
        history.add(input);

        System.out.println(guessCounter);
    }

    public boolean isGameOver() {
        return guessCounter > guessLimit;
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

    public int getGuessLimit() {
        return guessLimit;
    }

    public void setGuessLimit(int guessLimit) {
        this.guessLimit = guessLimit;
    }

    public int getGuessCounter() {
        return guessCounter;
    }

    public void setGuessCounter(int guessCounter) {
        this.guessCounter = guessCounter;
    }

    public String getCurrentImage(){
        if(guessLimit > 11) return "images/image_11.png";
        return "images/image_" + guessCounter + ".png";
    }

    public String getWord() {
        return word;
    }
}