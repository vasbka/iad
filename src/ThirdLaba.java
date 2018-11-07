import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class ThirdLaba {
    private static double rowCount = 0;
    private static double colCount = 4;
    private static DecimalFormat df2 = new DecimalFormat("##.##");

    public static void main(String[] args) throws Exception {
        Scanner scanner = null;
        ClassLoader classLoader = FirstLaba.class.getClassLoader();
        scanner = new Scanner(new File(classLoader.getResource("data.dat").getFile()));

        while (scanner.hasNextLine()) {
            rowCount++;
            scanner.nextLine();
        }

        double[][] arr = new double[(int) rowCount][(int) colCount];
        scanner = new Scanner(new File(classLoader.getResource("data.dat").getFile()));
        int indxI = 0, indxJ = 0;
        while (scanner.hasNext()) {

            arr[indxI][indxJ++] = new Double(scanner.next());
            if (indxJ == 4) {
                indxI++;
                indxJ = 0;
            }
        }

        double[] maxByEachCol = new double[(int) colCount];
        for (int i = 0; i < colCount; i++) {
            maxByEachCol[i] = Arrays.stream(getColumnByIndex(arr, i)).max().getAsDouble();
        }

//        Arrays.stream(maxByEachCol).forEach(System.out::println);
        Random random = new Random();
        double[][] centroids = new double[3][(int) colCount];
        for (int i = 0; i < centroids.length; i++) {
            for (int j = 0; j < colCount; j++) {
                centroids[i][j] = random.nextDouble() * maxByEachCol[j];
            }
        }

        double epsilon = 2;
        double actual = 10;
        List<Dots> list = new ArrayList<>();
        for (int i = 0; i < centroids.length; i++) {
            list.add(new Dots(i, centroids[i][0], centroids[i][1], centroids[i][2], centroids[i][3]));
        }
        while (epsilon < actual) {
            ArrayList<Dots> newDots = new ArrayList<>();
            //recalculate centroids
            for (int i = 0; i < rowCount; i++) {
                int index = getIndexMin(list, arr[i]);
                newDots.add(new Dots(index,
                                ((list.get(index).x - arr[i][0]) / 2),
                                ((list.get(index).y - arr[i][1]) / 2),
                                ((list.get(index).z - arr[i][1]) / 3),
                                ((list.get(index).q - arr[i][1]) / 4)
                        )
                );
                System.out.println("Dots " + arr[i][0] + " " + arr[i][1] + " " + arr[i][2] + " " + arr[i][3]+ " blije" +
                        " vsego k " + index);
            }

            List<Dots> temp = new ArrayList<>();
            for (int i = newDots.size() - 1; i >=0; i--) {
                boolean exist = false;
                for (Dots existsDot :
                        temp) {
                    if(existsDot.index == newDots.get(i).index) {
                        exist = true;
                    }
                }
                if(!exist) {
                    temp.add(newDots.get(i));
                }
            }
            int[] dots = new int[3];
            //classification iris
            for (int i = 0; i < rowCount; i++) {
                dots[getIndexMin(temp, arr[i])]++;
            }
            Arrays.stream(dots).forEach(System.out::println);


            //get accuracy
            double minActual = Double.MAX_VALUE;
            double tempActual;
            for (Dots dot :
                    newDots) {
                for (int i = 0; i < 3; i++) {
                    if (dot.index == i) {
                        if (minActual > (tempActual = length(list.get(i), newDots.get(i)))) {
                            minActual = tempActual;
                        }
                        break;
                    }
                }
            }
            actual = minActual;
//            System.out.println("Actual : " + actual);
        }
    }

    private static int getIndexMin(List<Dots> list, double[] iris) {
        double min = Double.MAX_VALUE;
        int minIndex = 0;
        double tempMin;
        int index = -1;
        for (Dots dot :
                list) {
            ++index;
            if (min > (tempMin = length(iris, dot))) {
                min = tempMin;
                minIndex = index;
            }
        }
        return minIndex;
    }

    public static double[] getColumnByIndex(double[][] arr, int index) {
        double rez[] = new double[(int) rowCount];
        int i = 0;
        for (double[] val : arr) {
            rez[i++] = val[index];
        }
        return rez;
    }

    public static double[] getRowByIndex(double[][] arr, int index) {
        double rez[] = new double[(int) colCount];
        int i = 0;
        for (double[] val : arr) {
            rez[i++] = val[index];
        }
        return rez;
    }

    public static double getDiffBetweenXandCentroid(double[] x, double[] centroid) {
        double[] dif = dif(x, centroid);
        return Math.sqrt(multiply(dif, dif));
    }

    public static double[] dif(double[] first, double[] second) {
        return new double[]{
                first[0] - second[0],
                first[1] - second[1],
                first[2] - second[2],
                first[3] - second[3]};
    }

    public static double multiply(double[] first, double[] second) {
        double rez = 0;
        for (int i = 0; i < first.length; i++) {
            rez += first[i] * second[i];
        }
        return rez;
    }

    public static double length(double[] iris, Dots centroid) {
        return Math.sqrt(Math.pow(iris[0] - centroid.x, 2) +
                Math.pow(iris[1] - centroid.y, 2) +
                Math.pow(iris[2] - centroid.z, 2) +
                Math.pow(iris[3] - centroid.q, 2));
    }

    public static double length(Dots old, Dots centroid) {
        return Math.sqrt(Math.pow(old.x - centroid.x, 2) +
                Math.pow(old.y - centroid.y, 2) +
                Math.pow(old.z - centroid.z, 2) +
                Math.pow(old.q - centroid.q, 2));
    }


}

class Dots {
    @Override
    public String toString() {
        return "Dots{" +
                "index=" + index +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", q=" + q +
                '}';
    }

    public Dots() {
    }

    public Dots(int index, double x, double y, double z, double q) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.z = z;
        this.q = q;
    }

    int index;
    double x;
    double y;
    double z;
    double q;
}
