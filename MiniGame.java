
package learningGame;


// Java packages
import javax.swing.JPanel;


public abstract class MiniGame extends JPanel {
    
    public MiniGame() {
        super(null);
    }
    
    abstract public void createGUI();
    abstract public void begin();
    abstract public void stop();
    abstract public void getScore();
}