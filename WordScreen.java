
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.log.Log2;
import learningGame.tools.Button2;
import learningGame.tools.TerminalErrorMessage;


// Java packages
import java.io.IOException;

import java.util.Random;

import javax.swing.JPanel;


// tmp
import javax.swing.JFrame;
import java.util.ArrayList;



public class WordScreen extends JPanel {
    final private Word word;
    
    // GUI
    Button2[][] wordOptionButtons = new Button2[3][2];
    
    WordScreen(Word word) {
        super(null);
        this.word = word;
    }
    
    public void createGUI() {
        // Create random
        Random rand = new Random();
        
        try {
            // Create the correct word button
            int x = rand.nextInt(wordOptionButtons.length);
            int y = rand.nextInt(wordOptionButtons[x].length);
            Button2 corBtn = wordOptionButtons[x][y] = new Button2(10);
            corBtn.setImage(word.getRandomImage(), true);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Stores all words that have been added to a button.
        ArrayList<Word> wordsSeen = new ArrayList<Word>();
        wordsSeen.add(word);
        
        for (int i = 0; i < wordOptionButtons.length; i++) {
            for (int j = 0; j < wordOptionButtons[i].length; j++) {
                try {
                    Word nextWord = LearningGame.getRandomWord(wordsSeen);
                    
                    // Check if the word exists.
                    if (nextWord == null) {
                        Log2.write("Not enough words in the list to fill all buttons!", Log2.WARNING);
                        
                        if (wordsSeen.size() <= 1) {
                            Log2.write(new Object[] {
                                    "= = = = = = = = = = = = = = = =   TERMINAL ERROR!   = = = = = = = = = = = = = = = =",
                                    "Word list is now empty or consists of one element.",
                                    "Action taken: initiate fail safe termination of the application.",
                                    "StackTrace: ",
                (new Throwable()).getStackTrace(), // This is 10x faster then Thread.currentThread.getStackTrace()
                                    " === END ERROR MESSAGE === ",
                                    ""
                            }, Log2.ERROR);
                            Log2.write("Only at most one words found in the input list!", Log2.ERROR);
                            new TerminalErrorMessage("There are too less words in the input list!");
                            
                        } else {
                            // If there are only a few (>= 2) words in the list, clear the list and try again.
                            wordsSeen.clear();
                            wordsSeen.add(word);
                            nextWord = LearningGame.getRandomWord(wordsSeen);
                            
                            if (nextWord == null) {
                                Log2.write(new Object[] {
                                    "= = = = = = = = = = = = = = = =   TERMINAL ERROR!   = = = = = = = = = = = = = = = =",
                                        "Word list was first non-empty and contained > 1 element.",
                                        "Word list is now empty or consists of one element.",
                                        "Action taken: initiate fail safe termination of the application.",
                                        "StackTrace: ",
                (new Throwable()).getStackTrace(), // This is 10x faster then Thread.currentThread.getStackTrace()
                                        " === END ERROR MESSAGE === ",
                                        ""
                                }, Log2.ERROR);
                                new TerminalErrorMessage("There are too less words in the input list!");
                            }
                        }
                    }
                    
                    if (wordOptionButtons[i][j] == null) {
                        wordOptionButtons[i][j] = new Button2(50, 50, 10, "test");
                    }
                    
                    this.add(wordOptionButtons[i][j]);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /* 
     * Sets the size and location of the WordScreen.
     * 
     * @param x the new x location of the panel.
     * @param y the new y location of the panel.
     * @param width the new width of the panel.
     * @param height the new height of the panel.
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        boolean resized = getWidth() != width || getHeight() != height;
        super.setBounds(x, y, width, height);
        
        if (resized) {
            int btnWidth = (int) (((double) width)  / wordOptionButtons.length * 0.5);
            for (int i = 0; i < wordOptionButtons.length; i++) {
                // 10% of the space is reserved for the word.
                int btnHeight = (int) (((double) height) / wordOptionButtons[i].length * 0.5 * 0.9);
                
                for (int j = 0; j < wordOptionButtons[i].length; j++) {
                    wordOptionButtons[i][j].setSize(btnWidth, btnHeight);
                    wordOptionButtons[i][j].setLocation(btnWidth * 1, 1);
                }
            }
        }
    }
    
    
    
    // tmp
    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setLayout(null);
        frame.setSize(500, 500);
        frame.setLocation(100, 100);
        
        WordScreen ws = new WordScreen(LearningGame.getWords()[0]);
        System.out.println(LearningGame.getWords()[0]);
        frame.add(ws);
        ws.createGUI();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        Log2.write(new Object[] {
                "= = = = = = = = = = = = = = = =   TERMINAL ERROR!   = = = = = = = = = = = = = = = =",
                "Word list is now empty or consists of one element.",
                "Action taken: initiate fail safe termination of the application.",
                "StackTrace: ",
                (new Throwable()).getStackTrace(), // This is 10x faster then Thread.currentThread.getStackTrace()
                " === END ERROR MESSAGE === ",
                ""
        }, Log2.ERROR);
        while(true) {
            try {
                Thread.sleep(1);
                ws.setSize(frame.getWidth(), frame.getHeight());
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}


