
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       02-02-2017      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools;


// Java packages
import javax.swing.KeyStroke;


public class Key {
    // Alphabet
    final public static Key A = new Key(65);
    final public static Key B = new Key(66);
    final public static Key C = new Key(67);
    final public static Key D = new Key(68);
    final public static Key E = new Key(69);
    final public static Key F = new Key(70);
    final public static Key G = new Key(71);
    final public static Key H = new Key(72);
    final public static Key I = new Key(73);
    final public static Key J = new Key(74);
    final public static Key K = new Key(75);
    final public static Key L = new Key(76);
    final public static Key M = new Key(77);
    final public static Key N = new Key(78);
    final public static Key O = new Key(79);
    final public static Key P = new Key(80);
    final public static Key Q = new Key(81);
    final public static Key R = new Key(82);
    final public static Key S = new Key(83);
    final public static Key T = new Key(84);
    final public static Key U = new Key(85);
    final public static Key V = new Key(86);
    final public static Key W = new Key(87);
    final public static Key X = new Key(88);
    final public static Key Y = new Key(89);
    final public static Key Z = new Key(90);
    
    // Numbers
    final public static Key N_0 = new Key(48);
    final public static Key N_1 = new Key(49);
    final public static Key N_2 = new Key(50);
    final public static Key N_3 = new Key(51);
    final public static Key N_4 = new Key(52);
    final public static Key N_5 = new Key(53);
    final public static Key N_6 = new Key(54);
    final public static Key N_7 = new Key(55);
    final public static Key N_8 = new Key(56);
    final public static Key N_9 = new Key(57);
    
    // Arrow keys
    final public static Key LEFT  = new Key(37);
    final public static Key UP    = new Key(38);
    final public static Key RIGHT = new Key(39);
    final public static Key DOWN  = new Key(40);
    
    // Function keys
    final public static Key F1  = new Key(112);
    final public static Key F2  = new Key(113);
    final public static Key F3  = new Key(114);
    final public static Key F4  = new Key(115);
    final public static Key F5  = new Key(116);
    final public static Key F6  = new Key(117);
    final public static Key F7  = new Key(118);
    final public static Key F8  = new Key(119);
    final public static Key F9  = new Key(120);
    final public static Key F10 = new Key(121);
    final public static Key F11 = new Key(122);
    final public static Key F12 = new Key(123);
    
    // Other keys
    final public static Key ENTER   = new Key(10);
    final public static Key SPACE   = new Key(32);
    final public static Key SHIFT   = new Key(16);
    final public static Key CTRL    = new Key(46);
    final public static Key WINDOWS = new Key(524);
    final public static Key DOT     = new Key(46);
    final public static Key COMMA   = new Key(44);
    final public static Key SLASH   = new Key(47);
    final public static Key BSLASH  = new Key(92);
    final public static Key DEL     = new Key(127);
    final public static Key ESC     = new Key(27);
    final public static Key BACKSPACE = new Key(8);
    final public static Key MINUS   = new Key(45);
    final public static Key EQUAL   = new Key(61);
    
    // Stores the key value.
    final private Integer key;
    
    /* 
     * Constructor
     * 
     * @param key value for this key.
     */
    public Key(int key) {
        this.key = key;
    }
    
    /* 
     * @return the key value.
     */
    public int getKey() {
        return key;
    }
    
    public KeyStroke toKeyStroke() {
        return KeyStroke.getKeyStroke((char) (int) key, 0);
    }
    
    /* 
     * Checks if two keys are equal. If just a number is given, compare the number with
     * the key value of the Key object
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Key) {
            return key == ((Key) obj).key;
            
        } else if (obj instanceof Number) {
            return key.equals((Integer) obj);
            
        } else if (obj instanceof KeyStroke) {
            return KeyStroke.getKeyStroke((char) (int) key).equals((KeyStroke) obj);
            
        } else {
            return false;
        }
    }
    
}


