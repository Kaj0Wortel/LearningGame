
package learningGame.words;


// Own packages
import learningGame.log.Log2;
import learningGame.tools.BufferedReaderPlus;


// Java packages
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;


public class Word {
    public enum Language {
        English, Italian;
    }
    
    private Hashtable<Language, String> wordTable = new Hashtable<Language, String>();
    
    public Word(String[] words, Language[] langs) throws IllegalArgumentException {
        if (words == null) 
            throw new IllegalArgumentException("The given word list is null.");
        if (langs == null)
            throw new IllegalArgumentException("The given language list is null.");
        if (words.length != langs.length)
            throw new IllegalArgumentException("The length of the words and language lists are unequal: "
                                         + "words.length = " + words.length + ", "
                                         + "language.length = " + langs.length + ".");
        
        for (int i = 0; i < words.length; i++) {
            wordTable.put(langs[i], words[i]);
        }
    }
    
    public String getWord(Language lang) {
        return wordTable.get(lang);
    }
    
    public ArrayList<Word> getWordList(String fileName) {
        try (BufferedReaderPlus brp = new BufferedReaderPlus(new FileReader(fileName))) {
            
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return null;
    }
}