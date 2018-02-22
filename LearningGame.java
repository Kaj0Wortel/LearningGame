package learningGame;

// Own packages
import learningGame.log.Log2;
import learningGame.tools.TerminalErrorMessage;

// Java packages
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.SwingUtilities;



public class LearningGame extends JFrame {
    
    /**
     * The String appName contains the name of our final application.
     */
    final public static String appName = "application name";
    
    public LearningGame() {
        super(appName);
        
        SwingUtilities.invokeLater(() -> {
            createGUI();
        });
    }
    
    private void createGUI() {
        this.setLayout(null);
        
        
        
        
        // Add listeners.
        this.addWindowListener(wl);
    }
    
    WindowListener wl = new WindowAdapter() {
        // Called when the user attempts to close the window
        @Override
        public void windowClosing(WindowEvent e) {
            
        }
    };
    
    public static void main(String[] args) {
        new LearningGame();
    }
}