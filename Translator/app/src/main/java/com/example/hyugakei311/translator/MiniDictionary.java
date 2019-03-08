package com.example.hyugakei311.translator;

/**
 * Created by hyugakei311 on 4/19/2018.
 */

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

public class MiniDictionary {
    //public static final String SENT_KEY = "sentence";
    public static final float MIN_CONFIDENCE = 0.5f;

    public static final String DEFAULT_SENT = "Please retry/Hãy thử lại";

    private Hashtable<String,String> dictionary;

    public MiniDictionary(Hashtable<String,String> newDictionary){
        dictionary = newDictionary;
    }

    public String firstMatchWithMinConfidence(ArrayList<String> sentences, float[] confidLevels){
        if (sentences == null || confidLevels == null){
            return DEFAULT_SENT;
        }

        int numberOfSentences = sentences.size();
        Enumeration<String> entries;
        for (int i = 0; i < numberOfSentences && i < confidLevels.length; i++){
            if (confidLevels[i] < MIN_CONFIDENCE)
                break;
            String sentence = sentences.get(i);
            entries = dictionary.keys();
            while (entries.hasMoreElements()){
                String entry = entries.nextElement();
                if (sentence.equalsIgnoreCase(entry))
                    return entry;
            }
        }
        return DEFAULT_SENT;
    }

    public String getTranslation(String sentence){
        return dictionary.get(sentence); //null if sentence not found
    }

    public int getIndex(String sentence){
        ArrayList<String> entries = new ArrayList<>(dictionary.keySet());
        int result = 50;
        for (int i = 0; i < dictionary.size(); i++){
            if (sentence.equalsIgnoreCase(entries.get(i))){
                result = i;
                break;
            }
        }
        return result;
    }
}
