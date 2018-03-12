
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;

import learningGame.log.Log2;

import learningGame.tools.BufferedReaderPlus;
import learningGame.tools.LoadImages2;
import learningGame.tools.MultiTool;
import learningGame.tools.TerminalErrorMessage;


// Java packages
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;


public class Word {
    // All definitions of the word are stored in here.
    final private Hashtable<String, String> wordTable = new Hashtable<String, String>();
    
    // The MiniGame that is associated to this word.
    final private Class<MiniGame> miniGameClass;
    
    // The location of the image file associated with this word.
    final private String wordImageLoc;
    
    // The random object of this word.
    final private Random rand = new Random();
    
    // The hash value of this object;
    final private int hashCode;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @param words the words in the language defined in {@code langs}.
     * @param langs the languages in which each element in {@code words} is defined.
     * @param miniGameClass the MiniGame class associated with this word.
     * @param wordImgLoc he location of the image file associated with this word.
     */
    public Word(String[] words, String[] langs, Class<MiniGame> miniGameClass, String wordImageLoc)
        throws IllegalArgumentException
    {
        if (wordImageLoc == null) 
            throw new NullPointerException("The given image word location was null.");
        if (words == null) 
            throw new NullPointerException("The given word list is null.");
        if (langs == null)
            throw new NullPointerException("The given language list is null.");
        if (words.length != langs.length)
            throw new IllegalArgumentException("The length of the words and language lists are unequal: "
                                                   + "words.length() = " + words.length + ", "
                                                   + "language.length() = " + langs.length + ".");
        this.miniGameClass = miniGameClass;
        this.wordImageLoc = wordImageLoc;
        
        // Put all words with their language in the hashtable.
        for (int i = 0; i < words.length; i++) {
            wordTable.put(langs[i].toUpperCase(), words[i]);
        }
        
        hashCode = MultiTool.calcHashCode(new Object[] {
            wordTable, miniGameClass, wordImageLoc
        });
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Create functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates a list of words from a given input file.
     * 
     * @param fileName the name of the file the words are read from.
     * @param miniGameDir the directory of the MiniGames.
     * @param imgDir the directory where the images related to the word are stored.
     * @return an ArrayList with all words that were defined in 
     */
    @SuppressWarnings("unchecked") // For the cast from Object to Class
    public static ArrayList<Word> createWordList(String dataFileName, String miniGameDir, String imgDir) {
        Log2.write(new String[] {
            (" === Started creating word list === "),
                ("Data file:    \"" + dataFileName + "\""),
                ("MiniGame dir: \"" + miniGameDir + "\""),
                ("Image dir:    \"" + imgDir + "\""),
                ("")
        }, Log2.INFO);
        
        ArrayList<Word> words = new ArrayList<Word>();
        
        try (BufferedReaderPlus brp = new BufferedReaderPlus(dataFileName, BufferedReaderPlus.JAVA_COMMENT, true)) {
            // Obtain the languages
            String[] langs = MultiTool.listToArray(brp.readCSVLine(true), String.class, 1);
            if (langs == null) return null;
            
            ArrayList<String> cells;
            while ((cells = brp.readCSVLine(true)) != null) {
                String className = "learningGame.miniGame." + cells.get(0);
                
                Class<MiniGame> miniGame = null;
                
                // If no data for the MiniGame class is available, then don't even bother to retrieve them.
                if (!cells.get(0).equals("")) {
                    // Obtain the corresponding MiniGame.
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
                    
                } else {
                    Log2.write("Reference to class was empty!", Log2.WARNING);
                }
                
                try {
                    String imgFileName = (cells.get(0).equals("") ? "" : imgDir + cells.get(0) + ".png");
                    Word word = new Word(MultiTool.listToArray(cells, String.class, 1),
                                         langs, miniGame,
                                         imgFileName);
                    
                    words.add(word);
                    Log2.write("Succesfully loaded word " + word + ".", Log2.INFO);
                    
                } catch (IllegalArgumentException | NullPointerException e) {
                    String errorText = "";
                    for (int i = 0; i < langs.length; i++) {
                        errorText += langs[i] + ": " + cells.get(i) + (i +1 != langs.length ? ", " : "");
                    }
                    
                    Log2.write("Failed to create word: " + errorText, Log2.ERROR);
                }
            }
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        Log2.write(new String[] {" === Finished creating word list === ", ""}, Log2.INFO);
        
        return words;
    }
    
    /* 
     * Creates a MiniGame of a certain class, with a certain terminate action.
     * 
     * @param miniGame the class of the object that will be created.
     * @param r the action that will run when the MiniGame is finished.
     * @return a new instance of the given MiniGame class.
     */
    public MiniGame createMiniGame(LearningGame lg, Runnable r, long timeOut) {
        if (miniGameClass == null) {
            throw new TerminalErrorMessage
                ("Tried to create MiniGame from null.",
                 "Class: " + this.getClass().getName(),
                 "Function: MiniGame createMiniGame(LearningGame, Runnable)",
                 "Error: Attempted to create a MiniGame from a null Class object.");
        }
        
        try {
            return miniGameClass
                .getConstructor(new Class<?>[] {LearningGame.class, Runnable.class, long.class})
                .newInstance(lg, r, timeOut);
            
        } catch (Exception e) {
            throw new TerminalErrorMessage
                ("Could not create MiniGame class",
                 "Failed to create an instance of the class \"" + miniGameClass.getName() + "\". Error: ", e);
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Determines whether the two objects are equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Word)) return false;
        
        Word word = (Word) obj;
        if (wordTable.equals(word.wordTable) &&
            miniGameClass.equals(word.miniGameClass) &&
            wordImageLoc.equals(word.wordImageLoc)) return true;
        
        return false;
    }
    
    /* 
     * Calculates the hash code of a word depending on it's distinguishable 
     */
    @Override
    public int hashCode() {
        return hashCode;
    }
    
    /* 
     * @return The String representation of the Word object.
     */
    @Override
    public String toString() {
        return "["
            + this.getClass().getName()
            + "  wordTable = \"" + (wordTable == null ? "null" : wordTable.toString()) + "\""
            + ", miniGameClass = \"" + (miniGameClass == null ? "null" : miniGameClass.getName()) + "\""
            + ", wordImageLoc = \"" + wordImageLoc + "\"]";
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @param lang the language of the word
     * @return the word in the given language
     */
    public String getWord(String lang) {
        return wordTable.get(lang.toUpperCase());
    }
    
    /* 
     * @return the MiniGame associated with this word.
     */
    public Class<MiniGame> getMiniGameClass() {
        return miniGameClass;
    }
    
    /* 
     * @return whether this words has a miniGame associated with it.
     */
    public boolean hasMiniGame() {
        return miniGameClass != null;
    }
    
    /* 
     * @return the word table of this word.
     */
    protected Hashtable getWordTable() {
        return wordTable;
    }
    
    /* 
     * @return a random image that is associated with this word.
     */
    public BufferedImage getRandomImage() {
        if (wordImageLoc == null || wordImageLoc.equals("")) return null;
        
        try {
            BufferedImage[][] imgs = LoadImages2.ensureLoadedAndGetImage(wordImageLoc, 250, 250);
            int x = rand.nextInt(imgs.length);
            int y = rand.nextInt(imgs[x].length);
            return imgs[x][y];
            
        } catch (IOException e) {
            Log2.write(new Object[] {
                ("Failed to load images of word: " + this.toString() + ". Error: "), e
            }, Log2.ERROR);
        }
        
        return null;
    }
    
}