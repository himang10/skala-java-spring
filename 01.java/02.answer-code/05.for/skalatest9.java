package skalajava;

public class skalatest9 {
    public static void main(String[] args) {
        String[] arrayOfStrings = {"Amy","Berry","Clark","David", "Ellit"};

        for (int i=0; i < arrayOfStrings.length; i++) {
            if(arrayOfStrings[i].equals("Clark")) {
                break;
            }
            System.out.println(arrayOfStrings[i]);
        }
    }
}
