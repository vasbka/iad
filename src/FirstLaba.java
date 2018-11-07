import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class FirstLaba {
    private static double rowCount = 0;
    private static double colCount = 5;
    private static DecimalFormat df2 = new DecimalFormat("##.##");
    public static void main(String[] args) throws Exception {
        Scanner scanner = null;
        ClassLoader classLoader = FirstLaba.class.getClassLoader();
        scanner = new Scanner(new File(classLoader.getResource("data.dat").getFile()));

        while(scanner.hasNextLine()){
            rowCount++;
            scanner.nextLine();
        }

        double[][] arr = new double[(int)rowCount][(int)colCount];
        scanner = new Scanner(new File(classLoader.getResource("data.dat").getFile()));
        int indxI = 0, indxJ = 0, generalCount = 0, generalClass = 1;
        while (scanner.hasNext()){
            arr[indxI][indxJ++] = new Double(scanner.next());
            generalCount++;
            if(indxJ == 4){
                arr[indxI][indxJ++] = generalClass;
                indxI++;
                indxJ = 0;
            }
            if(generalCount == 49){
                generalClass++;
            }
        }

        System.out.println("Average");
        for (int i = 0; i < 4; i++) {
            System.out.println((i+1) + " : "
                    + getAverage(getColumnByIndex(arr, i)));
        }
        System.out.println("Mediana");
        for (int i = 0; i < 4; i++) {
            System.out.println((i+1) + " : "
                    +getMediana(getColumnByIndex(arr, i)));
        }
        System.out.println("Border values");
        for (int i = 0; i < 4; i++) {
            System.out.println((i+1) + " : "
                    +getBorderValues(getColumnByIndex(arr, i)));
        }
        System.out.println("AverageSquaredDeviation");
        for (int i = 0; i < 4; i++) {
            System.out.println((i+1) + " : "
                    + averageSquaredDeviation(getColumnByIndex(arr, i),
                    getAverage(getColumnByIndex(arr, i))));
        }
        System.out.println("averageModuleDeviation");
        for (int i = 0; i < 4; i++) {
            System.out.println((i+1) + " : "
                    + averageModuleDeviation(getColumnByIndex(arr,i),
                    getMediana(getColumnByIndex(arr,i ))));
        }
        System.out.println("Range");
        for (int i = 0; i < 4; i++) {
            System.out.println((i + 1) + " : "
                    + range(getColumnByIndex(arr, i)));
        }
        System.out.println("Dispersion");
        for (int i = 0; i < 4; i++) {
            System.out.println((i + 1) + " : "
                    + dispersion(getColumnByIndex(arr, i)));
        }
        System.out.println();
        System.out.println("Rationing and centering");
        for (int i = 0; i < 4; i++) {
            System.out.print((i + 1) + " : ");
            Arrays.stream(rationingCentering(
                    getColumnByIndex(arr, i),
                    getAverage(getColumnByIndex(arr, i)),
                    averageSquaredDeviation(
                            getColumnByIndex(arr,i),
                            getAverage(getColumnByIndex(arr,i))
                    )))
                .forEach(value -> System.out.print(df2.format(value) + " "));
            System.out.println();
        }
        System.out.println();
        System.out.println("Coding for hypercube");
        for (int i = 0; i < 4; i++) {
            double min = Arrays.stream(getColumnByIndex(arr, i)).min().getAsDouble();
            double max = Arrays.stream(getColumnByIndex(arr, i)).max().getAsDouble();
            System.out.print((i + 1) + " : ");
            Arrays.stream(codingForHypercubeZeroToOne(getColumnByIndex(arr, i), min, max))
                    .forEach(value -> System.out.print(df2.format(value) + " "));
            System.out.println();
        }

        System.out.println("Coding for hypercube -1 to 1");
        for (int i = 0; i < 4; i++) {
            double min = Arrays.stream(getColumnByIndex(arr, i)).min().getAsDouble();
            double max = Arrays.stream(getColumnByIndex(arr, i)).max().getAsDouble();
            System.out.print((i + 1) + " : ");
            Arrays.stream(codingForHiperCubeMinusOneToOne(getColumnByIndex(arr, i), min, max))
                    .forEach(value -> System.out.print(df2.format(value)  + " "));
            System.out.println();
        }
        System.out.println("Hard task");
        for (int i = 0; i < 4; i++) {
            System.out.println(recurrence(1, getColumnByIndex(arr, i), getColumnByIndex(arr, i)[0]));
        }



        // SECOND LAB



    }

    public static double[] getColumnByIndex(double[][] arr, int index) {
        double rez[] = new double[(int)rowCount];
        int i = 0;
        for (double[] val: arr) {
            rez[i++] = val[index];
        }
        return rez;
    }

    public static double getAverage(double[] arr) {
        return Arrays.stream(arr).average().getAsDouble();
    }
    public static double getMediana(double[] arr){
        double res = 0;
            res = Arrays.stream(arr)
                    .sorted()
                    .skip((int) rowCount / 2 - (int)((rowCount + 1) % 2))
                    .limit(1 + (int)(rowCount % 2))
                    .average()
                    .getAsDouble();
        return res;
    }
    public static double getBorderValues(double[] arr){
        return (Arrays.stream(arr).min().getAsDouble()
                + Arrays.stream(arr).max().getAsDouble()) / 2d;
    }

    public static double averageSquaredDeviation(double[] arr, double average){
        return Math.sqrt(
                Arrays.stream(arr)
                    .map(value -> Math.pow((value - average), 2))
                    .sum()/rowCount
            );
    }

    public static double averageModuleDeviation(double[] arr, double mediana){
        return Arrays.stream(arr)
                .map(value -> Math.abs(value - mediana))
                .sum() / rowCount;
    }

    public static double range(double[] arr){
        return Arrays.stream(arr).max().getAsDouble()
                - Arrays.stream(arr).min().getAsDouble();
    }

    public static double dispersion(double[] arr){
        return Math.pow(averageSquaredDeviation(arr,
                getAverage(arr)), 2);
    }

    public static double[] rationingCentering(double[] arr, double average, double averageSquaredDeviation){
        return Arrays.stream(arr).map(value -> (value - average)/averageSquaredDeviation).toArray();
    }

    public static double[] codingForHypercubeZeroToOne(double[] arr, double min, double max){
        return Arrays.stream(arr).map(val -> (val - min)/(max - min)).toArray();
    }

    public static double[] codingForHiperCubeMinusOneToOne(double[] arr, double min, double max){
        return Arrays.stream(arr).map(val -> 2 * (val - min)/(max - min) - 1).toArray();
    }

    public static double recurrence(int index, double[] arr, double res){
        if(index == 150) {
            return res;
        }
        return recurrence(index + 1, arr,
                res + (1.0 / (double)index) * (sign(arr[index] - res)));
    }


    private static double sign(double v) {
        if(v > 0){
            return 1;
        }
        if(v == 0){
            return 0;
        }
        return -1;
    }

}
