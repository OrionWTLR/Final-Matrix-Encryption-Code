package EncryptionMethods;

import MatrixStructures.Matrix;
import NumberTypes.AbstractMathObject;

import java.util.ArrayList;

class PseudoInverse<E extends AbstractMathObject<E>> extends Encryption<E> {
    private final E access;
    private final E zero;
    PseudoInverse(){
        access = null; zero = null;
    }
    PseudoInverse(E access){
        this.access = access;
        zero = access.sendValue(0);
    }



    private int[][] primR = {
            {1, 5, 4, 5, 5},
            {5, 1, 4, 6, 5},
            {5, 1, 5, 7, 2},
    };
    /*int[][] primR = {
            {2, 5, 0, 0, 9},
            {3, 0, 7, 6, 4},
            {1, 0, 3, 0, 2}
    };*/
    /*int[][] primR = {
            {41, 37, 56, 45, 50},
            {43, 0, 37, 50, 0},
            {37, 52, 52, 48, 41}
    };*/
    private final Alphabet ALPHABET = new Alphabet();
    private final int PRIME = ALPHABET.size()-1;


    String oneWayHash(String plaintext){
        Matrix<E> R = new Matrix<>(primR, access),
                P = matrix(plaintext, R.height(), access),
                PR = P.times(R),
                C = PR.copy(); C.mod(PRIME);
        return word(C);
    }

    public String encrypt(String plaintext){
        Matrix<E> R = new Matrix<>(primR, access),
                P = matrix(plaintext, R.height(), access),
                PR = P.times(R),
                C = PR.copy(); C.mod(PRIME);

        Matrix<E> K = PR.minus(C); K.scaleAll(access.sendValue(PRIME).inverse());

        PR.print("PR");
        return blend(word(C), word(K)) + fInv(P.height());
    }
    public String decrypt(String ciphertext){

        String[] HK = splitK(ciphertext.substring(0, ciphertext.length()-1));
        String hash = HK[0], key = HK[1];

        int width = (int)f(ciphertext.charAt(ciphertext.length()-1));
        PseudoInverseOperations<E> op = new PseudoInverseOperations<>(access);
        Matrix<E> pseudoInvR = op.pseudoInv(new Matrix<>(primR, access));


        Matrix<E> K_transpose = matrix(key, width, access); K_transpose.scaleAll(access.sendValue(PRIME));

        Matrix<E> C_transpose = matrix(hash, width, access),
                PR = K_transpose.plus(C_transpose).transpose(),
                P = PR.times(pseudoInvR);

        return word(P.transpose());
    }

    void twoWayEncryption() {
        twoWayEncryption(this);
    }

    @Override
    void twoWayDecryption() {
        twoWayDecryption(this);
    }

    void oneWayEncryption(){
        oneWayHash(this);
    }

    public void testing(){

        /* int[][] mat = {
                {41, 43, 37, 0, 38, 42},
                {37, 0, 52, 42, 54, 37},
                {56, 37, 52, 51, 41, 55},
                {45, 50, 48, 54, 37, 56},
                {50, 0 , 41, 0, 47, 0 }
        };*/

        int[][] mat = {
                {41, 43, 37},
                {37, 0, 52},
                {56, 37, 52},
                {45, 50, 48},
                {50, 0 , 41}
        };
        PseudoInverseOperations<E> op = new PseudoInverseOperations<>(access);
        Matrix<E> R = new Matrix<>(primR, access), pseudoInvR = op.pseudoInv(R), P = new Matrix<>(mat, access);

        P.print(); R.print();
        P.times(R).print("PR");
        P.times(R).times(pseudoInvR).print("PR*pseudoInv(R)");

    }

    public void test(String plaintext){
        System.out.println(ALPHABET.size());
        PseudoInverseOperations<E> op = new PseudoInverseOperations<>(access);
        Matrix<E> R = new Matrix<>(primR, access), pseudoInvR = op.pseudoInv(R),
                P = matrix(plaintext, R.height(), access);


        E s = zero.copy();
        for(ArrayList<E> row : pseudoInvR.matrix) for(E e : row) if(s.compareDenominator(e) == -1) s = e;
        s = s.getDenominator();

        Matrix<E> S = pseudoInvR.copy(); S.scaleAll(s);
        Matrix<E> SP = S.times(P);
        Matrix<E> C = SP.copy(); C.mod(PRIME);
        Matrix<E> K = SP.minus(C); K.scaleAll(access.sendValue(PRIME).inverse());
        ArrayList<ArrayList<E>> values = new ArrayList<>();
        for(ArrayList<E> row : K.matrix){
            ArrayList<E> newRow = new ArrayList<>();
            for(E e : row) {
                newRow.add(access.sendValue(e.toLong()/89));
                newRow.add(access.sendValue(e.toLong()%89));
            }
            values.add(newRow);
        }
        Matrix<E> L = new Matrix<>(values, access);

        //String hash = matrixToWord(C),key = matrixToWord(K);

        StringBuilder list = new StringBuilder();
        for(int i = 0; i < L.width(); i++){
            for(int j = 0; j < L.height(); j++){
                E e = L.entry(j,i);
                if(e.lessThan(zero)){
                    list.append("~");
                    list.append(fInv(-1* (int)e.toLong()));
                }else{
                    list.append(fInv((int)e.toLong()));
                }
                //list.append(e.toLong());
            }
        }


        P.print("P");
        S.print("S");
        SP.print("SP");
        C.print("C");
        K.print("K");
        L.print("L");
        System.out.println(ALPHABET.size()+"\n"+list);

        //test2(C, K, R, s);

    }

    public void test2(Matrix<E> C, Matrix<E> K, Matrix<E> R, E s){
        K.scaleAll(PRIME);
        R.scaleAll(s.inverse());
        Matrix<E> SP = K.plus(C);
        Matrix<E> P = R.times(SP);

        K.print("mK");
        R.print("(1/s)R");
        SP.print("SP");
        P.print("P");
    }


}