package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Albert on 03.04.2016.
 */
public class ParametrizationHelper {

    private ParametrizationHelper(){

    }

    private static final ParametrizationHelper anInstance = new ParametrizationHelper();

    public static ParametrizationHelper getInstance(){
        return anInstance;
    }

    public List<Double> createParametrization(int size){
        Double table[] = new Double[size];
        IntStream.range(0,size).parallel()
                .forEach(i -> table[i] = (double) i * 100);
        return new ArrayList<>(Arrays.asList(table));
    }


}
