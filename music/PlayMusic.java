
package learningGame.music;


// Own packages 
import learningGame.LearningGame;
import learningGame.log.Log2;


// Java packages
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class PlayMusic {
    // Working directory for the music files.
    final protected static String workingDir = LearningGame.workingDir + "music\\";
    
    // Table containing all file names which correspond to a currently created clips.
    protected static Hashtable<String, Clip> clipTable = new Hashtable<String, Clip>();
    
    // Table containing all LineListeners for each clip.
    protected static Hashtable<Clip, ArrayList<LineListener>> listenerTable
        = new Hashtable<Clip, ArrayList<LineListener>>();
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This is a singleton class. No instances should be made.
     */
    @Deprecated
    private PlayMusic() { }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates a single playable clip.
     * 
     * @param fileName the location of the music file. Must be a wav file format.
     * @return the created clip.
     */
    public static Clip createClip(String fileName) {
        Clip clip;
        if ((clip = clipTable.get(fileName)) != null) {
            return clip;
        }
        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
        } catch (UnsupportedAudioFileException e) {
            Log2.write(e);
            
        } catch (IOException e) {
            Log2.write(e);
            
        } catch (LineUnavailableException e) {
            Log2.write(e);
        }
        
        clipTable.put(fileName, clip);
        listenerTable.put(clip, new ArrayList<LineListener>());
        
        
        return clip;
    }
    
    /* 
     * Plays a music file.
     * If there was no clip of the file, create a clip.
     * Uses the function play(Clip) to play the clip.
     * 
     * @param fileName the location of the music file. Must be a wav file format.
     */
    public static void play(String fileName) {
        Clip clip;
        
        if ((clip = clipTable.get(fileName)) == null) {
            clip = createClip(fileName);
        }
        
        play(clip);
    }
    
    /* 
     * Plays a clip.
     * 
     * IMPORTANT: NEVER USE THE Clip.start() FUNCTION ELSEWHERE!
     * 
     * @param clip clip to be played.
     */
    public static void play(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
        }
        
        clip.setFramePosition(0);
        clip.start();
        /*
        clip.setFramePosition(0);
        if (!clip.isRunning()) {
            clip.start();
        } else {
            System.out.println("check");
        }*/
    }
    
    /* 
     * Stops a music file from playing
     * If there was no clip of the file, ignore the action.
     * Uses the function stop(Clip) to play the clip.
     * 
     * @param fileName the location of the music file. Must be a wav file format.
     */
    public static void stop(String fileName) {
        Clip clip;
        
        // If the clip does not exist, do nothing.
        if ((clip = clipTable.get(fileName)) == null) {
            return;
        }
        
        stop(clip);
    }
    
    /* 
     * Stops a clip.
     * 
     * IMPORTANT: NEVER USE THE Clip.stop() FUNCTION ELSEWHERE!
     * 
     * @param clip clip to be played.
     */
    public static void stop(Clip clip) {
        clip.stop();
    }
    
    /* 
     * Resumes a music file from where it was stopped.
     * If there was no clip of the file, create a new clip and play it using the function play(Clip).
     * Otherwise uses the function resume(Clip) to play the clip.
     * 
     * @param fileName the location of the music file. Must be a wav file format.
     */
    public static void resume(String fileName) {
        Clip clip;
        
        if ((clip = clipTable.get(fileName)) == null) {
            clip = createClip(fileName);
            play(clip);
            
        } else {
            resume(clip);
        }
    }
    
    /* 
     * Resumes a clip from where it was stopped.
     * 
     * @param clip clip to be resumed.
     */
    public static void resume(Clip clip) {
        clip.start();
    }
    
    /* 
     * Adjust the volume of the given clip.
     * 
     * @param clip determines which clip should be affected by the volume change.
     * @param volume determines the volume that the clip should be played.
     *     It must hold that 0.0 <= volume <= 1.0.
     * 
     * TODO not working correctly when the music is already playing.
     */
    public static void setVolume(Clip clip, float volume) {
        if (volume > 1.0F) volume = 1.0F;
        if (volume < 0.0F) volume = 0.0F;
        
        // To factor in the logarithmic effects of sound.
        volume = (float) (Math.log(49*volume + 1) / Math.log(50.0));
        
        boolean isRunning = clip.isRunning();
        
        if (isRunning) stop(clip);
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        
        float max = control.getMaximum();
        float min = control.getMinimum();
        
        control.setValue(min + (max-min) * volume);
        if (isRunning) play(clip);
    }
    
    /* 
     * Stops all clips from playing.
     */
    public static void stopAllMusic() {
        for (Enumeration<Clip> e = clipTable.elements(); e.hasMoreElements();) {
            stop(e.nextElement());
        }
    }
    
    /* 
     * Adds the given actions to the current clip.
     * 
     * @param clip determines which clip is affected.
     * @param open action that is executed when the stream from the file is opened.
     * @param close action that is executed when the stream from the file is closed.
     * @param start action that is exectued when the clip is started.
     * @param stop action that is exectued when the clip is stopped.
     */
    public static void addAction(final Clip clip, final Runnable open,  final Runnable close,
                                 final Runnable start, final Runnable stop) {
        clip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.OPEN) {
                if (open != null) open.run();
                
            } else if (e.getType() == LineEvent.Type.CLOSE) {
                if (close != null) close.run();
                
            } else if (e.getType() == LineEvent.Type.START) {
                if (start != null) start.run();
                
            } else if (e.getType() == LineEvent.Type.STOP) {
                if (stop != null) stop.run();
            }
        });
    }
    
    /* 
     * Removes all LineListeners that were added via this class to the given clip.
     * 
     * @param clip denotes the clip of which the LineListeners have to be removed.
     */
    public static void removeAllActions(Clip clip) {
        ArrayList<LineListener> list = listenerTable.get(clip);
        for (LineListener listener : list) {
            clip.removeLineListener(listener);
        }
        
        list.clear();
    }
    
    /* 
     * Loops a clip a number of times.
     * 
     * @param clip clip to be looped.
     * @param loopCount the number of loops the clip has to play.
     *     Use Clip.LOOP_CONTINUOUSLY or -1 for loopCount to loop continuously.
     */
    public static void loop(Clip clip, int loopCount) {
        clip.loop(loopCount);
    }
    
    public static void main(String[] args) {
        new LearningGame();
    }
    
}