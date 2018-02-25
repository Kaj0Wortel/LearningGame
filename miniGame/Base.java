
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
import java.io.IOException;


public class Base extends MiniGame {
    public Base(LearningGame lg, Runnable r) {
        super(lg, r);
        
        // tmp
        System.out.println("SUCCES!");
        try {
            background = LoadImages2.ensureLoadedAndGetImage(LearningGame.workingDir + "img\\green_dot.png")[0][0];
        } catch (IOException e) {
            Log2.write(e);
        }
    }
    
    @Override
    protected void createGUI() {
        
    }
    
    @Override
    public void update(Key[] keys) {
        
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
}