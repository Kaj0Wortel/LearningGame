
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.Score;
import learningGame.Word;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Key;
import learningGame.tools.LoadImages2;
import learningGame.tools.TerminalErrorMessage;


// Java packages
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.sound.sampled.Clip;


public class ToFill extends BaseTopDownScroller {
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public ToFill(LearningGame lg, Runnable r, long timeOut) {
       super(lg, r, timeOut);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @param the word which has this MiniGame assoiated with it.
     * @param mistakes the number of wrong buttons that were pressed in the word screen.
     * @return the score of this miniGame
     */
    @Override
    public Score getScore(Word word, int mistakes) {
        return new Score(50, 100, word, mistakes);
    }
    
    /* 
     * @return the background image.
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Fill.png",
                                                       WORKING_DIR + "img\\sprites\\Fill.png_background",
                                                       38, 0,         // startX, startY
                                                       88, 50,        // endX, endY
                                                       50, 50)[0][0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            throw new TerminalErrorMessage("Background image of class " + this.getClass() + " could not be loaded.");
        }
    }
    
    /* 
     * @return the image sheets of the obstacles.
     */
    @Override
    protected BufferedImage[][] getObstacleSheets() {
        try {
            return new BufferedImage[][] {
                LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Fill.png",
                                                    WORKING_DIR + "img\\sprites\\Fill.png_obstacle",
                                                    0, 0,       // startX, startY
                                                    13, 10,     // endX, endY
                                                    13, 10)[0]  // sizeX, sizeY
            };
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            throw new TerminalErrorMessage("Obstacle image sheet of class " + this.getClass() + " could not be loaded.");
        }
    }
    
    /* 
     * @return the image sheets of the collectables.
     */
    @Override
    protected BufferedImage[][] getCollectableSheets() {
        try {
            return new BufferedImage[][] {
                    LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Fill.png",
                                                        WORKING_DIR + "img\\sprites\\Fill.png_collectable",
                                                        30, 0,    // startX, startY
                                                        37, 10,   // endX, endY
                                                        7, 10)[0] // sizeX, sizeY
            };
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            throw new TerminalErrorMessage("Collectable image sheet of class " + this.getClass() + " could not be loaded.");
        }
    }
    
    /* 
     * @return the image sheet of the player.
     */
    @Override
    protected BufferedImage[] getPlayerSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Fill.png",
                                                       WORKING_DIR + "img\\sprites\\Fill.png_player",
                                                       14, 0,      // startX, startY
                                                       30, 26,     // endX, endY
                                                       16, 13)[0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            throw new TerminalErrorMessage("Player sheet images of class " + this.getClass() + " could not be loaded.");
        }
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
        return PlayMusic.createClip(WORKING_DIR + "music\\sfx\\breaking_wood_sfx.wav");
    }
    
    /* 
     * @return the music for when the player has hit the given type collectable.
     */
    @Override
    protected Clip getCollectedClip(int type) {
        return PlayMusic.createClip(WORKING_DIR + "music\\sfx\\coin_sfx.wav");
    }
    
    /* 
     * @return the music file for the background music.
     */
    @Override
    protected Clip getBackgroundClip() {
        Clip clip = PlayMusic.createClip(WORKING_DIR + "music\\background\\water_background.wav");
        PlayMusic.setVolume(clip, 0.5F);
        return clip;
    }
    
    @Override
    protected double getPlayerAngle() {
        return 20;
    }
    
    /* 
     * @return the text to be displayed for the instruction panel. Supports HTML.
     */
    @Override
    protected String getInstruction() {
        return "Fill it! <br> Use the left and right arrow keys"
            + " to avoid the falling sheep <br> and collect the water drops";
    }
    
    /* 
     * This function is called to damage the player.
     */
    protected void damage() {
        super.damage(); // only prints debug text
    }
    
    /* 
     * This function is called when a collectable was picked up.
     */
    protected void collectedCollectable() {
        super.collectedCollectable(); // only prints debug text
    }
    
}