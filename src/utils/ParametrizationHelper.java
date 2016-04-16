package utils;

import java.math.BigDecimal;
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
                .forEach(i -> table[i] = translate(i * 1000, (size - 1) * 1000));
        return new ArrayList<>(Arrays.asList(table));
    }


    public Double translate(int i, int size) {
        BigDecimal result = new BigDecimal((double) i / (double) (size));
        result.setScale(4, BigDecimal.ROUND_HALF_UP);
        return Double.valueOf(result.toString());
    }


}
