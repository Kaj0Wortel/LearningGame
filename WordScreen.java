
package learningGame;


// Own packages
import learningGame.tools.Button2;


// Java packages
import java.io.IOException;

import javax.swing.JPanel;


public class WordScreen extends JPanel {
    final private String word;
    
    // GUI
    Button2[] wordOptionButtons = new Button2[6];
    
    WordScreen(String word) {
        this.word = word;
    }
    
    public void creatGUI() {
        for (int i = 0; i < wordOptionButtons.length; i++) {
            try {
                wordOptionButtons[i] = new Button2(200, 200, 10, "test");
            } catch (IOException e) {
                
            }
        }
    }
    
}


