
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

import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;


// tmp
import javax.swing.JFrame;




public class InstructionPanel extends JPanel {
    private JLabel label;
    private Button2 button;
    
    public InstructionPanel(String text, String buttonText, Runnable r) {
        super(null);
        
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);
        
        label = new JLabel("<html>" + text + "</html>");
        add(label);
        
        try {
            button = new Button2(10, 0, 0, "Start!");
            add(button);
            
            button.addActionListener((e) -> {
                if (r != null) r.run();
            });
            
        } catch (IOException e) {
            e.printStackTrace();
            Log2.write(e);
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        
        int compWidth = width - 2*25;
        int buttonHeight = (int) ((height  - 2*25) * 0.2);
        int labelHeight = height - 2*25 - buttonHeight;
        
        if (compWidth < 0) {
            label.setSize(0, 0);
            button.setSize(0, 0);
        } else {
            if (labelHeight < 0) {
                label.setSize(0, 0);
            } else {
                label.setSize(compWidth, labelHeight);
            }
            
            if (buttonHeight < 0) {
                label.setSize(0, 0);
            } else {
                label.setSize(compWidth, buttonHeight);
            }
        }
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        
    }
    
    
    public static void main(String[] args) {
        
    }
    
}


