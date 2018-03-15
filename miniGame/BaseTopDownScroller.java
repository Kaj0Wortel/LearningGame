
package learningGame.miniGame;


// Own packages
import learningGame.LearningGame;
import learningGame.MiniGame;

import learningGame.music.PlayMusic;

import learningGame.tools.ImageTools;
import learningGame.tools.Key;
import learningGame.tools.ModCursors;
import learningGame.tools.MultiTool;


// Java packages
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.Clip;

import javax.swing.JPanel;


abstract public class BaseTopDownScroller extends MiniGame {
    // The obstacles
    protected ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    
    // The collectables
    protected ArrayList<Collectable> collectables = new ArrayList<Collectable>();
    
    // The player
    protected Player player;
    
    // Scroll speed of the background (1.0 = 1 full screen further in 1 sec).
    protected double scrollSpeed = 1.5;
    
    // The current position of the background.
    // It always holds that 0.0 <= curPos < 1.0.
    protected double curPos = 0.0;
    
    // The spawn chance of an obstacle in [obstacles per second].
    // A value lower then 0 will cause no obstacles to be spawned.
    protected double obstacleSpawnChance = 1.0;
    
    // The spawn chance of a collectable in [collectables per second].
    // A value lower then 0 will cause no collectables to be spawned.
    protected double collectableSpawnChance = 0.5;
    
    public BaseTopDownScroller(LearningGame lg, Runnable r, long timeOut) {
       super(lg, r, timeOut);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Spawnable class
     * ----------------------------------------------------------------------------------------------------------------
     */
    abstract protected class Spawnable extends JPanel {
        // The type of obstacle.
        final protected int type;
        
        // The ratio X location of the spawnable.
        protected double widthLoc;
        
        // The ratio Y location of the spawnable.
        protected double heightLoc = 0.0;
        
        // The animation speed.
        final protected int animSpeed;
        
        // The previous animation change time stamp.
        protected long animTimeStamp = 0L;
        
        // The image number that is currently shown.
        protected int animNum = 0;
        
        /* ----------------------------------------------------------------------------------------------------------------
         * Spawnable constructor
         * ----------------------------------------------------------------------------------------------------------------
         */
        public Spawnable(int type, double loc, int animSpeed, Dimension dim) {
            super(null);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
            
            if (loc < 0 || loc > 1) {
                throw new IllegalArgumentException
                    ("Invallid location arugument. Expected: 0 <= loc <= 1. Found: loc = " + loc);
            }
            
            this.type = type;
            this.animSpeed = animSpeed;
            this.widthLoc = loc;
            
            setSize(dim);
            setLocation((int) (widthLoc * (BaseTopDownScroller.this.getWidth() - dim.getWidth())), (int) -dim.getHeight());
            animTimeStamp = System.currentTimeMillis();
        }
        
        /* 
         * Returns the type of spawnable.
         */
        final public int getType() {
            return type;
        }
        
        /* 
         * Update function.
         * All timed stuff goes in here.
         */
        final public void update(long timeStamp, double speed) {
            heightLoc += speed;
            if (heightLoc > 1.0) {
                destroySpawnable(this);
                return;
            }
            
            int panelWidth = BaseTopDownScroller.this.getWidth();
            int panelHeight = BaseTopDownScroller.this.getHeight();
            
            // Update the animation image
            if (timeStamp > animTimeStamp + animSpeed) {
                animTimeStamp = animTimeStamp + animSpeed;
                animNum = (animNum + 1) % getAnimMax();
            }
            
            // Update the location
            setLocation((int) (widthLoc * (panelWidth - getWidth())),
                        (int) (heightLoc * (panelHeight + getHeight())) - getHeight());
            
            // Check if the spawnable collides with the player
            int pX = player.getX();
            int pY = player.getY();
            int pW = player.getWidth();
            int pH = player.getHeight();
            
            if (pX < getX() + getWidth()  && pX + pW > getX() &&
                pY < getY() + getHeight() && pY + pW > getY())
            {
                intersect(this);
            }
        }
        
        @Override
        final protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Draw the image
            BufferedImage image = getDrawImage();
            
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            double widthRatio = ((double) getWidth()) / imgWidth;
            double heightRatio = ((double) getHeight()) / imgHeight;
            
            g2d.scale(widthRatio, heightRatio);
            if (image != null) g.drawImage(image, 0, 0, null);
        }
        
        /* 
         * This function returns the image to be drawn.
         */
        abstract protected BufferedImage getDrawImage();
        
        /* 
         * This function returns the maximal value that animNum is allowed to have minus 1
         * (so the exact length of the array of images used for animation).
         */
        abstract protected int getAnimMax();
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Obstacle class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Obstacle extends Spawnable {
        /* ----------------------------------------------------------------------------------------------------------------
         * Obstacle constructor
         * ----------------------------------------------------------------------------------------------------------------
         */
        public Obstacle(int type, double loc, int animSpeed) {
            super(type, loc, animSpeed, calcObstacleDims(BaseTopDownScroller.this.getWidth(),
                                                         BaseTopDownScroller.this.getHeight())[type]);
        }
        
        /* 
         * This function returns the image to be drawn.
         */
        @Override
        protected BufferedImage getDrawImage() {
            return getObstacleSheets()[type][animNum];
        }
        
        /* 
         * This function returns the maximal value that animNum is allowed to have minus 1
         * (so the exact length of the array of images used for animation).
         */
        @Override
        protected int getAnimMax() {
            return getObstacleSheets()[type].length;
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Collectable class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Collectable extends Spawnable {
        /* ----------------------------------------------------------------------------------------------------------------
         * Collectable constructor
         * ----------------------------------------------------------------------------------------------------------------
         */
        public Collectable(int type, double loc, int animSpeed) {
            super(type, loc, animSpeed, calcCollectableDims(BaseTopDownScroller.this.getWidth(),
                                                            BaseTopDownScroller.this.getHeight())[type]);
        }
        
        /* 
         * This function returns the image to be drawn.
         */
        @Override
        protected BufferedImage getDrawImage() {
            return getCollectableSheets()[type][animNum];
        }
        
        /* 
         * This function returns the maximal value that animNum is allowed to have minus 1
         * (so the exact length of the array of images used for animation).
         */
        @Override
        protected int getAnimMax() {
            return getCollectableSheets()[type].length;
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Player class
     * ----------------------------------------------------------------------------------------------------------------
     */
    protected class Player extends JPanel {
        final private static int FORWARD = 0;
        final private static int LEFT = 1;
        final private static int RIGHT = 2;
        final private static int DAMAGED = 3;
        
        // The current state of the player.
        private int state = FORWARD;
        // The ratio X location of the spawnable.
        protected double widthLoc = 0.5;
        
        // The ratio Y location of the spawnable.
        protected double heightLoc = 1.0;
        
        // The animation speed in framechanges per ms.
        final protected int animSpeed;
        
        // The previous animation change time stamp.
        protected long animTimeStamp = 0L;
        
        // The image number that is currently shown.
        protected int animNum = 0;
        
        /* ----------------------------------------------------------------------------------------------------------------
         * Player constructor
         * ----------------------------------------------------------------------------------------------------------------
         */
        public Player(int animSpeed) {
            super(null);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
            
            this.animSpeed = animSpeed;
            
            int width = BaseTopDownScroller.this.getWidth();
            int height = BaseTopDownScroller.this.getHeight();
            
            setLocation((int) (widthLoc * (width - getWidth())), (int) (heightLoc * (height - getHeight())));
            animTimeStamp = System.currentTimeMillis();
        }
        
        /* 
         * Update function.
         * All timed stuff goes in here.
         */
        public void update(int newState, long timeStamp, double speed) {
            int panelWidth = BaseTopDownScroller.this.getWidth();
            int panelHeight = BaseTopDownScroller.this.getHeight();
            
            // Find the new width location.
            double newWidthLoc = widthLoc;
            
            if (newState == LEFT) {
                state = LEFT;
                newWidthLoc -= speed;
                
            } else if (newState == RIGHT) {
                state = RIGHT;
                newWidthLoc += speed;
                
            } else if (newState == FORWARD) {
                state = FORWARD;
            }
            
            // Check bounds on the width value.
            if (newWidthLoc < 0) {
                newWidthLoc = 0;
                
            } else if (newWidthLoc > 1) {
                newWidthLoc = 1;
            }
            
            // Update the animation image
            if (timeStamp > animTimeStamp + animSpeed) {
                animTimeStamp = animTimeStamp + animSpeed;
                BufferedImage[] playerSheet = getPlayerSheet();
                
                if (playerSheet != null) {
                    animNum = (animNum + 1) % playerSheet.length;
                }
            }
            
            // Update the location
            widthLoc = newWidthLoc;
            setLocation((int) (widthLoc * (panelWidth - getWidth())), (int) heightLoc * (panelHeight - getHeight()));
        }
        
        /* 
         * This function is called to damage the player.
         */
        public void damage() {
            System.out.println("damaged!");
            // damage the player
            // todo
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            BufferedImage[] playerSheet = getPlayerSheet();
            if (playerSheet != null && playerSheet[animNum] != null) {
                Graphics2D g2d = (Graphics2D) g;
                
                // Scale the graphics
                double imgW = playerSheet[animNum].getWidth();
                double imgH = playerSheet[animNum].getHeight();
                double diagonalLength = Math.sqrt(getWidth()*getWidth() + getHeight()*getHeight());
                
                // Calculate scale factors and scale
                double widthScaleFactor  = (getWidth()  / imgW) * (getWidth()  / diagonalLength);
                double heightScaleFactor = (getHeight() / imgH) * (getHeight() / diagonalLength);
                g2d.scale(widthScaleFactor, heightScaleFactor);
                
                double angle = getPlayerAngle();
                
                // Calculate translation and translage
                double xTrans = Math.cos(angle) * (diagonalLength - getWidth() ) / (getWidth()  / imgW);
                double yTrans = Math.sin(angle) * (diagonalLength - getHeight()) / (getHeight() / imgW);
                g2d.translate(xTrans, yTrans);
                
                // Turn the image if nessecary
                // The anchor points are set at:
                // <original size> / <scale factor> / 2 <center of image>, 
                // which is equivalent to:
                // transX: getWidth()  / (getWidth()  / imgW) / 2 = imgW / 2
                // transY: getHeight() / (getHeight() / imgH) / 2 = imgH / 2
                if (state == LEFT) {
                    g2d.rotate(Math.toRadians(-angle), imgW / 2, imgH / 2);
                    
                } else if (state == RIGHT) {
                    g2d.rotate(Math.toRadians(angle), imgW / 2, imgH / 2);
                }
                
                // Draw the image
                g2d.drawImage(playerSheet[animNum], 0, 0, null);
            }
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This method is called to create the GUI of the application.
     */
    @Override
    final protected void createGUI() {
        player = new Player(getPlayerFrameSpeed());
        this.add(player, 0);
        player.setBackground(Color.RED);
        
        Clip backgroundClip = getBackgroundClip();
        backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    /* 
     * This method is invoked when the listeners of the sub components should be added.
     */
    @Override
    protected void addSubListeners() { }
    
    /* 
     * This method is invoked when the listeners of the sub components should be removed.
     */
    @Override
    protected void removeSubListeners() { }
    
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
     */
    @Override
    public void cleanUp() {
        // Set the default cursor.
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
        
        // Stop the clip
        PlayMusic.stop(getBackgroundClip());
    }
    
    /* 
     * Removes the spawnable from the screen and update list.
     */
    protected void destroySpawnable(Spawnable spawn) {
        if (spawn instanceof Obstacle) {
            Obstacle ob = (Obstacle) spawn;
            obstacles.remove(ob);
            
        } else if (spawn instanceof Collectable) {
            Collectable col = (Collectable) spawn;
            collectables.remove(col);
        }
        
        this.remove(spawn);
    }
    
    /* 
     * This method is called when a spawnable intersects with the player.
     */
    protected void intersect(Spawnable spawn) {
        if (spawn instanceof Obstacle) {
            // Play damaged sound.
            PlayMusic.play(getDamagedClip(spawn.getType()));
            
            player.damage();
            
        } else if (spawn instanceof Collectable) {
            // Play collected sound.
            PlayMusic.play(getCollectedClip(spawn.getType()));
            
            // todo: add points
        }
        
        // Remove the spawnable from the list.
        destroySpawnable(spawn);
    }
    
    
    /* 
     * The update method. Put all time based stuff in here.
     * 
     * @param keys the keys that were pressed since the previous update.
     * @param timeStamp the start of the update cycle.
     */
    @Override
    final protected void update(Key[] key, long timeStamp) {
        // Add spawnables
        spawnUpdate();
        
        // Calculate the top down speed and player speed (left/right).
        double speed = scrollSpeed / LearningGame.FPS;
        double playerSpeed = 1.3 / LearningGame.FPS;
        
        // Update the background position.
        curPos = (curPos + scrollSpeed / LearningGame.FPS) % 1.0;
        
        if (MultiTool.isInArray(key, Key.LEFT)) {
            player.update(Player.LEFT, timeStamp, playerSpeed);
            
        } else if (MultiTool.isInArray(key, Key.RIGHT)) {
            player.update(Player.RIGHT, timeStamp, playerSpeed);
            
        } else {
            player.update(Player.FORWARD, timeStamp, playerSpeed);
        }
        
        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).update(timeStamp, speed);
        }
        
        for (int i = 0; i < collectables.size(); i++) {
            collectables.get(i).update(timeStamp, speed);
        }
        
        repaint();
    }
    
    /* 
     * This method creates all obstacles and collectables.
     */
    protected void spawnUpdate() {
        Random random = new Random();
        int fps = LearningGame.FPS;
        
        // ---------------
        // Spawn obstacles
        // 
        int spawnObstacles = 0;
        
        // If the spawnChance is higher then the current fps, always spawn
        // the obstacles that should have been spawned.
        if (obstacleSpawnChance > fps) {
            spawnObstacles += obstacleSpawnChance / fps;
        }
        
        // For the remaining chance, approximate whether to spawn the obstacle or not.
        if (((double) obstacleSpawnChance % fps) / fps > random.nextDouble()) {
            spawnObstacles++;
        }
        
        while (spawnObstacles > 0) {
            // Generate a random type and location
            int type = random.nextInt(getMaxObstacleType() + 1);
            double loc = random.nextDouble();
            
            addSpawnable(new Obstacle(type, loc, getObstacleFrameSpeed(type)));
            spawnObstacles--;
        }
        
        // ------------------
        // Spawn collectables
        // 
        int spawnCollectables = 0;
        
        // If the spawnChance is higher then the current fps, always spawn
        // the collectables that should have been spawned.
        if (collectableSpawnChance > fps) {
            spawnCollectables += collectableSpawnChance / fps;
        }
        
        // For the remaining chance, approximate whether to spawn the obstacle or not.
        if (((double) collectableSpawnChance % fps) / fps > random.nextDouble()) {
            spawnCollectables++;
        }
        
        while (spawnCollectables > 0) {
            // Generate a random type and location
            int type = random.nextInt(getMaxCollectableType() + 1); // Value between 0 and maxType (inclusive).
            double loc = random.nextDouble(); // Value between 0 and 1.
            
            addSpawnable(new Collectable(type, loc, getCollectableFrameSpeed(type)));
            spawnCollectables--;
        }
    }
    
    /* 
     * Adds a spawnable to the panel.
     */
    protected void addSpawnable(Spawnable spawn) {
        if (spawn instanceof Obstacle) {
            obstacles.add((Obstacle) spawn);
            
        } else if (spawn instanceof Collectable) {
            collectables.add((Collectable) spawn);
            
        } else {
            throw new IllegalArgumentException("Tried to add invallid spawnable type");
        }
        
        add(spawn);
    }
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    @Override
    final protected void resized(int width, int height) {
        Dimension[] obstacleDims = calcObstacleDims(width, height);
        Dimension[] collectableDims = calcCollectableDims(width, height);
        Dimension playerDim = calcPlayerDim(width, height);
        
        player.setSize(playerDim);
        
        for (Obstacle ob : obstacles) {
            ob.setSize(obstacleDims[ob.getType()]);
        }
        
        for (Collectable col : collectables) {
            col.setSize(collectableDims[col.getType()]);
        }
    }
    
    /* 
     * @return the dimensions of a Obstacle.
     */
    protected Dimension[] calcObstacleDims(int newWidth, int newHeight) {
        return new Dimension[] {
            new Dimension((int) ((1.0/8.0) * newWidth),
                          (int) ((1.0/8.0) * newHeight))
        };
    }
    
    /* 
     * @return the dimensions of the Collectable.
     */
    protected Dimension[] calcCollectableDims(int newWidth, int newHeight) {
        return new Dimension[] {
            new Dimension((int) ((1.0/8.0) * newWidth),
                          (int) ((1.0/8.0) * newHeight))
        };
    }
    
    /* 
     * @return the dimension of the player.
     */
    protected Dimension calcPlayerDim(int newWidth, int newHeight) {
        return new Dimension((int) ((1.0/5.0) * newWidth),
                             (int) ((1.0/5.0) * newHeight));
    }
    
    /* 
     * This method draws a scrolling background.
     */
    @Override
    protected void drawBackground(Graphics2D g, BufferedImage background) {
        if (background != null) {
            Graphics2D g2d = (Graphics2D) g;
            
            // Retrieve the current g2d transformation.
            AffineTransform g2dTrans = g2d.getTransform();
            
            double widthRatio = ((double) getWidth()) / background.getWidth();
            double heightRatio = ((double) getHeight()) / background.getHeight();
            
            g2d.scale(widthRatio, heightRatio);
            g2d.drawImage(background, 0, (int) ((curPos - 1.0) * background.getHeight()), null);
            g2d.drawImage(background, 0, (int) (curPos * background.getHeight()), null);
            
            // Restore the g2d transformation.
            g2d.setTransform(g2dTrans);
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Abstract functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the image sheets of the obstacles.
     */
    abstract protected BufferedImage[][] getObstacleSheets();
    
    /* 
     * @return the image sheets of the collectables.
     */
    abstract protected BufferedImage[][] getCollectableSheets();
    
    /* 
     * @return the image sheet of the player.
     */
    abstract protected BufferedImage[] getPlayerSheet();
    
    /* 
     * @return the maximal obstacle type value (inclusive).
     */
    abstract protected int getMaxObstacleType();
    
    /* 
     * @return the maximal collectable type value (inclusive).
     */
    abstract protected int getMaxCollectableType();
    
    /* 
     * @return the frame speed for the given obstacle type.
     *     Speed is measured in frames per second.
     */
    abstract protected int getObstacleFrameSpeed(int type);
    
    /* 
     * @return the frame speed for the given collectable type.
     *     Speed is measured in frames per second.
     */
    abstract protected int getCollectableFrameSpeed(int type);
    
    /* 
     * @return the frame speed for the player.
     *     Speed is measured in frames per second.
     */
    abstract protected int getPlayerFrameSpeed();
    
    /* 
     * @return the music file for when the player has hit the given type obstacle.
     */
    abstract protected Clip getDamagedClip(int type);
    
    /* 
     * @return the music for when the player has hit the given type collectable.
     */
    abstract protected Clip getCollectedClip(int type);
    
    /* 
     * @return the music file for the background music.
     */
    abstract protected Clip getBackgroundClip();
    
    /* 
     * @return the angle that the player is turned when the left/right buttons
     * are pressed. A positive angle means that the player is turned clockwise
     * when the right directional button is pressed.
     */
    abstract protected double getPlayerAngle();
}


