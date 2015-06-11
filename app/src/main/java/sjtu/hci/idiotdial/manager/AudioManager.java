package sjtu.hci.idiotdial.manager;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.IOException;
import java.util.ArrayList;

import comirva.audio.util.MFCC;

/**
 * Created by Edward on 2015/6/11.
 */
public class AudioManager {

    private static AudioManager instance = null;

    private AudioRecord recorder;
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private Thread recordThread;
    private boolean isReacording;
    private ArrayList<double[]> audioDoubleList = new ArrayList<>();

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    public void startRecord(){
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);
        recorder.startRecording();
        isReacording = true;
        recordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveVoiceToBuffer();
            }
        }, "AudioRecord Thread");
        recordThread.start();
    }

    private double[][] stopRecord() throws IOException {
        isReacording = false;
        try {
            recordThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MFCC mfcc = new MFCC(RECORDER_SAMPLERATE);
        double[] src = flatten(this.audioDoubleList);
        return mfcc.process(src);
    }

    public String stopToGetName(){
        try {
            double[][] result = stopRecord();
            return RecognizeManager.getInstance().getName(result);
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    public void stopToTrain(String name){
        try {
            double[][] result = stopRecord();
            RecognizeManager.getInstance().train(name, result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveVoiceToBuffer(){
        while(isReacording){
            short[] sData = new short[BufferElements2Rec];
            recorder.read(sData, 0, BufferElements2Rec);
            double[] dData = short2double(sData);
            audioDoubleList.add(dData);
        }
    }


    private double[] short2double(short[] sData){
        int size = sData.length;
        double[] dData = new double[size];
        for (int i = 0; i < size; i++){
            dData[i] = sData[i] / 32768.0;
        }
        return dData;
    }

    private double[] flatten(ArrayList<double[]> doubleList){
        int arraySize = doubleList.get(0).length;
        int listSize = doubleList.size();
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
