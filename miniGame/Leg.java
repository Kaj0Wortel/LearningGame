
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.Score;
import learningGame.Word;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.LoadImages2;
import learningGame.tools.TerminalErrorMessage;
import learningGame.tools.matrix.Vec;

// Java packages
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.sound.sampled.Clip;


public class Leg extends BaseShave {
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Leg(LearningGame lg, Runnable r, long timeOut) {
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
    protected Dimension calcTrimmerDim(int newWidth, int newHeight) {
        return super.calcTrimmerDim(newWidth, newHeight);
    }
    
    /* 
     * @return the score of this miniGame
     * 
     * @param the word which has this MiniGame assoiated with it.
     * @param mistakes the number of wrong buttons that were pressed in the word screen.
     */
    @Override
    public Score getScore(Word word, int mistakes) {
        return new Score(50, 100, word, mistakes);
    }
    
    /* 
     * @return the gravity vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     * Note that a positive x coord creates gravity to the right,
     * while a positive y coord creates gravity downwards
     */
    @Override
    protected Vec getGravity() {
        return new Vec(0, 0.09);
    }
    
    /* 
     * @return the initial speed vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    @Override
    protected Vec getInitSpeed() {
        //
        double angle = Math.toRadians(random.nextDouble() * 120 + 30);
        double initSpeed = 0.03;
        
        return new Vec(Math.cos(angle) * initSpeed, -Math.sin(angle) * initSpeed);
    }
    
    /* 
     * @return the image used for the hair.
     */
    @Override
    protected BufferedImage getHairImage() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Leg.png",
                                                       WORKING_DIR + "img\\sprites\\Leg.png_hair",
                                                       50, 0,  // startX, startY
                                                       65, 3,  // endX, endY
                                                       15, 3)[0][0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            throw new TerminalErrorMessage("Hair image of class " + this.getClass() + " could not be loaded.");
        }
    }
    
    /* 
     * @return the image used for the shave object.
     */
    @Override
    protected BufferedImage[] getTrimmerSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Leg.png",
                                                       WORKING_DIR + "img\\sprites\\Leg.png_trimmer",
                                                       0, 0,      // startX, startY
                                                       8, 28,     // endX, endY
                                                       8, 14)[0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            throw new TerminalErrorMessage("Trimmer sheet images of class " + this.getClass() + " could not be loaded.");
        }
    }
    
    /*
     * @return the background image.
     */
    @Override
    protected BufferedImage getBackgroundImage() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(WORKING_DIR + "img\\sprites\\Leg.png",
                                                       WORKING_DIR + "img\\sprites\\Leg.png_leg",
                                                       8, 0,       // startX, startY
                                                       50, 43,     // endX, endY
                                                       42, 43)[0][0]; // sizeX, sizeY
            
        } catch (IOException | IllegalArgumentException e) {
            Log2.write(e);
            e.printStackTrace();
            throw new TerminalErrorMessage("Background sheet images of class " + this.getClass() + " could not be loaded.");
        }
    }
    
    /* 
     * @return the location of the hair patches on screen.
     * Each element must contain an array which contains two doubles
     *     which must each be between 0.0 and 1.0.
     */
    @Override
    protected double[][] getHairLoc() {
        return new double[][] {
            new double[] {0.5, 0.5},
                new double[] {0.55, 0.5},
                    new double[] {0.6, 0.5}
        };
    }
    
    /* 
     * @return the size of a hair patch on screen.
     * Each element must contain two doubles which must each be between 0.0 and 1.0.
     */
    @Override
    protected double[] getHairSize() {
        return new double[] {0.2, 0.04}; // image must be in ratio 5 : 1
    }
    
    
    /* 
     * @return the height adjustment factor for the hammer image.
     *     0 means no adjustment, -1 means pushing the image downwards with it's height,
     *     and 1 means pushing the image upwards with it's height.
     */
    @Override
    protected double getTrimmerWidthAdjustmentFactor() {
        return 0.0;
    }
    
    
    /* 
     * @return the width adjustment factor for the hammer image.
     *     0 means no adjustment, -1 means pushing the image to the left with it's height,
     *     and 1 means pushing the image to the right with it's height.
     */
    @Override
    protected double getTrimmerHeightAdjustmentFactor() {
        return 0.0;
    }
    
    /* 
     * @return the clip used to play the trimmer sound.
     */
    @Override
    protected Clip getTrimmerSoundClip() {
        Clip clip = PlayMusic.createClip(LearningGame.WORKING_DIR + "music\\sfx\\trimmer_sfx.wav");
        clip.loop(-1);
        return clip;
    }
    
    /* 
     * @return the text to be displayed for the instruction panel. Supports HTML.
     */
    @Override
    protected String getInstruction() {
        return "Shave the leg! <br> Move your mouse over the hair on the leg while pressing <br> the left mouse-button"
            + " in order to shave the leg";
    }
    
}
