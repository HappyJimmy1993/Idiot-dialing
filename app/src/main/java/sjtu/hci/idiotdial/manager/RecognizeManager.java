package sjtu.hci.idiotdial.manager;

import java.util.ArrayList;


/**
 * Created by Edward on 2015/6/11.
 */
public class RecognizeManager {

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
