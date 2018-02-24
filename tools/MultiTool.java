
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       22-02-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools;


// Java packages
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;


public class MultiTool {
    /* 
     * Converts a decimal integer to a 32 based number String.
     * 
     * @param dHex a decimal number.
     * @return a String containing the 32 based representation of {@code dHex}.
     */
    public static String dHexToString(int dHex) throws NumberFormatException {
        
        if (dHex < 0) throw new NumberFormatException();
        
        String answer = "";
        int heighestDHexNumber = 1;
        
        while (Math.pow(32, heighestDHexNumber) - 1 < dHex) {
            heighestDHexNumber += 1;
        }
        
        for (int counter = 0; counter < heighestDHexNumber; counter++) {
            int part = (int)((double)(dHex) / Math.pow(32, heighestDHexNumber - (counter+1)));
            dHex -= part * Math.pow(32, heighestDHexNumber - (counter+1));
            answer += singleDHexToString(part);
        }
        
        return answer;
    }
    
    /* 
     * Converts a 32 based number String to a decimal integer.
     * 
     * @param dHex 32 based number String.
     * @return an integer represented by the 32 based number {@code dHex}.
     */
    public static int stringToDHex(String dHex) throws NumberFormatException {
        
        if (dHex == null) throw new NumberFormatException();
        
        dHex = dHex.toUpperCase();
        int answer = 0;
        int length = dHex.length();
        if (length == 0) throw new NumberFormatException();
        
        for (int counter = 0; counter < length; counter++) {
            answer += Math.pow(32, length - (counter+1)) * singleStringToDHex(dHex.charAt(counter));
        }
        return answer;
    }
    
    /* 
     * Converts a single decimal number to a hexadecimal char.
     * 
     * @param dHex a decimal number (0 <= dHex < 32).
     * @return the 32 based representation of {@code dHex}.
     */
    private static char singleDHexToString(int dHex) throws NumberFormatException {
        if (dHex == 0) return '0';
        else if (dHex == 1) return '1';
        else if (dHex == 2) return '2';
        else if (dHex == 3) return '3';
        else if (dHex == 4) return '4';
        else if (dHex == 5) return '5';
        else if (dHex == 6) return '6';
        else if (dHex == 7) return '7';
        else if (dHex == 8) return '8';
        else if (dHex == 9) return '9';
        else if (dHex == 10) return 'A';
        else if (dHex == 11) return 'B';
        else if (dHex == 12) return 'C';
        else if (dHex == 13) return 'D';
        else if (dHex == 14) return 'E';
        else if (dHex == 15) return 'F';
        else if (dHex == 16) return 'G';
        else if (dHex == 17) return 'H';
        else if (dHex == 18) return 'I';
        else if (dHex == 19) return 'J';
        else if (dHex == 20) return 'K';
        else if (dHex == 21) return 'L';
        else if (dHex == 22) return 'M';
        else if (dHex == 23) return 'N';
        else if (dHex == 24) return 'O';
        else if (dHex == 25) return 'P';
        else if (dHex == 26) return 'Q';
        else if (dHex == 27) return 'R';
        else if (dHex == 28) return 'S';
        else if (dHex == 29) return 'T';
        else if (dHex == 30) return 'U';
        else if (dHex == 31) return 'V';
        else throw new NumberFormatException();
    }
    
    /* 
     * Converts a single 32 based char to a decimal number.
     * 
     * @param dHex a 32 based number ('0' <= dHex <= '9' || 'A' <= dHex <= 'V').
     * @return the decimal representation of {@code dHex}.
     */
    private static int singleStringToDHex(char dHex)  throws NumberFormatException {
        if (dHex == '0') return 0;
        else if (dHex == '1') return 1;
        else if (dHex == '2') return 2;
        else if (dHex == '3') return 3;
        else if (dHex == '4') return 4;
        else if (dHex == '5') return 5;
        else if (dHex == '6') return 6;
        else if (dHex == '7') return 7;
        else if (dHex == '8') return 8;
        else if (dHex == '9') return 9;
        else if (dHex == 'A') return 10;
        else if (dHex == 'B') return 11;
        else if (dHex == 'C') return 12;
        else if (dHex == 'D') return 13;
        else if (dHex == 'E') return 14;
        else if (dHex == 'F') return 15;
        else if (dHex == 'G') return 16;
        else if (dHex == 'H') return 17;
        else if (dHex == 'I') return 18;
        else if (dHex == 'J') return 19;
        else if (dHex == 'K') return 20;
        else if (dHex == 'L') return 21;
        else if (dHex == 'M') return 22;
        else if (dHex == 'N') return 23;
        else if (dHex == 'O') return 24;
        else if (dHex == 'P') return 25;
        else if (dHex == 'Q') return 26;
        else if (dHex == 'R') return 27;
        else if (dHex == 'S') return 28;
        else if (dHex == 'T') return 29;
        else if (dHex == 'U') return 30;
        else if (dHex == 'V') return 31;
        else throw new NumberFormatException();
    }
    
    /* 
     * Checks if a given number is in the array.
     * 
     * @param array array to look in.
     * @param number number to look for.
     * @return true iff the number is in the array.
     */
    public static boolean isInArray(int[] array, int number) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == number) {
                return true;
            }
        }
        
        return false;
    }
    
    /* 
     * Converts an ArrayList to an array.
     * 
     * @param array the array to be converted.
     * @return an ArrayList containing every element of {@code array} and in the same order.
     */
    public static <T> ArrayList<T> toArrayList(T[] array) {
        if (array == null) return null;
        
        ArrayList<T> list = new ArrayList<T>(array.length);
        
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        
        return list;
    }
    
    /* 
     * Converts an array to an ArrayList
     * 
     * @param list the list to be converted.
     * @return an array containing every element {@code list} and in the same order.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(ArrayList<T> list) {
        if (list == null) return null;
        
        T[] array = (T[]) new Object[list.size()];
        
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        
        return array;
    }
    
    /* 
     * Makes a copy of an arrayList.
     * 
     * @param list ArrayList to copy
     */
    public static <T> ArrayList<T> copyArrayList(ArrayList<T> list) {
        if (list == null) return null;
        
        ArrayList<T> newList = new ArrayList<T>(list.size());
        
        for (int i = 0; i < list.size(); i++) {
            newList.add(list.get(i));
        }
        
        return newList;
    }
    
    /* 
     * Makes a copy of an array.
     * 
     * @param array array to copy.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyArray(T[] array) {
        if (array == null) return null;
        
        T[] newArray = (T[]) new Object[array.length];
        
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /*
     * Sleeps the current thread.
     * Mainly used to avoid the annoying catch statement.
     * 
     * @param time time in ms that the thread sleeps.
     */
    public static void sleepThread(long time) {
        try {
            Thread.sleep(time);
            
        } catch (InterruptedException e) {
            System.err.println(e);
        }   
    }
    
    /* 
     * Fires an ActionEvent for all ActionListeners in the array.
     * Uses as time the current time stamp.
     * 
     * See fireActioneEvents(String, long, int, ActionListener[]) for more info.
     */
    public static void fireActionEvents(Object source, String command, int modifiers, ActionListener[] als) {
        fireActionEvents(source, command, System.currentTimeMillis(), modifiers, als);
    }
    
    /* 
     * Fires an ActionEvent for all ActionListeners currently listening.
     * Uses another thread for execution.
     * 
     * @param source the source of the event.
     * @param command the command used for the event.
     * @param when the time when the event occured.
     * @param modifiers the modifiers for the event.
     * @param als array containing the ActionListeners that need to be notified of the event.
     */
    public static void fireActionEvents(final Object source, final String command,
                                        final long when, final int modifiers, final ActionListener[] als) {
        if (als == null) return;
        
        new Thread(source.getClass().getName() + " ActionEvent") {
            public void run() {
                ActionEvent e = new ActionEvent(source,
                                                ActionEvent.ACTION_PERFORMED,
                                                command, when, modifiers);
                
                for (int i = 0; i < als.length; i++) {
                    if (als[i] == null) continue;
                    
                    als[i].actionPerformed(e);
                }
            }
        }.start();
    }
    
    /* 
     * Converts a double to a String, having 'decimals' decimals.
     * 
     * @param num the number to be converted.
     * @param the number of decimals.
     * @return String representation of a double, having 'decimals' decimals.
     */
    public static String doubleToStringDecimals(double num, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException("Number of decimals was negative: " + decimals);
        
        String number = Double.toString(num);
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == '.') {
                if (decimals == 0) {
                    return number.substring(0, i);
                    
                } else if (number.length() > i + decimals) {
                    return number.substring(0, i + decimals + 1);
                    
                } else {
                    while (number.length() < i + decimals + 1) {
                        number += "0";
                    }
                }
                
                return number;
            }
        }
        
        // '.' was not found
        number += ".";
        for (int i = 0; i < decimals; i++) {
            number += "0";
        }
        
        return number;
    }
    
    /* 
     * Converts an Integer to a String, with zero's filled till the n'th position.
     * 
     * @param i number to be converted.
     * @param n the length of the number + number of leading zeroes.
     * 
     * If the length of the number is bigger then n, then the full number is returned.
     */
    public static String fillZero(int i, int n) throws NumberFormatException {
        String number = Integer.toString(i);
        
        while (number.length() < n) {
            number = "0" + number;
        }
        
        return number;
    }
    
    /* 
     * Adds spaces to the left of the given text till the total length
     * of the text is equal to the given size.
     * If the initial length of the text is longer then the given size,
     * no action is taken.
     * 
     * @param text text to process.
     * @param size length of the text.
     */
    public static String fillSpaceLeft(String text, int size) {
        for (int i = text.length(); i < size; i++) {
            text = " " + text;
        }
        
        return text;
    }
    
    /* 
     * Adds spaces to the right of the given text till the total length
     * of the text is equal to the given size.
     * If the initial length of the text is longer then the given size,
     * no action is taken.
     * 
     * @param text text to process.
     * @param size length of the text.
     */
    public static String fillSpaceRight(String text, int size) {
        for (int i = text.length(); i < size; i++) {
            text = text + " ";
        }
        
        return text;
    }
    
    /*
     * Calculates the power of a base.
     * Has a much higher accuracy compared to the function
     * java.lang.Math.pow(double, double), but only accepts integer powers.
     * Takes significantly less time calculating powers less then 500, but more
     * when calculating higher powers compared to the same method.
     * 
     * @param base the base used for the power.
     * @param pow the power used for the power.
     * @result the result of base ^ pow.
     */
    public static double intPow(double base, int pow) {
        double result = 1;
        
        for (int i = 0; i < pow; i++) {
            result *= base;
        }
        
        for (int i = -1; i >= pow; i--) {
            result /= base;
        }
        
        return result;
    }
    
    /* 
     * Checks if a given object is an array.
     * Returns true if so, false otherwise.
     * 
     * @param obj the Object to be tested.
     * @return true if {@code obj} is an array. False otherwise.
     */
    public static boolean isArray(Object obj) {
        if (obj == null) return false;
        return obj.getClass().isArray();
    }
}