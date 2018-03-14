
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.Score;

import learningGame.font.FontLoader;

import learningGame.log.Log2;

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
    // The font used to display the text.
    final private static Font textFont = FontLoader.getLocalFont("Cooper Black\\Cooper Black Regular.ttf");
    
    // The total time left that the time is up text is displayed
    final private static long TIME_UP_TOTAL = 1000;
    
    // The action that is executed after the minigame has ended.
    final private Runnable R;
    
    // The instruction panel
    private InstructionPanel ip;
    
    // Denotes whether the application has started.
    private boolean started = false;
    // Deonotes whether the application has ended.
    private boolean stopped = false;
    
    // The KeyDetector used to detect the key presses between updates.
    private KeyDetector kd;
    
    // The time stamp from when the time was up.
    private Long timeUpTimeStamp;
    
    
    // The working directory
    final protected String WORKING_DIR = LearningGame.WORKING_DIR;
    
    // Instance of the parent.
    final protected LearningGame LG;
    
    // The time to complete the miniGame
    final protected long TIME_OUT;
    
    // The time stamp from when the start() method was invoked.
    protected long startTimeStamp;
    
    // The total time left to complete the MiniGame.
    // Must be an instance of Object to check for initialisation.
    protected Long timeLeft;
    
    // The color of the background and the counter
    protected Color backgroundColor;
    protected Color counterColor;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public MiniGame(LearningGame lg, Runnable r, long timeOut) {
        super();
        
        this.setLayout(null);
        this.LG = lg;
        this.R = r;
        this.TIME_OUT = timeOut;
        
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
     * @return whether the current MiniGame is running.
     */
    final public boolean isRunning() {
        return started && !stopped;
    }
    
    /* 
     * @return whether the current MiniGame was stopped.
     */
    final public boolean isStopped() {
        return stopped;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Other functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Adds all nessesary listeners.
     */
    final private void addListeners() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
    
    /* 
     * Starts the current minigame.
     */
    final public void start() {
        if (!started && !stopped) {
            createGUI();
            ip = new InstructionPanel(getInstruction(), "Start!", () -> instructionRead());
            this.add(ip, 0);
            // Update size and location of the instruction panel.
            setBounds(getX(), getY(), getWidth(), getHeight());
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
        timeLeft = TIME_OUT;
        startTimeStamp = System.currentTimeMillis();
        addListeners();
        addSubListeners();
        started = true;
    }
    
    /* 
     * Updates the frame for the minigame.
     */
    final public void update() {
        if (started) {
            long time = System.currentTimeMillis();
            timeLeft = TIME_OUT - (time - startTimeStamp);
            
            if (kd != null) {
                kd.update();
                update(kd.getKeysPressed(), time);
                
            } else {
                update(new Key[0], System.currentTimeMillis());
            }
        }
        
        repaint();
    }
    
    /* 
     * This method is called when the mini game is finished.
     * Calling this method prevents future calls to both the finish() and stop() method.
     */
    final public void finish() {
        if (!stopped) {
            stopped = true;
            cleanUp();
            if (R != null) R.run();
        }
    }
    
    /* 
     * This method is called when the MiniGame is interrupted in it's normal behaviour.
     * Calling this method prevents future calls to both the finish() and stop() method.
     * Also ignores the final action that is executed when the MiniGame ends from running.
     */
    final public void stop() {
        if (!stopped) {
            stopped = true;
            cleanUp();
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
        drawBackground(g, getBackgroundImage());
        
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
        
        // Retrieve the current g2d transformation.
        AffineTransform g2dTrans = g2d.getTransform();
        
        if (timeLeft != null) {
            g2d.setPaint(counterColor);
            
            if (timeLeft > 0) {
                String text = timeLeft.toString();
                g2d.setFont(textFont.deriveFont(50F));
                int textWidth = g2d.getFontMetrics().stringWidth(text);
                g2d.drawString(text, (getWidth() - textWidth) / 2, 50);
                
            } else {
                String text = "Time's Up !";
                g2d.setFont(textFont.deriveFont(100F));
                Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(text, g2d);
                double textWidth = bounds.getWidth();
                double textHeight = bounds.getHeight();
                //g2d.getFontMetrics().getAscent() + g2d.getFontMetrics().getDescent();
                int ascent = g2d.getFontMetrics().getAscent();
                
                double angle = Math.PI/12.0 * Math.sin
                         ((System.currentTimeMillis() % 500L) / 500.0 * 2*Math.PI);
                
                g2d.rotate(angle, getWidth() / 2, getHeight() / 2);
                g2d.drawString(text, (int) ((getWidth() - textWidth) / 2), (int) ((getHeight() - textHeight) / 2 + ascent));
            }
        }
        
        // Restore the g2d transformation.
        g2d.setTransform(g2dTrans);
    }
    
    /* 
     * This method draws the background.
     * Override this method to paint something else then the background.
     */
    protected void drawBackground(Graphics g, BufferedImage background) {
        if (background != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(((double) getWidth())  / background.getWidth(),
                      ((double) getHeight()) / background.getHeight());
            g.drawImage(background, 0, 0, null);
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
     * This method returns the score.
     */
    abstract public Score getScore(); // todo: determine score object
    
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
        LearningGame lg = new LearningGame();
    }
    
}