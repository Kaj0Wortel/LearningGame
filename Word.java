
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.log.Log2;
import learningGame.tools.BufferedReaderPlus;
import learningGame.tools.MultiTool;


// Java packages
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;


public class Word {
    // All definitions of the word are stored in here.
    final private Hashtable<String, String> wordTable = new Hashtable<String, String>();
    
    // The MiniGame that is attatched to this word.
    final private Class<MiniGame> miniGameClass;
    
    /* 
     * Constructor
     */
    public Word(String[] words, String[] langs, Class<MiniGame> miniGameClass) throws IllegalArgumentException {
        if (words == null) 
            throw new IllegalArgumentException("The given word list is null.");
        if (langs == null)
            throw new IllegalArgumentException("The given language list is null.");
        if (words.length != langs.length)
            throw new IllegalArgumentException("The length of the words and language lists are unequal: "
                                         + "words.length() = " + words.length + ", "
                                         + "language.length() = " + langs.length + ".");
        this.miniGameClass = miniGameClass;
        
        // Put all words with their language in the hashtable.
        for (int i = 0; i < words.length; i++) {
            wordTable.put(langs[i], words[i]);
        }
    }
    
    /* 
     * @param lang the language
     * @return the word in the given language
     */
    public String getWord(String lang) {
        return wordTable.get(lang);
    }
    
    public Class<MiniGame> getMiniGameClass() {
        return miniGameClass;
    }
    
    /* 
     * @return The String representation of the Word object.
     */
    @Override
    public String toString() {
        return "[" + this.getClass().getName() + "  " + wordTable.toString() + "]";
    }
    
    /* 
     * Creates a MiniGame of a certain class, with a certain terminate action.
     * 
     * @param miniGame the class of the object that will be created.
     * @param r the action that will run when the MiniGame is finished.
     * @return a new instance of the given MiniGame class.
     */
    public MiniGame createMiniGame(LearningGame lg, Runnable r) {
        if (miniGameClass == null) {
            Log2.write("Cannot create an instance of a null class.", Log2.ERROR);
            return null;
        }
        
        try {
            return miniGameClass.getConstructor(new Class<?>[] {LearningGame.class, Runnable.class}).newInstance(lg, r);
            
        } catch (Exception e) {
            Log2.write(new Object[] {
                ("Failed to create an instance of the class \"" + miniGameClass.getName() + "\". Error: "), e
            }, Log2.ERROR);
        }
        
        return null;
    }
    
    /* 
     * Creates a list of words from a given input file.
     * 
     * @param fileName the name of the file the words are read from.
     * @param miniGameDir the directory of the MiniGames.
     * @return an ArrayList with all words that were defined in 
     */
    @SuppressWarnings("unchecked") // For the cast from Object to Class
    public static ArrayList<Word> createWordList(String fileName, String miniGameDir) {
        ArrayList<Word> words = new ArrayList<Word>();
        
        try (BufferedReaderPlus brp = new BufferedReaderPlus(fileName, BufferedReaderPlus.JAVA_COMMENT, true)) {
            // Retrieve the languages
            String[] langs = MultiTool.listToArray(brp.readCSVLine(true), String.class, 1);
            if (langs == null) return null;
            
            ArrayList<String> cells;
            while ((cells = brp.readCSVLine(true)) != null) {
                String className = "learningGame.miniGame." + cells.get(0);
                Class<MiniGame> miniGame = null;
                
                try {
                    miniGame = (Class<MiniGame>) Class.forName(className);
                    Log2.write("Succesfully loaded class: \"" + miniGame.getName() + "\".", Log2.INFO);
                    
                    // The Exception catch here is only to catch of the shit-load of possible exceptions.
                    // This is allowed here since there is no further error handeling then logging.
                } catch (Exception e) {
                    Log2.write(new Object[] {
                        ("Failed to load class: \"" + className + "\". Error:"), e
                    }, Log2.ERROR);
                }
                
                Word word = new Word(MultiTool.listToArray(cells, String.class, 1), langs, miniGame);
                words.add(word);
                Log2.write("Succesfully loaded word " + word + ".", Log2.INFO);
            }
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return words;
    }
    
}