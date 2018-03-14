
package learningGame;


// Owm packages
import learningGame.log.Log2;

import learningGame.tools.KeyDetector;
import learningGame.tools.ModCursors;
import learningGame.tools.TerminalErrorMessage;


// Java packages


public class MiniGameHandler {
    // Screen aspect ratio.
    final public static double ASPECT_RATIO = 4.0/3.0; // width / height
    
    // The word.
    final private Word word;
    
    // The learningGame.
    final private LearningGame lg;
    
    // The question language.
    final private String langQ;
    
    // The answer language.
    final private String langA;
    
    // The action that is executed when the correct button has been pressed.
    final private Runnable r;
    
    // The time out for the MiniGame.
    final private long timeOut;
    
    // State enum class.
    private enum State {
        STATE_NONE,
            STATE_SHOW_WORD_SCREEN, STATE_END_WORD_SCREEN,
            STATE_SHOW_MINI_GAME, STATE_END_MINI_GAME,
            STATE_SHOW_SCORE_SCREEN, STATE_END_SCORE_SCREEN,
            STATE_FINISHED;
    }
    
    // The location and size of the active panel.
    private int x;
    private int y;
    private int width;
    private int height;
    
    // The current state
    private State state = State.STATE_NONE;
    
    // Screens
    private WordScreen wordScreen;
    private MiniGame miniGame;
    private ScoreScreen scoreScreen;
    
    // The keydetecor used for this application
    private KeyDetector kd = null;
    
    // The score of the MiniGame;
    private Score score;
    
    // The number of wrong choices were made in the wordScreen.
    private int mistakes = 0;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @param lg the current learningGame object.
     * @param word the word to make the miniGame from.
     * @param langQ the question language.
     * @param langA the answer language.
     * @param r the runnable that is executed after the miniGame all actions are completed.
     * @throws NullPointerException iff any of the inputs except {@code r} are null.
     */
    public MiniGameHandler(LearningGame lg, Word word, String langQ, String langA, Runnable r, long timeOut) {
        if (lg == null) throw new NullPointerException("No main frame was given as input!");
        if (word == null) throw new NullPointerException("No word was given as input!");
        if (langQ == null) throw new NullPointerException("No question language was given as input!");
        if (langA == null) throw new NullPointerException("No answer language was given as input!");
        
        this.word = word;
        this.lg = lg;
        this.langQ = langQ;
        this.langA = langA;
        this.r = r;
        this.timeOut = timeOut;
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
        
        // Create new wordScreen
        wordScreen = new WordScreen(word, langQ, langA, () -> endWordScreen());
        
        // Change state
        state = State.STATE_SHOW_WORD_SCREEN;
        
        // Set settings of the wordScreen
        lg.add(wordScreen);
        setBounds(x, y, width, height);
    }
    
    /* 
     * Method to handle the ending of the word screen.
     */
    public void endWordScreen() {
        // Check state
        checkState(State.STATE_SHOW_WORD_SCREEN);
        
        // Change state
        state = State.STATE_END_WORD_SCREEN;
        
        // Remove wordscreen actions
        lg.remove(wordScreen);
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
        mistakes = wordScreen.getNumMistakes();
        
        startMiniGame();
    }
    
    /* 
     * Method to handle the setup of the miniGame.
     */
    private void startMiniGame() {
        // Check state
        checkState(State.STATE_END_WORD_SCREEN);
        
        // Create new miniGame
        miniGame = word.createMiniGame(lg, () -> {
            miniGameEnded();
        }, timeOut);
        
        if (miniGame == null) {
            throw new TerminalErrorMessage("No mini game was available!",
                                           "Attempted to create MiniGame \"" + word.getMiniGameClass().getName() + "\"",
                                           "    of word \"" + word.toString() + "\", but failed.");
        }
        
        // Change state
        state = State.STATE_SHOW_MINI_GAME;
        // Set settings of the miniGame and start it
        lg.add(miniGame);
        miniGame.setLocation(x, y);
        miniGame.setSize(width, height);
        miniGame.useKeyDetector(kd);
        miniGame.start();
        setBounds(x, y, width, height);
    }
    
    /* 
     * Method to handle the ending of the miniGame.
     */
    public void miniGameEnded() {
        // Check state
        checkState(State.STATE_SHOW_MINI_GAME);
        
        // Change state
        state = State.STATE_END_MINI_GAME;
        
        // Remove miniGame actions
        score = miniGame.getScore(word, mistakes);
        lg.remove(miniGame);
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
        
        startScoreScreen();
    }
    
    /* 
     * Method to handle the setup of the score screen.
     */
    public void startScoreScreen() {
        // Check state
        checkState(State.STATE_END_MINI_GAME);
        
        // Create new scoreScreen
        if (miniGame == null ) {
            Log2.write("MiniGame was null!", Log2.ERROR);
            cleanUp();
            return;
            
        } else {
            scoreScreen = new ScoreScreen(score, langQ, langA, "Continue", false, () -> endScoreScreen());
            scoreScreen.setSize(width, height);
        }
        
        // Change state
        state = State.STATE_SHOW_SCORE_SCREEN;
        
        // Set settings of the score screen
        lg.add(scoreScreen);
        setBounds(x, y, width, height);
    }
    
    /* 
     * Method to handle the ending of the score screen.
     */
    private void endScoreScreen() {
        // Check state
        checkState(State.STATE_SHOW_SCORE_SCREEN);
        
        // Change state
        state = State.STATE_END_SCORE_SCREEN;
        
        lg.remove(scoreScreen);
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
        
        cleanUp();
    }
    
    /* 
     * Final clean-up method
     */
    private void cleanUp() {
        lg.setCursor(ModCursors.DEFAULT_CURSOR);
        wordScreen = null;
        miniGame = null;
        scoreScreen = null;
        
        if (r != null) r.run();
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Checks if the state is equal to the given state.
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
    
    /* 
     * The update method. Invokes the update method of the active panel.
     */
    public void update() {
        if (state == State.STATE_SHOW_WORD_SCREEN) {
            if (wordScreen == null) {
                Log2.write("Current wordScreen is null while it was active!", Log2.ERROR);
            }
            
        } else if (state == State.STATE_SHOW_MINI_GAME) {
            if (miniGame == null) {
                Log2.write("Current miniGame is null while it was active!", Log2.ERROR);

            } else {
                miniGame.update();
            }
            
        } else if (state == State.STATE_SHOW_SCORE_SCREEN) {
            if (scoreScreen == null) {
                Log2.write("Current scoreScreen is null while it was active!", Log2.ERROR);
            }
            
        } else if (!(state == State.STATE_NONE ||
              state == State.STATE_END_WORD_SCREEN ||
              state == State.STATE_END_MINI_GAME ||
              state == State.STATE_END_SCORE_SCREEN || 
              state == State.STATE_FINISHED)) {
            
            Log2.write("Current state is undefined!", Log2.ERROR);
        }
    }
    
    /* 
     * Sets the width and heigh of the panel currently shown.
     * 
     * @param width the new width of the panel that is currently shown.
     * @param height the new height of the panel that is currently shown.
     * 
     * Uses the method {@code setBounds(int, int, int, int)}.
     */
    public void setSize(int width, int height) {
        setBounds(getX(), getY(), width, height);
    }
    
    /* 
     * Sets the x and y location of the panel currently shown.
     * 
     * @param x the new x location of the panel that is currently shown.
     * @param y the new y location of the panel that is currently shown.
     * 
     * Uses the method {@code setBounds(int, int, int, int)}.
     */
    public void setLocation(int x, int y) {
        setBounds(x, y, getWidth(), getHeight());
    }
    
    /* 
     * Sets the size and location of the panel that is currently shown.
     * 
     * @param x the new x location of the panel that is currently shown.
     * @param y the new y location of the panel that is currently shown.
     * @param width the new width of the panel that is currently shown.
     * @param height the new height of the panel that is currently shown.
     * 
     * Uses the method {@code setBounds(int, int, int, int)}.
     */
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        int panelWidth;
        int panelHeight;
        if (height * ASPECT_RATIO > width) {
            panelWidth = width;
            panelHeight = (int) (width / ASPECT_RATIO);
            
        } else {
            panelWidth = (int) (height * ASPECT_RATIO);
            panelHeight = height;
        }
        
        int panelX = (width  - panelWidth)  / 2;
        int panelY = (height - panelHeight) / 2;
        
        
        if (state == State.STATE_SHOW_WORD_SCREEN) {
            if (wordScreen == null) {
                Log2.write("Current wordScreen is null while it was active!", Log2.ERROR);
                
            } else {
                wordScreen.setBounds(panelX, panelY, panelWidth, panelHeight);
            }
            
        } else if (state == State.STATE_SHOW_MINI_GAME) {
            if (miniGame == null) {
                Log2.write("Current miniGame is null while it was active!", Log2.ERROR);
                
            } else {
                miniGame.setBounds(panelX, panelY, panelWidth, panelHeight);
            }
            
        } else if (state == State.STATE_SHOW_SCORE_SCREEN) {
            if (scoreScreen == null) {
                Log2.write("Current scoreScreen is null while it was active!", Log2.ERROR);
                
            } else {
                scoreScreen.setBounds(panelX, panelY, panelWidth, panelHeight);
            }
            
        } else if (!(state == State.STATE_NONE ||
                     state == State.STATE_END_WORD_SCREEN ||
                     state == State.STATE_END_MINI_GAME ||
                     state == State.STATE_END_SCORE_SCREEN || 
                     state == State.STATE_FINISHED)) {
            
            Log2.write("Current state is undefined!", Log2.ERROR);
        }
    }
    
    /* 
     * Invokes the repaint method of the active panel.
     */
    public void repaint() {
        if (state == State.STATE_SHOW_WORD_SCREEN) {
            if (wordScreen == null) {
                Log2.write("Current wordScreen is null while it was active!", Log2.ERROR);
                
            } else {
                wordScreen.repaint();
            }
            
        } else if (state == State.STATE_SHOW_MINI_GAME) {
            if (miniGame == null) {
                Log2.write("Current miniGame is null while it was active!", Log2.ERROR);
            } else {
                miniGame.repaint();
            }
            
        } else if (state == State.STATE_SHOW_SCORE_SCREEN) {
            if (scoreScreen == null) {
                Log2.write("Current scoreScreen is null while it was active!", Log2.ERROR);
                
            } else {
                scoreScreen.repaint();
            }
            
        } else if (!(state == State.STATE_NONE ||
                     state == State.STATE_END_WORD_SCREEN ||
                     state == State.STATE_END_MINI_GAME ||
                     state == State.STATE_END_SCORE_SCREEN || 
                     state == State.STATE_FINISHED)) {
            
            Log2.write("Current state is undefined!", Log2.ERROR);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the x location of the active panel.
     */
    public int getX() {
        return x;
    }
    
    /* 
     * @return the y location of the active panel.
     */
    public int getY() {
        return y;
    }
    
    /* 
     * @return the width of the active panel.
     */
    public int getWidth() {
        return width;
    }
    
    /* 
     * @return the width of the active panel.
     */
    public int getHeight() {
        return height;
    }
    
    /* 
     * @return the score of the miniGame
     */
    public Score getScore() {
        return score;
    }
    
    public void useKeyDetector(KeyDetector kd) {
        this.kd = kd;
        
        if (miniGame != null) {
            miniGame.useKeyDetector(kd);
        }
    }
    
    
    public static void main(String[] args) {
        LearningGame lg = new LearningGame();
    }
    
}

