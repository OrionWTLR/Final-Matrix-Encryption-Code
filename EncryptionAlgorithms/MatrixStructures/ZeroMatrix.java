package MatrixStructures;

import NumberTypes.AbstractMathObject;

import java.util.ArrayList;

public class ZeroMatrix<E extends AbstractMathObject<E>> extends Matrix<E> {
    public ZeroMatrix(int height, int width, E access) {
        this.access = access;
        for (int i = 0; i < height; i++) {
            ArrayList<E> row = new ArrayList<>();
            for (int j = 0; j < width; j++) row.add(access.sendValue(0));
            matrix.add(row);
        }
    }
}
