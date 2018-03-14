
package learningGame;


// Own packages
import learningGame.font.FontLoader;

import learningGame.log.Log2;

import learningGame.tools.Button2;
import learningGame.tools.MultiTool;
import learningGame.tools.TerminalErrorMessage;


// Java pacakges
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// note: update order of imports!
// tmp
import javax.swing.JFrame;


/* 
 * Gives a visual representation of the given score object.
 */
public class ScoreScreen extends JPanel {
    // The spacing on the sides of the screen.
    final private static int BAR_SIZE = 20;
    
    // The distance between the components.
    final private static int COMP_DIST = 10;
    
    // The score object to represent.
    final private Score score;
    
    // The question and answer language.
    final private String langQ;
    final private String langA;
    
    // The text for the continue button.
    final private String continueName;
    
    // Whether to give the full representation.
    final private boolean full;
    
    // The function that is executed when the continue button has been pressed.
    final private Runnable r;
    
    // GUI
    // The labels and panels that display the score.
    private JScrollPane wrongWordsScrollPane;
    private JLabel wrongWordsLabel;
    
    private JLabel correctWordRatio;
    private JLabel gameRatio;
    
    // The continue button.
    private Button2 continueBtn;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public ScoreScreen(Score score, String langQ, String langA, String continueName, boolean full, Runnable r) {
        super(null);
        
        if (score == null)
            throw new TerminalErrorMessage("No score available!",
                                           "The score object to be displayed was null.");
        
        this.score = score;
        this.langQ = langQ;
        this.langA= langA;
        this.continueName = continueName;
        this.full = full;
        this.r = r;
        
        createGUI();
        
        // JScrollPane's have some display issues when created and then directly shown directly after.
        if (wrongWordsScrollPane != null) {
            SwingUtilities.invokeLater(() -> wrongWordsScrollPane.getViewport().revalidate());
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates the GUI of the application
     */
    public void createGUI() {
        float fontSize = 20F;
        Font fontUsed = FontLoader.getLocalFont("cousine\\Cousine-Bold.ttf").deriveFont(fontSize);
        
        if (full) {
            String wrongWordsText = "<html><br>   Incorrect words:"
                + "<br><br>   " + MultiTool.fillSpaceRight(langQ, 30) + "   " + langA;
            ArrayList<Word> list = score.listWrongWords();
            for (int i = 0; i < list.size(); i++) {
                String question = list.get(i).getWord(langQ);
                String answer = list.get(i).getWord(langA);
                wrongWordsText += "   <br>   " + MultiTool.fillSpaceRight(question, 30) + " = " + answer;
            }
            wrongWordsText += "</html>";
            
            wrongWordsLabel = new JLabel(MultiTool.toHTMLSpace(wrongWordsText));
            wrongWordsLabel.setVerticalAlignment(JLabel.TOP);
            wrongWordsLabel.setVerticalTextPosition(JLabel.TOP);
            
            wrongWordsScrollPane = new JScrollPane(wrongWordsLabel);
            wrongWordsScrollPane.setAlignmentY(TOP_ALIGNMENT);
            wrongWordsLabel.setFont(fontUsed);
            
            add(wrongWordsScrollPane);
            
            String ratio = MultiTool.doubleToStringDecimals(100*score.calcCorrectRatio(), 2) + "%";
            correctWordRatio = new JLabel("<html>Words correct: " + ratio + "</html>");
            
        } else {
            correctWordRatio = new JLabel("<html>Mistakes made: " + score.calcMistakes() + "</html>");
        }
        
        correctWordRatio.setFont(fontUsed);
        correctWordRatio.setHorizontalAlignment(SwingConstants.CENTER);
        add(correctWordRatio);
        
        String gameScore = MultiTool.doubleToStringDecimals(100*score.calcAvgGameScore(), 2) + "%";
        gameRatio = new JLabel("<html>Game score: " + gameScore + "</html>");
        gameRatio.setFont(fontUsed);
        gameRatio.setHorizontalAlignment(SwingConstants.CENTER);
        add(gameRatio);
        
        try {
            continueBtn = new Button2(0, 0, 20, continueName);
            
        } catch (IOException e) {
            Log2.write(e);
            e.printStackTrace();
        }
        
        continueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().contains("released")) {
                    if (r != null) r.run();
                    continueBtn.removeActionListener(this);
                }
            }
        });
        
        continueBtn.setFont(fontUsed);
        continueBtn.setTextSize(fontSize);
        add(continueBtn);
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (full) {
            int newWidth;
            int newHeight;
            
            if (height * MiniGameHandler.ASPECT_RATIO > width) {
                newWidth = width;
                newHeight = (int) (width / MiniGameHandler.ASPECT_RATIO);
                
            } else {
                newWidth = (int) (height * MiniGameHandler.ASPECT_RATIO);
                newHeight = height;
            }
            
            x = (width  - newWidth)  / 2;
            y = (height - newHeight) / 2;
            width = newWidth;
            height = newHeight;
        }
        
        super.setBounds(x, y, width, height);
        
        int compWidth = width - 2*BAR_SIZE;
        
        if (continueBtn != null) {
            continueBtn.setSize(compWidth, (int) (height * 0.1));
            continueBtn.setLocation(BAR_SIZE, height - BAR_SIZE - continueBtn.getHeight());
        }
        
        if (full) {
            if (correctWordRatio != null) {
                correctWordRatio.setSize(compWidth, 30);
                correctWordRatio.setLocation(BAR_SIZE, BAR_SIZE);
            }
            
            if (gameRatio != null) {
                gameRatio.setSize(compWidth, 30);
                gameRatio.setLocation(BAR_SIZE, BAR_SIZE + correctWordRatio.getHeight());
            }
            
            if (wrongWordsScrollPane != null) {
                wrongWordsScrollPane.setSize(compWidth, height - 2*BAR_SIZE - gameRatio.getHeight()
                                                 - correctWordRatio.getHeight() - continueBtn.getHeight()
                                                 - 2*COMP_DIST);
                wrongWordsScrollPane.setLocation(BAR_SIZE, BAR_SIZE + correctWordRatio.getHeight()
                                                     + correctWordRatio.getHeight() + COMP_DIST);
            }
            
        } else {
            if (correctWordRatio != null) {
                correctWordRatio.setSize(compWidth, 30);
                correctWordRatio.setLocation(BAR_SIZE, height / 2 - correctWordRatio.getHeight() - 50);
            }
            
            if (gameRatio != null) {
                gameRatio.setSize(compWidth, 30);
                gameRatio.setLocation(BAR_SIZE, height / 2 - gameRatio.getHeight() + 50);
            }
        }
        
    }
    
    
    
    
    // tmp
    public static void main(String[] args) {
        String tmp = LearningGame.WORKING_DIR;
        
        JFrame frame = new JFrame("test");
        frame.setLayout(null);
        frame.setLocation(50, 50);
        frame.setSize(800, 800);
        
        Score score = new Score(100, 100,
                                new Word(new String[] {"appel", "apple"},
                                         new String[] {"Dutch", "English"},
                                         MiniGame.class, "test"), 0);
        
        Score score_add1 = new Score(75, 100,
                                     new Word(new String[] {"broodrooster", "toaster"},
                                              new String[] {"Dutch", "English"},
                                              MiniGame.class, "test"), 0);
        
        Score score_add2 = new Score(25, 100,
                                     new Word(new String[] {"1", "2"},
                                              new String[] {"Dutch", "English"},
                                              MiniGame.class, "test"), 1);
        
        Score score_add3 = new Score(0, 100,
                                     new Word(new String[] {"3", "4"},
                                              new String[] {"Dutch", "English"},
                                              MiniGame.class, "test"), 1);
        
        Score score_add4 = new Score(50, 100,
                                     new Word(new String[] {"5", "6"},
                                              new String[] {"Dutch", "English"},
                                              MiniGame.class, "test"), 1);
        score.add(score_add1);
        score.add(score_add2);
        score.add(score_add3);
        score.add(score_add4);
        score.add(score_add4);
        
        ScoreScreen ss = new ScoreScreen(score, "Dutch", "English", "Continue", true, null);
        frame.add(ss);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        while(true) {
            java.awt.Insets in = frame.getInsets();
            ss.setSize(frame.getWidth() - in.left - in.right, frame.getHeight() - in.top - in.bottom);
        }
    }
    
}
