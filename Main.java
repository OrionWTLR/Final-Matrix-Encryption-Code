public class Main {
    public static void main(String[] args){
        //allMethods();
        test();
        //pseudoInverseExample();
        //matrixTest();
    }

    private static void allMethods(){
        EncryptionSquareMatrix<RootFraction> square = new EncryptionSquareMatrix<>(new RootFraction());
        EncryptionMoorePenrose<RootFraction> moore = new EncryptionMoorePenrose<>(new RootFraction());
        EncryptionPseudoInverse<RootFraction> pseudo = new EncryptionPseudoInverse<>(new RootFraction());
        EncryptionPseudoInverse2<RootFraction> pseudo2 = new EncryptionPseudoInverse2<>(new RootFraction());

        square.oneWayEncryption();
        moore.oneWayEncryption();
        pseudo.oneWayEncryption();
        pseudo2.oneWayEncryption();
    }

    private static void sample(Encryption<RootFraction> e){
        String plaintext = "David A. Patterson has been teaching computer architecture at the University of California";
        String ciphertext = e.encrypt(plaintext);
        String decrypted = e.decrypt(ciphertext);

        System.out.println(plaintext+"\n"+ciphertext+"\n"+decrypted+"\n");
    }

    private static void test(){
        EncryptionSquareMatrix<RootFraction> square = new EncryptionSquareMatrix<>(new RootFraction());
        EncryptionMoorePenrose<RootFraction> moore = new EncryptionMoorePenrose<>(new RootFraction());
        EncryptionPseudoInverse<RootFraction> pseudo = new EncryptionPseudoInverse<>(new RootFraction());
        EncryptionPseudoInverse2<RootFraction> pseudo2 = new EncryptionPseudoInverse2<>(new RootFraction());

        //sample(square);
        //sample(moore);
        //sample(pseudo);
        sample(pseudo2);
    }

    private static void pseudoInverseExample(){
        int[][] primR = {
                {3, 5, 4, 5, 5},
                {5, 1, 4, 6, 5},
                {5, 1, 5, 7, 2}
        };

        PseudoInverseOperations<RootFraction> op = new PseudoInverseOperations<>(new RootFraction());
        Matrix<RootFraction> R = new Matrix<>(primR, new RootFraction()), pseudoInvR = op.pseudoInv(R);
        System.out.println("\n");

        R.print("R");
        pseudoInvR.print("R`");
        System.out.println("\n");

        /*R.times(pseudoInvR).print("R R`");
        R.times(pseudoInvR).times(R).print("R R` R");
        System.out.println("\n");

        pseudoInvR.times(R).print("R` R");
        pseudoInvR.times(R).times(pseudoInvR).print("R` R R`");*/
    }

    private static void matrixTest(){
        String s = "Technology Advisory Committee to the U.S. President, as chair of the CS division in the Berkeley";
        int h = 7;
        EncryptionSquareMatrix<RootFraction> e = new EncryptionSquareMatrix<>(new RootFraction());

        Matrix<RootFraction> M = e.matrix(s, h, new RootFraction());

        System.out.println(s+"\n"+h);
        M.print("M");

    }
}
