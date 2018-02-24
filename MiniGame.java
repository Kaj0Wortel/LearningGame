
package learningGame;


// Own packages
import learningGame.tools.Key;
import learningGame.tools.KeyDetector;


// Java packages
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;


public abstract class MiniGame extends JPanel implements MouseMotionListener, MouseListener {
    // The action that is executed after the minigame has ended.
    final private Runnable r;
    final private KeyDetector kd = new KeyDetector();
    final protected LearningGame lg;
    
    protected boolean started = false;
    
    public MiniGame(LearningGame lg, Runnable r) {
        super(null);
        this.lg = lg;
        this.r = r;
        createGUI();
    }
    
    /* 
     * MouseMotionListener overrides.
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
    
    /* 
     * Adds all nessesary listeners.
     */
    final private void addListeners() {
        lg.addKeyListener(kd);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
    
    /* 
     * Starts the current minigame.
     */
    public void start() {
        if (!started) {
            kd.clear();
            started = true;
            addListeners();
        }
    }
    
    /* 
     * Updates the frame for the minigame.
     */
    final public void update() {
        if (started) {
            kd.update();
            update(kd.getKeysPressed());
        }
    }
    
    /* 
     * This method is called when the mini game is finished.
     */
    public void finish() {
        r.run();
    }
    
    abstract protected void createGUI();
    abstract protected void update(Key[] keys);
    abstract public void stop();
    abstract public void getScore(); // todo
}