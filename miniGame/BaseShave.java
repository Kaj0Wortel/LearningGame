
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;

import learningGame.log.Log2;

import learningGame.tools.Key;
import learningGame.tools.KeyDetector;
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

import javax.swing.JPanel;


abstract public class BaseShave extends MiniGame {
    protected Hair[] hair;
    
    // Whether mouse button 1 is pressed or not.
    protected boolean mouseButton1Pressed = false;
    
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
            this.setBackground(Color.RED);
        }
        
        /* 
         * Updates the current object.
         */
        public void update(long timeStamp) {
            if (isShaved && speed != null) {
                long delta = timeStamp - prevTimeStamp;
                speed.addVec(Vec.multiplyVec(delta / 1000.0, GRAVITY), true);
                setLocationRel(xRel + speed.x(), yRel + speed.y());
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
            xRel = x;
            yRel = y;
            widthRel = width;
            heightRel = height;
            
            if (xRel < -widthRel  || widthRel  > xRel ||
                yRel < -heightRel || heightRel > yRel) {
                BaseShave.this.detatchHair(this);
            }
            
            super.setBounds((int) ((x - width) * BaseShave.this.getWidth()),
                            (int) ((y - height) * BaseShave.this.getHeight()),
                            (int) (width * BaseShave.this.getWidth()),
                            (int) (height * BaseShave.this.getHeight()));
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
                g2d.scale(((double) getWidth()) / hair.getWidth(), ((double) getWidth()) / hair.getHeight());
                g2d.drawImage(hair, 0, 0, null);
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
            ((Hair) e.getSource()).shave(System.currentTimeMillis());
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseButton1Pressed = true;
            
            if (e.getSource() instanceof Hair) {
                ((Hair) e.getSource()).shave(System.currentTimeMillis());
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseButton1Pressed = false;
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * 
     */
    protected void detatchHair(Hair h) {
        h.removeMouseListener(this);
        remove(h);
        
        for (int i = 0; i < hair.length; i++) {
            if (hair[i] != null && hair[i].equals(h)) {
                System.out.println("deleted");
                hair[i] = null;
            }
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
    }
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    @Override
    public void resized(int width, int height) { }
    
    /* 
     * This method is invoked to create the GUI of the application.
     */
    @Override
    public void createGUI() {
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
            hair[i].addMouseListener(this);
        }
    }
    
    /* 
     * This method is always called when the MiniGame is about to shut down.
     */
    @Override
    public void cleanUp() { }
    
    /* 
     * This method is invoked when the minigame is started.
     */
    @Override
    public void startMiniGame() { }
    
    
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
     * @return the image used for the shave object.
     */
    abstract protected BufferedImage getShaveItemImage();
    
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
}

