package sjtu.hci.idiotdial.manager;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import sjtu.hci.idiotdial.util.MatrixHelper;

import static sjtu.hci.idiotdial.util.MatrixHelper.*;


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
                for (double[] aFeature : feature) {
                    JSONArray array = new JSONArray();
                    for (double anAFeature : aFeature) {
                        array.put(anAFeature);
                    }
                    jsonArray.put(array);
                }
            } catch (JSONException e){
                Log.e(TAG, "PUTJSONARRAYERROR!!!!!");
                Log.e(TAG, e.toString());
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
        ArrayNode node = new ArrayNode(name, feature);
        featureList.add(node);
        Log.e(TAG, "TRAIN:Name" + name +"Feature:" + feature.length + "Feature[0]:"+feature[0].length);
        return featureList.toString();
    }


    public String getName(double[][] feature){
        String name = "Not Found";
        double minDist = Double.MAX_VALUE;
        for (ArrayNode node : this.featureList){
            Log.e(TAG, "Node Value:" + node.feature.length);
            double dist =  get_DTW_value(node.feature, feature);
            Log.e(TAG, "Get_DTW_VALUE:" +dist);
            if (dist < minDist){
                minDist = dist;
                name = node.name;
            }
        }
        return name;
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
                Log.e(TAG, "LOAD:Name" + name + "Feature:" + feature.length);
                featureList.add(new ArrayNode(name, feature));
            }
        } catch (JSONException e) {
            Log.e(TAG, "!!!LoadError");
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
