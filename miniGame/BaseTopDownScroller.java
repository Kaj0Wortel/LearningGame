
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;
import learningGame.tools.Key;


// Java packages
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import javax.swing.JPanel;


public abstract class BaseTopDownScroller extends MiniGame {
    // The original images
    final private BufferedImage[][] originalObstaclesSheet;
    final private BufferedImage[][] originalCollectableSheet;
    final private BufferedImage[] originalPlayerSheet;
    
    // The resized images
    private BufferedImage[][] obstacleSheet;
    private BufferedImage[][] collectableSheet;
    private BufferedImage[] playerSheet;
    
    // The image creation thread
    private Thread imageCreateThread;
    
    // The obstacles
    protected ArrayList<Obstacle> obstacles;
    
    // The collectables
    protected ArrayList<Collectable> collectables;
    
    
    // Scroll speed of the background (1.0 = 1 full screen futher in 1 sec).
    protected double scrollSpeed = 0.3;
    
    // The current position of the background.
    // It always holds that 0.0 <= curPos < 1.0.
    protected double curPos = 0.0;
    
    
    public BaseTopDownScroller(LearningGame lg, Runnable r) {
        super(lg, r);
        
        originalObstaclesSheet = null;
        originalCollectableSheet = null;
        originalPlayerSheet = null;
        
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Obstacle class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Obstacle extends JPanel {
        final private int type;
        
        public Obstacle(int type) {
            super(null);
            this.type = type;
        }
        
        public int getType() {
            return type;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
        
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Collectable class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Collectable extends JPanel {
        final private int type;
        
        public Collectable(int type) {
            super(null);
            this.type = type;
        }
        
        public int getType() {
            return type;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    
    
    @Override
    final protected void createGUI() {
        
    }
    
    @Override
    final protected void update(Key[] key, long timeStamp) {
        curPos = (curPos + scrollSpeed / LearningGame.FPS) % 1.0;
        repaint();
    }
    
    @Override
    protected void drawBackground(Graphics g) {
        g.drawImage(backgroundResized, 0, (int) ((curPos-1.0) * getHeight()), null);
        g.drawImage(backgroundResized, 0, (int) (curPos * getHeight()), null);
    }
    
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Obstacle functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the images of the obstacles.
     */
    abstract BufferedImage[][] getObstacleImages();
    
}