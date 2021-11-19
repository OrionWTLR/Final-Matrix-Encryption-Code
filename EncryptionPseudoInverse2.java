import java.util.ArrayList;

class EncryptionPseudoInverse2<E extends AbstractMathObject<E>> extends Encryption<E>{
    private final E access;
    private final E ZERO;
    private final E PRIME;
    private final Alphabet ALPHABET = new Alphabet();
    private final int prime = ALPHABET.size()-1;
    private final long lambda = 73;

    private final int[][] primR = {
            {1, 5, 4, 5, 5},
            {5, 1, 4, 6, 5},
            {5, 1, 5, 7, 2}
    };

    public EncryptionPseudoInverse2(E access){
        this.access = access;
        ZERO = access.sendValue(0);
        PRIME = access.sendValue(prime);
    }


    String oneWayHash(String plaintext) {
        PseudoInverseOperations<E> op = new PseudoInverseOperations<>(access);
        int height = primR.length;
        Matrix<E> pseudoInvR = op.pseudoInv(new Matrix<>(primR, access)),
                P = matrix(plaintext, height, access).transpose();

        E s = largestDenominator(pseudoInvR);

        Matrix<E> S = pseudoInvR.copy(); S.scaleAll(s);
        Matrix<E> C = S.times(P); C.mod(prime);
        return word(C);
    }
    String encrypt(String plaintext) {
        PseudoInverseOperations<E> op = new PseudoInverseOperations<>(access);
        Matrix<E> R = new Matrix<>(primR, access),
                pseudoInvR = op.pseudoInv(R),
                P_transpose = matrix(plaintext, pseudoInvR.width(), access).transpose();

        E s = largestDenominator(pseudoInvR);

        Matrix<E> S = pseudoInvR.copy(); S.scaleAll(s);
        Matrix<E> SP = S.times(P_transpose);
        Matrix<E> C = S.times(P_transpose); C.mod(prime);
        Matrix<E> K = SP.minus(C); K.scaleAll(PRIME.inverse());
        Matrix<E> L = divModSplit(K);

        P_transpose.print("P_transpose");
        C.print("C");
        K.print("K");
        L.print("L");

        return blend(word(C), word(L));

    }
    String decrypt(String ciphertext) {

        String[] HK = splitL(ciphertext);
        String hash = HK[0], key = HK[1];

        PseudoInverseOperations<E> op = new PseudoInverseOperations<>(access);
        Matrix<E> R = new Matrix<>(primR, access),
                pseudoInvR = op.pseudoInv(R),
                L = matrix(key, pseudoInvR.height(), access).transpose(),
                K = divModSum(L),
                C = matrix(hash, pseudoInvR.height(), access).transpose();

        L.print("L");
        K.print("K");
        C.print("C");

        K.scaleAll(PRIME);
        Matrix<E> SP = K.plus(C);

        E s = largestDenominator(pseudoInvR);
        R.scaleAll(s.inverse());

        Matrix<E> P = R.times(SP);


        P.print("P");

        return word(P);
    }


    void twoWayEncryption() {
        twoWayEncryption(this);
    }

    void oneWayEncryption() {
        oneWayHash(this);
    }

    private Matrix<E> divModSplit(Matrix<E> A){
        Matrix<E> splitA = new Matrix<>();
        for(ArrayList<E> row : A.matrix){
            ArrayList<E> newRow = new ArrayList<>();
            for(E e : row) {
                newRow.add(access.sendValue((e.toLong()/ lambda)));
                newRow.add(access.sendValue(e.toLong() % lambda));
            }
            splitA.matrix.add(newRow);
        }
        return splitA;
    }
    private Matrix<E> divModSum(Matrix<E> splitA){
        Matrix<E> A = new Matrix<>(access);
        for(int i = 0; i < splitA.height(); i++){
            ArrayList<E> row = new ArrayList<>();
            for(int j = 0; j < splitA.width()-1; j+=2){
                E a = splitA.entry(i,j), b = splitA.entry(i, (j+1)), c = a.times(lambda).plus(b);
                row.add(c);
            }
            A.matrix.add(row);
        }
        return A;
    }

    private E largestDenominator(Matrix<E> A){
        E s = ZERO.copy();
        for(ArrayList<E> row : A.matrix) for(E e : row) if(s.compareDenominator(e) == -1) s = e;
        return s.getDenominator();
    }

}
