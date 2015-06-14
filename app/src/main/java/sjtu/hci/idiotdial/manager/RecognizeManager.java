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
        double minDist = Double.MAX_VALUE;
        for (ArrayNode node : this.featureList){
            double dist = calDist(distEu(node.feature, feature));
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
