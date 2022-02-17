package EncryptionMethods;

import FileReader.Parser;
import MatrixStructures.Matrix;
import NumberTypes.AbstractMathObject;

import java.util.ArrayList;

public abstract class Encryption<E extends AbstractMathObject<E>>{

    private final Alphabet alphabet = new Alphabet();

    long f(char c){
        if(alphabet.getCharMappedToInt().get(c) == null) return 0;
        return alphabet.getCharMappedToInt().get(c);
    }
    char fInv(int i){
        if(alphabet.getIntMappedToChar().get(i) == null) System.out.println(i);
        return alphabet.getIntMappedToChar().get(i);
    }

    public Matrix<E> matrix(String text, int width, E access){
        Matrix<E> A = new Matrix<>(access);

        int start = 0, end = start + width;
        while(end <= text.length()){
            //check the contents of the substring and extend end to accommodate
            for(int i = start; i < end; i++) if(text.charAt(i) == '~') end++;

            //map each character to
            ArrayList<E> row = makeRow(text.substring(start, end), access);

            A.matrix.add(row);
            start = end;
            end += width;


        }
        if(start < text.length()){
            String sub = text.substring(start);

            ArrayList<E> row = makeRow(sub, access);

            for(int i = sub.length(); i < width; i++) row.add(access.sendValue(0));
            A.matrix.add(row);
        }

        return A;
    }
    String word(Matrix<E> A){

        ArrayList<Long> numbers = convertToLongList(A);

        ArrayList<Character> brokenWord = new ArrayList<>();


        for(long n : numbers){
            if(n < 0) {
                brokenWord.add('~');
                brokenWord.add(fInv(-1*(int) n));
            }else{
                brokenWord.add(fInv((int) n));
            }
        }

        StringBuilder word = new StringBuilder();
        for(char c : brokenWord){
            word.append(c);
        }

        return word.toString();
    }
    private ArrayList<E> makeRow(String s, E access){
        ArrayList<E> row = new ArrayList<>();
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '~') {
                i++;
                row.add(access.sendValue( -1*f(s.charAt(i))));
            } else {
                row.add(access.sendValue(f(s.charAt(i))));
            }
        }
        return row;
    }



    public String blend(String hash, String key){
        StringBuilder blend = new StringBuilder();
        String[] hashChars = consolidate(hash), keyChars = consolidate(key);
        int c = 0, k = 0;
        if(hashChars.length == keyChars.length) {
            for (int i = 0; i < hashChars.length; i++) blend.append(hashChars[c++]).append(keyChars[k++]);
            return blend.toString();
        }
        for(int i = 0; i < hashChars.length; i++) blend.append(hashChars[c++]).append(keyChars[k++]).append(keyChars[k++]);
        return blend.toString();
    }
    public String[] splitK(String ciphertext){
        String[] cipherChars = consolidate(ciphertext);
        StringBuilder hash = new StringBuilder(), key = new StringBuilder();
        for(int i = 0; i < cipherChars.length; i+=2) hash.append(cipherChars[i]);
        for(int i = 1; i < cipherChars.length; i+=2) key.append(cipherChars[i]);
        return new String[]{hash.toString(), key.toString()};
    }
    public String[] splitL(String ciphertext){
        String[] cipherChars = consolidate(ciphertext);
        StringBuilder hash = new StringBuilder(), key = new StringBuilder();
        for(int i = 0; i < cipherChars.length; i+=3) hash.append(cipherChars[i]);
        for(int i = 1; i < cipherChars.length; i+=3) key.append(cipherChars[i]).append(cipherChars[i+1]);
        return new String[]{hash.toString(), key.toString()};
    }
    private int tildaCount(String s){
        int c = 0;
        for(int i = 0; i < s.length(); i++) if(s.charAt(i) == '~') c++;
        return c;
    }
    private String[] consolidate(String s){
        String[] characters = new String[s.length() - tildaCount(s)];
        int charIndex = 0;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(c == '~'){
                i++;
                characters[charIndex++] = c+""+s.charAt(i)+"";
            }else{
                characters[charIndex++] = c+"";
            }
        }
        return characters;
    }



    ArrayList<Long> convertToLongList(Matrix<E> matrix){
        ArrayList<Long> numbers = new ArrayList<>();
        for(int i = 0; i < matrix.width(); i++){
            for(int j = 0; j < matrix.height(); j++){
                E a_ji = matrix.entry(j, i);
                numbers.add(a_ji.toLong());
            }
        }
        return numbers;
    }

    abstract String oneWayHash(String plaintext);
    public abstract String encrypt(String plaintext);
    public abstract String decrypt(String ciphertext);
    abstract void twoWayEncryption();
    abstract void twoWayDecryption();
    abstract void oneWayEncryption();

    public void oneWayHash(Encryption<E> e){
        Parser p = new Parser();
        String plaintext = p.read("src/text/plaintext.txt");
        String ciphertext = oneWayHash(plaintext);
        p.reformatTextFile("one way hash", ciphertext);

    }
    public void twoWayEncryption(Encryption<E> e) {
        Parser p = new Parser();
        String plaintext = p.read("src/text/plaintext.txt");
        String ciphertext = encrypt(plaintext);
        p.reformatTextFile("two way encrypt", ciphertext);
    }
    public void twoWayDecryption(Encryption<E> e){
        Parser p = new Parser();
        String ciphertext = p.read("src/text/user_saved_text/two way encrypt.txt");

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ciphertext.length(); i++){
            if(ciphertext.charAt(i) != '\n') sb.append(ciphertext.charAt(i));
        }
        String ciphertextNoNewLines = sb.toString();

        String plaintext = decrypt(ciphertextNoNewLines);
        p.reformatTextFile("two way decrypt", plaintext);
    }

}
