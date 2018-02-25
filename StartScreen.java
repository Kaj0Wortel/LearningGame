
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.font.FontLoader;
import learningGame.log.Log2;
import learningGame.tools.Button2;
import learningGame.tools.ImageTools;
import learningGame.tools.LoadImages2;


// Java packages
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.swing.JPanel;


public class StartScreen extends JPanel {
    // Action that will be executed when finished
    Runnable r;
    
    // GUI
    Button2 startButton;
    
    // Background image and file location.
    private BufferedImage background;
    final private static String backgroundLoc = LearningGame.workingDir + "img\\blue_dot.png";
    
    
    /* 
     * Constructor
     */
    public StartScreen(Runnable r) {
        super(null);
        this.r = r;
        createGUI();
    }
    
    /* 
     * Creates the GUI of the application. 
     */
    private void createGUI() {
        try {
            startButton = new Button2(100, 25, 10, true, "Start");
            this.add(startButton);
            startButton.setSize(200, 50);
            startButton.setLocation(250, 170);
            startButton.setFont(FontLoader.Cousine);
            startButton.setTextSize(30);
            startButton.addActionListener((e) -> {
                if (r != null) r.run();
            });
            
        } catch (IOException e) {
            Log2.write(e);
        }
    }
    
    /* 
     * Sets the size and location of the panel.
     * {@code setSize(int, int)} and {@code setLocation(int, int)} both call this method.
     * Any resize events go via this method.
     * 
     * @param x the new x location coordinate relative to the upper left corner of the screen.
     * @param y the new y location coordinate relative to the upper left corner of the screen.
     * @param width the new width of the frame (incl. insets).
     * @param height the new height of the frame (incl. insets).
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        boolean resized = width != getWidth() || height != getHeight();
        super.setBounds(x, y, width, height);
        
        if (resized) {
            Insets in = this.getInsets();
            
            startButton.setSize(getWidth() / 3, getHeight() / 4);
            startButton.setLocation(getWidth() / 3, getHeight() / 3);
            
            if (getWidth() != 0 && getHeight() != 0) {
                try {
                    // Load, copy and resize the backgroundimage.
                    background = ImageTools.toBufferedImage
                        (ImageTools.imageDeepCopy
                             (LoadImages2.ensureLoadedAndGetImage
                                  (backgroundLoc)[0][0])
                             .getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST));
                    
                } catch (IOException e) {
                    Log2.write(e);
                }
            }
            
        }
    }
    
    /* 
     * Paints the images on the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (background != null) g.drawImage(background, 0, 0, null);
    }
}


