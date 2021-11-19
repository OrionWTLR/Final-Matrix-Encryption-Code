import java.util.ArrayList;
import java.util.Arrays;

public class Matrix<E extends AbstractMathObject<E>>{
    ArrayList<ArrayList<E>> matrix = new ArrayList<>();
    E ZERO;
    E ONE;
    E access;

    private void setTypeE(E access){
        this.access = access;
        ZERO = access.sendValue(0);
        ONE = access.sendValue(1);
    }

    //Constructors
    Matrix(){
    }
    Matrix(E access){
        setTypeE(access);
    }
    Matrix(ArrayList<ArrayList<E>> _m, E access){
        setTypeE(access);
        matrix = _m;
    }
    Matrix(int height, int width, int limit, E access){
        setTypeE(access);
        for(int i = 0; i < height; i++){

            ArrayList<E> row = new ArrayList<>();
            for(int j = 0; j < width; j++)
                row.add( access.sendRandomValue(limit));

            matrix.add(row);
        }
    }
    Matrix(E[][] _m, E access){
        setTypeE(access);

        for(E[] row : _m){
            ArrayList<E> newRow = new ArrayList<>(Arrays.asList(row));
            matrix.add(newRow);
        }
    }
    Matrix(long[][] _m,  E access){
        setTypeE(access);

        for(long[] row : _m){
            ArrayList<E> newRow = new ArrayList<>();
            for(long entry : row)
                newRow.add(access.sendValue(entry));

            matrix.add(newRow);
        }
    }
    Matrix(int[][] _m, E access){
        setTypeE(access);

        for(int[] row : _m){
            ArrayList<E> newRow = new ArrayList<>();
            for(int entry : row)
                newRow.add(access.sendValue(entry));

            matrix.add(newRow);
        }
    }
    Matrix(int height, int width, E access){
        setTypeE(access);

        ArrayList<ArrayList<E>> matrix = new ArrayList<>();
        for(int i = 0; i < height; i++){
            ArrayList<E> row = new ArrayList<>();
            for(int j = 0; j < width; j++){
                if(i == j) row.add(ONE);
                if(i != j) row.add(ZERO);
            }
            matrix.add(row);
        }
        this.matrix = matrix;
    }

    boolean equals(Matrix<E> M){

        if(height() != M.height()) return false;

        if(width() != M.width()) return false;

        if(height() == M.height() && width() == M.width()){
            for(int i=0; i < height(); i++)
                for(int j=0; j < width(); j++)
                    if(entry(i,j).notEqualTo(M.entry(i,j))) return false;
        }

        return true;
    }

    //get an element or vector from a single matrix
    E entry(int rowIndex, int columnIndex){return matrix.get(rowIndex).get(columnIndex);}
    ArrayList<E> row(int rowIndex){return matrix.get(rowIndex);}
    ArrayList<E> col(int colIndex){
        ArrayList<E> col = new ArrayList<>();
        for(int i = 0; i < height(); i++)
            col.add(entry(i,colIndex));

        return col;
    }

    //row operations
    void swapRows(int upper, int lower){
        ArrayList<E> temp = matrix.get(upper);
        matrix.set(upper, matrix.get(lower));
        matrix.set(lower, temp);
    }
    void rowScalar(int rowIndex, E scalar){

        ArrayList<E> oldRow = matrix.get(rowIndex);
        ArrayList<E> newRow = new ArrayList<>();

        for(E fraction : oldRow) newRow.add(scalar.times(fraction));

        matrix.set(rowIndex, newRow);
    }
    void rowAddition(int scaledRow, int plainRow, E scalar){
        ArrayList<E> nextLowRow = new ArrayList<>();

        for (int i = 0; i < width(); i++)
            nextLowRow.add(matrix.get(plainRow).get(i).plus(scalar.times(matrix.get(scaledRow).get(i))));

        matrix.set(plainRow, nextLowRow);
    }
    void rowReduce(int upperRowIndex, int lowerRowIndex, int focalColumn){

        //u is the diagonal entry at the focal column
        E u = matrix.get(upperRowIndex).get(focalColumn);

        //if u is 0 then we cannot proceed with the row reduction
        if(u.equals(ZERO)) {
            //find row with non-zero focal entry
            E possibleSwapEntry = entry((int)ZERO.toLong(), focalColumn);
            for (int i = 1; possibleSwapEntry.equals(ZERO) && i < height(); i++) {
                possibleSwapEntry = entry(i, focalColumn);
                lowerRowIndex = i;
            }

            //swap rows in question
            swapRows(upperRowIndex, lowerRowIndex);
        }else{
            //continue on normally after the matrix has been manipulated to avoid division by zero

            u = matrix.get(upperRowIndex).get(focalColumn);
            E l = matrix.get(lowerRowIndex).get(focalColumn);

            //l is the entry right below u
            //the fraction l/u is how necessary to get l to become 0
            rowAddition(upperRowIndex, lowerRowIndex, l.divide(u).times(-1));

        }
    }

    //operations between matrices
    Matrix<E> times(Matrix<E> B){
        ArrayList<ArrayList<E>> product = new ArrayList<>();

        //System.out.println("times{"); this.print(); B.print(); System.out.println("=");

        if(width() == B.height()) {
            for (int i = 0; i < height(); i++) {
                ArrayList<E> row = new ArrayList<>();
                for (int j = 0; j < B.width(); j++) row.add( dot(row(i), B.col(j)));
                product.add(row);
            }
        }else if(height() == B.width()){
            for (int i = 0; i < height(); i++) {
                ArrayList<E> row = new ArrayList<>();
                for (int j = 0; j < B.width(); j++) row.add( dot(col(i), B.row(j)) );
                product.add(row);
            }
        }

        //Convert new 2D array list to a matrix
        //new Matrix<E>(product, access).print();
        //System.out.println("}\n");

        return new Matrix<>(product, access);
    }

    SquareMatrix<E> makeSquare(){
        if(width() ==  height()) return new SquareMatrix<>(matrix, access);
        throw new IllegalStateException("cannot make square");
    }
    IdentityMatrix<E> makeIdentity(){
        boolean isNot = true;
        if(width() == height())
            for(int i = 0; i < height(); i++)
                for(int j = 0; j < width(); j++)
                    if((i == j && !entry(i,j).equalTo(access.sendValue(1))) ||
                            (i != j && !entry(i,j).equalTo(access.sendValue(0)))) isNot = false;

        if(isNot) return new IdentityMatrix<>(matrix.size(), matrix.size(), access);

        throw new IllegalStateException("cannot make identity");
    }
    ZeroMatrix<E> makeZero(){
        boolean isNot = true;
        if(width() == height())
            for(int i = 0; i < height(); i++)
                for(int j = 0; j < width(); j++)
                    if(entry(i,j).equalTo(access.sendValue(0))) isNot = false;

        if(isNot) return new ZeroMatrix<>(matrix.size(), matrix.size(), access);

        throw new IllegalStateException("cannot make zero");
    }

    Matrix<E> plus(Matrix<E> B){
        ArrayList<ArrayList<E>> sum = new ArrayList<>();
        if(width() == B.width() && height() == B.height()){
            for(int i = 0; i < height(); i++){
                ArrayList<E> rowA = row(i);
                ArrayList<E> rowB = B.row(i);
                ArrayList<E> sumRow = new ArrayList<>();
                for(int j = 0; j < width(); j++)
                    sumRow.add( rowA.get(j).plus(rowB.get(j)));
                sum.add(sumRow);
            }
        }
        return new Matrix<>(sum, access);
    }

    Matrix<E> minus(Matrix<E> B){
        ArrayList<ArrayList<E>> sum = new ArrayList<>();
        if(width() == B.width() && height() == B.height()){
            for(int i = 0; i < height(); i++){
                ArrayList<E> rowA = row(i);
                ArrayList<E> rowB = B.row(i);
                ArrayList<E> sumRow = new ArrayList<>();
                for(int j = 0; j < width(); j++)
                    sumRow.add( rowA.get(j).plus(rowB.get(j).times(-1)));
                sum.add(sumRow);
            }
        }
        return new Matrix<>(sum, access);
    }

    //operations on a single matrix
    /*void scaleAll(long a, long b){
        scaleAll(new Fraction(a,b));
    }*/
    void scaleAll(E scalar){
        for(int i = 0; i < matrix.size(); i++)
            rowScalar(i, scalar);
    }
    void scaleAll(long scalar){
        scaleAll(access.sendValue(scalar));
    }
    int rank(){
        int rankCount = 0;

        //copy method still points to the same matrix when it shouldn't
        Matrix<E> copyOfMatrix = copy();
        copyOfMatrix.upperTriangularForm();

        for(int i = 0; i < width() && i < height(); i++){
            E diag = copyOfMatrix.entry(i, i);

            if(diag.notEqualTo(ZERO)) rankCount++;
        }
        return rankCount;
    }
    void transposeSelf(){
        ArrayList<ArrayList<E>> primAT = new ArrayList<>();
        for(int i = 0; i < width(); i++){
            ArrayList<E> row = col(i);
            primAT.add(row);
        }
        matrix = primAT;
    }
    Matrix<E> transpose(){
        ArrayList<ArrayList<E>> primAT = new ArrayList<>();
        for(int i = 0; i < width(); i++){
            ArrayList<E> row = col(i);
            primAT.add(row);
        }
        return new Matrix<>(primAT, ONE);
    }


    void upperTriangularForm(){
        for (int i = 0; i < width(); i++)
            for (int j = (i + 1); j < height(); j++)
                rowReduce(i, j, i);
    }

    public void mod(long m){
        for(ArrayList<E> row : matrix){
            for(int j = 0; j < width(); j++){
                E oldE = row.get(j);

                while(oldE.lessThan(ZERO))
                    oldE = oldE.plus(m);

                row.set(j, oldE.mod(m));
            }
        }
    }

    //vector operations
    E dot(ArrayList<E> r, ArrayList<E> c){

        E dotProduct = access.sendValue(0);


        if(r.size() == c.size())
            for(int i = 0; i < r.size(); i++) {
                dotProduct = dotProduct.plus(r.get(i).times(c.get(i)));
            }
        return dotProduct;
    }
    void skipRowAddition(int p, E s, ArrayList<E> alphas){

        set(p, scalar(s, row(p)));
        int j = 0;
        for(int i = 0; i < height(); i++){
            if(i != p) {
                set(i, addRow(alphas.get(j), row(p), row(i)));
                j++;
            }
        }
    }

    ArrayList<E> scalar(E a, ArrayList<E> V){
        ArrayList<E> W = new ArrayList<>();
        for(E v : V){
            W.add(a.times(v));
        }
        return W;
    }
    ArrayList<E> addRow(E a, ArrayList<E> V1, ArrayList<E> V2){
        ArrayList<E> Sum = new ArrayList<>();
        if(V1.size() == V2.size()){
            int i = 0;
            for(E v1 : V1){
                E v2 = V2.get(i);
                E sum = (a.times(v1)).plus(v2);
                Sum.add(sum);
                i++;
            }
        }
        return Sum;
    }

    void stackOver(Matrix<E> other){
        matrix.addAll(other.matrix);
    }
    void stackSide(Matrix<E> other){
        for(int i = 0; i < other.height(); i++){
            for(int j = 0; j < other.width(); j++){
                matrix.get(i).add(other.entry(i, j));
            }
        }
    }

    //misc
    Matrix<E> copy(){
        ArrayList<ArrayList<E>> A = new ArrayList<>();
        for(ArrayList<E> row : matrix) A.add(new ArrayList<>(row));
        return new Matrix<>(A, access);
    }
    ArrayList<ArrayList<E>> matrix(){return matrix;}
    int height(){return matrix.size();}
    int width(){return matrix.get(0).size();}
    void set(int i, ArrayList<E> row){matrix.set(i, row);}
    String dimensions(){return ""+height()+", "+width();}

    //print statements
    void printCodeForm(String s){
        System.out.println(s);
        printCodeForm();
    }
    void printCodeForm(){
        System.out.print("{");
        for(int i = 0; i < height(); i++){
            ArrayList<E> row = matrix.get(i);
            System.out.print("{");
            for(int j = 0; j < width(); j++){
                E e = row.get(j);
                cutOff(j, width(), (e+""));
            }
            cutOff(i, height(), "}");
        }
        System.out.println("}");
    }
    private void cutOff(int i, int limit, String s){
        if(i < limit-1) System.out.print(s+", ");
        else if(i == limit-1) System.out.print(s);

    }
    void printRow(int rowIndex){
        for(E e : matrix.get(rowIndex))
            System.out.print(e+"   ");
    }
    void printRowLaTex(int rowIndex){
        int i = 0;
        for(E e : matrix.get(rowIndex)){

            if(i < matrix.get(rowIndex).size()-1) System.out.print(e + " & ");
            else System.out.print(e);
            i++;
        }
    }
    void print(){
        for(int i = 0; i < matrix.size(); i++){
            printRow(i);
            System.out.println();
        }
        System.out.println();
    }
    void printLaTex(){
        for(int i = 0; i < matrix.size(); i++){
            printRowLaTex(i);
            System.out.println();
        }
        System.out.println();
    }
    void printSideBySide(Matrix<E> B){
        if(height() == B.height() && width() == B.width()) {
            for (int i = 0; i < height(); i++) {
                int k = 0;
                for (int j = 0; j < width() + B.width(); j++) {
                    if (j < width()) {
                        System.out.print(matrix.get(i).get(j) + "  ");
                    } else if (j == width()) {
                        System.out.print("|  " + B.entry(i, k) + "  ");
                        k++;
                    } else if (j > width()) {
                        System.out.print(B.entry(i, k) + "  ");
                        k++;
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }
    void print(String label){
        System.out.println(label);
        print();
    }
    void printLaTex(String label){
        System.out.println(label);
        printLaTex();
    }

    public String toString(){
        return matrix.toString();
    }
}

class SquareMatrix<E extends AbstractMathObject<E>> extends Matrix<E> {
    SquareMatrix(int size, int limit, E access){
        this.access = access;
        ONE = access.sendValue(1);
        ZERO = access.sendValue(0);
        for(int i = 0; i < size; i++){
            ArrayList<E> row = new ArrayList<>();

            for(int j = 0; j < size; j++)
                row.add( access.sendRandomValue(limit));

            matrix.add(row);
        }
    }

    SquareMatrix(ArrayList<ArrayList<E>> _m, E access){
        this.access = access;
        ONE = access.sendValue(1);
        ZERO = access.sendValue(0);
        for(ArrayList<E> row : _m)
            if(_m.size() != row.size()) return;

        matrix = _m;
    }

    SquareMatrix(int[][] _m, E access){
        this.access = access;
        ONE = access.sendValue(1);
        ZERO = access.sendValue(0);
        for(int[] row : _m)
            if(_m.length != row.length) return;

        for(int[] r : _m){
            ArrayList<E> row = new ArrayList<>();

            for(int e : r)
                row.add(access.sendValue(e));

            matrix.add(row);
        }
    }

    void inverse(){
        double rank = rank();

        //this condition checks if matrix is square and if rank says matrix is invertible
        if(!(rank == height()) || !(rank == width())) throw new IllegalStateException("cannot invert this matrix");

        if(rank == height() && rank == width()){

            IdentityMatrix<E> I = new IdentityMatrix<>(height(), width(), access);

            for(int i = 0; i < height(); i++)
                inverseWholeReduction(i, I);

            //set matrix to be the inverse
            for(int i = 0; i < height(); i++){
                ArrayList<E> row = I.row(i);
                matrix.set(i, row);
            }
        }

    }
    //inverse helpers
    private void inverseWholeReduction(int i, Matrix<E> I){
        E u = entry(i,i);

        if(u.equalTo(ZERO)){
            int k = i;

            while(k < height() && entry(k,i) == ZERO)
                k++;

            swapRows(i,k);
            I.swapRows(i,k);
        }

        u = entry(i,i);
        rowScalar(i, u.inverse());
        I.rowScalar(i, u.inverse());

        for(int j = 0; j < height(); j++)
            if(j != i) inverseReduction(u, j, i, I);

    }
    private void inverseReduction(E u, int i, int j, Matrix<E> I){
        E l = entry(i, j);
        rowAddition(j, i, l.times(-1));
        I.rowAddition(j, i, l.times(-1));
    }

    SquareMatrix<E> copy(){
        ArrayList<ArrayList<E>> A = new ArrayList<>();
        for(ArrayList<E> row : matrix){
            ArrayList<E> ai = new ArrayList<>(row);
            A.add(ai);
        }
        return new SquareMatrix<>(A, access);
    }
}

class IdentityMatrix<E extends AbstractMathObject<E>> extends Matrix<E> {
    IdentityMatrix(int height, int width, E access){
        this.access = access;
        for(int i = 0; i < height; i++){
            ArrayList<E> row = new ArrayList<>();

            for(int j = 0; j < width; j++){
                if(i == j) row.add(access.sendValue(1));
                else row.add(access.sendValue(0));
            }

            matrix.add(row);
        }
    }
}

class ZeroMatrix<E extends AbstractMathObject<E>> extends Matrix<E>{
    ZeroMatrix(int height, int width, E access){
        this.access = access;
        for(int i = 0; i < height; i++){
            ArrayList<E> row = new ArrayList<>();
            for(int j = 0; j < width; j++) row.add(access.sendValue(0));
            matrix.add(row);
        }
    }
}



