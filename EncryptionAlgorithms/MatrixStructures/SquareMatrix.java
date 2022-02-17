package MatrixStructures;

import NumberTypes.AbstractMathObject;

import java.util.ArrayList;

public class SquareMatrix<E extends AbstractMathObject<E>> extends Matrix<E> {
    public SquareMatrix(int size, int limit, E access) {
        this.access = access;
        ONE = access.sendValue(1);
        ZERO = access.sendValue(0);
        for (int i = 0; i < size; i++) {
            ArrayList<E> row = new ArrayList<>();

            for (int j = 0; j < size; j++)
                row.add(access.sendRandomValue(limit));

            matrix.add(row);
        }
    }

    public SquareMatrix(ArrayList<ArrayList<E>> _m, E access) {
        this.access = access;
        ONE = access.sendValue(1);
        ZERO = access.sendValue(0);
        for (ArrayList<E> row : _m)
            if (_m.size() != row.size()) return;

        matrix = _m;
    }

    public SquareMatrix(int[][] _m, E access) {
        this.access = access;
        ONE = access.sendValue(1);
        ZERO = access.sendValue(0);
        for (int[] row : _m)
            if (_m.length != row.length) return;

        for (int[] r : _m) {
            ArrayList<E> row = new ArrayList<>();

            for (int e : r)
                row.add(access.sendValue(e));

            matrix.add(row);
        }
    }

    public void inverse() {
        double rank = rank();

        //this condition checks if matrix is square and if rank says matrix is invertible
        if (!(rank == height()) || !(rank == width())) throw new IllegalStateException("cannot invert this matrix");

        if (rank == height() && rank == width()) {

            IdentityMatrix<E> I = new IdentityMatrix<>(height(), width(), access);

            for (int i = 0; i < height(); i++)
                inverseWholeReduction(i, I);

            //set matrix to be the inverse
            for (int i = 0; i < height(); i++) {
                ArrayList<E> row = I.row(i);
                matrix.set(i, row);
            }
        }

    }

    //inverse helpers
    private void inverseWholeReduction(int i, Matrix<E> I) {
        E u = entry(i, i);

        if (u.equalTo(ZERO)) {
            int k = i;

            while (k < height() && entry(k, i) == ZERO)
                k++;

            swapRows(i, k);
            I.swapRows(i, k);
        }

        u = entry(i, i);
        rowScalar(i, u.inverse());
        I.rowScalar(i, u.inverse());

        for (int j = 0; j < height(); j++)
            if (j != i) inverseReduction(u, j, i, I);

    }

    private void inverseReduction(E u, int i, int j, Matrix<E> I) {
        E l = entry(i, j);
        rowAddition(j, i, l.times(-1));
        I.rowAddition(j, i, l.times(-1));
    }

    public SquareMatrix<E> copy() {
        ArrayList<ArrayList<E>> A = new ArrayList<>();
        for (ArrayList<E> row : matrix) {
            ArrayList<E> ai = new ArrayList<>(row);
            A.add(ai);
        }
        return new SquareMatrix<>(A, access);
    }
}
