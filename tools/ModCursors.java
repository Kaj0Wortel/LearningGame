
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       21-12-2016      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools;

// Own packages
import learningGame.LearningGame;
import learningGame.log.Log2;


// Java packages
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ModCursors {
    final public static String WORKING_DIR = LearningGame.WORKING_DIR;
    
    final public static Cursor EMPTY_CURSOR = createCursor(WORKING_DIR + "img\\", "empty_square.png");
    final public static Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private static Cursor createCursor(String path, String fileName) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        File file = new File(path + fileName);
        Cursor newCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        
        try {
            BufferedImage image = ImageIO.read(file);
            newCursor = tk.createCustomCursor(image, new Point(0, 0), fileName);
            
        } catch (IOException e) {
            Log2.write("Could not access: \"" + file.getName() + "\".", true);
        }
        
        return newCursor;
    }
    
}