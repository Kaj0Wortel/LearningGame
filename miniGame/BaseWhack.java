
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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import java.util.Random;

import java.io.IOException;

import javax.swing.JPanel;
//import javax.swing.JLayeredPane;


public abstract class BaseWhack extends MiniGame {
    // The original images
    final private BufferedImage[] originalHammerSheet;
    final private BufferedImage[] originalWhackSheet;
    final private BufferedImage originalWhacked;
    
    // The resized images
    private BufferedImage[] hammerSheet;
    private BufferedImage[] whackSheet;
    private BufferedImage whacked;
    
    // The image creation thread
    private Thread imageCreateThread;
    
    // The whacks
    protected Whack[][] whacks;
    
    // The hammer
    protected Hammer hammer;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public BaseWhack(LearningGame lg, Runnable r) {
        super(lg, r);
        
        // Set the empty cursor
        lg.setCursor(ModCursors.EMPTY_CURSOR);
        
        // Fetch images images
        originalHammerSheet = getHammerSheet();
        originalWhackSheet = getWhackSheet();
        originalWhacked = getWhackedImage();
        
        // Whacks
        whackSheet = new BufferedImage[originalWhackSheet.length];
        for (int i = 0; i < originalWhackSheet.length; i++) {
            whackSheet[i] = ImageTools.imageDeepCopy(originalWhackSheet[i]);
        }
        
        whacked = ImageTools.imageDeepCopy(originalWhacked);
        
        // Hammer
        hammerSheet = new BufferedImage[originalHammerSheet.length];
        for (int i = 0; i < originalHammerSheet.length; i++) {
            hammerSheet[i] = ImageTools.imageDeepCopy(originalHammerSheet[i]);
        }
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
            super();
            setLayout(null);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
        }
        
        /* 
         * This method swings the hammer.
         */
        public boolean whack(int moveTime, int waitTime, long timeStamp) {
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
                if (delta > moveTime * ((double) curHammerImageNum + 1.0) / hammerSheet.length) {
                    // Increase the image counter.
                    // If the end of the hammerSheet has been reached, set the state to {@code WAITING}.
                    if (++curHammerImageNum >= hammerSheet.length - 1) {
                        curHammerImageNum = hammerSheet.length - 1;
                        state = WAITING;
                    }
                    
                    BaseWhack.this.repaint();
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
            
            // Draw the image.
            if (hammerSheet != null && whackSheet.length > 0) {
                if (state == NOTHING) {
                    if (hammerSheet[0] != null)
                        g.drawImage(hammerSheet[0], 0, 0, null);
                    
                } else if (state == WHACKING) {
                    if (hammerSheet[curHammerImageNum] != null)
                        g.drawImage(hammerSheet[curHammerImageNum], 0, 0, null);
                    
                } else if (state == WAITING) {
                    if (hammerSheet[hammerSheet.length - 1] != null)
                        g.drawImage(hammerSheet[hammerSheet.length - 1], 0, 0, null);
                }
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
            
            // Draw the image.
             if (state == WHACKED) {
                if (whacked != null) g.drawImage(whacked, 0, 0, null);
                
             } else if (whackSheet != null && whackSheet.length > 0) {
                 if (state == NOTHING) {
                     //if (whackSheet[0] != null) g.drawImage(whackSheet[0], 0, 0, null);
                     //if (whacked != null) g.drawImage(whacked, 0, 0, null);// tmp
                     
                 } else if (curWhackImageNum < whackSheet.length && whackSheet[curWhackImageNum] != null) {
                     g.drawImage(whackSheet[curWhackImageNum], 0, 0, null);
                 }
             }
        }
        
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
            
            // tmp
            /*
            if (hammer.canWhack()) {
                whack.showWhackable(500, 200, e.getWhen());
            }*/
            
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
    final public void update(Key[] keys, long timeStamp) {
        whackUpdate(timeStamp);
        
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
                int dx = (int) ((1.0/3.0) * calcHammerWidth(getWidth(), getHeight()));
                int dy = (int) ((2.0/3.0) * calcHammerHeight(getWidth(), getHeight()));
                
                hammer.setLocation(mouseOnScreenX - thisX - dx, mouseOnScreenY - thisY - dy);
                
            } catch (IllegalStateException e) {
                // This might occur when the window is going to or from full screen.
                // No action should be taken.
            }
            
            hammer.update(timeStamp);
        }
    }
    
    // The chance that a whackable spawns in spawns / sec
    protected double spawnChance = 0.5;
    /* 
     * In this update method should be used for letting the whackables show.
     */
    protected void whackUpdate(long timeStamp) {
        Random random = new Random();
        
        for (int i = 0; whacks != null && i < whacks.length; i++) {
            for (int j = 0; whacks[i] != null && j < whacks[i].length; j++) {
                if (whacks[i][j] != null) {
                    //System.out.println(spawnChance * LearningGame.FPS * whacks.length * whacks[i].length);
                    if (random.nextDouble() < 1.0 / (spawnChance * LearningGame.FPS * whacks.length * whacks[i].length)) {
                        whacks[i][j].showWhackable(500, 200, timeStamp);
                        repaint();
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
        int whackWidth = calcWhackWidth(width, height);
        int whackHeight = calcWhackHeight(width, height);
        int hammerWidth = calcHammerWidth(width, height);
        int hammerHeight = calcHammerHeight(width, height);
        
        if (whacks != null) {
            if (whackWidth > 0 && whackHeight > 0) {
                if (imageCreateThread != null) {
                    imageCreateThread.interrupt();
                }
                
                imageCreateThread = new Thread("image create thread " + this.getClass().toString()) {
                    @Override
                    public void run() {
                        // Whack
                        resizeWhackImages(whackWidth, whackHeight);
                        updateWhackBounds(width, height, whackWidth, whackHeight);
                        
                        // Hammer
                        resizeHammerImages(hammerWidth, hammerHeight);
                        hammer.setSize(hammerWidth, hammerHeight);
                        
                        // Set image thread to null.
                        imageCreateThread = null;
                    }
                };
                
                imageCreateThread.start();
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
        return (int) ((2.0/3.0) * newWidth / (whacks.length + 1));
    }
    
    /* 
     * @return the height of each Whack, knowing the new width and height.
     */
    protected int calcWhackHeight(int newWidth, int newHeight) {
        return (int) ((2.0/3.0) * newHeight / (whacks[0].length + 1));
    }
    
    /* 
     * @return the width of each Whack, knowing the new width and height.
     */
    protected int calcHammerWidth(int newWidth, int newHeight) {
        return (int) ((2.0/3.0) * newWidth / (whacks.length + 1));
    }
    
    /* 
     * @return the height of each Whack, knowing the new width and height.
     */
    protected int calcHammerHeight(int newWidth, int newHeight) {
        return (int) ((2.0/3.0) * newHeight / (whacks[0].length + 1)) * 2;
    }
    
    /* 
     * Rresizes the images for the Whack class.
     */
    protected void resizeWhackImages(int width, int height) {
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
     * Resizes the images for the Hammer class.
     */
    protected void resizeHammerImages(int width, int height) {
        for (int i = 0; i < originalHammerSheet.length; i++) {
            hammerSheet[i] = ImageTools.toBufferedImage
                (originalHammerSheet[i]
                     .getScaledInstance(width, height, Image.SCALE_SMOOTH)
                );
        }
    }
    @Override
    protected void cleanUp() {
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
    }
    
    /* 
     * @return the size of the whack field such that int[] {width, height},
     * where width and height denote the number of whackables in resp.
     * the rows and columns.
     */
    protected abstract int[] getFieldSize();
    
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
    
    /* 
     * @return the location of the music file
     */
    protected abstract String getWhackMusicFile();
    
    /* 
     * This method is called when a whackable has been whacked.
     * 
     * @param timeStamp the time when the whackable has been whacked.
     *     Note that this is an event, so this part is NOT synchronized
     *     with the update thread.
     */
    abstract protected void whackEvent(long timeStamp);
    
}