package sjtu.hci.idiotdial.util;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Edward on 2015/6/15.
 */
public class MatrixHelper {

    private static final String TAG = "MatrixHelper";
    public static double THREHOLD = 0.005;

    public static double[] preProcess(double[] input){

        return preEmphasis(input);
    }

    public static ArrayList<Double> filterThreshold(ArrayList<Double> input){
        ArrayList<Double> outList = new ArrayList<>();
        for (int i = 0; i < input.size(); ++i) {
            if (Math.abs(input.get(i)) > THREHOLD) {
                outList.add(input.get(i));
            }
        }
        return outList;
    }

    public static double[] preEmphasis(double[] input){
        return input;
//        return filter(-0.9495, input);
    }


    public static double[] filter(double b, double[] input){
        double[] output = new double[input.length];
        output[0] = input[0];
        for (int i = 1; i < input.length; i++) {
            output[i] = input[i] + b * input[i - 1];
        }
        return output;
    }


    public static void filterAdd(double [] b,double [] a, ArrayList<Double> inputVector,ArrayList<Double> outputVector){
        double rOutputY = 0.0;
        int j = 0;
        for (int i = 0; i < inputVector.size(); i++) {
            if(j < b.length){
                rOutputY += b[j]*inputVector.get(inputVector.size() - i - 1);
            }
            j++;
        }
        j = 1;
        for (int i = 0; i < outputVector.size(); i++) {
            if(j < a.length){
                rOutputY -= a[j]*outputVector.get(outputVector.size() - i - 1);
            }
            j++;
        }
        outputVector.add(rOutputY);
    }


    static public double[][] mean(double[][] res)
    {
        for (int j=0; j<res[0].length; ++j)
        {
            double sum = 0;
            for (int i = 0; i < res.length; ++i)
                sum += res[i][j];
            sum = sum / res.length;
            for (int i = 0; i < res.length; ++i)
                res[i][j] -= sum;
        }
        return res;
    }

    public static double get_DTW_value(double[][] F, double[][] R)
    {
        F = transform(F);
        R = transform(R);
        int fWidth = F[0].length;
        int rWidth = R[0].length;
        double[][] dis = new double[fWidth][];
        double[][] times = new double[F[0].length][];
        for (int i = 0; i< fWidth; ++i)
        {
            dis[i] = new double[rWidth];
            times[i] = new double[rWidth];
            for (int j = 0; j < rWidth; ++j)
            {
                times[i][j] = 0;
                dis[i][j] = 0;
                for (int k=0; k<F.length; ++k)
                {
                    dis[i][j] += (F[k][i] - R[k][j]) * (F[k][i] - R[k][j]);
                }
                dis[i][j] = Math.sqrt(dis[i][j]);
            }
        }

        for (int i = 1; i < fWidth; ++i)
        {
            dis[i][0] = dis[i][0] + dis[i - 1][0];
            times[i][0] = i;
        }
        for (int j = 1; j < rWidth; ++j)
        {
            dis[0][j] = dis[0][j] + dis[0][j - 1];
            times[0][j] = j;
        }
        for (int i = 1; i < fWidth; ++i)
            for (int j = 1; j < rWidth; ++j)
            {
                double minval  = Math.min(Math.min(dis[i - 1][j],2* dis[i - 1][j - 1]), dis[i][j - 1]);//? i-1 out of index
                if (minval == dis[i - 1][j]) times[i][j] = times[i - 1][j] + 1;
                else if (minval == dis[i - 1][j - 1]) times[i][j] = times[i - 1][j - 1] + 1;
                else times[i][j] = times[i][j - 1] + 1;
                dis[i][j] = dis[i][j] + minval;
            }
        return dis[F[0].length - 1][R[0].length - 1] / (times[F[0].length - 1][R[0].length - 1]);
//        return dis[fWidth - 1][rWidth - 1] / (fWidth + rWidth + 1);
    }

    static public double calDist(double[][] a){
        double dist = 0.0;
        for (int i = 0; i < a.length; ++i){
            dist += minRow(a[i]);
        }
        dist = dist / a.length;
        return dist;
    }

    static public double minRow(double[] a){
        double min = Double.MAX_VALUE;
        for (int i = 0; i < a.length; ++i){
            if (a[i] < min){
                min = a[i];
            }
        }
        return min;
    }





    static public double[][] distEu(double[][] a, double[][] b){
        if (a.length != b.length){
        }
        int aWidth = a[0].length;
        int bWidth = b[0].length;
        double[][] aTrans = transform(a);
        double[][] bTrans = transform(b);
        double[][] dist = new double[aWidth][bWidth];
        if (aWidth < bWidth){
            for (int i = 0;i < aWidth;++i  ){
                for (int j = 0; j < bWidth; ++j){
                    dist[i][j] = du(aTrans[i], bTrans[j]);
                }
            }
        }
        return dist;
    }

    static public double[][] transform(double[][] a){
        int width = a.length;
        int height = a[0].length;
        double[][] res = new double[height][width];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                res[j][i] = a[i][j];
            }
        }
        return res;
    }

    static public double du(double[] a, double[] b){
        double sum = 0.0;
        for (int i = 0; i < a.length; ++i){
            double t = a[i] - b[i];
            sum += (t * t);
        }
        return Math.sqrt(sum);
    }

}