
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.font.FontLoader;
import learningGame.log.Log2;
import learningGame.tools.Button2;
import learningGame.tools.LoadImages2;
import learningGame.tools.TerminalErrorMessage;


// Java packages
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.io.IOException;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;


// tmp
import javax.swing.JFrame;
import java.util.ArrayList;



public class WordScreen extends JPanel {
    final private static String GOOD_BTN_IMG_LOC = LearningGame.WORKING_DIR + "\\img\\button2_img_good_answer.png";
    final private static String WRONG_BTN_IMG_LOC = LearningGame.WORKING_DIR + "\\img\\button2_img_wrong_answer.png";
    
    final private Word word;
    final private String langQ;
    final private String langA;
    final private Runnable r;
    
    private int mistakeCounter = 0;
    
    // GUI
    final private static int buttonsX = 3;
    final private static int buttonsY  = 2;
    Button2[][] wordOptionButtons = new Button2[buttonsX][buttonsY];
    JLabel[][] buttonWords = new JLabel[buttonsX][buttonsY];
    JLabel wordQ;
    
    /* 
     * @param word the word to be questioned.
     * @param langQ the question language.
     * @param langA the answer language.
     * @param r the runnable that is executed when the program terminates.
     */
    WordScreen(Word word, String langQ, String langA, Runnable r) {
        super(null);
        this.word = word;
        this.langQ = langQ;
        this.langA = langA;
        this.r = r;
        
        createGUI();
    }
    
    /* 
     * Creates the GUI of the panel.
     */
    private void createGUI() {
        // Create question panel
        wordQ = createLabel(word.getWord(langQ));
        
        try {
            // Create random
            Random rand = new Random();
            
            // Create the correct word button
            int x = rand.nextInt(wordOptionButtons.length);
            int y = rand.nextInt(wordOptionButtons[x].length);
            if (wordOptionButtons[x][y] != null)
                throw new TerminalErrorMessage("The correct answer button has already been initiated!",
                                               "Button [" + x + "][" + y + "] was already initiated!");
                
            Button2 corBtn = wordOptionButtons[x][y]
                = new Button2(10, LoadImages2.ensureLoadedAndGetImage
                                  (GOOD_BTN_IMG_LOC,
                                   0, 0,   // Start x/y
                                   48, 80, // End x/y
                                   16, 16) // Width/height
                             );
            corBtn.setImage(word.getRandomImage(), true);
            corBtn.addActionListener((e) -> {
                if (e.getActionCommand().contains("released")) {
                    corBtn.setEnabled(false);
                    correctWord();
                }
            });
            this.add(corBtn);
            
            buttonWords[x][y] = createLabel(word.getWord(langA));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Stores all words that have been added to a button.
        ArrayList<Word> wordsSeen = new ArrayList<Word>();
        wordsSeen.add(word);
        
        for (int i = 0; i < wordOptionButtons.length; i++) {
            for (int j = 0; j < wordOptionButtons[i].length; j++) {
                if (wordOptionButtons[i][j] != null) continue;
                
                Word nextWord = LearningGame.getRandomWord(wordsSeen);
                
                // Check if there was a word found
                if (nextWord == null) {
                    Log2.write("Not enough words in the list to fill all buttons!", Log2.WARNING);
                    
                    if (wordsSeen.size() <= 1) {
                        throw new TerminalErrorMessage("There are too less words in the input list!",
                                                       "Word list is empty or consists of one element.",
                                                       "Action taken: initiate fail safe termination of the application.");
                        
                    } else {
                        // If there are only a few (>= 2) words in the list, clear the list and try again.
                        wordsSeen.clear();
                        wordsSeen.add(word);
                        nextWord = LearningGame.getRandomWord(wordsSeen);
                        
                        if (nextWord == null) {
                            throw new TerminalErrorMessage("There are too less words in the input list!",
                                                           "Word list was first non-empty and contained > 1 element.",
                                                           "Word list is now empty or consists of one element.",
                                                           "Action taken: initiate fail safe termination of the application.");
                        }
                    }
                }
                
                try {
                    // Create new buttons
                    wordOptionButtons[i][j]
                        = new Button2(10, LoadImages2.ensureLoadedAndGetImage
                                          (WRONG_BTN_IMG_LOC,
                                           0, 0,   // Start x/y
                                           48, 80, // End x/y
                                           16, 16) // Width/height
                                     );
                    Button2 newBtn = wordOptionButtons[i][j];
                    newBtn.setImage(nextWord.getRandomImage(), true);
                    
                    wordOptionButtons[i][j].addActionListener((e) -> {
                        if (e.getActionCommand().contains("released")) {
                            newBtn.setEnabled(false);
                            wrongWord();
                        }
                    });
                    
                    this.add(wordOptionButtons[i][j]);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                // Create new labels
                buttonWords[i][j] = createLabel(nextWord.getWord(langA));
            } // End for
        } // End for
        
        resized(getWidth(), getHeight());
    }
    
    /* 
     * @param name the text to be set to the JLabel.
     * @return a JLabel with text 'name' and with the default settings.
     *     Also adds the JLabel to the panel.
     */
    private JLabel createLabel(String name) {
        JLabel label = new JLabel(name);
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label);
        return label;
    }
    
    /* 
     * This function is invoked when the correct button has been pressed.
     */
    public void correctWord() {
        for (int i = 0; i < wordOptionButtons.length; i++) {
            for (int j = 0; j < wordOptionButtons[i].length; j++) {
                wordOptionButtons[i][j].setEnabled(false);
            }
        }
        
        repaint();
        new Thread("correct word") {
            @Override
            public void run() {
                if (r != null) r.run();
            }
        }.start();
    }
    
    /* 
     * This function is invoked when a wrong button has been pressed.
     */
    public void wrongWord() {
        mistakeCounter++;
    }
    
    /* 
     * Sets the size and location of the WordScreen.
     * Updates the components of the panel iff the new width or height is different
     * from resp. the previous width and height.
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
        
        if (resized) resized(width, height);
    }
    
    /* 
     * Forces the panel to resize to the given width and height.
     * 
     * @param width the new width of the panel.
     * @param height the new height of the panel.
     */
    private void resized(int width, int height) {
        Font defaultFont = FontLoader.getLocalFont("source-sans-pro\\SourceSansPro-Bold.ttf");
        Font buttonFont = defaultFont.deriveFont(width / 50F);
        
        double spareFactor = 0.5;
        double wordHeightFactor = 0.15;
        double labelHeightFactor = 0.1;
        
        wordQ.setSize(width,(int) (height * wordHeightFactor));
        wordQ.setLocation(0, 0);
        wordQ.setFont(defaultFont.deriveFont(width / 25F));
        
        double reservedWidth = ((double) width) / wordOptionButtons.length;
        
        for (int i = 0; i < wordOptionButtons.length; i++) {
            double reservedHeight = ((double) height) / wordOptionButtons[i].length * (1 - wordHeightFactor);
            
            for (int j = 0; j < wordOptionButtons[i].length; j++) {
                if (wordOptionButtons[i][j] != null) {
                    wordOptionButtons[i][j]
                        .setSize((int) (reservedWidth * spareFactor),
                                 (int) (reservedHeight * (spareFactor - labelHeightFactor)));
                    wordOptionButtons[i][j]
                        .setLocation((int) ((i + 0.5 * spareFactor)* reservedWidth),
                                     (int) ((j + 0.5 * spareFactor + wordHeightFactor)
                                                * reservedHeight));
                }
                
                if (buttonWords[i][j] != null) {
                    buttonWords[i][j]
                        .setSize((int) (reservedWidth),
                                 (int) (reservedHeight * labelHeightFactor));
                    buttonWords[i][j]
                        .setLocation((int) (i * reservedWidth),
                                     wordOptionButtons[i][j].getY() + wordOptionButtons[i][j].getHeight());
                    buttonWords[i][j].setFont(buttonFont);
                }
            }
        }
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    }
    
    
    
    // tmp
    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setLayout(null);
        frame.setSize(1000, 1000);
        frame.setLocation(0, 0);
        
        WordScreen ws = new WordScreen(LearningGame.getWords()[0], "Italian", "English", null);
        System.out.println(LearningGame.getWords()[0]);
        frame.add(ws);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
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


