
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       24-01-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package learningGame.tools.matrix;

public class Vec extends Mat {
    /* ----------------------------------------------------------------------------------------------------------------
     * Public constants
     * ----------------------------------------------------------------------------------------------------------------
     */
    // xyzw-coordinate system variables (vectors)
    final public static int X_COORD = 0;
    final public static int Y_COORD = 1;
    final public static int Z_COORD = 2;
    final public static int W_COORD = 3;
    
    // rgba-coordinate system variables (colors)
    final public static int R_COORD = 0;
    final public static int G_COORD = 1;
    final public static int B_COORD = 2;
    final public static int A_COORD = 3;
    
    // stpq-coordinate system variables (texture)
    final public static int S_COORD = 0;
    final public static int T_COORD = 1;
    final public static int P_COORD = 2;
    final public static int Q_COORD = 3;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Vec(double... values) {
        this(false, values);
    }
    
    public Vec(boolean transpose, double... values) {
        super(values.length, 1, values);
        if (transpose) this.transpose();
    }
    
    public Vec(Vec vec) {
        super(vec);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Public static functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Computes the dot product of the given vectors.
     * Makes use of the fact that
     * v1 dot v2 == v1^T x v2
     */
    public static double dot(Vec v1, Vec v2) {
        if (v1.col != 1 || v2.col != 1)
            throw new MatrixDimensionException("Cannot take the dot product of a transposed vector."
                                                   + "Expected: 1xn dot 1xn, found: "
                                                   + v1.row + "x" + v1.col + " dot "
                                                   + v2.row + "x" + v2.col + ".");
        if (v1.row != v2.row)
            throw new MatrixDimensionException("Cannot take the dot product of two vectors of a different dimension."
                                                   + "Expected: 1xn dot 1xn, found: "
                                                   + v1.row + "x" + v1.col + " dot "
                                                   + v2.row + "x" + v2.col + ".");
        
        return multiply(transpose(v1, false), v2).getValues()[0][0];
    }
    
    /* 
     * Calculates the normal vector of a vector.
     * Returns the same vector when the length is 0.
     * Update denotes whether the vector should be updated.
     * Default is true.
     */
    public static Vec normalize(Vec v) {
        return normalize(v, false);
    }
    
    public static Vec normalize(Vec v, boolean update) {
        double length = v.length();
        
        // length == 0 is only possible for the vector (0, 0...0, 0),
        // which will not change when normalized.
        if (length == 0) return v;
        
        double[][] values = v.getValues();
        
        if (v.row == 1) { // || row == col == 1
            for (int i = 0; i < values.length; i++) {
                values[0][i] = values[0][i] / length;
            }
            
        } else { // col == 1
            for (int i = 0; i < values[0].length; i++) {
                values[i][0] = values[i][0] / length;
            }
        }
        
        if (update) {
            return (Vec) v.setMatrix(values);
            
        } else {
            // This is allowed since it is certain that row == 1 || col == 1.
            return (Vec) new Mat(values);
        }
    }
    
    /* 
     * Calculates the angle between two vectors.
     */
    public static double cosAngle(Vec v1, Vec v2) throws MatrixDimensionException {
        return v1.dot(v2) / (v1.length() * v2.length());
    }
    
    public static double sinAngle(Vec v1, Vec v2) throws MatrixDimensionException {
        if (v1.col != 1 || v2.col != 1)
            throw new MatrixDimensionException("Cannot calculate the angle between a transposed vector."
                                                   + "Expected: 1xn dot 1xn, found: "
                                                   + v1.row + "x" + v1.col + " dot "
                                                   + v2.row + "x" + v2.col + ".");
        if (v1.row != v2.row)
            throw new MatrixDimensionException("Cannot calculate the angle between two vectors of a different dimension."
                                                   + "Expected: 1xn dot 1xn, found: "
                                                   + v1.row + "x" + v1.col + " dot "
                                                   + v2.row + "x" + v2.col + ".");
        
        if (v1.row == 3 || v1.col == 3) {
            return ((Vec) multiply(1.0 / (v1.length() * v2.length()), Vec3.cross(v1, v2), new Vec())).length();
            
        } else {
            return Math.sin(angle(v1, v2));
        }
    }
    
    public static double angle(Vec v1, Vec v2) throws MatrixDimensionException {
        return Math.acos(cosAngle(v1, v2));
    }
    
    /* 
     * Multiplies two vectors.
     * Returns the resulting matrix.
     * The result is stored in mat.
     */
    public static Vec multiplyVec(Vec vecLeft, Vec vecRight) throws MatrixDimensionException {
        Vec outVec = new Vec();
        multiply(vecLeft, vecRight, outVec);
        return outVec;
    }
    
    /* 
     * Multiplies the matrix with a constant.
     * Returns the result in the given variable outMat
     */
    public static Vec multiplyVec(double value, Vec mat) {
        Vec outVec = new Vec();
        multiply(value, mat, outVec);
        return outVec;
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Public functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Normalizes this vector.
     * See the static method for more details.
     */
    public Vec normalize() {
        return normalize(this);
    }
    
    /* 
     * Calculates the square of the euclidian length.
     */
    public double lengthSqr() {
        double length = 0;
        
        if (col != 1) {
            for (int i = 0; i < values[0].length; i++) {
                length += values[0][i] * values[0][i];
            }
            
        } else {
            for (int i = 0; i < values.length; i++) {
                length += values[i][0] * values[i][0];
            }
        }
        
        return length;
    }
    
    /* 
     * Calculates the euclidian length of the vector.
     * Uses the function lengthSqrt.
     */
    public double length() {
        return Math.sqrt(lengthSqr());
    }
    
    /* 
     * @param v the vector to take the cross product of.
     * @return the dot product of the current vector with the given vector.
     */
    public double dot(Vec v) {
        return dot(this, v);
    }
    
    /* 
     * Adds two vectors.
     */
    public Vec addVec(Vec vec, boolean update) {
        Vec outVec = new Vec();
        super.add(vec, update, outVec);
        return outVec;
    }
    
    /* 
     * Subtracts two vectors.
     */
    public Vec subVec(Vec vec, boolean update) {
        Vec outVec = new Vec();
        super.sub(vec, update, outVec);
        return outVec;
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Returns the  x coordinate of the vector in:
     * - vector notation  : [x]yzw
     * - color notation   : [r]gba
     * - texture notation : [s]tpq
     */
    public double x() {
        return values[0][0];
    }
    
    public double r() {
        return x();
    }
    
    public double s() {
        return x();
    }
    
    /* 
     * @return the y coordinate of the vector in:
     * - vector notation  : x[y]zw
     * - color notation   : r[g]ba
     * - texture notation : s[t]pq
     * 
     * @throws MatrixDimensionException iff the vector is not in R2 or higher.
     */
    public double y() throws MatrixDimensionException {
        if (row > col) {
            if (col != 1 && row < 2)
                throw new MatrixDimensionException
                ("Expected vector dimensions 1xn or nx1, with n >= 2, but found vector dimensions "
                     + row + "x" + col + ".");
            
            return values[1][0];
            
        } else {
            if (row != 1 && col < 2)
                throw new MatrixDimensionException
                ("Expected vector dimensions 1xn or nx1, with n >= 2, but found vector dimensions "
                     + row + "x" + col + ".");
            
            return values[0][1];
        }
    }
    
    public double g() throws MatrixDimensionException {
        return y();
    }
    
    public double t() throws MatrixDimensionException {
        return y();
    }
    
    /* 
     * @return the z coordinate of the vector in:
     * - vector notation  : xy[z]w
     * - color notation   : rg[b]a
     * - texture notation : st[p]q
     * 
     * @throws MatrixDimensionException iff the vector is not in R3 or higher.
     */
    public double z() throws MatrixDimensionException {
        if (row > col) {
            if (col != 1 && row < 2)
                throw new MatrixDimensionException
                ("Expected vector dimensions 1xn or nx1, with n >= 3, but found vector dimensions "
                     + row + "x" + col + ".");
            
            return values[2][0];
            
        } else {
            if (row != 1 && col < 2)
                throw new MatrixDimensionException
                ("Expected vector dimensions 1xn or nx1, with n >= 3, but found vector dimensions "
                     + row + "x" + col + ".");
            
            return values[0][2];
        }
    }
    
    public double b() throws MatrixDimensionException {
        return z();
    }
    
    public double p() throws MatrixDimensionException {
        return z();
    }
    
    /* 
     * Returns the w coordinate of the vector in:
     * - vector notation  : xyz[w]
     * - color notation   : rgb[a]
     * - texture notation : stp[q]
     * 
     * @throws MatrixDimensionException iff the vector is not in R4 or higher.
     */
    public double w() throws MatrixDimensionException {
        if (row > col) {
            if (col != 1 && row < 2)
                throw new MatrixDimensionException
                ("Expected vector dimensions 1xn or nx1, with n >= 4, but found vector dimensions "
                     + row + "x" + col + ".");
            
            return values[3][0];
            
        } else {
            if (row != 1 && col < 2)
                throw new MatrixDimensionException
                ("Expected vector dimensions 1xn or nx1, with n >= 4, but found vector dimensions "
                     + row + "x" + col + ".");
            
            return values[0][3];
        }
    }
    
    public double a() throws MatrixDimensionException {
        return w();
    }
    
    public double q() throws MatrixDimensionException {
        return w();
    }
    
    
    /* 
     * Returns the coordinate represented by i.
     */
    public double getCoord(int i) {
        if (i < 0)
            throw new IllegalArgumentException("Index is not allowed to be < 0! Index found: " + i + ".");
        if (i >= row || i >= col)
            throw new IllegalArgumentException();
        
        if (row >= col) {
            return values[i][0];
            
        } else {
            return values[0][i];
        }
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
        if (row != 1 && col != 1)
            throw new MatrixDimensionException("Illegal change of matrix size. Found: " + row + "x" + col
                                                   + ", expected: 1xn or nx1.");
        return true;
    }
    
    /* 
     * Clones the object
     */
    @Override
    public Vec clone() {
        return new Vec(this);
    }
    
}
