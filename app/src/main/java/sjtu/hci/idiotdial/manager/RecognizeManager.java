package sjtu.hci.idiotdial.manager;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * Created by Edward on 2015/6/11.
 */
public class RecognizeManager {
    private static final String TAG = "RecognizeManager";

    private static class ArrayNode{
        public String name;
        double[][] feature;
        public ArrayNode(String name, double[][] feature){
            this.name = name;
            this.feature = feature;
        }

        @Override
        public String toString(){
            JSONArray jsonArray = new JSONArray();
            try {
                for (int i = 0; i < feature.length; ++i) {
                    JSONArray array = new JSONArray();
                    for (int j = 0; j < feature[i].length; ++j) {
                        array.put(feature[i][j]);
                    }
                    jsonArray.put(array);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return "{ name:"+ this.name + ", feature: " +  jsonArray.toString() + " }";
        }
    }

    private ArrayList<ArrayNode> featureList = new ArrayList<>();
    public boolean loaded = false;

    public String train(String name, double[][] feature){
        feature = mean(feature);
        for (int i = 0; i < featureList.size(); ++i){
            if (featureList.get(i).name.equals(name)){
                featureList.remove(i);
                break;
            }
        }
        featureList.add(new ArrayNode(name, feature));
        return featureList.toString();
    }

    public void loadFromString(String str){
        this.featureList.clear();
        try {
            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); ++i){
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("name");
                JSONArray featureArray = object.getJSONArray("feature");
                double[][] feature = new double[featureArray.length()][];
                for (int j = 0; j < feature.length; ++j){
                    JSONArray array2 = featureArray.getJSONArray(j);
                    feature[j] = new double[array2.length()];
                    for (int k = 0; k < feature[j].length; ++k){
                        double value = array2.getDouble(k);
                        feature[j][k] = value;
                    }
                }
                featureList.add(new ArrayNode(name, feature));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        double[][] sample = {
                {1.2, 1.3, 1.4},
                {2.3, 2.5, 2.7},
                {4.5, 2.4}
        };
        RecognizeManager manager = RecognizeManager.getInstance();
        String str = manager.train("test", sample);
        manager.loadFromString(str);
        System.out.println(manager.featureList.toString());
    }

    public String getName(double[][] feature){
        String name = "Not Found";
        feature = mean(feature);
        double minDist = Double.MAX_VALUE;
        for (ArrayNode node : this.featureList){
            double dist =  get_DTW_value(node.feature, feature);
            if (dist < minDist){
                minDist = dist;
                name = node.name;
            }
        }
        return name;
    }

    private double calDist(double[][] a){
        double dist = 0.0;
        for (int i = 0; i < a.length; ++i){
            dist += minRow(a[i]);
        }
        dist = dist / a.length;
        return dist;
    }

    private double minRow(double[] a){
        double min = Double.MAX_VALUE;
        for (int i = 0; i < a.length; ++i){
            if (a[i] < min){
                min = a[i];
            }
        }
        return min;
    }


    private double[][] mean(double[][] res)     //CMN,对res每一列减去其平均值
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

    double get_DTW_value(double[][] F, double[][] R)
    {
        //计算F与R之间欧几里得距离
        double[][] dis = new double[F[0].length][];
        //double[][] times = new double[F[0].Length][];
        for (int i = 0; i< F[0].length; ++i)
        {
            dis[i] = new double[F[0].length];
            // times[i] = new double[F[0].Length];
            for (int j = 0; j < R[0].length; ++j)
            {
                //times[i][j] = 0;
                dis[i][j] = 0;
                for (int k=0; k<F.length; ++k)
                {
                    dis[i][j] += (F[k][i] - R[k][j]) * (F[k][i] - R[k][j]);
                }
                dis[i][j] = Math.sqrt(dis[i][j]);
            }
        }

        //从点(0,0)开始寻找最短路径
        for (int i = 1; i < F[0].length; ++i)
        {
            dis[i][0] = dis[i][0] + dis[i - 1][0];
            //times[i][0] = i;
        }
        for (int j = 1; j < R[0].length; ++j)
        {
            dis[0][j] = dis[0][j] + dis[0][j - 1];
            //times[0][j] = j;
        }
        for (int i = 0; i < F[0].length; ++i)
            for (int j = 1; j < R[0].length; ++j)
            {
                double minval  = Math.min(Math.min(dis[i - 1][j], 2 * dis[i - 1][j - 1]), dis[i][j - 1]);
                //if (minval == dis[i - 1][j]) times[i][j] = times[i - 1][j] + 1;
                //else if (minval == dis[i - 1][j - 1]) times[i][j] = times[i - 1][j - 1] + 1;
                //else times[i][j] = times[i][j - 1] + 1;
                dis[i][j] = dis[i][j] + minval;
            }
        return dis[F[0].length - 1][R[0].length - 1] / (F[0].length + R[0].length + 1);
    }


    private double[][] distEu(double[][] a, double[][] b){
        if (a.length != b.length){
            Log.e(TAG, "!!!a.length not equal to b.length");
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

    private double[][] transform(double[][] a){
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

    private double du(double[] a, double[] b){
        double sum = 0.0;
        for (int i = 0; i < a.length; ++i){
            double t = a[i] - b[i];
            sum += (t * t);
        }
        return Math.sqrt(sum);
    }

    protected RecognizeManager(){

    }

    private static RecognizeManager instance = null;

    public static RecognizeManager getInstance(){
        if (instance == null){
            instance = new RecognizeManager();
        }
        return instance;
    }
}
