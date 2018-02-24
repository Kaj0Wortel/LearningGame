
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       03-02-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.font;


// Own packages
import learningGame.LearningGame;
import learningGame.log.Log2;


// Java packages
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

import java.io.FileInputStream;
import java.io.IOException;


public class FontLoader {
    final private static String staticPath = LearningGame.workingDir + "font\\";
    
    final public static String COUSINE    = "Cousine";
    final public static String COUSINE_I  = "Cousine-Italic";
    final public static String COUSINE_B  = "Cousine-Bold";
    final public static String COUSINE_BI = "Cousine-BoldItalic";
    
    final public static Font Cousine   = loadFont("cousine\\Cousine-Regular.ttf"   , Font.PLAIN);
    final public static Font CousineI  = loadFont("cousine\\Cousine-Italic.ttf"    , Font.ITALIC);
    final public static Font CousineB  = loadFont("cousine\\Cousine-Bold.ttf"      , Font.BOLD);
    final public static Font CousineBI = loadFont("cousine\\Cousine-BoldItalic.ttf", Font.BOLD + Font.ITALIC);
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This is a singleton class. No instances should be made.
     */
    @Deprecated
    private FontLoader() { }
    
    /* 
     * Loads a font from a file.
     * 
     * @param localPath the path of the font file relative to the font directoy
     * @param style the style of teh font. Should be one of: {@code Font.PLAIN},
     *     {@code Font.ITALIC}, {@code Font.BOLD} or {@code Font.BOLD + Font.ITALIC}
     */
    public static Font loadFont(String localPath, int style) {
        Font font = null;
        
        try (FileInputStream fis = new FileInputStream(staticPath + localPath)) {
            font = Font.createFont(Font.TRUETYPE_FONT, fis).deriveFont(style, 12F);
            
        } catch (FontFormatException e) {
            Log2.write(e);
            
        } catch (IOException e) {
            Log2.write(e);
        }
        
        return font;
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Registers the given font.
     * When successfull, the font can be used in the application for e.g. html code.
     * 
     * @param font the font to be registered
     */
    public static boolean registerFont(Font font) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(FontLoader.CousineB);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Static
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Registers all fonts from this class.
     * See registerFont(Font) for more info about registering a font.
     */
    static {
        Font errorFont = null;
        
        Font[] fonts = {
            Cousine, CousineI, CousineB, CousineBI
        };
        
        for (int i = 0; i < fonts.length; i++) {
            if (fonts[i] == null) continue;
            
            if (!registerFont(fonts[i])) {
                Log2.write(new Object[] {("Could not register font: "), fonts[i]}, Log2.WARNING);
            }
        }
    }
    
}
