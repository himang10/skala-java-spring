package skalajava;
public class skalatest10 {
    public static void main(String[] args) {
        String[] arrayOfStrings = {"Amy","Berry","Clark","David", "Ellit"};

        for (int i=0; i < arrayOfStrings.length; i++) {
            if(arrayOfStrings[i].equals("Clark")) {
                continue;
            }
            System.out.println(arrayOfStrings[i]);
        }
    }
}
