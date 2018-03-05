
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

import javax.sound.sampled.Clip;


public class RowBoat extends BaseTopDownScroller {
    public RowBoat(LearningGame lg, Runnable r) {
        super(lg, r);
    }
    
    @Override
    public void cleanUp() {
        
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
            return LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\bar.png")[0][0];
            
        } catch (IOException e) {
            Log2.write(e);
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
                new BufferedImage[] {
                    LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\whack_test.png", 20, 20)[0][4]
                }
            };
            
        } catch (IOException e) {
            Log2.write(e);
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
                new BufferedImage[] {
                    LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\whack_test.png", 20, 20)[0][2]
                }
            };
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return null;
    }
    
    /* 
     * @return the image sheet of the player.
     */
    @Override
    protected BufferedImage[] getPlayerSheet() {
        try {
            return new BufferedImage[] {
                LoadImages2.ensureLoadedAndGetImage(workingDir + "img\\sprites\\whack_test.png", 20, 20)[0][0]
            };
            
        } catch (IOException e) {
            Log2.write(e);
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
     *     Speed is measured in frames per second.
     */
    @Override
    protected int getObstacleFrameSpeed(int type) {
        return 12;
    }
    
    /* 
     * @return the frame speed for the given collectable type.
     *     Speed is measured in frames per second.
     */
    @Override
    protected int getCollectableFrameSpeed(int type) {
        return 12;
    }
    
    /* 
     * @return the frame speed for the player.
     *     Speed is measured in frames per second.
     */
    @Override
    protected int getPlayerFrameSpeed() {
        return 12;
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