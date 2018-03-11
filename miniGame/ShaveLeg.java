
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
     * Note that a positive x coord creates gravity to the right,
     * while a positive y coord creates gravity downwards
     */
    @Override
    protected Vec getGravity() {
        return new Vec(0, 0.09);
    }
    
    /* 
     * @return the initial speed vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    @Override
    protected Vec getInitSpeed() {
        //
        double angle = Math.toRadians(random.nextDouble() * 120 + 30);
        double initSpeed = 0.03;
        
        return new Vec(Math.cos(angle) * initSpeed, -Math.sin(angle) * initSpeed);
    }
    
    /* 
     * @return the image used for the hair.
     */
    @Override
    protected BufferedImage getHairImage() {
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
     * Each element must contain an array which contains two doubles
     *     which must each be between 0.0 and 1.0.
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
     * Each element must contain two doubles which must each be between 0.0 and 1.0.
     */
    @Override
    protected double[] getHairSize() {
        return new double[] {0.025, 0.025};
    }
    
}
