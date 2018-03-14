
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Key;
import learningGame.tools.KeyDetector;
import learningGame.tools.ModCursors;
import learningGame.tools.TerminalErrorMessage;
import learningGame.tools.matrix.Vec;


// Java packages
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import java.util.Random;

import javax.sound.sampled.Clip;

import javax.swing.JPanel;


abstract public class BaseShave extends MiniGame {
    protected Hair[] hair;
    protected Trimmer trimmer;
    
    // Whether mouse button 1 is pressed or not.
    protected boolean mouseButton1Pressed = false;
    
    // The random object of this object.
    protected Random random = new Random();
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public BaseShave(LearningGame lg, Runnable r, long timeOut) {
        super(lg, r, timeOut);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Hair class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Hair extends JPanel {
        final private Vec GRAVITY = (Vec) getGravity();
        
        // Whether the patch has been shaved or not.
        private boolean isShaved = false;
        
        // The current speed of the patch.
        private Vec speed = getInitSpeed();
        
        // The relative location and size of the patch.
        // The patch is fully visible all variables are between 0.0 and 1.0.
        private double xRel;
        private double yRel;
        private double widthRel;
        private double heightRel;
        
        // The previous time stamp of the update method
        long prevTimeStamp;
        
        /* ------------------------------------------------------------------------------------------------------------
         * Hair constructor
         * ------------------------------------------------------------------------------------------------------------
         */
        public Hair(double x, double y, double width,  double height) {
            super(null);
            
            setBoundsRel(x, y, width, height);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
        }
        
        /* 
         * Updates the current object.
         */
        public void update(long timeStamp) {
            if (isShaved && speed != null) {
                long delta = timeStamp - prevTimeStamp;
                speed.addVec(Vec.multiplyVec(delta / 1000.0, GRAVITY), true);
                setLocationRel(xRel + speed.x(), yRel + speed.y());
                
            } else {
                setLocationRel(xRel, yRel);
            }
            
            prevTimeStamp = timeStamp;
        }
        
        /* 
         * Shaves the hair and let's it jump off with the given angle and speed.
         * 
         * @param angle the angle in radians under which the hair is shoot off.
         */
        public void shave(long timeStamp) {
            if (!isShaved) {
                isShaved = true;
            }
        }
        
        /* 
         * Sets the relative location of the object.
         * For the object to be completely visible, all values must be between 0.0 and 1.0.
         * Uses the function setBoundsRel(double, double, double, double) for futher handeling.
         */
        public void setLocationRel(double x, double y) {
            setBoundsRel(x, y, widthRel, heightRel);
        }
        
        /* 
         * Sets the relative bounds of the object.
         * For the object to be completely visible, all values must be between 0.0 and 1.0.
         */
        public void setBoundsRel(double x, double y, double width, double height) {
            boolean resized = (getWidth() != width || getHeight() != height);
            boolean relocated = (getX() != x || getY() != y);
            
            
            if (resized || relocated) {
                xRel = x;
                yRel = y;
                widthRel = width;
                heightRel = height;
                
                BaseShave base = BaseShave.this;
                
                super.setBounds((int) ((x - width) * base.getWidth()),
                                (int) ((y - height) * base.getHeight()),
                                (int) (width * base.getWidth()),
                                (int) (height * base.getHeight()));
                
                if (getX() - getWidth( ) < 0 || getX() > base.getWidth() ||
                    getY() - getHeight() < 0 || getY() > base.getHeight()) {
                    BaseShave.this.detatchHair(this);
                }
            }
        }
        
        @Override
        @Deprecated
        public void setBounds(int x, int y, int width, int height) {
            throw new UnsupportedOperationException
                ("This function is not used. Use setBoundsRel(double, double, double, double) instead.");
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            BufferedImage hair = getHairImage();
            if (hair != null) {
                g2d.scale(((double) getWidth()) / hair.getWidth(),
                          ((double) getHeight()) / hair.getHeight());
                g2d.drawImage(hair, 0, 0, null);
            }
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Trimmer class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Trimmer extends JPanel {
        final private static int NOTHING = 0;
        final private static int TRIMMING = 1;
        
        // The current state
        private int state = NOTHING;
        
        // The image to show
        private int curTrimmerImageNum = 0;
        
        // The time stamp (in ms) of when the trimmer started to swing.
        private long trimStartedTime = 0L;
        // The time (in ms) it takes to cycle through all trimmer images.
        private int moveTime = 100;
        
        /* ------------------------------------------------------------------------------------------------------------
         * Trimmer constructor
         * ------------------------------------------------------------------------------------------------------------
         */
        public Trimmer() {
            super(null);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
        }
        
        /* 
         * Sets the state of the trimmer.
         */
        public void setTrim(long timeStamp, int state) {
            if (this.state != state) {
                trimStartedTime = timeStamp;
                this.state = state;
            }
        }
        
        /* 
         * This method updates the trimmer.
         */
        public void update(long timeStamp) {
            long delta = timeStamp - trimStartedTime;
            
            if (state == TRIMMING) {
                BufferedImage[] trimmerSheet = getTrimmerSheet();
                if (trimmerSheet != null && 
                    delta > moveTime * ((double) curTrimmerImageNum + 1.0) / trimmerSheet.length) {
                    // Increase the image counter. If the end of the trimmerSheet has been reached,
                    // set the counter to 0 and reset the timer.
                    if (++curTrimmerImageNum > trimmerSheet.length - 1) {
                        curTrimmerImageNum = 0;
                        trimStartedTime = timeStamp;
                    }
                }
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            BufferedImage[] trimmerSheet = getTrimmerSheet();
            
            // If there is nothing to draw, return immediately.
            if (trimmerSheet == null || trimmerSheet.length <= 0) return;
            
            int draw = -1;
            if (state == NOTHING) {
                if (trimmerSheet[0] != null) draw = 0;
                
            } else if (state == TRIMMING) {
                if (trimmerSheet[curTrimmerImageNum] != null) draw = curTrimmerImageNum;
            }
            
            if (draw >= 0) {
                Graphics2D g2d = (Graphics2D) g;
                
                // Scale the graphics
                g2d.scale(((double) getWidth())  / trimmerSheet[draw].getWidth(),
                          ((double) getHeight()) / trimmerSheet[draw].getHeight());
                
                // Draw the image
                g2d.drawImage(trimmerSheet[draw], 0, 0, null);
            }
        }
        
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Mouse functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof Hair && mouseButton1Pressed) {
            Hair hair = (Hair) e.getSource();
            if (hair != null) hair.shave(e.getWhen());
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseButton1Pressed = true;
            if (trimmer != null) trimmer.setTrim(e.getWhen(), Trimmer.TRIMMING);
            PlayMusic.play(getTrimmerSoundClip());
            
            if (e.getSource() instanceof Hair) {
                Hair hair = (Hair) e.getSource();
                if (hair != null) hair.shave(e.getWhen());
            }
        }
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseButton1Pressed = false;
            if (trimmer != null) trimmer.setTrim(e.getWhen(), Trimmer.NOTHING);
            PlayMusic.stop(getTrimmerSoundClip());
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Registers the hair as detatched (fallen of the screen).
     */
    protected void detatchHair(Hair h) {
        if (h == null) return;
        h.removeMouseListener(this);
        remove(h);
        
        boolean allRemoved = true;
        for (int i = 0; i < hair.length; i++) {
            if (hair!= null && hair[i] != null && hair[i].equals(h)) {
                hair[i] = null;
                
            } else if (hair[i] != null) {
                allRemoved = false;
            }
        }
        
        if (allRemoved) finish();
    }
    
    /* 
     * This method is invoked to create the GUI of the application.
     */
    @Override
    final public void createGUI() {
        trimmer = new Trimmer();
        trimmer.setSize(calcTrimmerDim(getWidth(), getHeight()));
        this.add(trimmer);
        
        double[] size = getHairSize();
        if (size == null)
            throw new TerminalErrorMessage("Invallid method implementation!",
                                           "Cannot assign null as size to a Hair patch!",
                                           "Cause: BaseShave.getHairSize() is wrongly implemented.");
        
        double[][] hairLoc = getHairLoc();
        hair = new Hair[hairLoc.length];
        
        for (int i = 0; i < hairLoc.length; i++) {
            double[] loc = hairLoc[i];
            
            if (loc == null) {
                Log2.write("Attempted to initialize Hair patch at null location.", Log2.WARNING);
                continue;
            }
            
            if (loc.length != 2) {
                Log2.write("Attempted to initialize Hair patch at invalid location. Location had length: "
                               + loc.length, Log2.WARNING);
            }
            
            hair[i] = new Hair(loc[0], loc[1], size[0], size[1]);
            this.add(hair[i], i);
        }
    }
    
    /* 
     * This method is invoked when the minigame is started.
     */
    @Override
    protected void startMiniGame() {
        // Set the empty cursor
        lg.setCursor(ModCursors.EMPTY_CURSOR);
    }
    
    /* 
     * This method is invoked when the listeners of the sub components should be added.
     */
    @Override
    protected void addSubListeners() {
        for (int i = 0; hair != null && i < hair.length; i++) {
            if (hair[i] != null) hair[i].addMouseListener(this);
        }
    }
    
    /* 
     * This method is invoked when the listeners of the sub components should be removed.
     */
    @Override
    protected void removeSubListeners() {
        for (int i = 0; hair != null && i < hair.length; i++) {
            if (hair[i] != null) hair[i].removeMouseListener(this);
        }
    }
    
    /* 
     * Updates the frame for the minigame.
     */
    @Override
    public void update(Key[] keys, long timeStamp) {
        if (hair != null) {
            for (int i = 0; i < hair.length; i++) {
                if (hair[i] != null) hair[i].update(timeStamp);
            }
        }
        
        // Update the trimmer
        if (trimmer != null) {
            try {
                int mouseOnScreenX = MouseInfo.getPointerInfo().getLocation().x;
                int mouseOnScreenY = MouseInfo.getPointerInfo().getLocation().y;
                int thisX = this.getLocationOnScreen().x;
                int thisY = this.getLocationOnScreen().y;
                Dimension trimmerDim = calcTrimmerDim(getWidth(), getHeight());
                int dx = (int) (getTrimmerWidthAdjustmentFactor()  * trimmerDim.getWidth());
                int dy = (int) (getTrimmerHeightAdjustmentFactor() * trimmerDim.getHeight());
                
                trimmer.setLocation(mouseOnScreenX - thisX + dx, mouseOnScreenY - thisY + dy);
                trimmer.setSize(trimmerDim);
                
            } catch (IllegalStateException e) {
                // This might occur when the window is going to or from full screen.
                // No action should be taken.
            }
            
            trimmer.update(timeStamp);
        }
    }
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    @Override
    public void resized(int width, int height) {
        if (trimmer != null) trimmer.setSize(calcTrimmerDim(width, height));
    }
    
    /* 
     * @return the dimension of the Trimmer.
     */
    protected Dimension calcTrimmerDim(int newWidth, int newHeight) {
        return new Dimension((int) (0.1 * newWidth),
                             (int) (0.2 * newHeight));
    }
    
    /* 
     * This method is always called when the MiniGame is about to shut down.
     */
    @Override
    protected void cleanUp() {
        // Set the default cursor.
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Abstract functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the gravity vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    abstract protected Vec getGravity();
    
    /* 
     * @return the initial speed vector used for the hair.
     * This method is invoked exactly once for each Hair object.
     * Measured in screen size per second.
     */
    abstract protected Vec getInitSpeed();
    
    /* 
     * @return the image used for the hair.
     */
    abstract protected BufferedImage getHairImage();
    
    /* 
     * @return the image used for the trimmer object.
     */
    abstract protected BufferedImage[] getTrimmerSheet();
    
    /* 
     * @return the location of the hair patches on screen.
     * Each element must contain an array which contains two doubles
     *     which must each be between 0.0 and 1.0.
     */
    abstract protected double[][] getHairLoc();
    
    /* 
     * @return the size of a hair patch on screen.
     * Each element must contain two doubles which must each be between 0.0 and 1.0.
     */
    abstract protected double[] getHairSize();
    
    /* 
     * @return the height adjustment factor for the hammer image.
     *     0 means no adjustment, -1 means pushing the image downwards with it's height,
     *     and 1 means pushing the image upwards with it's height.
     */
    abstract protected double getTrimmerWidthAdjustmentFactor();
    
    
    /* 
     * @return the width adjustment factor for the hammer image.
     *     0 means no adjustment, -1 means pushing the image to the left with it's height,
     *     and 1 means pushing the image to the right with it's height.
     */
    abstract protected double getTrimmerHeightAdjustmentFactor();
    
    /* 
     * @return the clip used to play the trimmer sound.
     */
    abstract protected Clip getTrimmerSoundClip();
}

