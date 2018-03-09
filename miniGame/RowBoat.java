
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

import javax.sound.sampled.Clip;


public class RowBoat extends BaseTopDownScroller {
    public RowBoat(LearningGame lg, Runnable r) {
        super(lg, r);
    }
    
    @Override
    public Score getScore() {
        return null;
    }
    
    /* 
     * @return the background image.
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\Row.png",
                                                       workingDir + "img\\sprites\\Row.png_background",
                                                       41, 0,         // startX, startY
                                                       91, 50,        // endX, endY
                                                       50, 50)[0][0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            new TerminalErrorMessage("Background image of class" + this.getClass() + " could not be loaded.");
        }
        
        return null;
    }
    
    /* 
     * @return the image sheets of the obstacles.
     */
    @Override
    protected BufferedImage[][] getObstacleSheets() {
        try {
            return new BufferedImage[][] {
                LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\Row.png",
                                                    workingDir + "img\\sprites\\Row.png_obstacle",
                                                    9, 0,       // startX, startY
                                                    22, 57,     // endX, endY
                                                    13, 19)[0]  // sizeX, sizeY
            };
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            new TerminalErrorMessage("Obstacle image sheet of class" + this.getClass() + " could not be loaded.");
        }
        
        return null;
    }
    
    /* 
     * @return the image sheets of the collectables.
     */
    @Override
    protected BufferedImage[][] getCollectableSheets() {
        try {
            return new BufferedImage[][] {
                    LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\Row.png",
                                                        workingDir + "img\\sprites\\Row.png_collectable",
                                                        0, 0,    // startX, startY
                                                        9, 72,   // endX, endY
                                                        9, 9)[0] // sizeX, sizeY
            };
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            new TerminalErrorMessage("Collectable image sheet of class" + this.getClass() + " could not be loaded.");
        }
        
        return null;
    }
    
    /* 
     * @return the image sheet of the player.
     */
    @Override
    protected BufferedImage[] getPlayerSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\Row.png",
                                                       workingDir + "img\\sprites\\Row.png_player",
                                                       22, 0,      // startX, startY
                                                       41, 26,     // endX, endY
                                                       19, 13)[0]; // sizeX, sizeY
                
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            new TerminalErrorMessage("Player sheet images of class" + this.getClass() + " could not be loaded.");
        }
        
        return null;
    }
    
    /* 
     * @return the maximal obstacle type value (inclusive).
     */
    @Override
    protected int getMaxObstacleType() {
        return 0;
    }
    
    /* 
     * @return the maximal collectable type value (inclusive).
     */
    @Override
    protected int getMaxCollectableType() {
        return 0;
    }
    
    /* 
     * @return the frame speed for the given obstacle type.
     *     Speed is measured in miliseconds per frame.
     */
    @Override
    protected int getObstacleFrameSpeed(int type) {
        return 1000 * 1/12;
    }
    
    /* 
     * @return the frame speed for the given collectable type.
     *     Speed is measured in miliseconds per frame.
     */
    @Override
    protected int getCollectableFrameSpeed(int type) {
        return 1000 * 1/24;
    }
    
    /* 
     * @return the frame speed for the player.
     *     Speed is measured in miliseconds per frame.
     */
    @Override
    protected int getPlayerFrameSpeed() {
        return 1000 * 1/4;
    }
    
    /* 
     * @return the music file for when the player has hit the given type obstacle.
     */
    @Override
    protected Clip getDamagedClip(int type) {
        return PlayMusic.createClip(workingDir + "music\\sfx\\breaking_wood_sfx.wav");
    }
    
    /* 
     * @return the music for when the player has hit the given type collectable.
     */
    @Override
    protected Clip getCollectedClip(int type) {
        return PlayMusic.createClip(workingDir + "music\\sfx\\coin_sfx.wav");
    }
    
    /* 
     * @return the music file for the background music.
     */
    @Override
    protected Clip getBackgroundClip() {
        Clip clip = PlayMusic.createClip(workingDir + "music\\background\\water_background.wav");
        PlayMusic.setVolume(clip, 0.5F);
        return clip;
    }
    
}