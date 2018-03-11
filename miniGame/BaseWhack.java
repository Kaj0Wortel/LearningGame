
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.Score;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.ImageTools;
import learningGame.tools.Key;
import learningGame.tools.LoadImages2;
import learningGame.tools.ModCursors;


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

import java.io.IOException;

import javax.swing.JPanel;


abstract public class BaseWhack extends MiniGame {
    // The whacks
    protected Whack[][] whacks;
    
    // The hammer
    protected Hammer hammer;
    
    // The chance that a whackable spawns in spawns / sec
    protected double spawnChance = 0.5;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public BaseWhack(LearningGame lg, Runnable r, long timeOut) {
       super(lg, r, timeOut);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Hammer class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Hammer extends JPanel {
        final private static int NOTHING = 0;
        final private static int WHACKING = 1;
        final private static int WAITING = 2;
        
        // The current state
        private int state = NOTHING;
        
        // The image to show
        private int curHammerImageNum = 0;
        
        // The time stamp (in ms) of when the hammer started to swing.
        private long swingStartedTime = 0L;
        // The time (in ms) it takes for a hammer to fully swing.
        private int moveTime = 0;
        // The time (in ms) it takes before the hammer is back to it's initial position.
        private int waitTime = 0;
        
        /* ------------------------------------------------------------------------------------------------------------
         * Hammer constructor
         * ------------------------------------------------------------------------------------------------------------
         */
        public Hammer() {
            super(null);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
        }
        
        /* 
         * This method swings the hammer.
         */
        public boolean whack(int moveTime, int waitTime, Long timeStamp) {
            if (state == NOTHING) {
                swingStartedTime = timeStamp;
                this.moveTime = moveTime;
                this.waitTime = waitTime;
                state = WHACKING;
                
                return true;
                
            } else {
                return false;
            }
        }
        
        public boolean canWhack() {
            return state == NOTHING;
        }
        
        /* 
         * This method updates the hammer.
         */
        public void update(long timeStamp) {
            long delta = timeStamp - swingStartedTime;
            
            if (state == WHACKING) {
                BufferedImage[] hammerSheet = getHammerSheet();
                
                if (hammerSheet != null) {
                    if (delta > moveTime * ((double) curHammerImageNum + 1.0) / hammerSheet.length) {
                        // Increase the image counter.
                        // If the end of the hammerSheet has been reached, set the state to {@code WAITING}.
                        if (++curHammerImageNum >= hammerSheet.length - 1) {
                            curHammerImageNum = hammerSheet.length - 1;
                            state = WAITING;
                        }
                        
                        //BaseWhack.this.repaint();
                    }
                }
                
            } else if (state == WAITING) {
                // If the wait time has elapsed, set the state to {@code NOTHING}
                if (delta > moveTime + waitTime) {
                    state = NOTHING;
                    curHammerImageNum = 0;
                    BaseWhack.this.repaint();
                }
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            BufferedImage[] hammerSheet = getHammerSheet();
            
            // If there is nothing to draw, return immediately.
            if (hammerSheet == null || hammerSheet.length <= 0) return;
            
            int draw = -1;
            if (state == NOTHING) {
                if (hammerSheet[0] != null) draw = 0;
                
            } else if (state == WHACKING) {
                if (hammerSheet[curHammerImageNum] != null) draw = curHammerImageNum;
                
            } else if (state == WAITING) {
                if (hammerSheet[hammerSheet.length - 1] != null) draw = hammerSheet.length - 1;
            }
            
            if (draw >= 0) {
                Graphics2D g2d = (Graphics2D) g;
                
                // Scale the graphics
                g2d.scale(getWidth()  / hammerSheet[draw].getWidth(),
                          getHeight() / hammerSheet[draw].getHeight());
                
                // Draw the image
                g2d.drawImage(hammerSheet[draw], 0, 0, null);
            }
        }
        
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Whack class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Whack extends JPanel {
        final private static int NOTHING = 0;    // No changes, not whackable.
        final private static int GOING_UP = 1;   // Whackable going up, whackable.
        final private static int OUT = 2;        // Whackable is out, whackable.
        final private static int GOING_DOWN = 3; // Whackable is going down, whackable.
        final private static int WHACKED = 4;    // Whackable has been wacked, not whackable.
        
        // The current state
        private int state = NOTHING;
        
        // The image to show
        private int curWhackImageNum = 0;
        
        // The time stamp (in ms) of when the whackable showed up
        private long whackableShownTime = 0L;
        // The time (in ms) it takes for a whackable to fully appear.
        private int moveTime = 0;
        // The time (in ms) it takes before a surfaced whackable to start diappearing again.
        private int waitTime= 0;
        
        /* ------------------------------------------------------------------------------------------------------------
         * Whack constructor
         * ------------------------------------------------------------------------------------------------------------
         */
        public Whack() {
            super(null);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
        }
        
        /* 
         * Shows the whackable.
         * First let the whackable appear in {@code moveTime} ms, then stay there for {@code stayTime} ms,
         * and finally disappear in {@code moveTime} ms.
         */
        public void showWhackable(int moveTime, int stayTime, long timeStamp) {
            if (state == NOTHING) {
                whackableShownTime = timeStamp;
                this.moveTime = moveTime;
                this.waitTime = stayTime;
                state = GOING_UP;
            }
        }
        
        /* 
         * Whacks the whackable iff the whackable is shown.
         * @return true iff the whackable can be whacked. False otherwise.
         */
        public boolean whack(long timeStamp) {
            if (state == GOING_UP || state == OUT || state == GOING_DOWN) {
                whackableShownTime = timeStamp;
                state = WHACKED;
                return true;
            }
            
            return false;
        }
        
        /* 
         * Update function.
         * All timed stuff goes in here.
         */
        public void update(long timeStamp) {
            long delta = timeStamp - whackableShownTime;
            BufferedImage[] whackSheet = getWhackSheet();
            
            // If there is nothing to draw, return immediately.
            if (whackSheet == null) return;
            
            // Update the shown image
            if (state == GOING_UP) {
                if (delta > moveTime * (curWhackImageNum + 1.0) / whackSheet.length) {
                    // Increase the image counter.
                    // If the end of the whackSheet has been reached, set the state to {@code OUT}.
                    if (++curWhackImageNum >= whackSheet.length - 1) {
                        curWhackImageNum = whackSheet.length - 1;
                        state = OUT;
                    }
                    
                    BaseWhack.this.repaint();
                }
                
            } else if (state == OUT) {
                if (delta - moveTime > waitTime) {
                    // If the surface time has elapsed, set the state to {@code GOING_DOWN}
                    state = GOING_DOWN;
                }
                
            } else if (state == GOING_DOWN) {
                if (delta - moveTime - waitTime > moveTime / whackSheet.length *
                    Math.abs((curWhackImageNum) - whackSheet.length)) {
                    
                    // Decrease the image counter.
                    // If the begin of the whackSheet has been reached, set the state to {@code NOTHING}.
                    if (--curWhackImageNum <= 0) {
                        curWhackImageNum = 0;
                        state = NOTHING;
                    }
                    
                    BaseWhack.this.repaint();
                }
                
            } else if (state == WHACKED) {
                if (delta > 750) {
                    state = NOTHING;
                    BaseWhack.this.repaint();
                }
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            BufferedImage whacked = getWhackedImage();
            BufferedImage[] whackSheet = getWhackSheet();
            
            Graphics2D g2d = (Graphics2D) g;
            
            if (state == WHACKED) {
                if (whacked != null) {
                    // Scale the graphics for the image
                    g2d.scale(getWidth()  / whacked.getWidth(),
                              getHeight() / whacked.getHeight());
                    
                    // Draw image
                    g2d.drawImage(whacked, 0, 0, null);
                }
                
             } else if (whackSheet != null && whackSheet.length > 0) {
                 if (state == NOTHING) {
                     //if (whackSheet[0] != null) g.drawImage(whackSheet[0], 0, 0, null);
                     //if (whacked != null) g.drawImage(whacked, 0, 0, null);// tmp
                     
                 } else if (curWhackImageNum < whackSheet.length && whackSheet[curWhackImageNum] != null) {
                     // Scale the graphics for the image
                     g2d.scale(getWidth()  / whackSheet[curWhackImageNum].getWidth(),
                               getHeight() / whackSheet[curWhackImageNum].getHeight());
                     
                     // Draw image
                     g2d.drawImage(whackSheet[curWhackImageNum], 0, 0, null);
                 }
             }
        }
        
        /* 
         * @return the current state of the whack.
         */
        public int getState() {
            return state;
        }
        
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Mouse functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof Whack) {
            System.out.println("clicked");
            Whack whack = (Whack) e.getSource();
            
            if (hammer.canWhack() && 
                whack.whack(e.getWhen()) && 
                hammer.whack(175, 25, e.getWhen()))
            {
                PlayMusic.play(getWhackMusicFile());
                whackEvent(e.getWhen());
            }
            
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    final protected void createGUI() {
        // Retrieve the width and height to prevent in time changes.
        int width = getWidth();
        int height = getHeight();
        
        // Create the hammer
        hammer = new Hammer();
        this.add(hammer, 0);
        
        // Create the whacks
        int[] size = getFieldSize();
        whacks = new Whack[size[0]][size[1]];
        
        for (int i = 0; i < whacks.length; i++) {
            for (int j = 0; j < whacks[i].length; j++) {
                whacks[i][j] = new Whack();
                this.add(whacks[i][j], i + j*whacks[i].length + 1);
                whacks[i][j].addMouseListener(this);
            }
        }
        
        updateWhackBounds(width, height, calcWhackDim(width, height));
        resized(getWidth(), getHeight());
    }
    
    /* 
     * The update method.
     * 
     * @param keys the keys that were pressed since the previous update.
     * @param timeStamp the start of the update cycle.
     */
    @Override
    final public void update(Key[] keys, long timeStamp) {
        // Show new whackables
        showWhackables(timeStamp);
        
        // Update the whacks
        if (whacks != null) {
            for (int i = 0; i < whacks.length; i++) {
                if (whacks[i] == null) continue;
                
                for (int j = 0; j < whacks[i].length; j++) {
                    if (whacks[i][j] == null) continue;
                    
                    whacks[i][j].update(timeStamp);
                }
            }
        }
        
        // Update the hammer
        if (hammer != null) {
            try {
                int mouseOnScreenX = MouseInfo.getPointerInfo().getLocation().x;
                int mouseOnScreenY = MouseInfo.getPointerInfo().getLocation().y;
                int thisX = this.getLocationOnScreen().x;
                int thisY = this.getLocationOnScreen().y;
                Dimension hammerDim = calcHammerDim(getWidth(), getHeight());
                int dx = (int) (getHammerWidthAdjustmentFactor()  * hammerDim.getWidth());
                int dy = (int) (getHammerHeightAdjustmentFactor() * hammerDim.getHeight());
                
                hammer.setLocation(mouseOnScreenX - thisX + dx, mouseOnScreenY - thisY + dy);
                
            } catch (IllegalStateException e) {
                // This might occur when the window is going to or from full screen.
                // No action should be taken.
            }
            
            hammer.update(timeStamp);
        }
        
        repaint();
    }
    
    /* 
     * In this update method should be used for letting the whackables show.
     */
    protected void showWhackables(long timeStamp) {
        Random random = new Random();
        
        for (int i = 0; whacks != null && i < whacks.length; i++) {
            for (int j = 0; whacks[i] != null && j < whacks[i].length; j++) {
                if (whacks[i][j] != null) {
                    //System.out.println(spawnChance * LearningGame.FPS * whacks.length * whacks[i].length);
                    if (random.nextDouble() < 1.0 / (spawnChance * LearningGame.FPS * whacks.length * whacks[i].length)) {
                        whacks[i][j].showWhackable(500, 200, timeStamp);
                        //whacks[i][j].showWhackable(2000, 200, timeStamp);
                    }
                }
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
    public void resized(int width, int height) {
        updateWhackBounds(width, height, calcWhackDim(width, height));
        hammer.setSize(calcHammerDim(width, height));
        repaint();
    }
    
    /* 
     * Updates the bounds of the used Whacks.
     */
    protected void updateWhackBounds(int panelWidth, int panelHeight, Dimension whackDim) {
        for (int i = 0; i < whacks.length; i++) {
            for (int j = 0; j < whacks[i].length; j++) {
                whacks[i][j].setSize(whackDim);
                whacks[i][j].setLocation
                    ((int) ((i + 1) * panelWidth  / (whacks.length + 1) - 0.5*whackDim.getWidth()),
                     (int) ((j + 1) * panelHeight / (whacks[i].length + 1) - 0.5*whackDim.getHeight()));
            }
        }
    }
    
    /* 
     * @return the dimension of a Whack.
     */
    protected Dimension calcWhackDim(int newWidth, int newHeight) {
        return new Dimension((int) ((2.0/3.0) * newWidth / (whacks.length + 1)),
                             (int) ((2.0/3.0) * newHeight / (whacks[0].length + 1)));
    }
    
    /* 
     * @return the dimension of the Hammer.
     */
    protected Dimension calcHammerDim(int newWidth, int newHeight) {
        return new Dimension((int) ((2.0/3.0) * newWidth / (whacks.length + 1)),
                             (int) ((4.0/3.0) * newHeight / (whacks[0].length + 1)));
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
     * This method is always called when the MiniGame is about to shut down.
     * Only resets the mouse to it's default cursor.
     */
    @Override
    protected void cleanUp() {
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Abstract functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the size of the whack field such that int[] {width, height},
     * where width and height denote the number of whackables in resp.
     * the rows and columns.
     */
    abstract protected int[] getFieldSize();
    
    /* 
     * @return the width adjustment factor for the hammer image.
     *     0 means no adjustment, -1 means pushing the image to the left with it's height,
     *     and 1 means pushing the image to the right with it's height.
     */
    abstract protected double getHammerWidthAdjustmentFactor();
    
    /* 
     * @return the height adjustment factor for the hammer image.
     *     0 means no adjustment, -1 means pushing the image downwards with it's height,
     *     and 1 means pushing the image upwards with it's height.
     */
    abstract protected double getHammerHeightAdjustmentFactor();
    
    /* 
     * @return the image sheet for the whackable animation.
     */
    abstract protected BufferedImage[] getWhackSheet();
    
    /* 
     * @return the image for when the whackable has been whacked.
     */
    abstract protected BufferedImage getWhackedImage();
    
    /* 
     * @return the image sheet for the hammer.
     */
    abstract protected BufferedImage[] getHammerSheet();
    
    /* 
     * @return the location of the music file
     */
    abstract protected String getWhackMusicFile();
    
    /* 
     * This method is called when a whackable has been whacked.
     * 
     * @param timeStamp the time when the whackable has been whacked.
     *     Note that this is an event, so this part is NOT synchronized
     *     with the update thread.
     */
    abstract protected void whackEvent(long timeStamp);
    
}