
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.Score;

import learningGame.font.FontLoader;

import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.ImageTools;
import learningGame.tools.Key;
import learningGame.tools.KeyDetector;


// Java packages
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JLayeredPane;


abstract public class MiniGame extends JLayeredPane implements MouseMotionListener, MouseListener {
    // The working directory.
    final protected String WORKING_DIR = LearningGame.WORKING_DIR;
    
    // The font used to display the text.
    final private static Font TEXT_FONT = FontLoader.getLocalFont("Cooper Black\\Cooper Black Regular.ttf");
    
    // The total time left that the time is up text is displayed
    final private static long TIME_UP_TOTAL = 1500;
    
    // The action that is executed after the minigame has ended.
    final private Runnable r;
    
    // The instruction panel
    private InstructionPanel ip;
    
    // Denotes whether the minigame has started.
    private boolean started = false;
    // Denotes whether the minigame has finished.
    private boolean finished = false;
    // Deonotes whether the minigame has ended.
    private boolean stopped = false;
    
    // The KeyDetector used to detect the key presses between updates.
    private KeyDetector kd;
    
    
    // Instance of the parent.
    final protected LearningGame lg;
    
    // The time to complete the miniGame.
    final protected long timeOut;
    
    // The time stamp from when the start() method was invoked.
    protected long startTimeStamp;
    
    // The total time left to complete the MiniGame.
    // Must be an instance of Object to check for initialisation.
    protected Long timeLeft;
    
    // The color of the background and the counter.
    protected Color backgroundColor;
    protected Color counterColor;
    
    // Whether the MiniGame succeded or not.
    protected boolean succes = false;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public MiniGame(LearningGame lg, Runnable r, long timeOut) {
        super();
        
        this.setLayout(null);
        this.lg = lg;
        this.r = r;
        this.timeOut = timeOut;
        
        backgroundColor = new Color(190, 190, 190);
        counterColor = Color.BLACK;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * MouseMotionListener overrides.
     * These are supposed to be overridden by the child child class.
     * They are here for easy access.
     */
    @Override
    public void mouseDragged(MouseEvent e) { }
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    /* 
     * MouseListener overrides.
     * These are supposed to be overridden by the child child class.
     * They are here for easy access.
     */
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return whether the current MiniGame was started.
     */
    final public boolean isStarted() {
        return started;
    }
    
    /* 
     * @return whether the current MiniGame was finished.
     */
    final public boolean isFinished() {
        return finished;
    }
    
    /* 
     * @return whether the current MiniGame was stopped.
     */
    final public boolean isStopped() {
        return stopped;
    }
    
    /* 
     * @return whether the current MiniGame is running.
     */
    final public boolean isRunning() {
        return started && !stopped;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Other functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Adds all nessesary listeners.
     */
    final private void addListeners() {
        addMouseMotionListener(this);
        addMouseListener(this);
        addSubListeners();
    }
    
    /* 
     * Removes all nessesary listeners.
     */
    final private void removeListeners() {
        removeMouseMotionListener(this);
        removeMouseListener(this);
        removeSubListeners();
    }
    
    /* 
     * Starts the current minigame.
     * No action is taken when already started, already finished or already stopped.
     */
    final public void start() {
        if (!started && !finished && !stopped) {
            createGUI();
            ip = new InstructionPanel(getInstruction(), "Start!", () -> instructionRead());
            this.add(ip, 0);
            // Update size and location of the instruction panel.
            setBounds(getX(), getY(), getWidth(), getHeight());
            resized(getWidth(), getHeight());
            repaint();
            update(new Key[0], System.currentTimeMillis());
        }
    }
    
    /* 
     * Shows the instructions
     */
    final private void instructionRead() {
        this.remove(ip);
        ip = null;
        resized(getWidth(), getHeight());
        startMiniGame();
        timeLeft = timeOut;
        startTimeStamp = System.currentTimeMillis();
        addListeners();
        started = true;
    }
    
    /* 
     * Updates the frame for the minigame.
     */
    final public void update() {
        long time = System.currentTimeMillis();
        timeLeft = timeOut - (time - startTimeStamp);
        
        if (started && (finished || timeLeft < 0)) {
            finish(false);
        }
        
        if (started && !stopped) {
            if (kd == null || finished) {
                update(new Key[0], System.currentTimeMillis());
                
            } else {
                kd.update();
                update(kd.getKeysPressed(), time);
            }
        }
        
        repaint();
    }
    
    /* 
     * This method is called when the mini game is finished.
     * No action is taken when not yet started, already finished, or already stopped,
     *     except for when the time up count down has finished. Then it calls the stop() method.
     * 
     * @param succes
     */
    final public void finish(boolean succes) {
        if (started && !finished && !stopped) {
            finished = true;
            this.succes = succes;
            removeListeners();
            
            if (timeLeft == null) {
                startTimeStamp = 0;
                
            } else {
                startTimeStamp -= timeLeft;
            }
        }
        
        if (timeLeft == null || timeLeft + TIME_UP_TOTAL <= 0) {
            stop();
        }
    }
    
    /* 
     * This method is called when the MiniGame terminates normally.
     * No action is taken when not yet started, not yet finished, or already stopped.
     * Otherwise calling this method prevents future calls to both the finish() and stop() method.
     */
    final public void stop() {
        if (started && finished && !stopped) {
            stopped = true;
            removeListeners();
            cleanUp();
            PlayMusic.stopAllMusic();
            if (r != null) r.run();
        }
    }
    
    /* 
     * This method is called when the MiniGame is interrupted in it's normal behaviour.
     * Calling this method prevents future calls to both the finish() and stop() method.
     * No action is taken when already stopped.
     * Executes the cleanUp method, but does not execute the final runnable that was given.
     */
    final public void terminate() {
        if (!stopped) {
            stopped = true;
            cleanUp();
            PlayMusic.stopAllMusic();
        }
    }
    
    /* 
     * This method is called when the MiniGame is resized/relocated.
     */
    @Override
    final public void setBounds(int x, int y, int width, int height) {
        boolean resized = (width != getWidth() && getWidth() != 0) ||
                           (height != getHeight() && getHeight() != 0);
        
        super.setBounds(x, y, width, height);
        if (ip != null) {
            ip.setSize((int) (width * 0.6), (int) (height * 0.6));
            ip.setLocation((int) (width * 0.2), (int) (height * 0.2));
        }
        
        if (resized) {
            resized(width, height);
        }
        
        if (!started && !finished && !stopped) {
            repaint();
            //update(new Key[0], System.currentTimeMillis());
        }
    }
    
    /* 
     * Uses the given KeyDetector as key input.
     */
    final public void useKeyDetector(KeyDetector kd) {
        this.kd = kd;
    }
    
    /* 
     * This method is called when the panel is repainted.
     */
    @Override
    final protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Convert graphics object
        Graphics2D g2d = (Graphics2D) g;
        
        // Paint the background
        g2d.setPaint(backgroundColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        
        // Retrieve the current g2d transformation.
        AffineTransform g2dTrans = g2d.getTransform();
        
        // Draw background
        drawBackground(g2d, getBackgroundImage());
        
        // Restore the g2d transformation.
        g2d.setTransform(g2dTrans);
    }
    
    /* 
     * Overrides the paint method.
     * Note:
     * - ONLY use this method to draw something on top of all components.
     * - PUT ALL OTHER STUFF IN {@code paintComponent}!
     * - NEVER remove the call to the super method.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Convert graphics object
        Graphics2D g2d = (Graphics2D) g;
        
        if (started) {
            // Retrieve the current g2d transformation.
            AffineTransform g2dTrans = g2d.getTransform();
            
            if (timeLeft != null) {
                g2d.setPaint(counterColor);
                
                if (timeLeft > 0) {
                    String text = timeLeft.toString();
                    g2d.setFont(TEXT_FONT.deriveFont(50F));
                    int textWidth = g2d.getFontMetrics().stringWidth(text);
                    g2d.drawString(text, (getWidth() - textWidth) / 2, 50);
                    
                } else {
                    String text = (succes
                                       ? "felicitazioni!"
                                       : "Time's up!");
                    
                    g2d.setFont(TEXT_FONT.deriveFont(100F));
                    Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(text, g2d);
                    double textWidth = bounds.getWidth();
                    double textHeight = bounds.getHeight();
                    int ascent = g2d.getFontMetrics().getAscent();
                    
                    double angle = Math.PI/12.0 * Math.sin
                        ((System.currentTimeMillis() % 500L) / 500.0 * 2*Math.PI);
                    
                    g2d.rotate(angle, getWidth() / 2, getHeight() / 2);
                    g2d.drawString(text,
                                   (int) ((getWidth() - textWidth) / 2),
                                   (int) ((getHeight() - textHeight) / 2 + ascent));
                }
            }
            
            // Restore the g2d transformation.
            g2d.setTransform(g2dTrans);
        }
    }
    
    /* 
     * This method draws the background.
     * Override this method to paint something else then the background.
     */
    protected void drawBackground(Graphics2D g2d, BufferedImage background) {
        if (background != null) {
            g2d.scale(((double) getWidth())  / background.getWidth(),
                      ((double) getHeight()) / background.getHeight());
            g2d.drawImage(background, 0, 0, null);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Abstract functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This method is invoked to create the GUI of the application.
     */
    abstract protected void createGUI();
    
    /* 
     * This method is invoked when the minigame is started.
     */
    abstract protected void startMiniGame();
    
    /* 
     * This method is invoked when the listeners of the sub components should be added.
     */
    abstract protected void addSubListeners();
    
    /* 
     * This method is invoked when the listeners of the sub components should be removed.
     */
    abstract protected void removeSubListeners();
    
    /* 
     * The update method. Put all time based stuff in here.
     * 
     * @param keys the keys that were pressed since the previous update.
     * @param timeStamp the start of the update cycle.
     */
    abstract protected void update(Key[] keys, long timeStamp);
    
    /* 
     * This method is always called when the MiniGame is about to shut down.
     */
    abstract protected void cleanUp();
    
    /* 
     * @param the word which has this MiniGame assoiated with it.
     * @param mistakes the number of wrong buttons that were pressed in the word screen.
     * @return the score of this miniGame
     */
    abstract public Score getScore(Word word, int mistakes);
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    abstract protected void resized(int width, int height);
    
    /*
     * @return the background image.
     */
    abstract protected BufferedImage getBackgroundImage();
    
    /* 
     * @return the text to be displayed for the instruction panel. Supports HTML.
     */
    abstract protected String getInstruction();
    
    
    // tmp
    public static void main(String[] args) {
        new LearningGame();
    }
    
}