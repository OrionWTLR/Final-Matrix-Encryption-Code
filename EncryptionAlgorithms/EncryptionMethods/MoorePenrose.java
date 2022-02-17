package EncryptionMethods;

import MatrixStructures.IdentityMatrix;
import MatrixStructures.Matrix;
import MatrixStructures.SquareMatrix;
import MatrixStructures.ZeroMatrix;
import NumberTypes.AbstractMathObject;

import java.util.ArrayList;

public class MoorePenrose<T extends AbstractMathObject<T>> extends Encryption<T> {
    private final Alphabet ALPHABET = new Alphabet();
    private final long PRIME = ALPHABET.size()-1;
    private final long PHI = 83;
    private final Matrix<T> E;
    private Matrix<T> F;
    private final T access;
    public MoorePenrose(T access){
        this.access = access;
        int[][] e = {
                {1, 3, 4},
                {4, 1, 5},
                {1, 1, 0},
                {1, 1, 1},
                {2, 5, 5},
                {1, 1, 3}
        };
        int[][] fAdd = {
                {1,0,-1},
                {1,0,0},
                {0,1,1}
        };

        E = new Matrix<>(e, access);
        F = new IdentityMatrix<>(E.width(), E.width(), access);
        F = F.plus(new Matrix<>(fAdd, access));
    }

    public String oneWayHash(String plaintext){
        Matrix<T> R = E.times(F),
                P = matrix(plaintext, R.width(), access).transpose(),
                RP = R.times(P),
                C = RP.copy();
        C.mod(PRIME);
        return matrixWord(C);
    }

    public String encrypt(String plaintext){
        Matrix<T> R = E.times(F),
                P = matrix(plaintext, R.width(), access).transpose(),
                RP = R.times(P),
                C = RP.copy(); C.mod(PRIME);

        Matrix<T> K = RP.minus(C);

        K.scaleAll(access.sendValue(PRIME).inverse());

        return blend(word(C), word(K));
    }
    public String decrypt(String ciphertext){

        String[] HK = splitK(ciphertext);
        String hash = HK[0], key = HK[1];

        Matrix<T> C = matrix(hash, E.height(), access).transpose(),
        K = matrix(key, E.height(), access).transpose();

        K.scaleAll(access.sendValue(PRIME));

        Matrix<T> pseudoR = pseudoInv(E,F),
        RP = K.plus(C),
        P = pseudoR.times(RP);

        return word(P);
    }


    private void generalizedInverseDefinition(Matrix<T> E, Matrix<T> F){
        Matrix<T> R = E.times(F),
        Rp = pseudoInv(E, F);
        E.print("E="); F.print("F=");

        System.out.println("(1)");
        R.print("R=");
        R.times(Rp).times(R).print("R*Rp*R=");


        System.out.println("(2)");
        Rp.print("Rp=");
        Rp.times(R).times(Rp).print("Rp*R*Rp=");


        System.out.println("(3)");
        R.times(Rp).transpose().print("(R*Rp)_t=");
        R.times(Rp).print("R*Rp=");

        System.out.println("(4)");
        Rp.times(R).transpose().print("(Rp*R)_t=");
        Rp.times(R).print("Rp*R=");

        if(R.rank() == E.width() && R.rank() == F.transpose().width()){
            System.out.println("Full Rank Factorization");
        }else{
            System.out.println("Not Full");
        }

        Matrix<T> O = new ZeroMatrix<>( R.height()-R.rank(), R.width(), access);

        Matrix<T> B = F.copy();
        B.stackOver(O);
        B.print("B=");

    }
    private void basicPropertiesOfGeneralizedInverse(Matrix<T> E, Matrix<T> F){
        Matrix<T> R = E.times(F),
        Rp = pseudoInv(E, F),
        Rt = R.transpose(),
        Ep = pseudoInvLeft(E),
        Et = E.transpose(),
        Fp = pseudoInvRight(F),
        Ft = F.transpose(),
        lambdaInvRp = Rp.copy(),
        lambdaE = E.copy();


        T lambda = access.sendValue(57);
        lambdaE.scaleAll(lambda);
        lambdaInvRp.scaleAll(lambda.inverse());

        System.out.println("(1)");
        pseudoInv(Fp, Ep).print("(Rp)p=");
        E.times(F).print("R=");


        System.out.println("(2)");
        Fp.times(Ep).transpose().print("(Rp)_t=");
        pseudoInv(Ft, Et).print("(R_t)p=");


        System.out.println("(3)");
        pseudoInv(lambdaE, F).print("(lambdaEF)p=");
        lambdaInvRp.print("(1/lambda)Rp=");


        System.out.println("(4)");
        Rt.print("Rt=");
        Rt.times(R).times(Rp).print("R_t*R*Rp=");
        Rp.times(R).times(Rt).print("Rp*R*R_t=");


        SquareMatrix<T> invRRt = (SquareMatrix<T>) R.times(Rt),
        invRtR = (SquareMatrix<T>) Rt.times(R);
        invRRt.inverse(); invRtR.inverse();


        System.out.println("(5)");
        pseudoInv(Rt, R).print("(R_t*R)p");
        Rt.times(invRRt.times(invRRt)).times(R).print("Rt*(inv(RRt)^2)*R");

        System.out.println("(6)");
        Rt.times(invRRt.times(invRRt)).times(R).times(Rt).print("(Rt*R)p*Rt");
        Rt.times(R).times(invRtR.times(invRtR)).times(Rt).print("Rt(R*Rt)p=");


    }


    private Matrix<T> pseudoInv(Matrix<T> E, Matrix<T> F){
        return pseudoInvRight(F).times(pseudoInvLeft(E));
    }
    private Matrix<T> pseudoInvRight(Matrix<T> F){
        SquareMatrix<T> invFFt = F.times(F.transpose()).makeSquare();
        invFFt.inverse();
        return F.transpose().times(invFFt);
    }
    private Matrix<T> pseudoInvLeft(Matrix<T> E){
        SquareMatrix<T> invEtE = E.transpose().times(E).makeSquare();
        invEtE.inverse();
        return invEtE.times(E.transpose());
    }

    String matrixWord(Matrix<T> matrix){
        ArrayList<Long> numbers = convertToLongList(matrix);

        StringBuilder word = new StringBuilder();
        for(long i : numbers){
            if(i < 0) {
                char negativeChar = fInv((int) (-1*i));
                word.append("~").append(negativeChar);
            }else{
                char fInv = fInv((int) i);
                word.append(fInv);
            }
        }

        return word.toString();
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