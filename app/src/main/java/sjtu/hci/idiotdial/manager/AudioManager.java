package sjtu.hci.idiotdial.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import comirva.audio.util.MFCC;
import sjtu.hci.idiotdial.util.MatrixHelper;

/**
 * Created by Edward on 2015/6/11.
 */
public class AudioManager {
    private final static String TAG = "AudioManager";
    private final static String DATA_KEY = "DataKey";
    private static AudioManager instance = null;

    private AudioRecord recorder;
    private static final int RECORDER_SAMPLERATE = 11025;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private Thread recordThread;
    private boolean isRecording;
    private ArrayList<double[]> audioDoubleList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    public void startRecord(Context context){
        sharedPreferences = context.getSharedPreferences("IDIOTDIAL.REFRECNCE", Context.MODE_PRIVATE);
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);
        recorder.startRecording();
        isRecording = true;
        recordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveVoiceToBuffer();
            }
        }, "AudioRecord Thread");
        recordThread.start();
    }

    private double[][] stopRecord() throws IOException {
        isRecording = false;
        try {
            recordThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MFCC mfcc = new MFCC(RECORDER_SAMPLERATE);
        double[] src = flatten(this.audioDoubleList);
        this.audioDoubleList.clear();
        Log.e(TAG, "BeforeProcess:"+ src.length);
//        src = MatrixHelper.preProcess(src);
        //showVoiceData(src);
        double[][] res = mfcc.process(src);
        Log.e(TAG, "After Process:" + res.length);
        return res;
    }

    private void showVoiceData(double[] src) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < src.length; ++i){
            try {
                array.put(src[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "VoiceData:"+ array.toString() );
    }

    public String stopToGetName(){
        try {
            double[][] result = stopRecord();
            RecognizeManager manager = RecognizeManager.getInstance();
            if (!manager.loaded){
                String str = sharedPreferences.getString(DATA_KEY, "");
                manager.loadFromString(str);
            }
            return RecognizeManager.getInstance().getName(result);
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    public void stopToTrain(String name){
        try {
            double[][] result = stopRecord();
            RecognizeManager manager = RecognizeManager.getInstance();
            if (!manager.loaded){
                String savedStr = sharedPreferences.getString(DATA_KEY, "");
                manager.loadFromString(savedStr);
            }
            String str = manager.train(name, result);
            sharedPreferences.edit().putString(DATA_KEY, str).apply();
            Log.e(TAG, "After train and save");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveVoiceToBuffer(){
        while(isRecording){
            short[] sData = new short[BufferElements2Rec];
            recorder.read(sData, 0, BufferElements2Rec);
            double[] dData = short2double(sData);
            audioDoubleList.add(dData);
        }
    }


    private double[] short2double(short[] sData){
        int size = sData.length;
        System.gc();
        double[] dData = new double[size];
        for (int i = 0; i < size; i++){
            dData[i] = sData[i] / 32768.0;
        }
        return dData;
    }

    private double[] flatten(ArrayList<double[]> doubleList){
        int arraySize = doubleList.get(0).length;
        int listSize = doubleList.size();
        System.gc();
        double[] result = new double[arraySize * listSize];
        int index = 0;
        for (int i = 0; i < listSize; ++i){
            for (int j = 0; j < arraySize; ++j){
                result[index++] = doubleList.get(i)[j];
            }
        }
        return result;
    }

    protected AudioManager(){

    }

   static public AudioManager getInstance(){
       if (instance == null){
           instance = new AudioManager();
       }
       return instance;
   }
}
