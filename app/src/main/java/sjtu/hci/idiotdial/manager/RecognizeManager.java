package sjtu.hci.idiotdial.manager;

import android.util.Log;

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
    }

    ArrayList<ArrayNode> featureList = new ArrayList<>();

    public void train(String name, double[][] feature){
        featureList.add(new ArrayNode(name, feature));
    }

    public String getName(double[][] feature){
        return "SomeName";
    }

    private float[][] distEu(double[][] a, double[][] b){
        if (a.length != b.length){
            Log.e(TAG, "!!!a.length not equal to b.length");
        }
        int aWidth = a[0].length;
        int bWidth = b[0].length;
        float[][] dist = new float[aWidth][bWidth];
        if (aWidth < bWidth){
            for (int i = 0;i < aWidth;++i  ){
                for (int j = 0; j < bWidth; ++j){
                }
            }
        }
        return dist;
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
