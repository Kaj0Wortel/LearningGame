
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


// Java packages
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.swing.JPanel;


public abstract class BaseWhack extends MiniGame {
    final private BufferedImage[] originalWhackSheet;
    final private BufferedImage originalWhacked;
    private BufferedImage[] whackSheet;
    private BufferedImage whacked;
    private Thread imageCreateThread;
    
    protected Whack[][] whacks = new Whack[5][5];
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public BaseWhack(LearningGame lg, Runnable r) {
        super(lg, r);
        
        // Create images
        originalWhackSheet = getWhackSheet();
        originalWhacked = getWhackedImage();
        
        whackSheet = new BufferedImage[originalWhackSheet.length];
        for (int i = 0; i < originalWhackSheet.length; i++) {
            whackSheet[i] = ImageTools.imageDeepCopy(originalWhackSheet[i]);
        }
        
        whacked = ImageTools.imageDeepCopy(originalWhacked);
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
        // The tiem (in ms) it takes before a surfaced whackable to start diappearing again.
        private int stayTime = 0;
        
        /* ------------------------------------------------------------------------------------------------------------
         * Whack constructor
         * ------------------------------------------------------------------------------------------------------------
         */
        public Whack(int width, int height) {
            super(null);
            setSize(width, height);
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
                this.stayTime = stayTime;
                state = GOING_UP;
            }
        }
        
        /* 
         * Whacks the whackable iff the whackable is shown.
         * @return true iff the whackable can be whacked. False otherwise.
         */
        public boolean whack() {
            if (state == GOING_UP || state == OUT || state == GOING_DOWN) {
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
            // Update the shown image
            if (state == GOING_UP) {
                if (delta > moveTime * (curWhackImageNum + 1.0) / whackSheet.length) {
                    // Increase the image counter.
                    // If the end of the whackSheet has been reached, set the state to {@code OUT}.
                    if (++curWhackImageNum >= whackSheet.length - 1) {
                        curWhackImageNum = whackSheet.length - 1;
                        state = OUT;
                    }
                    
                    repaint();
                }
                
            } else if (state == OUT) {
                if (delta - moveTime > stayTime) {
                    state = GOING_DOWN;
                }
                
            } else if (state == GOING_DOWN) {
                if (delta - moveTime - stayTime > moveTime / whackSheet.length *
                    Math.abs((curWhackImageNum) - whackSheet.length)) {
                    
                    // Decrease the image counter.
                    // If the begin of the whackSheet has been reached, set the state to {@code NOTHING}.
                    if (--curWhackImageNum <= 0) {
                        curWhackImageNum = 0;
                        state = NOTHING;
                    }
                    
                    repaint();
                }
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw the image.
             if (state == WHACKED) {
                if (whacked != null) g.drawImage(whacked, 0, 0, null);
                
             } else if (whackSheet != null && whackSheet.length > 0) {
                 if (state == NOTHING) {
                     //if (whackSheet[0] != null) g.drawImage(whackSheet[0], 0, 0, null);
                     if (whacked != null) g.drawImage(whacked, 0, 0, null);// tmp
                     
                 } else if (curWhackImageNum < whackSheet.length && whackSheet[curWhackImageNum] != null) {
                     g.drawImage(whackSheet[curWhackImageNum], 0, 0, null);
                 }
             }
        }
        
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Hammer class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Hammer extends JPanel {
        
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Mouse functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof Whack) {
            System.out.println("clicked");
            ((Whack) e.getSource()).showWhackable(500, 200, System.currentTimeMillis()); // tmp
            //((Whack) e.getSource()).whack();
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    final protected void createGUI() {
        int width = getWidth();
        int height = getHeight();
        BufferedImage[] whackSheet = getWhackSheet();
        
        for (int i = 0; i < whacks.length; i++) {
            for (int j = 0; j < whacks[i].length; j++) {
                whacks[i][j] = new Whack(width, height);
                this.add(whacks[i][j]);
                whacks[i][j].addMouseListener(this);
            }
        }
        
        updateWhackBounds(width, height, calcWhackWidth(width, height), calcWhackHeight(width, height));
        resized(getWidth(), getHeight());
    }
    
    /* 
     * The update method.
     * 
     * @param keys the keys that were pressed since the previous update.
     * @param timeStamp the start of the update cycle.
     */
    @Override
    public void update(Key[] keys, long timeStamp) {
        if (whacks == null) return;
        
        for (int i = 0; i < whacks.length; i++) {
            if (whacks[i] == null) continue;
            
            for (int j = 0; j < whacks[i].length; j++) {
                if (whacks[i][j] == null) continue;
                
                whacks[i][j].update(timeStamp);
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
        int whackWidth = calcWhackWidth(width, height);
        int whackHeight = calcWhackHeight(width, height);
        
        if (whacks != null) {
            if (whackWidth > 0 && whackHeight > 0) {
                if (imageCreateThread != null) {
                    imageCreateThread.interrupt();
                }
                /*
                imageCreateThread = new Thread("image create thread " + this.getClass().toString()) {
                    @Override
                    public void run() {
                        resizeWhackImage(whackWidth, whackHeight);
                        imageCreateThread = null;
                    }
                };*/
                resizeWhackImage(whackWidth, whackHeight);
                
                //imageCreateThread.start();
                updateWhackBounds(width, height, whackWidth, whackHeight);
            }
        }
    }
    
    /* 
     * Updates the bounds of the used Whacks.
     */
    protected void updateWhackBounds(int panelWidth, int panelHeight, int whackWidth, int whackHeight) {
        for (int i = 0; i < whacks.length; i++) {
            for (int j = 0; j < whacks[i].length; j++) {
                whacks[i][j].setSize(whackWidth, whackHeight);
                whacks[i][j].setLocation((int) ((i + 1) * panelWidth  /(whacks.length    + 1) - 0.5*whackWidth),
                                         (int) ((j + 1) * panelHeight / (int) (whacks[i].length + 1) - 0.5*whackHeight));
            }
        }
    }
    
    /* 
     * @return the width of each Whack, knowing the new width and height.
     */
    protected int calcWhackWidth(int newWidth, int newHeight) {
        return (int) (newWidth / (1.5*whacks.length + 1));
    }
    
    /* 
     * @return the height of each Whack, knowing the new width and height.
     */
    protected int calcWhackHeight(int newWidth, int newHeight) {
        return (int) (newHeight / (1.5*whacks[0].length + 1));
    }
    
    /* 
     * Creates a thread that resizes the images for the Whack class.
     */
    protected void resizeWhackImage(int width, int height) {
        for (int i = 0; i < originalWhackSheet.length; i++) {
            whackSheet[i] = ImageTools.toBufferedImage
                (originalWhackSheet[i]
                     .getScaledInstance(width, height, Image.SCALE_SMOOTH)
                );
        }
        
        whacked = ImageTools.toBufferedImage
            (originalWhacked
                 .getScaledInstance(width, height, Image.SCALE_SMOOTH)
            );
    }
    
    /* 
     * @return the image sheet for the whackable animation.
     */
    protected abstract BufferedImage[] getWhackSheet();
    
    /* 
     * @return the image for when the whackable has been whacked.
     */
    protected abstract BufferedImage getWhackedImage();
    
    /* 
     * @return the image sheet for the hammer.
     */
    protected abstract BufferedImage[] getHammerSheet();
    
}