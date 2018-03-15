
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.font.FontLoader;
import learningGame.log.Log2;
import learningGame.tools.Button2;
import learningGame.tools.ImageTools;
import learningGame.tools.LoadImages2;


// Java packages
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;


public class StartScreen extends JPanel {
    // Action that will be executed when finished
    Runnable r;
    
    // Background image and file location.
    private BufferedImage background;
    final private static String backgroundLoc = LearningGame.WORKING_DIR + "img\\blue_dot.png";
    
    final private static String[] supportedLangs = Word.getSupportedLangs();
    
    // GUI
    // The button to start the application
    private Button2 startButton;
    
    // The radio buttons for selecting the language
    private JLabel labelQ;
    private ButtonGroup bgLangQ;
    private JRadioButtonMenuItem[] rbmiLangQ;
    private JLabel labelA;
    private ButtonGroup bgLangA;
    private JRadioButtonMenuItem[] rbmiLangA;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public StartScreen(Runnable r) {
        super(null);
        this.r = r;
        
        try {
            background = LoadImages2.ensureLoadedAndGetImage(backgroundLoc)[0][0];
        } catch (IOException e) {
            Log2.write(e);
        }
        
        createGUI();
        setBounds(getX(), getY(), getWidth(), getHeight());
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates the GUI of the application. 
     */
    private void createGUI() {
        // Start button
        try {
            startButton = new Button2(100, 25, 10, "Start");
            add(startButton);
            startButton.setFont(FontLoader.getLocalFont("cousine\\Cousine-Regular.ttf"));
            startButton.setTextSize(30);
            
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (r != null) r.run();
                    startButton.removeActionListener(this);
                }
            });
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        // Labels
        labelQ = new JLabel("Question language:");
        labelA = new JLabel("Answer language:");
        add(labelQ);
        add(labelA);
        
        // Radio buttons
        bgLangQ = new ButtonGroup();
        bgLangA = new ButtonGroup();
        
        rbmiLangQ = new JRadioButtonMenuItem[supportedLangs.length];
        rbmiLangA = new JRadioButtonMenuItem[supportedLangs.length];
        
        for (int i = 0; i < supportedLangs.length; i++) {
            rbmiLangQ[i] = new JRadioButtonMenuItem(supportedLangs[i]);
            bgLangQ.add(rbmiLangQ[i]);
            rbmiLangQ[i].setOpaque(false);
            rbmiLangQ[i].setBorderPainted(false);
            rbmiLangQ[i].setBackground(new Color(0, 0, 0, 0));
            add(rbmiLangQ[i]);
            
            rbmiLangA[i] = new JRadioButtonMenuItem(supportedLangs[i]);
            bgLangA.add(rbmiLangA[i]);
            rbmiLangA[i].setOpaque(false);
            rbmiLangA[i].setBorderPainted(false);
            rbmiLangA[i].setBackground(new Color(0, 0, 0, 0));
            add(rbmiLangA[i]);
        }
        
        if (supportedLangs.length >= 2) {
            rbmiLangA[1].setSelected(true);
            rbmiLangQ[0].setSelected(true);
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
        super.setBounds(x, y, width, height);
        
        // Set the location and size of the start button.
        startButton.setSize(getWidth() / 3, getHeight() / 4);
        startButton.setLocation(getWidth() / 3, getHeight() / 3);
        
        int radioWidth = 200;
        int radioHeight = 20;
        int yLoc = startButton.getY() + startButton.getHeight() + 10;
        int xLocQ = getWidth() / 3;
        int xLocA = getWidth() * 4 / 7;
        
        // Set the location and size of the labels.
        labelQ.setSize(radioWidth, radioHeight);
        labelQ.setLocation(xLocQ, yLoc);
        labelA.setSize(radioWidth, radioHeight);
        labelA.setLocation(xLocA, yLoc);
        yLoc += radioHeight;
        
        // Set the location and size of the radio buttons.
        for (int i = 0; i < supportedLangs.length; i++) {
            rbmiLangQ[i].setSize(radioWidth, radioHeight);
            rbmiLangQ[i].setLocation(xLocQ, yLoc);
            rbmiLangA[i].setSize(radioWidth, radioHeight);
            rbmiLangA[i].setLocation(xLocA, yLoc);
            yLoc += radioHeight;
        }
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    public String getLangQ() {
        for (JRadioButtonMenuItem item : rbmiLangQ) {
            if (item.isSelected()) {
                return item.getText();
            }
        }
        
        return null;
    }
    
    public String getLangA() {
        for (JRadioButtonMenuItem item : rbmiLangA) {
            if (item.isSelected()) {
                return item.getText();
            }
        }
        
        return null;
    }
    
    /* 
     * Paints the images on the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        if (background != null) {
            // Retrieve the current g2d transformation.
            AffineTransform g2dTrans = g2d.getTransform();
            
            g2d.scale(((double) getWidth()) / background.getWidth(),
                      ((double) getHeight()) / background.getHeight());
            g2d.drawImage(background, 0, 0, null);
            
            // Restore the g2d transformation.
            g2d.setTransform(g2dTrans);
        }
    }
}


