
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
    public WhackAMole(LearningGame lg, Runnable r) {
        super(lg, r);
        
        // tmp
        System.out.println("SUCCES!");
        try {
            background = LoadImages2.ensureLoadedAndGetImage(LearningGame.workingDir + "img\\green_dot.png")[0][0];
        } catch (IOException e) {
            Log2.write(e);
        }
    }
    
    /* 
     * The update method.
     * 
     * @param keys the keys that were pressed since the previous update.
     * @param timeStamp the start of the update cycle.
     */
    @Override
    public void update(Key[] keys, long timeStamp) {
        super.update(keys, timeStamp);
        //whacks[0][0].showWhackable(500, 200, timeStamp);
    }
    
    @Override
    public void cleanUp() {
        
    }
    
    @Override
    public Score getScore() {
        return null;
    }
    
    @Override
    protected BufferedImage[] getWhackSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\mole.png", 31, 14)[0];
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return null;
    }
    
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
    
    @Override
    protected BufferedImage[] getHammerSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\hammer.png", 26, 39)[0];
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return null;
    }
}