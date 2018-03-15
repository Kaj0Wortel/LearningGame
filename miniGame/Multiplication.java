
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



public class Multiplication extends MiniGame {
    private Random random = new Random();
    private int numberA = random.nextInt(9) + 1;
    private int numberB = random.nextInt(9 + 1);
    private int[] result = new int[4];
    private int correctResult = random.nextInt(result.length);
    private String displayText = numberA + " * " + numberB + " = ?";
    
    private Font font = FontLoader.getLocalFont("Cooper Black\\Cooper Black Regular.ttf");
    
    // GUI
    private Button2[] resultButtons = new Button2[result.length];
    
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
                while ((num = random.nextInt(99) + 1) == correctResult) {}
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
        }
    }
    
    /* 
     * @return the background image.
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        return null;
    }
    
    @Override
    protected void drawBackground(Graphics2D g2d, BufferedImage background) {
        super.drawBackground(g2d, background);
        
        g2d.setFont(font.deriveFont(((float) getWidth()) / 40F));
        Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(displayText, g2d);
        double textWidth = bounds.getWidth();
        double textHeight = bounds.getHeight();
        int ascent = g2d.getFontMetrics().getAscent();
        g2d.drawString("test", 0, 0);
        g2d.drawString(displayText,
                       (int) ((getWidth() - textWidth) / 2),
                       (int) ((getHeight() / 2 - textHeight) / 2 + ascent));
    }
    
    /* 
     * @return the text to be displayed for the instruction panel. Supports HTML.
     */
    @Override
    protected String getInstruction() {
        return "todo";
    }
    
}
