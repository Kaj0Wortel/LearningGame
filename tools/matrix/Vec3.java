
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       24-01-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools.matrix;

/* 
 * Represents a 3D vector.
 * Mainly used to ensure bounds.
 */
public class Vec3 extends Vec {
    /* ----------------------------------------------------------------------------------------------------------------
     * Public constants
     * ----------------------------------------------------------------------------------------------------------------
     */
    // Origin and axis vectors.
    public final static Vec3 O = new Vec3(0, 0, 0);
    public final static Vec3 X = new Vec3(1, 0, 0);
    public final static Vec3 Y = new Vec3(0, 1, 0);
    public final static Vec3 Z = new Vec3(0, 0, 1);
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Vec3(double... values) {
        super(values);
    }
    
    public Vec3(boolean transpose, double... values) {
        super(transpose, values);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Static functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Computes the cross vector of the two vectors.
     */
    public static Vec3 cross(Vec v1, Vec v2) {
        if (v1.row != 3 || v1.col != 1 || v2.row != 3 && v1.col != 1)
            throw new MatrixDimensionException("Cannot take the dot product of two vectors of a different dimension."
                                                   + "Expected: 1x3 cross 1x3, found: "
                                                   + v1.row + "x" + v1.col + " dot "
                                                   + v2.row + "x" + v2.col + ".");
        return new Vec3(v1.y() * v2.z() - v1.z() * v2.y(),
                        v1.z() * v2.x() - v1.x() * v2.z(),
                        v1.x() * v2.y() - v1.y() * v2.x());
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Vec3 cross(Vec v) {
        return cross(this, v);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Overridden functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Overrides the matrix dimension check.
     * See the Matrix class for more info.
     */
    @Override
    protected boolean matrixDimCheck(int row, int col) {
        if ((row != 1 || col != 3) && (row != 3 || col != 1))
            throw new MatrixDimensionException("Illegal change of matrix size. Found: " + row + "x" + col
                                                   + ", expected: 1x3 or 3x1.");
        return true;
    }
}
