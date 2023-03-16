package Classes;

import java.util.Random;

public class Randomization {
    public static int getRandomNumber(int min, int max) {
        if (min >= max) {
            System.out.println(min + " " + max);
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static int[] randomizeArray(int[] array) {
        Random r = new Random();
        for (int i = 0; i < array.length; i++) {
            int randomPosition = r.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }
        return array;
    }
}
