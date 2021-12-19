package coronaCity.utils;

import java.util.Random;

public class RandomUtils {
    private static final Random r = new Random();

    public static int randomInt(int range) {
        return(r.nextInt(range));
    }
  
    public static double randomDouble(){
        return r.nextDouble();
    }
 
    public static int randomIndex(Object[] array) {
        return(randomInt(array.length));
    }

    public static <T> T randomElement(T[] array) {
        return(array[randomIndex(array)]);
    } 
}
