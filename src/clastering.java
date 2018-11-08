import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

public class clastering {
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
        List<Dot> dots = new ArrayList<>();
        int indxI = 0, indxJ = 0;
        while (scanner.hasNext()) {

            arr[indxI][indxJ++] = new Double(scanner.next());
            if (indxJ == 4) {
                indxI++;
                indxJ = 0;
            }
        }

        for (int i = 0; i < 4; i++) {
            double min = Arrays.stream(getColumnByIndex(arr, i)).min().getAsDouble();
            double max = Arrays.stream(getColumnByIndex(arr, i)).max().getAsDouble();
            int j = 0;
            for (double value :
                    FirstLaba.codingForHiperCubeMinusOneToOne(getColumnByIndex(arr, i), min, max)) {
                arr[j++][i] = value;
            }
        }
        for (int i = 0; i < rowCount; i++) {
            Dot dot = new Dot();
            dot.setX(arr[i][0]);
            dot.setY(arr[i][1]);
            dot.setZ(arr[i][2]);
            dot.setQ(arr[i][3]);
            dots.add(dot);
        }

        List<Centroid> centroids = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            centroids.add(new Centroid(-1 + (1 + 1) * random.nextDouble(),
                    -1 + (1 + 1) * random.nextDouble(),
                    -1 + (1 + 1) * random.nextDouble(),
                    -1 + (1 + 1) * random.nextDouble()));
        }
        double epsilon = 0.0001, actualValue = 10;
        int countLoop = 0;
        while (epsilon < actualValue) {
            countLoop++;
            //3.2
            //3.3
            centroids.forEach(centroid -> centroid.getDots().clear());
            dots.forEach(dot -> {
                List<Double> lengths = new ArrayList<>();
                centroids.forEach(centroid -> lengths.add(length(dot, centroid)));
                double minLength = Double.MAX_VALUE;
                int index = -1, minIndex = 0;
                for (double length :
                        lengths) {
                    index++;
                    if (minLength > length) {
                        minLength = length;
                        minIndex = index;
                    }
                }
                centroids.get(minIndex).addDot(dot);
            });
            //3.4
            List<Centroid> old = new ArrayList<>();
            centroids.forEach(centroid -> old.add(new Centroid(centroid.getX(), centroid.getY(), centroid.getZ(),
                    centroid.getQ())));
            centroids.forEach(centroid -> {
                double x = 0, y = 0, z = 0, q = 0;
                for (Dot dot :
                        centroid.getDots()) {
                    x += dot.getX();
                    y += dot.getY();
                    z += dot.getZ();
                    q += dot.getQ();
                }
                int sz = centroid.getDots().size();
                x /= sz;
                y /= sz;
                q /= sz;
                z /= sz;
                centroid.setQ((q + centroid.getQ()) / 2);
                centroid.setX((x + centroid.getX()) / 2);
                centroid.setY((y + centroid.getY()) / 2);
                centroid.setZ((z + centroid.getZ()) / 2);
            });
            for (int i = 0; i < 3; i++) {
                double temp = 0;
                if (actualValue > (temp = (length(centroids.get(i), old.get(i))))) {
                    actualValue = temp;
                }
            }
        }
        System.out.println("COUNT : " + countLoop);
        centroids.forEach(centroid -> System.out.println(centroid.getDots().size()));
        String[][] fields = new String[200][200];

        int index = 0;
        for (int i = 100; i > -100; i--) {
            double temp = Math.abs(i);
            if (temp % 20 == 0) {
                fields[index++][100] = String.valueOf(temp / 100).substring(2, 3);
            } else {
                fields[index++][100] = "-";
            }
        }
        index = 0;
        for (double i = -100; i < 100; i++) {
            double temp = Math.abs(i);
            if (temp % 20 == 0)
                fields[100][index++] = String.valueOf(temp / 100).substring(2, 3);
            else
                fields[100][index++] = "-";
        }
        PrintWriter writer = new PrintWriter("iadSecond.txt", "UTF-8");
        int ind = 0;
        String sym = "";
        for (Centroid centroid : centroids) {
            int resX, resY;
            resX = getRelativeIndex(centroid.getX());
            resY = getRelativeIndex(centroid.getY());
            fields[resX][resY] = "" +ind++;
            int color = 0;
            if (ind == 1) {
                color = 31;
                sym = (char) 27 + "[" + color + "m" + "Z" + (char) 27 + "[30m";;
            } else if (ind == 2) {
                color = 32;
                sym = (char) 27 + "[" + color + "m" + "Q" + (char) 27 + "[30m";;
            } else if(ind == 3){
                color = 34;
                sym = (char) 27 + "[" + color + "m" + "K" + (char) 27 + "[30m";;
            }
            for (Dot dot : centroid.getDots()) {
                int localX, localY;
                localX = getRelativeIndex(dot.getX());
                localY = getRelativeIndex(dot.getY());
                if(localX - 1 >= 0 && Objects.isNull(fields[localX - 1][localY])) {
                    fields[localX - 1][localY] = sym;
                }
                if(localY - 1 >= 0 && Objects.isNull(fields[localX][localY - 1])) {
                    fields[localX][localY - 1] = sym;
                }
                if(localX -1 >=0 && localY - 1 >= 0 && Objects.isNull(fields[localX - 1][localY - 1])) {
                    fields[localX - 1][localY - 1] = sym;
                }
                if(localX - 1 >= 0 && localY + 1 < 200 && Objects.isNull(fields[localX - 1][localY + 1])) {
                    fields[localX - 1][localY + 1] = sym;
                }
                if(localX + 1 < 200 && localY - 1 >= 0 && Objects.isNull(fields[localX + 1][localY - 1])) {
                    fields[localX + 1][localY - 1] = sym;
                }
                fields[localX][localY] = sym;
                if(localY +1 < 200 && Objects.isNull(fields[localX][localY + 1])) {
                    fields[localX][localY + 1] = sym;
                }
                if(localX +1 < 200 && Objects.isNull(fields[localX + 1][localY])) {
                    fields[localX + 1][localY] = sym;
                }
                if(localX + 1 < 200 && localY + 1 <200 && Objects.isNull(fields[localX + 1][localY + 1])) {
                    fields[localX + 1][localY + 1] = sym;
                }
            }
        }
        for (double i = 0; i < 200; i++) {
            for (double j = 0; j < 200; j++) {
                if (Objects.isNull(fields[(int) i][(int) j])) {
                    writer.print(String.format("%2s", " "));
                    System.out.print(String.format("%1s", " "));
                } else {
                    writer.print(String.format("%2s", fields[(int) i][(int) j]));
                    System.out.print(String.format("%1s", fields[(int) i][(int) j]));
                }
            }
            System.out.println();
            writer.println();
        }
        writer.close();
    }

    private static int getRelativeIndex(double x2) {
        if (x2 > 0.0) {
            return 100 + (int) (x2 * 100) == 200 ? 199 : 100 + (int) (x2 * 100);
        }
        return (int) (100 - Math.abs(x2 * 100)) == 200 ? 199 : (int)(100 - Math.abs((x2 * 100)));
    }

    public static double[] getColumnByIndex(double[][] arr, int index) {
        double rez[] = new double[(int) rowCount];
        int i = 0;
        for (double[] val : arr) {
            rez[i++] = val[index];
        }
        return rez;
    }

    public static double length(Dot dot, Centroid centroid) {
        return Math.sqrt(Math.pow(dot.getX() - centroid.getX(), 2) +
                Math.pow(dot.getY() - centroid.getY(), 2) +
                Math.pow(dot.getZ() - centroid.getZ(), 2) +
                Math.pow(dot.getQ() - centroid.getQ(), 2));
    }


}

class Dot {
    protected double x, y, z, q;

    public Dot() {
    }

    public Dot(double x, double y, double z, double q) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.q = q;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    @Override
    public String toString() {
        return "Dot{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", q=" + q +
                '}';
    }
}

class Centroid extends Dot {
    private List<Dot> dots;

    public Centroid(double x, double y, double z, double q) {
        this.dots = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.z = z;
        this.q = q;
    }

    public Centroid(int x, int y, int z, int q) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.q = q;
        this.dots = new ArrayList<>();
    }

    public List<Dot> getDots() {
        return dots;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public void addDot(Dot dot) {
        this.dots.add(dot);
    }

    @Override
    public String toString() {
        return "Centroid{" +
                "dots=" + dots +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", q=" + q +
                '}';
    }
}
