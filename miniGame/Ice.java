
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.Word;
import java.awt.Graphics2D;
import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Key;
import learningGame.tools.LoadImages2;
import learningGame.tools.TerminalErrorMessage;


// Java packages
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.sound.sampled.Clip;


public class Ice extends BaseTopDownScroller {
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Ice(LearningGame lg, Runnable r, long timeOut) {
       super(lg, r, timeOut);
       scrollSpeed = 1;
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the background image.
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Ice.png",
                                                       WORKING_DIR + "img\\sprites\\Ice.png_background",
                                                       53, 0,         // startX, startY
                                                       103, 50,        // endX, endY
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
                LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Ice.png",
                                                    WORKING_DIR + "img\\sprites\\Ice.png_obstacle",
                                                    21, 0,       // startX, startY
                                                    53, 22,     // endX, endY
                                                    32, 22)[0]  // sizeX, sizeY
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
                    LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Ice.png",
                                                        WORKING_DIR + "img\\sprites\\Ice.png_collectable",
                                                        0, 0,    // startX, startY
                                                        9, 72,   // endX, endY
                                                        9, 9)[0] // sizeX, sizeY
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
            return LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Ice.png",
                                                       WORKING_DIR + "img\\sprites\\Ice.png_player",
                                                       9, 0,      // startX, startY
                                                       21, 13,     // endX, endY
                                                       12, 13)[0]; // sizeX, sizeY
            
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
        return 100;
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
        return 80;
    }
    
    /* 
     * @return the music file for when the player has hit the given type obstacle.
     */
    @Override
    protected Clip getDamagedClip(int type) {
        return PlayMusic.createClip(WORKING_DIR + "music\\sfx\\oil_slip_sfx.wav");
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
        PlayMusic.setVolume(clip, 0);
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
        return "Skate over the ice <br> Use the left and right arrow keys"
            + " to avoid the holes <br> and collect the coins!";
    }
    
    /* 
     * This function is called to damage the player.
     *//*
    @Override
    protected void damage() {
        //super.damage(); // only prints debug text
    }
    
    /* 
     * This function is called when a collectable was picked up.
     *//*
    @Override
    protected void collectedCollectable() {
        //super.collectedCollectable(); // only prints debug text
    }
    
    /* 
     * Whether to despawn an obstacle when it hits the player
     */
    @Override
    protected boolean despawnObstacle() {
        return false;
    }
    
    /* 
     * Whether a moving background should be drawn.
     */
    @Override
    protected boolean moveBackground() {
        return true;
    }
    
}