
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.log.Log2;
import learningGame.tools.BufferedReaderPlus;
import learningGame.tools.LoadImages2;
import learningGame.tools.MultiTool;


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
    
    // The MiniGame that is attatched to this word.
    final private Class<MiniGame> miniGameClass;
    
    final private String wordImgLoc;
    
    /* 
     * Constructor
     */
    public Word(String[] words, String[] langs, Class<MiniGame> miniGameClass, String wordImgLoc)
        throws IllegalArgumentException
    {
        if (wordImgLoc == null) 
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
        this.wordImgLoc = wordImgLoc;
        
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
    
    /* 
     * @return the MiniGame associated with this word.
     */
    public Class<MiniGame> getMiniGameClass() {
        return miniGameClass;
    }
    
    private Random rand = new Random();
    /* 
     * @return a random image that is associated with this word.
     */
    public BufferedImage getRandomImage() {
        if (wordImgLoc == null || wordImgLoc.equals("")) return null;
        
        try {
            BufferedImage[][] imgs = LoadImages2.ensureLoadedAndGetImage(wordImgLoc);
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
    
    /* 
     * @return the word table of this word.
     */
    protected Hashtable getWordTable() {
        return wordTable;
    }
    
    /* 
     * @return The String representation of the Word object.
     */
    @Override
    public String toString() {
        return "[" + this.getClass().getName() + "  " + wordTable.toString() + "]";
    }
    
    /* 
     * Determines whether the two objects are equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Word)) return false;
        return this.getWordTable().equals(((Word) obj).getWordTable());
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
            Log2.write(new String[] {
                ("Class: " + this.getClass().getName()),
                    ("Function: MiniGame createMiniGame(LearningGame, Runnable)"),
                    ("Error: Cannot create a MiniGame from a null class.")
            }, Log2.ERROR);
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
    
}