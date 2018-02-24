
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       31-03-2017      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools;


// Java packages
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;


public class KeyDetector extends KeyAdapter {
    // List containing all keys that are currently pressed.
    private ArrayList<Key> keysCurPressed = new ArrayList<Key>();
    
    // List containing all keys that were at least once pressed between the last update and now.
    private ArrayList<Key> keysPressedSinceLastUpdate = new ArrayList<Key>();
    
    // List containing all keys that were pressed between the last two updates.
    // This list is only updated by the update method, and not via events.
    private ArrayList<Key> keysPressedHistory = new ArrayList<Key>();
    
    /* 
     * Method is called when a key was pressed.
     * Adds the new key both {@code keysCurPressed} and {@code keysPressedSinceLastUpdate}
     * if it is not in there yet.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Key key = new Key(e.getExtendedKeyCode());
        
        if (!keysCurPressed.contains(key)) {
            keysCurPressed.add(key);
        }
        
        if (!keysPressedSinceLastUpdate.contains(key)) {
            keysPressedSinceLastUpdate.add(key);
        }
    }
    
    /* 
     * Method is called when a key was released.
     * Removes the key from {@code keysCurPressed}.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        Key key = new Key(e.getExtendedKeyCode());
        keysCurPressed.remove(key);
    }
    
    
    /* 
     * Returns true iff the given key was pressed between the two last updates.
     * 
     * @param key key-value to check for.
     */
    public boolean isPressed(Key key) {
        return keysPressedHistory.contains(key);
    }
    
    /* 
     * Returns all keys that were pressed between the two last updates.
     */
    public Key[] getKeysPressed() {
        return MultiTool.toArray(keysPressedHistory);
    }
    
    /* 
     * Clears all lists.
     */
    public void clear() {
        keysCurPressed.clear();
        keysPressedSinceLastUpdate.clear();
        keysPressedHistory.clear();
    }
    
    /* 
     * Updates the lists to their new status.
     */
    public void update() {
        keysPressedHistory = MultiTool.copyArrayList(keysPressedSinceLastUpdate);
        keysPressedSinceLastUpdate = MultiTool.copyArrayList(keysCurPressed);
    }
}

