
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       05-02-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools;


// Own packages
import learningGame.tools.TerminalErrorMessage;


// Java packages
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.imageio.IIOException;


public class LoadImages2 {
    public static Hashtable<String, BufferedImage[][]> images = new Hashtable<String, BufferedImage[][]>();
    
    @Deprecated
    public LoadImages2() {}
    
    /* 
     * Loads an image from a given file(-name) and converts it into separate pieces.
     * The images are created from (startX, startY) and upto and including with (endX, endY).
     * Each image in this range has size (width, height).
     * 
     * File file, String name,
                                              int startX, int startY,
                                              int endX, int endY,
                                              int width, int height)
     * 
     * 
     * @param fileName the path of the image file.
     * @param file image file.
     * @param startX the pixel x-coordinate of the start location in the image.
     * @param startY the pixel y-coordinate of the start location in the image.
     * @param endX the pixel x-coordinate of the end location of the image.
     * @param endY the pixel y-coordinate of the end location of the image.
     * @param width the width of each subimage.
     * @param height the height of each subimage.
     * 
     * @return an 2D BufferedImage array, with the images loaded such that bi[x][y]
     *     will retrieve the image part at (x, y) from the selected part (starting at (0, 0)).
     * 
     * @throws IIOException iff the file does not exist or is not accessable.
     * @throws IllegalArgumentException iff:
     * - startX >= endX  OR  startY >= endY
     * - (endX - startX) % width != 0  OR  (endY - startY) % height != 0
     * - endX < the width of the image  OR  endY < the height of the image
     * 
     * If any endX or endY equals -1 (e.g. endX == -1), then this coord will
     *     be changed to the max size of the image (width for endX, height for endY).
     * If the width or height equals -1, then this will be changed
     *     to resp. endX - startX and endY - startY.
     */
    
    // Wrapper functions
    
    // (0) Using "new File(String)" as file
    //     (1) Using "fileName" as name
    public static BufferedImage[][] loadImage(String fileName)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(new File(fileName), fileName, 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] loadImage(String fileName,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(new File(fileName), fileName, 0, 0, -1, -1, width, height);
    }
    
    public static BufferedImage[][] loadImage(String fileName,
                                              int startX, int startY,
                                              int endX, int endY,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(new File(fileName), fileName, startX, startY, endX, endY, width, height);
    }
    
    //     (1) Using "name" as name
    public static BufferedImage[][] loadImage(String fileName, String name)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(new File(fileName), name, 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] loadImage(String fileName, String name,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(new File(fileName), name, 0, 0, -1, -1, width, height);
    }
    
    public static BufferedImage[][] loadImage(String fileName, String name,
                                              int startX, int startY,
                                              int endX, int endY,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(new File(fileName), name, startX, startY, endX, endY, width, height);
    }
    
    
    // (0) Using "file" as file
    //     (1) Using "fileName" as name
    public static BufferedImage[][] loadImage(File file)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(file, file.getPath(), 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] loadImage(File file,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(file, file.getPath(), 0, 0, -1, -1, width, height);
    }
    
    public static BufferedImage[][] loadImage(File file,
                                              int startX, int startY,
                                              int endX, int endY,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(file, file.getPath(), startX, startY, endX, endY, width, height);
    }
    
    //     (1) Using "name" as name
    public static BufferedImage[][] loadImage(File file, String name)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(file, name, 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] loadImage(File file, String name,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return loadImage(file, name, 0, 0, -1, -1, width, height);
    }
    
    // Full function
    public static BufferedImage[][] loadImage(File file, String name,
                                              int startX, int startY,
                                              int endX, int endY,
                                              int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        BufferedImage[][] newImg = null;
        
        synchronized(images) {
            // Check if the store name is correct
            if (images.contains(name))
                throw new IllegalArgumentException("Name was already used: \"" + name + "\"");
            
            // Read the image
            BufferedImage bigImg = ImageIO.read(file);
            
            
            // Check if the end coords are correct
            if (endX == -1) {
                endX = bigImg.getWidth();
                
            } else if (endX > bigImg.getWidth()) {
                throw new IllegalArgumentException("Given end width was larger then the image. end width: " + endX + ", img width: " + bigImg.getWidth());
            }
            
            if (endY == -1 ) {
                endY = bigImg.getHeight();
                
            } else if (endY > bigImg.getHeight()) {
                throw new IllegalArgumentException("Given end height was larger then the image. end height: " + endY + ", img height: " + bigImg.getHeight());
            }
            
            // Check if the end-coords are bigger then the starting coords.
            if (startX >= endX)
                throw new IllegalArgumentException("Starting x coord >= end x coord: " + startX + " >= " + endX);
            if (startY >= endY)
                throw new IllegalArgumentException("Starting x coord >= end x coord" + startY + " >= " + endY);
            
            int dX = endX - startX;
            int dY = endY - startY;
            
            // Check if the image sizes are valid
            if (width == -1) {
                width = endX - startX;
                
            } else if (dX % width != 0) {
                throw new IllegalArgumentException("Given width (" + width + "), startX (" + startX + ") and/or endX (" + endX + ") is invallid");
            }
            
            if (height == -1) {
                height = endY - startY;
                
            } else if (dY % height != 0) {
                throw new IllegalArgumentException("Given width (" + height + "), startY (" + startY + ") and/or endY (" + endY + ") is invallid");
            }
            
            // Split the image into parts
            newImg = new BufferedImage[dX / width][dY / height];
            for (int i = startX ; i < endX; i += width) {
                for (int j = startY ; j < endY; j += height) {
                    newImg[(i - startX) / width][(j - startY) / height] = bigImg.getSubimage(i, j, width, height);
                }
            }
            
            if (newImg != null) {
                images.put(name, newImg);
            }
        }
        
        return newImg;
    }
    
    /* 
     * Check if the current entry already exists. If it doesn't,
     *     create it with the given settings.
     * In any case, return the image array.
     * See loadImage(String fileName, name,
     *     int startX, int startY, int width, int height,
     *     int endX, int endY) for more detailed info.
     */
    // (0) Using "new File(String)" as file
    //     (1) Using "fileName" as name
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(new File(fileName), fileName, 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName,
                                                            int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(new File(fileName), fileName, 0, 0, -1, -1, width, height);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName,
                                                            int startX, int startY,
                                                            int endX, int endY,
                                                            int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(new File(fileName), fileName, startX, startY, endX, endY, width, height);
    }
    
    //     (1) Using "name" as name
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName, String name)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(new File(fileName), name, 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName, String name,
                                                            int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(new File(fileName), name, 0, 0, -1, -1, width, height);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName, String name,
                                                            int startX, int startY,
                                                            int endX, int endY,
                                                            int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(new File(fileName), name, startX, startY, endX, endY, width, height);
    }
    
    
    // (0) Using "file" as file
    //     (1) Using "fileName" as name
    public static BufferedImage[][] ensureLoadedAndGetImage(File file)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(file, file.getPath(), 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(File file,
                                                            int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(file, file.getPath(), 0, 0, -1, -1, width, height);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(File file,
                                                            int startX, int startY,
                                                            int endX, int endY,
                                                            int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(file, file.getPath(), startX, startY, endX, endY, width, height);
    }
    
    //     (1) Using "name" as name
    public static BufferedImage[][] ensureLoadedAndGetImage(File file, String name)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(file, name, 0, 0, -1, -1, -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(File file, String name,
                                                            int width, int height)
        throws IllegalArgumentException, IOException, IIOException {
        return ensureLoadedAndGetImage(file, name, 0, 0, -1, -1, width, height);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(File file, String name,
                                                            int startX, int startY,
                                                            int endX, int endY,
                                                            int width, int height)
        throws IOException, IllegalArgumentException, IIOException {
        synchronized(images) {
            if (images.containsKey(name)) {
                return images.get(name);
                
            } else {
                return loadImage(file, name,
                                 startX, startY,
                                 endX, endY,
                                 width, height);
            }
        }
    }
    
    /* 
     * Returns an earlier stored image array.
     * 
     * @param name the name of the image that will be retrieved.
     * @return the image stored at "name", if it exists. null otherwise.
     */
    public static BufferedImage[][] getImage(String name) {
        synchronized(images) {
            return images.get(name);
        }
    }
    
    /* 
     * Returns an earlier stored image.
     * 
     * @param name the name of the image that will be retrieved.
     * @return the image stored at "name", if it exists.
     * @throws NoSuchFieldException if this image does not exist.
     */
    public static BufferedImage[][] getImageException(String name) throws NoSuchFieldException {
        synchronized(images) {
            if (images.containsKey(name)) {
                return getImage(name);
                
            } else {
                throw new NoSuchFieldException("Image \"" + name + "\" does not exist.");
            }
        }
    }
    
    /* 
     * Removes a stored image.
     * 
     * @param name the name of the image that will be retrieved.
     * @return true iff the image was in the hashtable and removed.
     */
    public static BufferedImage[][] removeImage(String name) {
        synchronized(images) {
            return images.remove(name);
        }
    }
    
    /* 
     * Removes all stored images.
     */
    public static void clear() {
        synchronized(images) {
            images.clear();
        }
    }
}

