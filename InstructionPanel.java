
package learningGame;


// Own packages
import learningGame.LearningGame;

import learningGame.log.Log2;

import learningGame.tools.Button2;
import learningGame.tools.LoadImages2;


// Java packages
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;


// tmp
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.BorderFactory;


public class InstructionPanel extends JPanel {
    private JLabel label;
    private Button2 button;
    final private static int BAR_WIDTH = 20;
    
    public InstructionPanel(String text, String buttonText, Runnable r) {
        super(null);
        
        setBackground(new Color(255, 172, 100));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                                                     BorderFactory.createLoweredBevelBorder()));
        
        label = new JLabel("<html>" + text + "</html>");
        add(label);
        
        try {
            button = new Button2(0, 0, 15, "Start!");
            add(button);
            
            ActionListener al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    r.run();
                    button.removeActionListener(this);
                }
            };
            button.addActionListener(al);
            
        } catch (IOException e) {
            e.printStackTrace();
            Log2.write(e);
        }
        
    }
    
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        
        int compWidth = width - 2*BAR_WIDTH;
        int buttonHeight = (int) ((height  - 2*BAR_WIDTH) * 0.2);
        int labelHeight = height - 2*BAR_WIDTH - buttonHeight;
        
        // Sets the size and location of the label and the button,
        // and give the size a lowerbound at (0, 0).
        if (compWidth < 0) {
            label.setSize(0, 0);
            button.setSize(0, 0);
            
        } else {
            if (labelHeight < 0) {
                label.setSize(0, 0);
                
            } else {
                label.setSize(compWidth, labelHeight);
                label.setLocation(BAR_WIDTH, BAR_WIDTH);
            }
            
            if (buttonHeight < 0) {
                button.setSize(0, 0);
                
            } else {
                button.setSize(compWidth, buttonHeight);
                button.setLocation(BAR_WIDTH, BAR_WIDTH + labelHeight);
            }
        }
        
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
    
}


