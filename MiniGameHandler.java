
package learningGame;


// Owm packages
import learningGame.log.Log2;


// Java packages


public class MiniGameHandler {
    final private Word word;
    final private LearningGame lg;
    
    private enum State {
    STATE_NONE,
        STATE_SHOW_WORD_SCREEN, STATE_END_WORD_SCREEN,
        STATE_SHOW_MINI_GAME, STATE_END_MINI_GAME,
        STATE_SHOW_SCORE_SCREEN, STATE_FINISHED;
    }
    
    // The current state
    private State state = State.STATE_NONE;
    
    // Screens
    private WordScreen wordScreen;
    private MiniGame miniGame;
    private ScoreScreen scoreScreen;
    
    // Question info
    String langQ;
    String langA;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public MiniGameHandler(LearningGame lg, Word word, String langQ, String langA) {
        if (lg == null) throw new NullPointerException("No main frame was given as input!");
        if (word == null) throw new NullPointerException("No word was given as input!");
        if (langQ == null) throw new NullPointerException("No question language was given as input!");
        if (langA == null) throw new NullPointerException("No answer language was given as input!");
        
        this.word = word;
        this.lg = lg;
        this.langQ = langQ;
        this.langA = langA;
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Flow functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    public void begin() {
        if (state != State.STATE_NONE) {
            Log2.write(new String[] {
                "IllegalStateException in " + this.getClass() + "occured!",
                    "Expected: STATE_NONE. Found: " + state.toString()
            }, Log2.ERROR);
            throw new IllegalStateException("Incorrect state: " + state.toString() + " (expected: STATE_NONE).");
        }
        
        // Change state
        state = State.STATE_SHOW_WORD_SCREEN;
        
        
        // todo
    }
    
    public void endWordScreen() {
        if (state != State.STATE_SHOW_WORD_SCREEN) {
            Log2.write(new String[] {
                "IllegalStateException in " + this.getClass() + "occured!",
                    "Expected: STATE_SHOW_WORD_SCREEN. Found: " + state.toString()
            }, Log2.ERROR);
            throw new IllegalStateException("Incorrect state: " + state.toString() + " (expected: STATE_SHOW_WORD_SCREEN).");
        }
        
        // Change state
        state = State.STATE_END_WORD_SCREEN;
        
        
        // todo
        
        
        startMiniGame();
    }
    
    private void startMiniGame() {
        if (state != State.STATE_END_WORD_SCREEN) {
            Log2.write(new String[] {
                "IllegalStateException in " + this.getClass() + "occured!",
                    "Expected: STATE_END_WORD_SCREEN. Found: " + state.toString()
            }, Log2.ERROR);
            throw new IllegalStateException("Incorrect state: " + state.toString() + " (expected: STATE_END_WORD_SCREEN).");
        }
        
        // Change state
        state = State.STATE_SHOW_MINI_GAME;
        
        
        // todo
    }
    
    public void miniGameEnded() {
        if (state != State.STATE_SHOW_MINI_GAME) {
            Log2.write(new String[] {
                "IllegalStateException in " + this.getClass() + "occured!",
                    "Expected: STATE_SHOW_MINI_GAME. Found: " + state.toString()
            }, Log2.ERROR);
            throw new IllegalStateException("Incorrect state: " + state.toString() + " (expected: STATE_SHOW_MINI_GAME).");
        }
        
        // Change state
        state = State.STATE_END_MINI_GAME;
        
        
        //todo
        
        
        startScoreScreen();
    }
    
    public void startScoreScreen() {
        if (state != State.STATE_END_MINI_GAME) {
            Log2.write(new String[] {
                "IllegalStateException in " + this.getClass() + "occured!",
                    "Expected: STATE_END_MINI_GAME. Found: " + state.toString()
            }, Log2.ERROR);
            throw new IllegalStateException("Incorrect state: " + state.toString() + " (expected: STATE_END_MINI_GAME).");
        }
        
        // Change state
        state = State.STATE_SHOW_SCORE_SCREEN;
        
        
        // todo
    }
    
    private void endScoreScreen() {
        if (state != State.STATE_SHOW_SCORE_SCREEN) {
            Log2.write(new String[] {
                "IllegalStateException in " + this.getClass() + "occured!",
                    "Expected: STATE_SHOW_SCORE_SCREEN. Found: " + state.toString()
            }, Log2.ERROR);
            throw new IllegalStateException("Incorrect state: " + state.toString() + " (expected: STATE_SHOW_SCORE_SCREEN).");
        }
        
        // Change state
        state = State.STATE_FINISHED;
        
        
        //todo
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
}

