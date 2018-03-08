
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.Score;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Key;
import learningGame.tools.LoadImages2;
import learningGame.tools.TerminalErrorMessage;


// Java packages
import java.awt.image.BufferedImage;

import java.io.IOException;


public class WhackAMole extends BaseWhack {
    final private static String whackFile = LearningGame.workingDir + "music\\sfx\\whack_sfx.wav";
    
    public WhackAMole(LearningGame lg, Runnable r) {
        super(lg, r);
    }
    
    @Override
    public Score getScore() {
        return null;
    }
    
    /* 
     * @return the size of the whack field such that int[] {width, height},
     * where width and height denote the number of whackables in resp.
     * the rows and columns.
     */
    @Override
    protected int[] getFieldSize() {
        return new int[] {3, 3};
    }
    
    /* 
     * @return the width adjustment factor for the hammer image.
     *     0 means no adjustment, 1 means pushing the image to the right with it's height,
     *     and -1 means pushing the image to the left with it's height.
     */
    @Override
    protected double getHammerWidthAdjustmentFactor() {
        return -0.17;
    }
    
    /* 
     * @return the height adjustment factor for the hammer image.
     *     0 means no adjustment, 1 means pushing the image downwards with it's height,
     *     and -1 means pushing the image upwards with it's height.
     */
    protected double getHammerHeightAdjustmentFactor() {
        return -0.525;
    }
    
    /* 
     * @return the background image
     * TODO: add image
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(LearningGame.workingDir + "img\\green_dot.png")[0][0];
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return null;
    }
    
    /* 
     * @return the image sheet for the whackable animation.
     */
    @Override
    protected BufferedImage[] getWhackSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\Mole.png",
                                                       workingDir + "img\\sprites\\Mole.png_whack",
                                                       26, 0,      // startX, startY
                                                       57, 56,     // endX, endY
                                                       31, 14)[0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            new TerminalErrorMessage("Whack image sheet of class" + this.getClass() + " could not be loaded.");
        }
        
        return null;
    }
    
    /* 
     * @return the image for when the whackable has been whacked.
     */
    @Override
    protected BufferedImage getWhackedImage() {
        try {
            /*
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\Mole.png",
                                                       workingDir + "img\\sprites\\Mole.png_whacked",
                                                       26, 0,         // startX, startY
                                                       57, 56,        // endX, endY
                                                       31, 14)[0][0]; // sizeX, sizeY
            */
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\old\\whack_test.png",
                                                       workingDir + "img\\sprites\\Mole.png_whacked",
                                                       0, 0,         // startX, startY
                                                       20, 20,        // endX, endY
                                                       20, 20)[0][0]; // sizeX, sizeY
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            new TerminalErrorMessage("Whacked image sheet of class" + this.getClass() + " could not be loaded.");
        }
        
        return null;
    }
    
    /* 
     * @return the image sheet for the hammer.
     */
    @Override
    protected BufferedImage[] getHammerSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\Mole.png",
                                                       workingDir + "img\\sprites\\Mole.png_hammer",
                                                       0, 0,       // startX, startY
                                                       26, 156,    // endX, endY
                                                       26, 39)[0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            new TerminalErrorMessage("Hammer image sheet of class" + this.getClass() + " could not be loaded.");
        }
        
        return null;
    }
    
    /* 
     * @return the location of the music file
     */
    @Override
    protected String getWhackMusicFile() {
        return whackFile; 
    }
    
    /* 
     * This method is called when a whackable has been whacked.
     * 
     * @param timeStamp the time when the whackable has been whacked.
     *     Note that this is an event, so this part is NOT synchronized
     *     with the update thread.
     */
    protected void whackEvent(long timeStamp) {
        
    }
}