
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.Score;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Key;
import learningGame.tools.LoadImages2;


// Java packages
import java.awt.image.BufferedImage;

import java.io.IOException;


public class WhackAMole extends BaseWhack {
    final private static String whackFile = LearningGame.workingDir + "music\\sfx\\whack.wav";
    
    public WhackAMole(LearningGame lg, Runnable r) {
        super(lg, r);
        
        try {
            background = LoadImages2.ensureLoadedAndGetImage(LearningGame.workingDir + "img\\green_dot.png")[0][0];
        } catch (IOException e) {
            Log2.write(e);
        }
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
     * @return the image sheet for the whackable animation.
     */
    @Override
    protected BufferedImage[] getWhackSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\mole.png", 31, 14)[0];
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return null;
    }
    
    /* 
     * @return the image for when the whackable has been whacked.
     */
    @Override
    protected BufferedImage getWhackedImage() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\whack_test.png",
                                                       workingDir + "img\\sprites\\whack_test.png_small",
                                                       0, 0,   // startX, startY
                                                       20, 20, // sizeX, sizeY
                                                       20, 20)[0][0]; // endX, endY
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return null;
    }
    
    /* 
     * @return the image sheet for the hammer.
     */
    @Override
    protected BufferedImage[] getHammerSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\hammer.png", 26, 39)[0];
            
        } catch (IOException e) {
            Log2.write(e);
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