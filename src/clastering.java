import java.io.File;
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
            centroids.add(new Centroid(random.nextDouble() * random.nextInt(100),
                    random.nextDouble() * random.nextInt(100),
                    random.nextDouble() * random.nextInt(100),
                    random.nextDouble() * random.nextInt(100)));
        }
        // SECOND LABS
//        double epsilon = 0.0001, actualValue = 10;
//        int countLoop = 0;
//        while (epsilon < actualValue) {
//            countLoop++;
//            //3.2
//            //3.3
//            centroids.forEach(centroid -> centroid.getDots().clear());
//            dots.forEach(dot -> {
//                List<Double> lengths = new ArrayList<>();
//                centroids.forEach(centroid -> lengths.add(length(dot, centroid)));
//                double minLength = Double.MAX_VALUE;
//                int index = -1, minIndex = 0;
//                for (double length :
//                        lengths) {
//                    index++;
//                    if (minLength > length) {
//                        minLength = length;
//                        minIndex = index;
//                    }
//                }
//                centroids.get(minIndex).addDot(dot);
//            });
//            //3.4
//            List<Centroid> old = new ArrayList<>();
//            centroids.forEach(centroid -> old.add(new Centroid(centroid.getX(), centroid.getY(), centroid.getZ(),
//                    centroid.getQ())));
//            centroids.forEach(centroid -> {
//                double x = 0, y = 0, z = 0, q = 0;
//                for (Dot dot :
//                        centroid.getDots()) {
//                    x += dot.getX();
//                    y += dot.getY();
//                    z += dot.getZ();
//                    q += dot.getQ();
//                }
//                int sz = centroid.getDots().size();
//                x /= sz;
//                y /= sz;
//                q /= sz;
//                z /= sz;
//                centroid.setQ((q + centroid.getQ()) / 2);
//                centroid.setX((x + centroid.getX()) / 2);
//                centroid.setY((y + centroid.getY()) / 2);
//                centroid.setZ((z + centroid.getZ()) / 2);
//            });
//            for (int i = 0; i < 3; i++) {
//                double temp = 0;
//                if (actualValue > (temp = (length(centroids.get(i), old.get(i))))) {
//                    actualValue = temp;
//                }
//            }
//        }
//        System.out.println("COUNT : " + countLoop);
//        centroids.forEach(centroid -> System.out.println(centroid.getDots().size()));
//        String[][] fields = new String[200][200];
//
//        int index = 0;
//        for (int i = 100; i > -100; i--) {
//            double temp = Math.abs(i);
//            if (temp % 20 == 0) {
//                fields[index++][100] = String.valueOf(temp / 100).substring(2, 3);
//            } else {
//                fields[index++][100] = "-";
//            }
//        }
//        index = 0;
//        for (double i = -100; i < 100; i++) {
//            double temp = Math.abs(i);
//            if (temp % 20 == 0)
//                fields[100][index++] = String.valueOf(temp / 100).substring(2, 3);
//            else
//                fields[100][index++] = "-";
//        }
//        PrintWriter writer = new PrintWriter("iadSecond.txt", "UTF-8");
//        int ind = 0;
//        String sym = "";
//        for (Centroid centroid : centroids) {
//            int resX, resY;
//            resX = getRelativeIndex(centroid.getX());
//            resY = getRelativeIndex(centroid.getY());
//            fields[resX][resY] = "" +ind++;
//            int color = 0;
//            if (ind == 1) {
//                color = 31;
//                sym = (char) 27 + "[" + color + "m" + "Z" + (char) 27 + "[30m";;
//            } else if (ind == 2) {
//                color = 32;
//                sym = (char) 27 + "[" + color + "m" + "Q" + (char) 27 + "[30m";;
//            } else if(ind == 3){
//                color = 34;
//                sym = (char) 27 + "[" + color + "m" + "K" + (char) 27 + "[30m";;
//            }
//            for (Dot dot : centroid.getDots()) {
//                int localX, localY;
//                localX = getRelativeIndex(dot.getX());
//                localY = getRelativeIndex(dot.getY());
//                if(localX - 1 >= 0 && Objects.isNull(fields[localX - 1][localY])) {
//                    fields[localX - 1][localY] = sym;
//                }
//                if(localY - 1 >= 0 && Objects.isNull(fields[localX][localY - 1])) {
//                    fields[localX][localY - 1] = sym;
//                }
//                if(localX -1 >=0 && localY - 1 >= 0 && Objects.isNull(fields[localX - 1][localY - 1])) {
//                    fields[localX - 1][localY - 1] = sym;
//                }
//                if(localX - 1 >= 0 && localY + 1 < 200 && Objects.isNull(fields[localX - 1][localY + 1])) {
//                    fields[localX - 1][localY + 1] = sym;
//                }
//                if(localX + 1 < 200 && localY - 1 >= 0 && Objects.isNull(fields[localX + 1][localY - 1])) {
//                    fields[localX + 1][localY - 1] = sym;
//                }
//                fields[localX][localY] = sym;
//                if(localY +1 < 200 && Objects.isNull(fields[localX][localY + 1])) {
//                    fields[localX][localY + 1] = sym;
//                }
//                if(localX +1 < 200 && Objects.isNull(fields[localX + 1][localY])) {
//                    fields[localX + 1][localY] = sym;
//                }
//                if(localX + 1 < 200 && localY + 1 <200 && Objects.isNull(fields[localX + 1][localY + 1])) {
//                    fields[localX + 1][localY + 1] = sym;
//                }
//            }
//        }
//        for (double i = 0; i < 200; i++) {
//            for (double j = 0; j < 200; j++) {
//                if (Objects.isNull(fields[(int) i][(int) j])) {
//                    writer.print(String.format("%2s", " "));
//                    System.out.print(String.format("%1s", " "));
//                } else {
//                    writer.print(String.format("%2s", fields[(int) i][(int) j]));
//                    System.out.print(String.format("%1s", fields[(int) i][(int) j]));
//                }
//            }
//            System.out.println();
//            writer.println();
//        }
//        writer.close();

        //THIRD LABS    
//        for (int i = 0; i < dots.size() - 1; i++) {
        List<Matrix> matrixs = new ArrayList<>();
        for(int i = 0; i < colCount; i++) {
            matrixs.add(new Matrix());
        }
        for(int i = 0; i < colCount; i++){
            for (int j = 0; j < 4; j++) {
                double val;
                int finalI = i;
                int finalJ = j;
                if(i == j) {
                    val = 1;
                } else {
                    val = 2;
                }
                matrixs.forEach(matrix -> matrix.setValue(finalI, finalJ, val));
            }
        }
        Dot dot = getDobFromArrByIndex(arr, 0);
        System.out.println(dot);
        System.out.println(centroids.get(0));
//            for (int j = 0; j < centroids.size() - 1; j++) {
        dot.diff(centroids.get(0));
        System.out.println(dot);

        System.out.println("1");
        double[] res = multiplyMatrixAndRow(new Matrix(multiplyMatrices(transposeMatrix(dot.getAsArr()),
                        inverse(matrixs.get(0).getAsArr()))),
                dot);
        System.out.println("RES");
        Arrays.stream(res).forEach(System.out::println);


//            }
//        }

    }

    private static int getRelativeIndex(double x2) {
        if (x2 > 0.0) {
            return 100 + (int) (x2 * 100) == 200 ? 199 : 100 + (int) (x2 * 100);
        }
        return (int) (100 - Math.abs(x2 * 100)) == 200 ? 199 : (int) (100 - Math.abs((x2 * 100)));
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


//    public static double mahala(int index, Centroid centroid) {
//
//    }

    public static double[][] transposeMatrix(double[][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
    public static double[] multiplyMatrixAndRow(Matrix matrix, Dot dot) {
        double[] arr = new double[(int)colCount];
        for (int i = 0; i < colCount; i++) {
            double summ = 0;
            for (int j = 0; j < matrix.getRowByIndex(i).length; j++) {
                double[] tmpArr = matrix.getRowByIndex(j);
                summ += tmpArr[0] * dot.getX();
                summ += tmpArr[1] * dot.getY();
                summ += tmpArr[2] * dot.getZ();
                summ += tmpArr[3] * dot.getQ();
            }
            arr[i] = summ;
        }
        return arr;
    }

    public static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int m1_rows = matrix1.length;
        int m1_columns = matrix1[0].length;
        int m2_rows = matrix2.length;
        int m2_columns = matrix2[0].length;
        double[][] product = new double[m1_rows][m2_columns];
        if ((m1_columns != m2_rows)) {
            System.out.println("Error - Number of columns of matrix1 IS NOT EQUAL TO number of rows of matrix2.");
        } else {

            for (int i = 0; i < m1_rows; i++) {
                for (int j = 0; j < m2_columns; j++) {
                    for (int k = 0; k < m1_columns; k++) {
                        product[i][j] += matrix1[i][k] * matrix2[k][j];
                    }
                }
            }
        }
        return product;
    }

    public static Dot getDobFromArrByIndex(double[][] arr, int index) {
        return new Dot(arr[index][0], arr[index][1], arr[index][2], arr[index][3]);
    }





    // OP WITH MATRIX
    private static double[][] inverse(double[][] matrix) {
        double[][] inverse = new double[matrix.length][matrix.length];

        // minors and cofactors
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(minor(matrix, i, j));

        // adjugate and determinant
        double det = 1.0 / determinant(matrix);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return inverse;
    }
    private static double determinant(double[][] matrix) {
        if (matrix.length != matrix[0].length)
            throw new IllegalStateException("invalid dimensions");

        if (matrix.length == 2)
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int i = 0; i < matrix[0].length; i++)
            det += Math.pow(-1, i) * matrix[0][i]
                    * determinant(minor(matrix, 0, i));
        return det;
    }
    private static double[][] minor(double[][] matrix, int row, int column) {
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return minor;
    }
    //END OP WITH MATRIX

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

    public void diff(Dot dot) {
        this.x -= dot.x;
        this.y -= dot.y;
        this.q -= dot.q;
        this.z -= dot.z;
    }

    public double[][] getAsArr() {
        return new double[][]{{this.x, this.y, this.z, this.q}};
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

class Matrix {
    double[][] arr;

    public Matrix() {
        arr = new double[150][4];
    }

    public Matrix(double[][] arr) {
        this.arr = arr;
    }

    void setValue(int i, int j, double value) {
        arr[i][j] = value;
    }

    public double[][] getAsArr() {
        return arr;
    }

    public double[] getRowByIndex(int i){
        double[] arr = new double[4];
        for (int j = 0; j < 4; j++) {
            arr[j] = this.arr[i][j];
        }
        return arr;
    }

    public void print() {
        Arrays.stream(arr).forEach(doubles
                -> {
            Arrays.stream(doubles).forEach(System.out::print);
            System.out.println();
        });
    }
}