
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;

import learningGame.log.Log2;

import learningGame.tools.Key;
import learningGame.tools.KeyDetector;
import learningGame.tools.matrix.Vec;


// Java packages
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


abstract public class BaseShave extends MiniGame {
    protected Hair[] hair;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public BaseShave(LearningGame lg, Runnable r, long timeOut) {
        super(lg, r, timeOut);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Hair class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Hair extends JPanel {
        final private Vec GRAVITY = (Vec) getGravity();
        
        private boolean isShaved = false;
        
        private Vec speed = getInitSpeed();
        
        /* ----------------------------------------------------------------------------------------------------------------
         * Hair constructor
         * ----------------------------------------------------------------------------------------------------------------
         */
        public Hair() {
            super(null);
        }
        
        long prevTimeStamp;
        /* 
         * Updates the current object.
         */
        public void update(long timeStamp) {
            if (isShaved && speed != null) {
                speed.add(GRAVITY.multiply((timeStamp - prevTimeStamp) / 1000.0, false));
                
                prevTimeStamp = timeStamp;
            }
        }
        
        /* 
         * Shaves the hair and let's it jump off with the given angle and speed.
         * 
         * @param angle the angle in radians under which the hair is shoot off.
         */
        public void shave(double angle, double initSpeed, long timeStamp) {
            prevTimeStamp = timeStamp;
            isShaved = true;
            speed = new Vec(Math.sin(angle) * initSpeed, Math.cos(angle) * initSpeed);
            setLocation((int) (getX() + speed.x()), (int) (getY() + speed.y()));
        }
        
        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Updates the frame for the minigame.
     */
    @Override
    public void update(Key[] keys, long timeStamp) {
        return;
    }
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    @Override
    public void resized(int width, int height) {
        return;
    }
    
    /* 
     * This method is invoked to create the GUI of the application.
     */
    @Override
    public void createGUI() {
        for (double[] loc : getHairLoc()) {
            
        }
    }
    
    /* 
     * This method is always called when the MiniGame is about to shut down.
     */
    @Override
    public void cleanUp() { }
    
    /* 
     * This method is invoked when the minigame is started.
     */
    @Override
    public void startMiniGame() { }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Abstract functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the gravity vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    abstract protected Vec getGravity();
    
    /* 
     * @return the initial speed vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    abstract protected Vec getInitSpeed();
    
    /* 
     * @return the image used for the hair.
     */
    abstract protected BufferedImage getHairImageImage();
    
    /* 
     * @return the image used for the shave object.
     */
    abstract protected BufferedImage getShaveItemImage();
    
    /* 
     * @return the location of the hair patches on screen.
     * Each element must be between 0.0 and 1.0.
     */
    abstract protected double[][] getHairLoc();
    
    /* 
     * @return the size of a hair patch on screen.
     * Each element must be between 0.0 and 1.0.
     */
    abstract protected double[] getHairSize();
}

