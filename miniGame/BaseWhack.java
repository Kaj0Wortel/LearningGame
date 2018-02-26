
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
import java.awt.Graphics;
import java.awt.Image;
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
    
    protected Whack[][] whacks = new Whack[3][3];
    
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
        
        public Whack(int x, int y, int width, int height) {
            super(null);
            this.setBounds(x, y, width, height);
        }
        
        public void showWackable(int moveTime, int stayTime) {
            if (state == NOTHING) {
                whackableShownTime = System.currentTimeMillis();
                this.moveTime = moveTime;
                this.stayTime = stayTime;
                state = GOING_UP;
            }
        }
        
        public boolean whack() {
            if (state == GOING_UP || state == OUT || state == GOING_DOWN) {
                state = WHACKED;
                return true;
            }
            
            return false;
        }
        
        public void update() {
            int prevWhackImageNum = curWhackImageNum;
            long curTime = System.currentTimeMillis();
            long delta = whackableShownTime - curTime;
            
            if (state == GOING_UP) {
                if (delta > moveTime / whackSheet.length * (curWhackImageNum + 1)) {
                    curWhackImageNum++;
                }
                
                if (curWhackImageNum == whackSheet.length - 1) {
                    state = OUT;
                }
                
            } else if (state == OUT) {
                if (delta > whackableShownTime + moveTime + stayTime) {
                    state = GOING_DOWN;
                }
                
            } else if (state == GOING_DOWN) {
                if (delta - moveTime - stayTime > moveTime / whackSheet.length *
                    Math.abs((curWhackImageNum) - whackSheet.length)) {
                    curWhackImageNum--;
                }
                
                if (curWhackImageNum == 0) {
                    state = NOTHING;
                }
            }
            
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
             if (state == WHACKED) {
                if (whacked != null) g.drawImage(whacked, 0, 0, null);
                
             } else {
                if (whackSheet != null && whackSheet.length > 0) {
                    if (state == NOTHING) {
                        if (whackSheet[0] != null) g.drawImage(whackSheet[0], 0, 0, null);
                        
                    } else {
                        if (whackSheet.length > curWhackImageNum && whackSheet[curWhackImageNum] != null) {
                            g.drawImage(whackSheet[curWhackImageNum], 0, 0, null);
                        }
                    }
                }
                
            }
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Other methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    
    @Override
    protected void createGUI() {
        BufferedImage[] whackSheet = getWhackSheet();
        
        for (int i = 0; i < whacks.length; i++) {
            for (int j = 0; j < whacks[i].length; j++) {
                whacks[i][j] = new Whack(getX(), getY(), getWidth(), getHeight());
                this.add(whacks[i][j]);
                whacks[i][j].addMouseListener(this);
            }
        }
    }
    
    @Override
    public void update(Key[] keys) {
        
    }
    
    @Override
    public void resized(int width, int height) {
        if (whacks != null) {
            if (imageCreateThread != null) {
                imageCreateThread.interrupt();
            }
            
            imageCreateThread = createImageCreationThread();
            imageCreateThread.start();
            
            for (int i = 0; i < whacks.length; i++) {
                for (int j = 0; j < whacks[i].length; j++) {
                    whacks[i][j].setSize(width, height);
                    whacks[i][j].setLocation(width / (int) (1.5*whacks.length + 1),
                                             height / (int) (1.5*whacks[i].length + 1));
                }
            }
        }
    }
    
    /* 
     * Creates 
     */
    private Thread createImageCreationThread() {
        return new Thread("image create thread <Whack>") {
            @Override
            public void run() {
                for (int i = 0; i < originalWhackSheet.length; i++) {
                    whackSheet[i] = ImageTools.toBufferedImage
                        (originalWhackSheet[i]
                             .getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)
                        );
                }
                
                whacked = ImageTools.toBufferedImage
                    (originalWhacked
                         .getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)
                    );
                
                imageCreateThread = null;
            }
        };
    }
    
    @Override
    public void cleanUp() {
        
    }
    
    @Override
    public Score getScore() {
        return null;
    }
    
    /* 
     * @return the image sheet for the animation.
     */
    protected abstract BufferedImage[] getWhackSheet();
    
    /* 
     * @return the image for when the whackable has been whacked.
     */
    protected abstract BufferedImage getWhackedImage();
    
}