
package learningGame;

// Own packages


// Java classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

import java.util.Enumeration;


public class Score {
    private double gamePoints;
    private double obtainableGamePoints;
    private HashMap<Word, ArrayList<Integer>> wordTable = new HashMap<Word, ArrayList<Integer>>();
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates an empty score object with no scores.
     */
    public Score() {
        gamePoints = 0.0;
        obtainableGamePoints = 0.0;
    }
    
    /* 
     * Creates a score object from a game.
     */
    public Score(double gamePoints, double obtainableGamePoints, Word word, int mistakes) {
        this.gamePoints = gamePoints;
        this.obtainableGamePoints = obtainableGamePoints;
        addWordEntry(word, mistakes);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Merges two scores.
     * 
     * @param addScore score to be added. If this is null, no action is taken.
     */
    public Score add(Score addScore) {
        if (addScore == null) return this;
        
        gamePoints += addScore.getGamePoints();
        obtainableGamePoints += addScore.getObtainableGamePoints();
        
        for (Map.Entry<Word, ArrayList<Integer>> entry : addScore.getWordTable().entrySet()) {
            Word word = entry.getKey();
            ArrayList<Integer> curList = wordTable.get(word);
            ArrayList<Integer> addList = entry.getValue();
            
            if (curList == null) {
                if (addList != null) {
                    wordTable.put(word, addList);
                }
                
            } else { // curList != null
                if (addList != null) {
                    curList.addAll(addList);
                }
            }
        }
        
        return this;
    }
    
    /* 
     * Adds an entry to the wordTable. Creates a new list iff the entry does not yet exist.
     */
    private void addWordEntry(Word word, int entry) {
        if (wordTable.containsKey(word)) {
            wordTable.get(word).add(entry);
            
        } else {
            ArrayList<Integer> newList = new ArrayList<Integer>();
            newList.add(entry);
            wordTable.put(word, newList);
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the list containing the wrong words of this score.
     */
    public HashMap<Word, ArrayList<Integer>> getWordTable() {
        return wordTable;
    }
    
    /*
     * @return the game points of this score object.
     */
    protected double getGamePoints() {
        return gamePoints;
    }
    
    /*
     * @return the obtainable game points of this score object.
     */
    protected double getObtainableGamePoints() {
        return obtainableGamePoints;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Calculate methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Calculates the average score of all MiniGames.
     */
    public double calcAvgGameScore() {
        return gamePoints / obtainableGamePoints;
    }
    
    /* 
     * Calculates the total number of mistakes.
     */
    public int calcMistakes() {
        int mistakeCounter = 0;
        
        for (Map.Entry<Word, ArrayList<Integer>> entry : wordTable.entrySet()) {
            for (Integer elem : entry.getValue()) {
                mistakeCounter += elem;
            }
        }
        
        return mistakeCounter;
    }
    
    public double calcCorrectRatio() {
        int wordCounter = 0;
        int correctCounter = 0;
        
        for (Map.Entry<Word, ArrayList<Integer>> entry : wordTable.entrySet()) {
            for (Integer elem : entry.getValue()) {
                if (elem == 0) correctCounter++;
                
                wordCounter++;
            }
        }
        
        return ((double) correctCounter) / wordCounter;
    }
    
    /* 
     * Calculates the average number of mistakes per word.
     * 
     * Note: Not tested yet.
     */
    public double calcAvgMistakes() {
        int wordCount = 0;
        int mistakeCounter = 0;
        
        for (Map.Entry<Word, ArrayList<Integer>> entry : wordTable.entrySet()) {
            for (Integer elem : entry.getValue()) {
                mistakeCounter += elem;
            }
            
            wordCount++;
        }
        
        return ((double) mistakeCounter) / ((double) wordCount);
    }
    
    /* 
     * Calculates the avarage number of wrong words.
     * 
     * Note: Not tested yet.
     */
    public double calcAvgWrongWords() {
        double wordCount = 0;
        double wrongWordCount = 0;
        
        for (Map.Entry<Word, ArrayList<Integer>> entry : wordTable.entrySet()) {
            boolean isWrong = true;
            for (Integer elem : entry.getValue()) {
                if (elem != 0) {
                    wrongWordCount++;
                    break;
                }
            }
            
            wordCount++;
        }
        
        return wrongWordCount / wordCount;
    }
    
    /* 
     * Lists all words that have at least one mistake.
     */
    public ArrayList<Word> listWrongWords() {
        ArrayList<Word> wordMistakes = new ArrayList<Word>();
        
        for (Map.Entry<Word, ArrayList<Integer>> entry : wordTable.entrySet()) {
            for (Integer mistake : entry.getValue()) {
                if (mistake != 0) {
                    wordMistakes.add(entry.getKey());
                    break;
                }
            }
        }
        
        return wordMistakes;
    }
    
    /* 
     * Resets the score object.
     * After this call the object should be the same as a {@code new Score())
     * (note that {@code this.equals(new Score())} still yields false).
     */
    public void clear() {
        gamePoints = 0;
        obtainableGamePoints = 0;
        wordTable.clear();
    }
    
    
    /*
     * Prints the current score.
     */
    @Override
    public String toString() {
        return "[" + this.getClass().getName() + ": {gamePoints = " + gamePoints
            + ", obtainableGamePoints = " + obtainableGamePoints
            + " wordTable  " + wordTable.toString() + "}]";
    }
    
    public static void main(String[] args) {
        Score score_0 = new Score();
        Score score_1 = new Score(10, 100, new Word(new String[] {"word"}, new String[] {"lang"}, MiniGame.class, "test"), 10);
        Score score_2 = new Score(40, 200, new Word(new String[] {"word"}, new String[] {"lang"}, MiniGame.class, "test"), 10);
        
        System.out.println(score_1);
        score_0.add(score_1);
        System.out.println(score_2);
        score_0.add(score_2);
        System.out.println(score_0);
    }
    
}