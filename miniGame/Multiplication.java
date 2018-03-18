
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.Score;
import learningGame.Word;

import learningGame.font.FontLoader;

import learningGame.log.Log2;

import learningGame.tools.Button2;
import learningGame.tools.Key;


// Java pacakges
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.IOException;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class Multiplication extends MiniGame {
    private Random random = new Random();
    private int numberA = random.nextInt(9) + 1;
    private int numberB = random.nextInt(9) + 1;
    private int[] result = new int[4];
    private int correctResult = random.nextInt(result.length);
    private String displayText = numberA + " * " + numberB + " = ?";
    
    private Font font = FontLoader.getLocalFont("Cooper Black\\Cooper Black Regular.ttf");
    
    // GUI
    private Button2[] resultButtons = new Button2[result.length];
    private JLabel labelQ;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Multiplication(LearningGame lg, Runnable r, long timeOut) {
        super(lg, r, timeOut);
        
        for (int i = 0; i < result.length; i++) {
            if (i == correctResult) {
                result[i] = numberA * numberB;
                
            } else {
                int num;
                while ((num = random.nextInt(99) + 1) == numberA * numberB) {}
                result[i] = num;
            }
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This method is invoked to create the GUI of the application.
     */
    @Override
    protected void createGUI() {
        for (int i = 0; i < resultButtons.length; i++) {
            try {
                resultButtons[i] = new Button2(0, 0, 20, Integer.toString(result[i]));
                add(resultButtons[i]);
                resultButtons[i].setFont(font.deriveFont(20F));
                
                if (i == correctResult) {
                    resultButtons[i].addActionListener((e) -> finish(true));
                    
                } else {
                    resultButtons[i].addActionListener((e) -> finish(false));
                }
                
            } catch (IOException e) {
                Log2.write(e);
            }
        }
        
        labelQ = new JLabel(displayText, SwingConstants.CENTER);
        add(labelQ);
    }
    
    /* 
     * This method is invoked when the minigame is started.
     */
    @Override
    protected void startMiniGame() {
        
    }
    
    /* 
     * This method is invoked when the listeners of the sub components should be added.
     */
    @Override
    protected void addSubListeners() { }
    
    /* 
     * This method is invoked when the listeners of the sub components should be removed.
     */
    @Override
    protected void removeSubListeners() { }
    
    /* 
     * The update method. Put all time based stuff in here.
     * 
     * @param keys the keys that were pressed since the previous update.
     * @param timeStamp the start of the update cycle.
     */
    @Override
    protected void update(Key[] keys, long timeStamp) {
        
    }
    
    /* 
     * This method is always called when the MiniGame is about to shut down.
     */
    @Override
    protected void cleanUp() {
        
    }
    
    /* 
     * @param the word which has this MiniGame assoiated with it.
     * @param mistakes the number of wrong buttons that were pressed in the word screen.
     * @return the score of this miniGame
     */
    @Override
    public Score getScore(Word word, int mistakes) {
        return new Score(50, 100, word, mistakes);
    }
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    @Override
    protected void resized(int width, int height) {
        int buttonY = height * 5 / 8;
        int buttonWidth = (int) (width * 0.8 / resultButtons.length);
        int buttonHeight = height * 1 / 3;
        int spacing = (width - resultButtons.length * buttonWidth) / (resultButtons.length + 1);
        
        for (int i = 0; i < resultButtons.length; i++) {
            resultButtons[i].setLocation(spacing * (i + 1) + buttonWidth * i, buttonY);
            resultButtons[i].setSize(buttonWidth, buttonHeight);
            resultButtons[i].setTextSize(width / 20F);
        }
        
        labelQ.setFont(font.deriveFont(width / 20F));
        labelQ.setSize(width, height / 2);
        labelQ.setLocation(0, 0);
    }
    
    /* 
     * @return the background image.
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        return null;
    }
    
    /* 
     * @return the text to be displayed for the instruction panel. Supports HTML.
     */
    @Override
    protected String getInstruction() {
        return "todo";
    }
    
}
