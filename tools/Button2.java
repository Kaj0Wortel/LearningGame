
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       02-02-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools;


// Own packages
import learningGame.LearningGame;
import learningGame.tools.ImageTools;
import learningGame.tools.LoadImages2;


// Java packages
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class Button2 extends AbstractButton {
    final protected static String imgLoc = LearningGame.workingDir + "img\\";
    final private Button2 thisButton = this;
    
    final public static int NORMAL = 0;
    final public static int HOOVER = 1;
    final public static int PRESSED = 2;
    final public static int DISABLED = 3;
    
    final public static int TYPE_TURNED = 0;
    final public static int TYPE_MIRRORED = 1;
    
    protected Image[][] originalImages;
    protected Thread generateImagesThread = null;
    protected BufferedImage[] buttonImages = new BufferedImage[4];
    final private int imageType;
    final private int scaleType;
    
    public enum State {
        NORMAL_OPERATION, NO_CHANGE, HOOVER_EXCEPT_PRESSED, NO_HOOVER, 
            ALWAYS_PRESSED, ALWAYS_HOOVER, ALWAYS_DISABLED, ALWAYS_NORMAL;
    }
    
    private State state = State.NORMAL_OPERATION;
    private int noChangeType = -1;
    
    // Size and barsize
    private int barSize;
    
    // Contents
    private JLabel label;
    private Image image;
    
    // Font for the label
    private Font font = new Font(Font.DIALOG, Font.BOLD, 12);
    
    // Used for storing the current button state
    private boolean enabled = true;
    private boolean mouseIsOverButton = false;
    private boolean mouseIsPressed = false;
    
    
    /* ---------------------------------------------------------------------------------
     * Constructors
     * ---------------------------------------------------------------------------------
     */
    public Button2(int sizeX, int sizeY, int barSize, boolean resizable) throws IOException {
        this(sizeX, sizeY, barSize,
             LoadImages2.ensureLoadedAndGetImage
                 (imgLoc + "button2_img_TYPE_001.png", 16, 16),
             Button2.TYPE_TURNED, resizable, Image.SCALE_SMOOTH);
    }
    
    public Button2(int sizeX, int sizeY, int barSize, boolean resizable, char character) throws IOException {
        this(sizeX, sizeY, barSize, resizable, "" + character);
    }
    
    public Button2(int sizeX, int sizeY, int barSize, boolean resizable, String text) throws IOException {
        this(sizeX, sizeY, barSize,
             LoadImages2.ensureLoadedAndGetImage
                 (imgLoc + "button2_img_TYPE_001.png", 16, 16),
             Button2.TYPE_TURNED, resizable, Image.SCALE_SMOOTH, text);
    }
    
    public Button2(int sizeX, int sizeY, int barSize, Image[][] img, int type, boolean resizable, int scaleType,
                   char character) {
        this(sizeX, sizeY, barSize, img, type, resizable, scaleType,
             "" + character);
    }
    
    public Button2(int sizeX, int sizeY, int barSize, Image[][] img, int type, boolean resizable, int scaleType,
                   String text) {
        this(sizeX, sizeY, barSize, img, type, resizable, scaleType,
             new JLabel(text));
    }
    
    public Button2(int sizeX, int sizeY, int barSize, Image[][] img, int type, boolean resizable, int scaleType,
                   JLabel label) {
        this(sizeX, sizeY, barSize, img, type, resizable, scaleType);
        this.label = label;
        this.add(label);
        
        label.setHorizontalAlignment(JLabel.CENTER);
        
        label.setLocation(barSize, barSize);
        label.setSize(this.getWidth() - 2*barSize, this.getHeight() - 2*barSize);
    }
    
    
    /* 
     * Creates a Button2 with the given size and bar size.
     * type determines the algorithm that is used to generate the button.
     * Must be one of:
     *  - Button2.TYPE_TURNED
     *    This generates the corners by rotating them.
     *  - Button2.TYPE_MIRRORED
     *    This generates teh corners by mirroring them.
     * 
     * Resizable determines whether it is possible to recalculate the images after
     * resizing the button.
     * 
     * scaleType determines the scale type which is used to scale the images.
     * Must be one of:
     *  - Image.SCALE_AREA_AVERAGING
     *  - Image.SCALE_DEFAULT
     *  - Image.SCALE_FAST
     *  - Image.SCALE_REPLICATE
     *  - Image.SCALE_SMOOTH
     * 
     * img depends on the the type:
     *  - For Button2.TYPE_TURNED
     *    and Button2.TYPE_MIRRORED, the following is assumed about img:
     *     o img[0][0] contains the overlay of the upper left corner.
     *     o img[1][0] contains the overlay of the uppper bar.
     *     o img[2][0] contains the overlay of the center part
     *     o img[0][x] with 1 <= x < 4 contain the backgrounds for the corners.
     *     o img[1][x] with 1 <= x < 4 contain the backgrounds for the edges.
     *     o img[2][x] with 1 <= x < 4 contain the backgrounds for the center part
     */
    public Button2(int sizeX, int sizeY, int barSize, Image[][] img, int type, boolean resizable, int scaleType) {
        this.imageType = type;
        this.barSize = barSize;
        this.scaleType = scaleType;
        
        // Sets the size of the Button.
        // Uses the parent function to avoid throwing errors and
        // incorrect image manipulations.
        super.setBounds(this.getX(), this.getY(), sizeX, sizeY);
        this.setLayout(null);
        
        
        if (resizable) {
            originalImages = new Image[3][5];
            
            for (int i = 0; i < originalImages.length; i++) {
                for (int j = 0; j < originalImages[i].length; j++) {
                    originalImages[i][j] = img[i][j];
                }
            }
            
        } else {
            originalImages = null;
        }
        
        generateNewImages(img);
        
        this.addMouseListener(listener);
    }
    
    
    /* ---------------------------------------------------------------------------------
     * Mouse listener
     * ---------------------------------------------------------------------------------
     */
    MouseAdapter listener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            mouseIsPressed = false;
            mouseIsOverButton = true;
            
            if (enabled) {
                thisButton.repaint();
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            mouseIsPressed = false;
            mouseIsOverButton = false;
            
            if (enabled) {
                thisButton.repaint();
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            mouseIsPressed = true;
            
            if (enabled) {
                fireActionEvents(Integer.toString(e.getButton()) + ";pressed",
                                 e.getWhen(),
                                 e.getModifiers());
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            mouseIsPressed = false;
            
            if (enabled) {
                fireActionEvents(Integer.toString(e.getButton()) + ";released",
                                 e.getWhen(),
                                 e.getModifiers());
            }
        }
    };
    
    /*---------------------------------------------------------------------------------
     * Private functions
     * ---------------------------------------------------------------------------------
     */
    /* 
     * Fires an ActionEvent for all ActionListeners currently listening.
     * Uses as time the current time.
     * 
     * See fireActioneEvents(String, long, int) for more info.
     * 
     * @param command the command that is given for the events.
     * @param modifiers the modifiers that are given for the events.
     */
    private void fireActionEvents(String command, int modifiers) {
        fireActionEvents(command, System.currentTimeMillis(), modifiers);
    }
    
    /* 
     * Fires an ActionEvent for all ActionListeners currently listening.
     * Uses another thread for execution.
     * 
     * @param command the command that is given for the events.
     * @param when the time (in ms) when the event occured.
     * @param modifiers the modifiers that are given for the events.
     */
    private void fireActionEvents(final String command, final long when, final int modifiers) {/*
        new Thread("tools.Button2 ActionEvent") {
            @Override
            public void run() {
                ActionListener[] als = thisButton.getListeners(ActionListener.class);
                ActionEvent e = new ActionEvent(thisButton,
                                                ActionEvent.ACTION_PERFORMED,
                                                command, when, modifiers);
                
                for (int i = 0; i < als.length; i++) {
                    als[i].actionPerformed(e);
                }
                
                fireActionPerformed
                    (new ActionEvent(thisButton,
                                     ActionEvent.ACTION_PERFORMED,
                                     command, when, modifiers));
                
                mouseIsOverButton = false;
                mouseIsPressed = false;
                thisButton.repaint();
            }
            }.start();*/
        
        fireActionPerformed
            (new ActionEvent
                 (thisButton,
                  ActionEvent.ACTION_PERFORMED,
                  command, when, modifiers)
            );
        
        mouseIsOverButton = false;
        mouseIsPressed = false;
        thisButton.repaint();
    }
    
    /* ---------------------------------------------------------------------------------
     * Set functions
     * ---------------------------------------------------------------------------------
     */
    /* 
     * Sets the size of the text.
     * Does nothing if no text was set yet.
     * 
     * @param size the new size of the text.
     */
    public void setTextSize(float size) {
        if (label == null) return;
        font = font.deriveFont(size);
        label.setFont(font);
    }
    
    /* 
     * Sets the given image as icon image
     * 
     * @param image the new image of the buttons.
     */
    public void setImage(Image image) {
        this.image = image;
    }
    
    /* 
     * If there was no label set, create a new label with the text and
     * add it to the button.
     * Otherwise change the text.
     * 
     * @param text new text of the button.
     */
    public void setText(String text) {
        if (label == null) {
            label = new JLabel(text);
            label.setHorizontalAlignment(JLabel.CENTER);
            
            this.add(label);
            updateLabel();
            
        } else {
            label.setText(text);
        }
    }
            
    /* 
     * en/disables button.
     * When enabled:
     * A press of the mouse button when the mouse is over the button notifies all ActionListeners.
     * Also the background is changed accordingly.
     * 
     * 
     * When disabled:
     * The button ignores the mouse. The background is always "backgroundDisabled".
     * 
     * When is already enabled and is enabled again (and vice verca for disabled),
     * nothing happens.
     * 
     * @param enable whether the button should be enabled or disabled.
     */
    @Override
    public void setEnabled(boolean enable) {
        if (enable != enabled) {
            enabled = enable;
        }
        
        super.setEnabled(enable);
    }
    
    /* 
     * Sets the bounds of the Button.
     * 
     * @param x the x location of the button.
     * @param y the y location of the button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @throws UnsupportedOperationException if the button was initialized as not-resizable.
     * 
     * See setBarAndBounds(int, int, int, int, int) for more info.
     */
    @Override
    public void setBounds(int x, int y, int width, int height) throws UnsupportedOperationException {
        setBarAndBounds(barSize, x, y, width, height);
    }
    
    /* 
     * Sets the barSize of the image.
     * 
     * @param bz the new barsize of the button.
     * @throws UnsupportedOperationException if the button was initialized as not-resizable.
     * 
     * See setBarAndBounds(int, int, int, int, int) for more info.
     */
    public void setBarsize(int bz) throws UnsupportedOperationException {
        setBarAndSize(this.getWidth(), this.getHeight(), bz);
    }
    
    /* 
     * Sets the size of the image and the bar.
     * 
     * @param bz the new barsize of the button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @throws UnsupportedOperationException if the button was initialized as not-resizable.
     * 
     * See setBarAndBounds(int, int, int, int, int) for more info.
     */
    public void setBarAndSize(int bz, int width, int height) throws UnsupportedOperationException {
        setBarAndBounds(bz, this.getX(), this.getY(), width, height);
    }
    
    /* 
     * Sets the size of the image and the bar, and sets the location.
     * 
     * @param bz the new barsize of the button.
     * @param x the x location of the button.
     * @param y the y location of the button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @throws UnsupportedOperationException if the button was initialized as not-resizable.
     */
    public void setBarAndBounds(int bz, int x, int y, int width, int height) {
        if (barSize == bz && getWidth() == width && getHeight() == height) {
            super.setBounds(x, y, width, height);
            
        } else {
            if (originalImages != null) {
                if (barSize != bz || width != getWidth() || height != getHeight()) {
                    super.setBounds(x, y, width, height);
                    barSize = bz;
                    
                    generateNewImages(originalImages);
                    
                    updateLabel();
                    
                } else if (x != getX() || y != getY()) {
                    super.setBounds(x, y, width, height);
                }
            } else {
                throw new UnsupportedOperationException("Button2 was initialized as not-resizable.");
            }
        }
    }
    
    /* 
     * Sets the current state of the button.
     * 
     * @param state the new state of the button. Must be one of NORMAL_OPERATION, NO_CHANGE, HOOVER_EXCEPT_PRESSED,
     *     NO_HOOVER, ALWAYS_PRESSED, ALWAYS_HOOVER, ALWAYS_DISABLED or ALWAYS_NORMAL.
     */
    public void setState(State state) {
        if (state == State.NO_CHANGE) {
            noChangeType = calculateType();
        }
        
        int oldType = calculateType();
        this.state = state;
        int newType = calculateType();
        
        if (newType == PRESSED && oldType != PRESSED) {
            fireActionEvents("-1;pressed",  (int) System.currentTimeMillis());
            
        } else if (newType != PRESSED && oldType == PRESSED) {
            fireActionEvents("-1;released",  (int) System.currentTimeMillis());
        }
        
        this.repaint();
    }
    
    /* 
     * Sets the current state of the button.
     * Does not fires ActionEvents accordingly.
     * 
     * @param state the new state of the button. Must be one of NORMAL_OPERATION, NO_CHANGE, HOOVER_EXCEPT_PRESSED,
     *     NO_HOOVER, ALWAYS_PRESSED, ALWAYS_HOOVER, ALWAYS_DISABLED or ALWAYS_NORMAL.
     */
    public void setStateNoAction(State state) {
        if (state == State.NO_CHANGE) {
            noChangeType = calculateType();
        }
        
        this.state = state;
        this.repaint();
    }
    
    /*
     * ---------------------------------------------------------------------------------
     * Get functions
     * ---------------------------------------------------------------------------------
     */
    /* 
     * @return the text from the label if the label exists. null otherwise.
     */
    public String getText() {
        return (label == null ? null : label.getText());
    }
    
    /* 
     * @return the current state of the button.
     */
    public State getState() {
        return state;
    }
    
    /* 
     * @return true iff the mouse is currently over the button and pressed.
     * Does NOT depend on its current state.
     * Use "calculateType()" to determine the current state.
     */
    public boolean isPressed() {
        return mouseIsPressed;
    }
    
    /* 
     * @return true iff the mouse is currently over the button.
     * Does NOT depend on its current state.
     */
    public boolean isHoover() {
        return mouseIsOverButton;
    }
    
    /* 
     * @return true iff the button is enabled.
     * Does NOT depend on its current state.
     * False otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /*
     * ---------------------------------------------------------------------------------
     * Action functions
     * ---------------------------------------------------------------------------------
     */
    /* 
     * Generates the background image for all four types, given the images in img.
     * img must have the same constraints as the images described in the constructor.
     * Is executed on another thread to improve speed. This thread is interrupted when
     * it is still calculating and the a new image generation request is done.
     * 
     * @param img see constructor for detailed info.
     */
    private void generateNewImages(Image[][] imgs) {
        if (getWidth() == 0 || getHeight() == 0) return;
        
        if (generateImagesThread != null) {
            generateImagesThread.interrupt();
        }
        
        generateImagesThread = new Thread() {
            @Override
            public void run() {
                Image[][] scaledImages = new Image[4][5];
                
                // Scale each of the images to the right size
                for (int i = 0; i < scaledImages.length; i++) {
                    for (int j = 0; j < scaledImages[i].length; j++) {
                        if (i == 0) { // Corner
                            scaledImages[i][j] = imgs[i][j]
                                .getScaledInstance(barSize, barSize, scaleType);
                            
                        } else if (i == 1) { // Horizontal bars
                            if (getWidth() - 2*barSize > 0) {
                                scaledImages[i][j] = imgs[i][j]
                                    .getScaledInstance(getWidth() - 2*barSize, barSize, scaleType);
                            }
                            
                        } else if (i == 2) { // Vertical bars
                            if (getHeight() - 2*barSize > 0) {
                                scaledImages[i][j] = imgs[i-1][j]
                                    .getScaledInstance(getHeight() - 2*barSize, barSize, scaleType);
                            }
                            
                        } else if (i == 3) { // Center
                            if (getWidth() - 2*barSize > 0 && getHeight() - 2*barSize > 0) {
                                scaledImages[i][j] = imgs[i-1][j]
                                    .getScaledInstance(getWidth() - 2*barSize, getHeight() - 2*barSize, scaleType);
                            }
                        }
                    }
                }
                
                for (int i = 0; i < buttonImages.length; i++) {
                    buttonImages[i] = new BufferedImage(getWidth(),
                                                        getHeight(),
                                                        BufferedImage.TYPE_4BYTE_ABGR);
                    
                    Graphics2D g2d = buttonImages[i].createGraphics();
                    
                    if (getWidth() - 2*barSize > 0 && getHeight() - 2*barSize > 0) {
                        g2d.drawImage(scaledImages[3][i+1], barSize, barSize, null); // Draw center background
                    }
                    
                    // Todo: add TYPE_MIRRORED
                    for (int j = 0; j < 4; j++) { // Repeat all 4 times.
                        g2d.drawImage(scaledImages[0][i+1], 0, 0, null); // Draw corner background
                        g2d.drawImage(scaledImages[0][0], 0, 0, null); // Draw corner
                        
                        if ((i % 2 == 0 && getWidth() - 2*barSize > 0) ||
                            (i % 2 == 1 && getHeight() - 2*barSize > 0))
                        {
                            g2d.drawImage(scaledImages[(j % 2 == 0 ? 1 : 2)][i+1], barSize, 0, null); // Draw edge background
                            g2d.drawImage(scaledImages[(j % 2 == 0 ? 1 : 2)][0], barSize, 0, null); // Draw edge
                        }
                        
                        
                        g2d.rotate(0.5 * Math.PI);
                        g2d.translate(0, (j % 2 == 0 ? -getWidth() : -getHeight()));
                    }
                    
                    g2d.dispose();
                }
                
                generateImagesThread = null;
            }
        };
        
        generateImagesThread.start();
    }
    
    /* 
     * Updates the size and location of the label.
     */
    public void updateLabel() {
        if (label == null) return;
        
        if ((int) (this.getWidth() - barSize * 2) > 0 && (int) (this.getHeight() - barSize * 2) > 0) {
            label.setSize((int) (this.getWidth() - barSize * 2), (int) (this.getHeight() - barSize * 2));
            label.setLocation((int) (barSize * 1), (int) (barSize * 1));
            
        } else {
            label.setSize(this.getWidth(), this.getHeight());
            label.setLocation(0, 0);
        }
    }
    
    /* 
     * Simulates a button press event.
     * 
     * @param source the source of the event.
     */
    public void pressButton(Component source) {
        pressButton(source, MouseEvent.NOBUTTON);
    }
    
    public void pressButton(Component source, int button) {
        listener.mousePressed
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_PRESSED,
                  System.currentTimeMillis(),
                  0,    // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  1,    // 1 click
                  false, button)
            );
    }
    
    /* 
     * Simulates a button release event
     * 
     * @param source the source of the event.
     */
    public void mouseReleased(Component source) {
        pressButton(source, MouseEvent.NOBUTTON);
    }
    
    public void releaseButton(Component source, int button) {
        listener.mouseReleased
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_RELEASED,
                  System.currentTimeMillis(),
                  0,    // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  1,    // 1 click
                  false, button)
            );
    }
    
    /* 
     * Simulates a mouse entered event
     * 
     * @param source the source of the event.
     */
    public void mouseEntered(Component source) {
        mouseEntered(source, MouseEvent.NOBUTTON);
    }
    
    public void mouseEntered(Component source, int button) {
        listener.mouseEntered
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_ENTERED,
                  System.currentTimeMillis(),
                  0,    // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  1,    // 1 click
                  false, button)
            );
    }
    
    /* 
     * Simulates a mouse exited event
     * 
     * @param source the source of the event.
     */
    public void mouseExited(Component source) {
        mouseExited(source, MouseEvent.NOBUTTON);
    }
    
    public void mouseExited(Component source, int button) {
        listener.mouseExited
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_EXITED,
                  System.currentTimeMillis(),
                  0, // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  1, // 1 click
                  false, button)
            );
    }

    /* 
     * Calculates the type number used for printing the images.
     * 
     * @return the current state for printing the images. -1 if the current state is not defined.
     */
    public int calculateType() {
        if (state == State.NORMAL_OPERATION) {
            if (!enabled) {
                return DISABLED;
                
            } else if (mouseIsPressed) {
                return PRESSED;
                
            } else if (mouseIsOverButton) {
                return HOOVER;
                
            } else {
                return NORMAL;
            }
            
        } else if (state == State.ALWAYS_DISABLED) {
            return DISABLED;
            
        } else if (state == State.ALWAYS_PRESSED) {
            return PRESSED;
            
        } else if (state == State.ALWAYS_HOOVER) {
            return HOOVER;
            
        } else if (state == State.ALWAYS_NORMAL) {
            return NORMAL;
            
        } else if (state == State.NO_CHANGE) {
            return noChangeType;
            
        } else if (state == State.HOOVER_EXCEPT_PRESSED) {
            if (mouseIsPressed) {
                return PRESSED;
                
            } else {
                return HOOVER;
            }
            
        } else if (state == State.NO_HOOVER) {
            if (!enabled) {
                return DISABLED;
                
            } else if (mouseIsPressed) {
                return PRESSED;
                
            } else {
                return NORMAL;
            }
            
        } else {
            return -1;
        }
    }
    
    /* 
     * Painting the button.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int type = calculateType();
        if (buttonImages != null && buttonImages != null) {
            g.drawImage(buttonImages[type], 0, 0, null);
        }
        
        // Contents
        if (image != null) {
            g.drawImage(image, (this.getWidth() - image.getWidth(null)) / 2, (this.getHeight() - image.getHeight(null)) / 2, null);
        }
    }
    
    /* 
     * @return the button converted to a String.
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "[state=" + state.toString()
            + ", showState=" + calculateType()
            + ", location=(" + this.getX() + ", " + this.getY() + ")"
            + ", size=(" + this.getWidth() + ", " + this.getHeight() + ")"
            + ", resizable=" + (originalImages != null)
            + ", type=" + imageType
            + ", scaleTyp=" + scaleType
            + ", noChangeType=" + noChangeType + "]";
    }
    
    /*
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        
        if (obj instanceof Button2) {
            Button2 btn = (Button2) obj;
            if (btn.calculateType() == this.calculateType() && 
                btn.getX() == this.getX() && 
                btn.getY() == this.getY() && 
                btn.getWidth() == this.getWidth() && 
                btn.getHeight() == this.getHeight() && 
                btn.imageType == this.imageType && 
                btn.scaleType == this.scaleType && 
                btn.noChangeType == this.noChangeType && 
                btn.images.equals(this.images) && 
                btn.originalImages.equals(this.originalImages) && 
                btn.getState() == this.getState() && 
                btn.getText() == this.getText() && 
                btn.isPressed() == this.isPressed() && 
                btn.isHoover() == this.isHoover()
            ) {
                return true;
            }
        }
        
        return false;
    }
    */
}