package sample;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;


public class Controller_Builder {

    private double x0, y0, X;
    private int Ninit, Nfinal;
    private int N;
    private double[] arr_x;  // x

    //---------methods-----------
    private double[] y_euler;  //y for euler method solution
    private double[] y_imp;    //y for improve Euler method solution
    private double[] y_runge;  //y for Runge-Kutta method solution
    private double[] y_exact;  //y for exact solution

    //----------errors-----------
    private double[] euler_error;
    private double[] impr_error;
    private double[] runge_error;

    private double[] euler_total;
    private double[] impr_total;
    private double[] runge_total;

    //----------------constructor--------------------
    public Controller_Builder(double ax0, double ay0, double aX, int aN, int aNinit, int aNfinal){
        this.x0 = ax0;
        this.y0 = ay0;
        this.X = aX;
        this.N = aN;
        this.Ninit = aNinit;
        this.Nfinal = aNfinal;

        arr_x = new double[Nfinal];

        y_euler = new double[Nfinal];
        y_imp = new double[Nfinal];
        y_runge = new double[Nfinal];
        y_exact = new double[Nfinal];

        euler_error = new double[Nfinal];
        impr_error = new double[Nfinal];
        runge_error = new double[Nfinal];

        euler_total = new  double[Nfinal-Ninit+1];
        impr_total = new double[Nfinal-Ninit+1];
        runge_total = new double[Nfinal-Ninit+1];
    }

    //------------------functions that constructs charts-------------------------
    Scene graphMethods(boolean[] isSelected) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("x");
        y.setLabel("y");

        XYChart.Series[] chart = new XYChart.Series[5];
        for(int j=1; j<=4; j++){
            chart[j] = new XYChart.Series();
        }
        chart[1].setName("Exact solution");
        chart[2].setName("Euler's method");
        chart[3].setName("Improved Euler's method");
        chart[4].setName("Runge-Kutta method");

        for (int i=0; i<N; i++) {
            XYChart.Data[] to_add = new XYChart.Data[5];
            to_add[1] = new XYChart.Data(arr_x[i], y_exact[i]);
            to_add[2] = new XYChart.Data(arr_x[i], y_euler[i]);
            to_add[3] = new XYChart.Data(arr_x[i], y_imp[i]);
            to_add[4] = new XYChart.Data(arr_x[i], y_runge[i]);

            Rectangle[] rect = new Rectangle[5];
            for(int j=1; j<=4; j++){
                rect[j] = new Rectangle(0,0);
                rect[j].setVisible(false);
                to_add[j].setNode(rect[j]);
                chart[j].getData().add(to_add[j]);
            }
        }

        LineChart<Number, Number> coordinateSystem = new LineChart<Number, Number>(x, y);
        coordinateSystem.setTitle("Charts");

        for(int j=1; j<=4; j++)
            if (isSelected[j]) coordinateSystem.getData().add(chart[j]);

        return new Scene(coordinateSystem, 800, 800);
    }

    Scene graphErrors(boolean[] isSelected){
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("x");
        y.setLabel("y");

        XYChart.Series[] chart = new XYChart.Series[4];
        for(int j=1; j<=3; j++){
            chart[j] = new XYChart.Series();
        }
        chart[1].setName("Euler's error");
        chart[2].setName("Improved Euler's error");
        chart[3].setName("Runge-Kutta's error");

        for (int i=0; i<N; i++) {
            XYChart.Data[] to_add = new XYChart.Data[5];
            to_add[1] = new XYChart.Data(arr_x[i], euler_error[i]);
            to_add[2] = new XYChart.Data(arr_x[i], impr_error[i]);
            to_add[3] = new XYChart.Data(arr_x[i], runge_error[i]);

            Rectangle[] rect = new Rectangle[5];
            for(int j=1; j<=3; j++){
                rect[j] = new Rectangle(0,0);
                rect[j].setVisible(false);
                to_add[j].setNode(rect[j]);
                chart[j].getData().add(to_add[j]);
            }
        }

        LineChart<Number, Number> coordinateSystem = new LineChart<Number, Number>(x, y);
        coordinateSystem.setTitle("Error Charts");
        for(int j=1; j<=3; j++)
            if (isSelected[j]) coordinateSystem.getData().add(chart[j]);

        return new Scene(coordinateSystem, 800, 800);
    }

    Scene TotalError(){
        NumberAxis n = new NumberAxis();
        NumberAxis y = new NumberAxis();
        n.setLabel("N");
        y.setLabel("y");

        XYChart.Series[] chart = new XYChart.Series[4];
        for(int j=1; j<=3; j++){
            chart[j] = new XYChart.Series();
        }
        chart[1].setName("Euler's total error");
        chart[2].setName("Improved Euler's total error");
        chart[3].setName("Runge-Kutta's total error");

        for (int i=Ninit; i<=Nfinal; i++) {
            XYChart.Data[] to_add = new XYChart.Data[5];
            to_add[1] = new XYChart.Data(i, euler_total[i-Ninit]);
            to_add[2] = new XYChart.Data(i, impr_total[i-Ninit]);
            to_add[3] = new XYChart.Data(i, runge_total[i-Ninit]);

            Rectangle[] rect = new Rectangle[5];
            for(int j=1; j<=3; j++){
                rect[j] = new Rectangle(0,0);
                rect[j].setVisible(false);
                to_add[j].setNode(rect[j]);
                chart[j].getData().add(to_add[j]);
            }
        }

        LineChart<Number, Number> coordinateSystem = new LineChart<Number, Number>(n, y);
        coordinateSystem.setTitle("Total Error Charts");
        for(int j=1; j<=3; j++)
            coordinateSystem.getData().add(chart[j]);

        return new Scene(coordinateSystem, 800, 800);
    }

    //-------------function that finds all xes and yes for each method and their errors------------
    void calc(int aN){
        y_euler[0] = y0;
        y_imp[0] = y0;
        y_runge[0] = y0;
        y_exact[0] = y0;

        double h = (X-x0)/aN;

        for (int i=0; i<aN; i++)
            arr_x[i] = x0+i*h;

        for (int i=1; i<aN; i++) {
            //------------exact---------------
            y_exact[i] = exact_sol(arr_x[i], constant(x0, y0));

            //Euler
            y_euler[i] = y_euler[i-1] + h*func(arr_x[i-1], y_euler[i-1]);

            //----------------Improved Euler-----------------
            double dy1 = h*(func(arr_x[i-1] + h/2, y_imp[i-1] + h/2*func(arr_x[i-1], y_imp[i-1])));
            y_imp[i] = y_imp[i-1]+dy1;

            //----------------Runge-Kutta-------------------
            double k1, k2, k3, k4, dy2;
            k1 = func(arr_x[i-1], y_runge[i-1]);
            k2 = func(arr_x[i-1] + h/2, y_runge[i-1] + h*k1/2);
            k3 = func(arr_x[i-1] + h/2, y_runge[i-1] + h*k2/2);
            k4 = func(arr_x[i-1] + h, y_runge[i-1] + h*k3);
            dy2 = h/6*(k1+k2*2+2*k3+k4);
            y_runge[i] = y_runge[i - 1] + dy2;

            //----------------errors------------------
            euler_error[i] = Math.abs(y_exact[i] - y_euler[i]);
            impr_error[i] = Math.abs(y_exact[i] - y_imp[i]);
            runge_error[i] = Math.abs(y_exact[i] - y_runge[i]);
        }

        for (int i = 0; i < aN; i++)
            System.out.println(i+" x:"+arr_x[i]+"; Exact solution: " + y_exact[i] + "; Euler: " + y_euler[i] + "; Improved: " + y_imp[i] + "; Runge: " + y_runge[i]);
    }

    void calcTotal(){
        for(int Ni=Ninit; Ni<=Nfinal; Ni++){
            calc(Ni);
            euler_total[Ni-Ninit] = max1(euler_error, Ni);
            impr_total[Ni-Ninit] = max1(impr_error, Ni);
            runge_total[Ni-Ninit] = max1(runge_error, Ni);
        }
    }

    private double max1(double[] arr, int aN){
        double ans = 0;
        for(int i=0; i<aN; i++){
            if (arr[i]>ans)
                ans = arr[i];
        }
        return ans;
    }

    private double func(double x, double y) {
        return Math.exp(2*x)+Math.exp(x)+Math.pow(y,2)-2*y*Math.exp(x);
    }

    private double exact_sol(double x, double Const){
        return Math.exp(x)-1/(x+Const);

    }
    private double constant(double x0, double y0){
        return 1/(Math.exp(x0)-y0)-x0;
    }
}