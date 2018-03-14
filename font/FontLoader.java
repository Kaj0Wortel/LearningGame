
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

import learningGame.tools.MultiTool;


// Java packages
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;


public class FontLoader {
    final private static String staticPath = LearningGame.WORKING_DIR + "font\\";
    final public static Hashtable<String, Font> fonts = new Hashtable<String, Font>();
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This is a singleton class. No instances should be made.
     */
    @Deprecated
    private FontLoader() { }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Loads a font from a file.
     * 
     * @param localPath the path of the font file relative to the font directoy.
     * @param path the path to the font file.
     * @param style the style of teh font. Should be one of: {@code Font.PLAIN},
     *     {@code Font.ITALIC}, {@code Font.BOLD} or {@code Font.BOLD + Font.ITALIC}
     */
    public static Font loadLocalFont(String localPath, int style) {
        return loadFont(staticPath + localPath, style);
    }
    
    public static Font loadFont(String path, int style) {
        return loadFont(path, style, Font.TRUETYPE_FONT);
    }
    
    public static Font loadFont(String path, int style, int type) {
        Font font = null;
        
        try (FileInputStream fis = new FileInputStream(path)) {
            //font = Font.createFont(type, fis).deriveFont(style, 12F);
            font = Font.createFont(type, fis).deriveFont(12F);
            fonts.put(path, font);
            
        } catch (FontFormatException | IOException e) {
            Log2.write(e);
        }
        
        return font;
    }
    
    /* 
     * @param fontName the name of the font.
     * @return the font.
     */
    public static Font getLocalFont(String localName) {
        return getFont(staticPath + localName);
    }
    
    public static Font getFont(String fontName) {
        return fonts.get(fontName);
    }
    
    /* 
     * Registers the given font.
     * When successfull, the font can be used in the application for e.g. html code.
     * 
     * @param font the font to be registered
     */
    public static boolean registerFont(Font font) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Static
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Registers all fonts in the fonts folder.
     * See registerFont(Font) for more info about registering a font.
     */
    static {
        Font[] allFonts = null;
        
        Log2.write(" === Start loading fonts === ", Log2.INFO);
        ArrayList<File[]> files = MultiTool.listFilesAndPathsFromRootDir(new File(staticPath), false);
        
        for (File[] file : files) {
            String fontLoc = file[0].toString();
            
            if (fontLoc.endsWith(".ttf")) {
                String fontString = fontLoc.toLowerCase();
                
                int style = (fontString.contains("bold") ||
                             fontString.endsWith("b.ttf") ||
                             fontString.endsWith("bi.ttf") ||
                             fontString.endsWith("ib.ttf") ? Font.BOLD : 0) |
                    (fontString.contains("italic") ||
                     fontString.contains("it") ||
                     fontString.endsWith("i.ttf") ||
                     fontString.endsWith("bi.ttf") ||
                     fontString.endsWith("ib.ttf") ? Font.ITALIC : 0);
                if (style == 0) style = Font.PLAIN;
                /*
                System.out.println(style == Font.PLAIN ? "PLAIN"
                                       : style == Font.BOLD ? "BOLD"
                                       : style == Font.ITALIC ? "ITALIC"
                                       : style == (Font.BOLD | Font.ITALIC) ? "BOLD + ITALIC"
                                       : "ERROR!");
                */
                
                Font font = loadFont(file[0].toString(), style, Font.TRUETYPE_FONT);
                if (font == null) {
                    Log2.write("A null font has been created: " + file[0].toString(), Log2.ERROR);
                    
                } else if (!registerFont(font)) {
                    if (allFonts == null) {
                        allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
                    }
                    
                    boolean isRegistered = false;
                    for (Font checkFont : allFonts) {
                        if (checkFont.getName().equals(font.getName())) {
                            isRegistered = true;
                            break;
                        }
                    }
                    
                    if (isRegistered) {
                        Log2.write("Font was already registered: " + file[0].toString(), Log2.WARNING);
                        
                    } else {
                        Log2.write("Could not register font: " + file[0].toString(), Log2.ERROR);
                    }
                    
                } else {
                    Log2.write("Successfully loaded font: " + file[0].toString(), Log2.INFO);
                }
            } else {
                Log2.write("Ignored file: " + file[0].toString(), Log2.INFO);
            }
            
        }
        
        Log2.write(new String[] {" === Finished loading fonts === ", ""}, Log2.INFO);
    }
    
    public static void main(String[] args) {
        
    }
    
}
