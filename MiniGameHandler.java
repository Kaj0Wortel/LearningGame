
package learningGame;


// Owm packages
import learningGame.log.Log2;

import learningGame.tools.ModCursors;


// Java packages


public class MiniGameHandler {
    final private Word word;
    final private LearningGame lg;
    
    private enum State {
    STATE_NONE,
        STATE_SHOW_WORD_SCREEN, STATE_END_WORD_SCREEN,
        STATE_SHOW_MINI_GAME, STATE_END_MINI_GAME,
        STATE_SHOW_SCORE_SCREEN, STATE_END_SCORE_SCREEN,
        STATE_FINISHED;
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
    /* 
     * Method to handle the setup of the word screen.
     */
    public void begin() {
        // Check state
        checkState(State.STATE_NONE);
        
        // Change state
        state = State.STATE_SHOW_WORD_SCREEN;
        
        
        // todo
    }
    
    /* 
     * Method to handle the ending of the word screen.
     */
    public void endWordScreen() {
        // Check state
        checkState(State.STATE_SHOW_WORD_SCREEN);
        
        // Change state
        state = State.STATE_END_WORD_SCREEN;
        
        
        // todo
        
        
        startMiniGame();
    }
    
    /* 
     * Method to handle the setup of the miniGame.
     */
    private void startMiniGame() {
        // Check state
        checkState(State.STATE_END_WORD_SCREEN);
        
        // Change state
        state = State.STATE_SHOW_MINI_GAME;
        
        
        // todo
    }
    
    /* 
     * Method to handle the ending of the miniGame.
     */
    public void miniGameEnded() {
        // Check state
        checkState(State.STATE_SHOW_MINI_GAME);
        
        // Change state
        state = State.STATE_END_MINI_GAME;
        
        
        //todo
        
        
        startScoreScreen();
    }
    
    /* 
     * Method to handle the setup of the score screen.
     */
    public void startScoreScreen() {
        // Check state
        checkState(State.STATE_END_MINI_GAME);
        
        // Change state
        state = State.STATE_SHOW_SCORE_SCREEN;
        
        
        // todo
    }
    
    /* 
     * Method to handle the ending of the score screen.
     */
    private void endScoreScreen() {
        // Check state
        checkState(State.STATE_SHOW_SCORE_SCREEN);
        
        // Change state
        state = State.STATE_END_SCORE_SCREEN;
        
        
        //todo
        
        
        cleanUp();
    }
    
    /* 
     * Final clean-up method
     */
    private void cleanUp() {
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    private void checkState(State check) throws IllegalStateException {
        if (state != check) {
            Log2.write(new String[] {
                "IllegalStateException in " + this.getClass() + "occured!",
                    "Expected: " + check.toString() + ". Found: " + state.toString()
            }, Log2.ERROR);
            throw new IllegalStateException("Incorrect state: " + state.toString()
                                                + " (expected: " + check.toString() + ").");
        }
    }
    
}

