
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




public class RowBoat extends BaseTopDownScroller {
    public RowBoat(LearningGame lg, Runnable r) {
        super(lg, r);
        
        try {
            background = LoadImages2.ensureLoadedAndGetImage(LearningGame.workingDir + "img\\bar.png")[0][0];
        } catch (IOException e) {
            Log2.write(e);
        }
    }
    
    
    @Override
    public void resized(int width, int height) {
        
    }
    
    @Override
    public void cleanUp() {
        
    }
    
    @Override
    public Score getScore() {
        return null;
    }
    
    @Override
    protected BufferedImage[][] getObstacleImages() {
        return null;
    }
}