package EncryptionMethods;

import java.util.ArrayList;
import java.util.HashMap;

class Alphabet {
    private final HashMap<Integer, Character> intMappedToChar = new HashMap<>();
    private final HashMap<Character, Integer> charMappedToInt = new HashMap<>();

    private final char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    private final char[] punctuationMarks = {'.', ',', ';', ':', '"', '`', '!', '?',/* '~',*/ '-', '_', '[', ']'};
    private final char[] specialCharacters = {'@', '#', '$', '%', '^', '&', '*', '<', '>', '|', '/', '+', '='};

    private final int LETTER_LIST_SIZE = 2*27;
    private final int CHAR_LIST_SIZE = (LETTER_LIST_SIZE) + (digits.length) + (punctuationMarks.length) + (specialCharacters.length);

    Alphabet(){

        ArrayList<Integer> numbers = new ArrayList<>();

        for(int i = 1; i < CHAR_LIST_SIZE; i++){
            numbers.add(i);
        }

        //scramble numbers list

        //set 0 equal to space
        intMappedToChar.put(0, ' ');
        charMappedToInt.put(' ', 0);

        //gather all non letter characters into an array list
        ArrayList<char[]> nonLetterCharacters = new ArrayList<>();
        nonLetterCharacters.add(specialCharacters); //get 2
        nonLetterCharacters.add(digits); //get 0
        nonLetterCharacters.add(punctuationMarks); //get 1


        //add all of the above non letter characters to the map
        //so for each array in the array list
        int i = 0;
        for(char[] nonLetterArray : nonLetterCharacters){
            //and for each character in that array
            for(char c : nonLetterArray){
                //map integers and characters to each other
                intMappedToChar.put(numbers.get(i), c);
                charMappedToInt.put(c, numbers.get(i));
                i++;
            }
        }


        //put lowercase letters into map
        for(char c = 'a'; c <= 'z'; ++c){
            intMappedToChar.put(numbers.get(i), c);
            charMappedToInt.put(c, numbers.get(i));
            i++;
        }

        //put uppercase letters into map
        for(char c ='A'; c<='Z'; ++c){
            intMappedToChar.put(numbers.get(i), c);
            charMappedToInt.put(c, numbers.get(i));
            i++;
        }

    }

    HashMap<Integer, Character> getIntMappedToChar() {
        return intMappedToChar;
    }
    HashMap<Character, Integer> getCharMappedToInt(){
        return charMappedToInt;
    }

    void printMap(){
        for(int i = 0; i < intMappedToChar.size(); i++){
            System.out.print("("+i+", "+ intMappedToChar.get(i)+"); ");
        }
    }

    char[] getDigits(){
        return digits;
    }
    char[] getPunctuationMarks(){
        return punctuationMarks;
    }
    char[] getSpecialCharacters(){
        return specialCharacters;
    }
    int size(){
        return CHAR_LIST_SIZE;
    }

}