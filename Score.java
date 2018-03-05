
package learningGame;

// Own packages


// Java classes
import java.util.ArrayList;


public class Score {
    private long gamePoints;
    private boolean wordCorrect;
    
    /*
     * Constructor
     */
    public Score(long gamePoints, boolean wordCorrect) {
        this.gamePoints = gamePoints;
        this.wordCorrect = wordCorrect;
    }
    
    
    /*
     * Prints the current score. Todo.
     */
    @Override
    public String toString() {
        return "";
    }
}