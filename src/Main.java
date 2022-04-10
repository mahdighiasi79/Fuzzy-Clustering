import com.gembox.spreadsheet.*;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.awt.*;
import java.awt.geom.*;

class Point {

    double[] values;
    double[] belongings;

    Point(int dimension, double[] values) {

        this.values = new double[dimension];
        System.arraycopy(values, 0, this.values, 0, dimension);
    }
}

class C_Mean {

    private int N;
    private int dimension;
    private int C;
    private double m;
    private Point[] centers;
    Point[] points;
    double cost;

    C_Mean(int N, int dimension, int C, double m, Point[] centers, Point[] points) {

        this.N = N;
        this.dimension = dimension;
        this.C = C;
        this.m = m;
        this.centers = centers;
        this.points = points;

        for (int i = 0; i < 100; i++) {
            Belongings();
            Vi();
        }

        Cost();
    }

    private double Distance(Point p1, Point p2) {

        double result = 0;
        for (int i = 0; i < dimension; i++)
            result += Math.pow(p1.values[i] - p2.values[i], 2);
        result = Math.pow(result, 0.5);
        return result;
    }

    private double Uik(int i, int k) {

        double result = 0;

        double distIK = Distance(centers[i], points[k]);
        if (distIK == 0)
            return 1;

        double distJK;

        for (int j = 0; j < C; j++) {

            distJK = Distance(centers[j], points[k]);
            if (distJK == 0)
                return 0;

            result += Math.pow(distIK / distJK, 2 / (m - 1));
        }

        result = 1 / result;

        return result;
    }

    private void Belongings() {

        for (int i = 0; i < C; i++) {

            for (int k = 0; k < N; k++)
                points[k].belongings[i] = Uik(i, k);
        }
    }

    private void Vi() {

        double d1 = 0, d2 = 0;

        for (int i = 0; i < C; i++) {

            for (int j = 0; j < dimension; j++) {

                for (int k = 0; k < N; k++) {

                    d1 += (Math.pow(points[k].belongings[i], m) * points[k].values[j]);
                    d2 += Math.pow(points[k].belongings[i], m);
                }

                centers[i].values[j] = d1 / d2;
                d1 = d2 = 0;
            }
        }
    }

    private void Cost() {

        cost = 0;

        double temp1, temp2;

        for (int j = 0; j < N; j++) {

            for (int i = 0; i < C; i++) {

                temp1 = Math.pow(points[j].belongings[i], m);
                temp2 = Math.pow(Distance(points[j], centers[i]), 2);
                cost += temp1 * temp2;
            }
        }
    }
}

//class G extends JPanel {
//
//    private int[] c;
//    private double[] cost;
//
//    G(int[] c, double[] cost) {
//        this.c = c;
//        this.cost = cost;
//    }
//
//    protected void paintComponent(Graphics g) {
//
//        int mar = 50;
//
//        super.paintComponent(g);
//        Graphics2D g1 = (Graphics2D) g;
//        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        int width = getWidth();
//        int height = getHeight();
//
//        g1.draw(new Line2D.Double(mar, mar, mar, height - mar));
//        g1.draw(new Line2D.Double(mar, height - mar, width - mar, height - mar));
//
//        double x_axis_length = width - (2 * mar);
//        double y_axis_length = height - (2 * mar);
//
//        for (int i = 0; i < 7; i++) {
//
//            double x1_axis_value = (x_axis_length / 10) * c[i];
//            double x2_axis_value = (x_axis_length / 10) * c[i + 1];
//            double y1_axis_value = (y_axis_length / 300) * cost[i];
//            double y2_axis_value = (y_axis_length / 300) * cost[i + 1];
//
//            double x1_draw = x1_axis_value + mar;
//            double x2_draw = x2_axis_value + mar;
//            double y1_draw = height - mar - y1_axis_value;
//            double y2_draw = height - mar - y2_axis_value;
//
//            g1.setPaint(Color.BLUE);
//
//            g1.draw(new Line2D.Double(x1_draw, y1_draw, x2_draw, y2_draw));
//        }
//    }
//}

class G2 extends JPanel {

    private Point[] points;
    private boolean crisp;
    private boolean delivery;

    G2(Point[] points, boolean crisp, boolean delivery) {
        this.points = points;
        this.crisp = crisp;
        this.delivery = delivery;
    }

    private static int GetColor(Point point, boolean crisp) {

        if (!crisp) {

            int red = (int) (point.belongings[0] * 255);
            int green = (int) (point.belongings[1] * 255);
            int blue = (int) (point.belongings[2] * 255);

            red *= Math.pow(2, 16);
            green *= 256;

            return red + green + blue;
        }

        if ((point.belongings[0] >= point.belongings[1]) && (point.belongings[0] >= point.belongings[2]))
            return 255 * 256 * 256;
        else if ((point.belongings[1] >= point.belongings[0]) && (point.belongings[1] >= point.belongings[2]))
            return 255 * 256;
        return 255;
    }

    protected void paintComponent(Graphics g) {

        int mar = 50;

        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g1.draw(new Line2D.Double(mar, mar, mar, height - mar));
        g1.draw(new Line2D.Double(mar, height - mar, width - mar, height - mar));

        double x_axis_length = width - (2 * mar);
        double y_axis_length = height - (2 * mar);

        for (int i = 0; i < 495; i++) {

            double x_axis_value = (x_axis_length / 1) * points[i].values[0];
            double y_axis_value = (y_axis_length / 1) * points[i].values[1];

            double x_draw = x_axis_value + mar;
            double y_draw = height - mar - y_axis_value;

            if (delivery) {
                Color color = new Color(GetColor(points[i], crisp));
                g1.setPaint(color);

            } else {

                int temp = 0;
                for (int j = 0; j < 5; j++) {

                    if (points[i].belongings[temp] < points[i].belongings[j])
                        temp = j;
                }

                if (temp == 0)
                    g1.setPaint(Color.RED);
                else if (temp == 1)
                    g1.setPaint(Color.GREEN);
                else if (temp == 2)
                    g1.setPaint(Color.BLUE);
                else if (temp == 3)
                    g1.setPaint(Color.YELLOW);
                else
                    g1.setPaint(Color.GRAY);
            }

                g1.fill(new Ellipse2D.Double(x_draw, y_draw, 6, 6));
        }
    }
}

public class Main {

    private static double Parse(String s) {

        int intNumber;
        int floatNumber;

        intNumber = s.indexOf('.');
        floatNumber = s.length() - intNumber - 1;

        double trueDigits = 0;
        double floatingPoints = 0;

        double temp;
        for (int i = 0; i < intNumber; i++) {

            temp = (int) s.charAt(i);
            temp -= 48;
            temp *= Math.pow(10, intNumber - i - 1);
            trueDigits += temp;
        }

        for (int i = intNumber + 1; i < s.length(); i++) {

            temp = (int) s.charAt(i);
            temp -= 48;
            temp *= Math.pow(10, s.length() - i - 1);
            floatingPoints += temp;
        }

        floatingPoints *= Math.pow(10, -floatNumber);

        return trueDigits + floatingPoints;
    }

    public static void main(String[] args) {

        SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
        SpreadsheetInfo.addFreeLimitReachedListener(eventArguments -> eventArguments.setFreeLimitReachedAction(FreeLimitReachedAction.CONTINUE_AS_TRIAL));

        double m = 2;
        int N = 756;
        int dimension = 2;
        Point[] points = new Point[N];

        try {

            ExcelFile dataset = ExcelFile.load("D:\\computer\\Computational Intelligence\\FuzzyLogic\\data3.csv");
            ExcelWorksheet worksheet = dataset.getWorksheet(0);
            ExcelRow row;
            ExcelCell cell;

            double[] tempValues = new double[dimension];

            for (int i = 0; i < N; i++) {

                row = worksheet.getRow(i);

                for (int j = 0; j < dimension; j++) {

                    cell = row.getCell(j);
                    tempValues[j] = Parse(cell.getStringValue());
                }

                points[i] = new Point(dimension, tempValues);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        int[] randoms;
        Point[] centers;

        C_Mean c_mean;

        double cost;

        // first delivery item
//        int[] cs = new int[8];
//        double[] costs = new double[8];
//        int j = 0;
//
//        for (int c = 2; c < 10; c++) {
//
//            for (int i = 0; i < N; i++)
//                points[i].belongings = new double[c];
//
//            randoms = new int[c];
//            for (int i = 0; i < c; i++)
//                randoms[i] = random.nextInt(450);
//            centers = new Point[c];
//            for (int i = 0; i < c; i++)
//                centers[i] = points[randoms[i]];
//
//            c_mean = new C_Mean(N, dimension, c, m, centers, points);
//            cost = c_mean.cost;
//
//            cs[j] = c;
//            costs[j] = cost;
//            j++;
//
//            System.out.println(c + ": " + cost);
//        }
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new G(cs, costs));
//        frame.setSize(600, 600);
//        frame.setLocation(200, 200);
//        frame.setVisible(true);
        // end of first delivery item

        // second delivery item(true)
//        for (int i = 0; i < N; i++)
//            points[i].belongings = new double[3];
//
//        randoms = new int[3];
//        for (int i = 0; i < 3; i++)
//            randoms[i] = random.nextInt(756);
//        centers = new Point[3];
//        for (int i = 0; i < 3; i++)
//            centers[i] = points[randoms[i]];
//
//        c_mean = new C_Mean(N, dimension, 3, m, centers, points);
//        cost = c_mean.cost;
//        System.out.println(cost);
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new G2(c_mean.points, true, true));
//        frame.setSize(600, 600);
//        frame.setLocation(200, 200);
//        frame.setVisible(true);
        // end of second delivery item

        // third delivery item(false)
        for (int i = 0; i < N; i++)
            points[i].belongings = new double[5];

        randoms = new int[5];
        for (int i = 0; i < 5; i++)
            randoms[i] = random.nextInt(756);
        centers = new Point[5];
        for (int i = 0; i < 5; i++)
            centers[i] = points[randoms[i]];

        c_mean = new C_Mean(N, dimension, 5, m, centers, points);
        cost = c_mean.cost;
        System.out.println(cost);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new G2(c_mean.points, true, false));
        frame.setSize(600, 600);
        frame.setLocation(200, 200);
        frame.setVisible(true);
        // end of first delivery item
    }
}