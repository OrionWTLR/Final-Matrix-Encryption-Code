package EncryptionMethods;

import MatrixStructures.Matrix;
import NumberTypes.AbstractMathObject;

public class SquareMatrix<E extends AbstractMathObject<E>> extends Encryption<E> {
    private final MatrixStructures.SquareMatrix<E> CodingMatrix;
    private final int prime;
    private final E PRIME;
    private final E access;
    private final int PHI = 73;

    public SquareMatrix(E acc){
        int[][] primCodingMatrix = {
                {2, 3, 6, 3, 5},
                {8, 5, 2, 8, 7},
                {4, 0, 5, 7, 8},
                {1, 6, 3, 1, 5},
                {6, 1, 0, 7, 4}
        };

/*        double[][] primCodingMatrix = {
                {2.0, 3.0, 6.0},
                {8.0, 5.0, 2.0},
                {4.0, 0.0, 5.0}};*/

        /*
        double[][] primCodingMatrix =
                {{46.0, 5.0, 43.0, 4.0, 27.0, 2.0, 38.0},
                {20.0, 16.0, 7.0, 7.0, 41.0, 23.0, 31.0},
                {1.0, 19.0, 36.0, 28.0, 2.0, 49.0, 16.0},
                {30.0, 11.0, 1.0, 20.0, 37.0, 19.0, 36.0},
                {41.0, 37.0, 22.0, 17.0, 34.0, 0.0, 3.0},
                {49.0, 6.0, 29.0, 18.0, 20.0, 48.0, 36.0},
                {23.0, 12.0, 9.0, 27.0, 28.0, 5.0, 47.0}};*/
        access = acc;
        CodingMatrix = new MatrixStructures.SquareMatrix<>(primCodingMatrix, access);
        prime = 87;
        PRIME = access.sendValue(prime);
    }

    String oneWayHash(String plaintext){
        MatrixStructures.SquareMatrix<E> A = CodingMatrix.copy();
        Matrix<E> B = matrix(plaintext, A.width(), access).transpose(),
                AB = A.times(B),
                C = AB.copy(); C.mod(prime);

        return word(C);
    }
    public String encrypt(String plaintext){
        MatrixStructures.SquareMatrix<E> A = CodingMatrix.copy();
        Matrix<E> B = matrix(plaintext, A.width(), access).transpose(),
                AB = A.times(B),
                C = AB.copy(); C.mod(prime);
        Matrix<E> K = AB.minus(C);

        K.scaleAll(PRIME.inverse());

        return blend(word(C), word(K));
    }
    public String decrypt(String ciphertext){

        String[] HK = splitK(ciphertext);
        String hash = HK[0], key = HK[1];

        Matrix<E> K = matrix(key, CodingMatrix.height(), access).transpose(),
                C = matrix(hash, CodingMatrix.height(), access).transpose();

        K.scaleAll(PRIME);

        Matrix<E> AB = K.plus(C);
        MatrixStructures.SquareMatrix<E> invA = CodingMatrix.copy(); invA.inverse();
        Matrix<E> B = invA.times(AB);

        return word(B);
    }

    public void twoWayEncryption() {
        twoWayEncryption(this);
    }

    @Override
    void twoWayDecryption() {
        twoWayDecryption(this);
    }

    public void oneWayEncryption(){
        oneWayHash(this);
    }

}