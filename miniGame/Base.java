
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Key;
import learningGame.tools.LoadImages2;
import learningGame.tools.TimerTool;


// Java packages



public class Base extends MiniGame {
    public Base(LearningGame lg, Runnable r) {
        super(lg, r);
        System.out.println("SUCCES!");
    }
    
    @Override
    protected void createGUI() {
        
    }
    
    @Override
    public void start() {
        
    }
    
    @Override
    public void update(Key[] keys) {
        
    }
    
    @Override
    public void stop() {
        
    }
    
    @Override
    public void getScore() {
        
    }
}