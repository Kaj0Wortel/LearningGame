
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.Score;

import learningGame.log.Log2;

import learningGame.tools.matrix.Vec;

// Java packages
import java.awt.Dimension;
import java.awt.image.BufferedImage;


public class ShaveLeg extends BaseShave {
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public ShaveLeg(LearningGame lg, Runnable r, long timeOut) {
        super(lg, r, timeOut);
    }
    
    
    /* 
     * This method returns the score.
     */
    @Override
    public Score getScore() {
        return null;
    }
    
    /* 
     * @return the gravity vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    @Override
    protected Vec getGravity() {
        return new Vec(0, 0.5);
    }
    
    /* 
     * @return the initial speed vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    @Override
    protected Vec getInitSpeed() {
        return new Vec(1, 1);
    }
    
    /* 
     * @return the image used for the hair.
     */
    @Override
    protected BufferedImage getHairImageImage() {
        return null;
    }
    
    /* 
     * @return the image used for the shave object.
     */
    @Override
    protected BufferedImage getShaveItemImage() {
        return null;
    }
    
    /*
     * @return the background image.
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        return null;
    }
    
    /* 
     * @return the location of the hair patches on screen.
     * Each element must be between 0.0 and 1.0.
     */
    @Override
    protected double[][] getHairLoc() {
        return new double[][] {
            new double[] {0.5, 0.5},
                new double[] {0.55, 0.5},
                    new double[] {0.6, 0.5}
        };
    }
    
    /* 
     * @return the size of a hair patch on screen.
     * Each element must be between 0.0 and 1.0.
     */
    @Override
    protected double[] getHairSize() {
        return new double[] {0.05, 0.05};
    }
    
}
