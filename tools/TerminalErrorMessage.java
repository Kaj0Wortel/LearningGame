package learningGame.tools;

// Java packages
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import learningGame.log.Log2;
/* 
 * Creates a terminal error message.
 * Gives a message to the user about the error.
 * Kills the program after the user presses the ok button.
 */
public class TerminalErrorMessage extends Error {
    private static Boolean terminalMessageStarted = false;
    
    public TerminalErrorMessage (String errorMessage, Object... data) {
        super();
        
        // Checks if another thread is has already invoked this method.
        synchronized(terminalMessageStarted) {
            if (terminalMessageStarted) {
                return;
                
            } else {
                terminalMessageStarted = true;
            }
        }
        
        JFrame errorFrame = new JFrame("Error");
        JPanel errorPanel = new JPanel();
        JLabel errorText_1 = new JLabel("A fatal error occured:");
        JLabel errorTextCustom = new JLabel(errorMessage);
        JLabel errorText_2 = new JLabel("Check \"log\\log.txt\" for more specific info.");
        JButton ok = new JButton("ok");
        
        errorFrame.add(errorPanel);
        errorPanel.add(errorText_1);
        errorPanel.add(errorTextCustom);
        errorPanel.add(errorText_2);
        errorPanel.add(ok);
        
        errorPanel.setLayout(null);
        
        errorText_1.setLocation(85, 10);
        errorTextCustom.setLocation(10, 30);
        errorText_2.setLocation(30, 50);
        ok.setLocation(100, 70);
        
        errorText_1.setSize(150, 15);
        errorTextCustom.setSize(300, 15);
        errorText_2.setSize(300, 15);
        ok.setSize(80, 30);
        
        errorFrame.setSize(300, 150);
        errorFrame.setLocationRelativeTo(null);
        errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        errorFrame.setVisible(true);
        
        ok.addActionListener((e) -> {
            System.exit(0);
        });
        
        Log2.write(new Object[] {
            "= = = = = = = = = = = = = = = =   TERMINAL ERROR!   = = = = = = = = = = = = = = = =",
                "Word list is empty or consists of one element.",
                data,
                " === STACK === ",
                (new Throwable()).getStackTrace(), // This is 10x faster then Thread.currentThread.getStackTrace()
                " === END TERMINAL ERROR MESSAGE === ",
                ""
        }, Log2.ERROR);
    }
    
    public static void main(String[] args) {
        throw new TerminalErrorMessage("test", "line 1", "line 2");
    }
    
}