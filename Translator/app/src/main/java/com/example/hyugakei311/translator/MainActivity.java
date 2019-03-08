package com.example.hyugakei311.translator;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
//import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageButton;
import android.media.SoundPool;

public class MainActivity extends AppCompatActivity {
    private static final int TRANS_REQUEST = 1;
    public static MiniDictionary entries;
    private ImageButton speak;

    private SoundPool pool;
    private int[] ids = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speak = findViewById(R.id.speak);

        Hashtable<String,String> dictionary = new Hashtable<String,String>();
        dictionary.put("Hello","Xin chào");
        dictionary.put("How are you","Bạn có khỏe không?");
        dictionary.put("What's your name","Tên bạn là gì?");
        dictionary.put("Nice to meet you", "Rất vui được gặp bạn!");
        dictionary.put("Where are you from", "Bạn từ đâu tới?");
        dictionary.put("Where's the restroom","Nhà vệ sinh ở đâu vậy?");
        dictionary.put("What do you do", "Bạn làm nghề gì?");
        dictionary.put("How long have you been here","Bạn ở đây được bao lâu rồi?");
        dictionary.put("When will you leave", "Bao giờ bạn rời đi?");
        dictionary.put("Did you enjoy your stay", "Bạn có thấy hài lòng với chuyến đi này không?");

        entries = new MiniDictionary(dictionary);

        //Test if device supports speech recognition
        PackageManager manager = getPackageManager();
        List<ResolveInfo> listOfMatches = manager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if (listOfMatches.size() > 0)
            listen();
        else {//speech recognition not supported
            speak.setEnabled(false);
            Toast.makeText(this, "Sorry - Your device does not support speech recognition", Toast.LENGTH_LONG).show();
        }

        SoundPool.Builder poolBuilder = new SoundPool.Builder();
        poolBuilder.setMaxStreams(1);
        pool = poolBuilder.build();
        ids[0] = pool.load(this, R.raw.m1,1);
        ids[1] = pool.load(this, R.raw.m2,1);
        ids[2] = pool.load(this, R.raw.m3,1);
        ids[3] = pool.load(this, R.raw.m4,1);
        ids[4] = pool.load(this, R.raw.m5,1);
        ids[5] = pool.load(this, R.raw.m6,1);
        ids[6] = pool.load(this, R.raw.m7,1);
        ids[7] = pool.load(this, R.raw.m8,1);
        ids[8] = pool.load(this, R.raw.m9,1);
        ids[9] = pool.load(this, R.raw.m10,1);
    }

    public void startSpeaking(View v){
        listen();
    }

    private void listen(){
        speak.setEnabled(false);
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "To Translate/Cần Dịch");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        startActivityForResult(listenIntent, TRANS_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TRANS_REQUEST && resultCode == RESULT_OK) {
            //retrieve a list of possible words
            ArrayList<String> returnedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //retrieve array of scores for returnedWords
            float[] scores = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

            //retrieve first good match
            String firstMatch = entries.firstMatchWithMinConfidence(returnedWords, scores);

            //output to screen
            TextView output = findViewById(R.id.result);
            String trans = entries.getTranslation(firstMatch);
            if (trans != null) {
                output.setText(firstMatch.substring(0, 1).toUpperCase() + firstMatch.substring(1) + "\n" + trans);
                pool.play(ids[entries.getIndex(firstMatch)], 1.0f, 1.0f, 1, 0, 1.0f);
            }
            else{
                output.setText(returnedWords.get(0) + "\n" + firstMatch);
            }

        }
        speak.setEnabled(true);
    }
}
