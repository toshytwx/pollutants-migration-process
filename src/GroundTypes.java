import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GroundTypes {
    public static HashMap<String, ArrayList<Double>> groundMap = new HashMap<>(6);


    public static void fillMap() {
        ArrayList<Double> first = new ArrayList<>(2);
        first.addAll((Arrays.asList(0.1, 0.1)));
        groundMap.put("Густа трава, дернина, підстилка", first);

        ArrayList<Double> second = new ArrayList<>(2);
        second.addAll((Arrays.asList(0.1, 0.1)));
        groundMap.put("Рідкісна трава без деревини і підстилки", second);

        ArrayList<Double> third = new ArrayList<>(2);
        third.addAll((Arrays.asList(0.2, 0.12)));
        groundMap.put("Трава середньої гущини, дернина", third);

        ArrayList<Double> fourth = new ArrayList<>(2);
        fourth.addAll((Arrays.asList(0.2, 0.1)));
        groundMap.put("Густа трава, дернина", fourth);

        ArrayList<Double> fiveth = new ArrayList<>(2);
        fiveth.addAll((Arrays.asList(0.1, 0.03)));
        groundMap.put("Хвойний ліс", fiveth);

        ArrayList<Double> sixth = new ArrayList<>(2);
        sixth.addAll((Arrays.asList(0.1, 0.04)));
        groundMap.put("Змішаний ліс", sixth);
    }
}
