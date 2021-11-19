import java.util.ArrayList;

public class PseudoInverseOperations<E extends AbstractMathObject<E>>{
    private final E access;
    private final E ZERO;
    private final E ONE;
    PseudoInverseOperations(){
        access = null; ZERO = null; ONE = null;
    }
    PseudoInverseOperations(E access){
        this.access = access;
        ZERO = access.sendValue(0);
        ONE = access.sendValue(1);
    }

    private E norm(ArrayList<E> vector){
        E squareSum = ZERO;
        for(E v : vector){
            //System.out.println(squareSum+" + "+v+"^2");
            squareSum = squareSum.plus(v.pow(2));
        }
        return squareSum.sqrt();
    }

    private ArrayList<E> subRow(Matrix<E> R, ArrayList<Integer> K, int rowIndex){
        ArrayList<E> keyRow = new ArrayList<>();
        if(K.size() == 0){
            throw new NullPointerException("K is empty");
        }else if(K.size() == 1){
            int k = K.get(0);
            keyRow.add(R.row(rowIndex).get(k));
            return keyRow;
        }else {
            for (int i : K) {
                keyRow.add(R.row(rowIndex).get(i));
            }
            return keyRow;
        }
    }

    private E produceAlpha(Matrix<E> R, ArrayList<Integer> k_i, ArrayList<E> keyRow, E norm, int rowIndex){
        //extract the following row in R that is not 0
        ArrayList<E> partialRow = subRow(R, k_i, rowIndex);
        //determine the dot product of keyRow and partialRow

        E dot = R.dot(keyRow, partialRow);

        return (dot.times(-1)).divide(norm);
    }

    private ArrayList<E> produceAlphaList(Matrix<E> R, ArrayList<Integer> K, ArrayList<E> keyRow, E norm, int pivotRowIndex){
        ArrayList<E> alphas = new ArrayList<>();
        for(int partialRowIndex = 0; partialRowIndex < R.height(); partialRowIndex++){
            if(partialRowIndex != pivotRowIndex){
                alphas.add(produceAlpha(R, K, keyRow, norm, partialRowIndex));
            }
        }
        return alphas;
    }

    private void rowOp(Matrix<E> R, ArrayList<E> alphas, E norm, int pivotRowIndex){
        //In matrix R, divide the row keyRow was extracted from by the norm. Call that row the pivotRow.

        R.rowScalar(pivotRowIndex, (norm.inverse()));

        //then perform the row addition: row_n = row_n + alpha_n * pivotRow where n != pivotRowIndex.
        //set alphaCounter outside loop to 0 then increment it in the loop only when i isn't equal to the pivotRow
        //this allows alphaCounter to keep the same count for each new row that gets operated on
        int alphaCounter = 0;
        for(int i = 0; i < R.height(); i++){
            if(i != pivotRowIndex){
                R.rowAddition(pivotRowIndex, i, alphas.get(alphaCounter));
                alphaCounter++;
            }
        }
    }

    private ArrayList<E> innerPseudoInverseSteps(Matrix<E> R, Matrix<E> iMM, ArrayList<Integer> K, int pivotRowIndex){
        //extract keyRow from matrix R according to key indexes
        ArrayList<E> keyRow = subRow(R, K, pivotRowIndex);

        //determine the norm of keyRow
        E norm = norm(keyRow);

        //get a list of alpha values. these are scalars that will be used later in row operations
        ArrayList<E> alphas = produceAlphaList(R, K, keyRow, norm, pivotRowIndex);

        //perform these specific row echelon operations with alphas, norm and pivotRowIndex on matrix R
        rowOp(R, alphas, norm, pivotRowIndex);

        //Do the exact same thing to i[M,M]
        rowOp(iMM, alphas, norm, pivotRowIndex);

        //the following will be used to construct matrix B
        ArrayList<E> pivotElements = subRow(R, K, pivotRowIndex);

        //use key indexes to determine which indexes each pivot element belongs in
        ArrayList<E> pivotVector = new ArrayList<>();
        //fill that vector with 0s
        for(int i = 0; i < R.width(); i++){
            pivotVector.add(ZERO);
        }
        //remove a 0 and replace it with a pivot element at kth place in the vector
        int i = 0;
        for(int k : K){
            pivotVector.remove(k);
            pivotVector.add(k, pivotElements.get(i));
            i++;
        }

        return pivotVector;
    }

    private ArrayList<ArrayList<Integer>> keyPress(Matrix<E> A){
        ArrayList<ArrayList<Integer>> Keys = new ArrayList<>();
        int h = A.height();
        int w = A.width();
        if(h == 1){//(w/h) must be integer division
            Keys.add(subListIntervals(0, w/h));
        }else {//the integer division must be done within the loop
            for(int i = 1; i <= h; i++) Keys.add(subListIntervals( (i-1)*w/h, i*w/h ));
        }

        return Keys;
    }

    private ArrayList<Integer> subListIntervals(int start, int end){
        ArrayList<Integer> k1 = new ArrayList<>();
        for(int i = start; i < end; i++) k1.add(i);
        return k1;
    }

    public Matrix<E> pseudoInv(Matrix<E> A){
        //height cannot be less than 3
        Matrix<E> R = A.copy();
        IdentityMatrix<E> iMM = new Matrix<>(R.height(), R.height(), access).makeIdentity();

        //Number of key lists must equal the number of rows
        ArrayList<ArrayList<Integer>> Keys = keyPress(R);

        //there MUST be as many keys as there are rows in matrices R and i[M,M]
        ArrayList<ArrayList<E>> pivotVectors = new ArrayList<>();
        int pivotRowIndex = 0;
        for(ArrayList<Integer> k_i : Keys) {
            //Extract the pivotVectors after each step(); to construct a new Matrix
            pivotVectors.add( innerPseudoInverseSteps(R, iMM, k_i, pivotRowIndex) );
            pivotRowIndex++;
        }

        Matrix<E> B = new Matrix<>(pivotVectors, access);

        Matrix<E> Bt = B.copy().transpose();

        return Bt.times(iMM);
    }
}
