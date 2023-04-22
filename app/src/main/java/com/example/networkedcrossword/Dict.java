package com.example.networkedcrossword;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Dict extends AppCompatActivity {
    private int number_of_words;
    private int word_length;
    private Context context;
    private HashMap<String,String> dict;
    ArrayList<String> file = null;

    public Dict(int length, int amount, Context cxt){
        this.word_length = length;
        this.context = cxt;
        this.dict = new HashMap<String, String>();
        this.number_of_words = amount;
    }

    public void build() throws IOException {
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = context.getResources().openRawResource(R.raw.words);
            reader = new BufferedReader(new InputStreamReader(is));
            String read;
            file = new ArrayList<String>();
            while ((read=reader.readLine()) != null) {
                file.add(read);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("[HERE]" + is);
            assert is != null;
            is.close();
        }

        Random rand = new Random();

        while (this.dict.size() != this.number_of_words) {
            int index=rand.nextInt(file.size());
            String[] parts = file.get(index).split(":");
            if (!this.dict.containsKey(parts[0])) {
                if (parts[0].length() <= word_length) {
                    this.dict.put(parts[0], parts[1]);
                }
            }
        }
        System.out.println(this.dict.size());
        System.out.println("[KEYSET2]"+this.dict.keySet());
    }
    public Set get_words() {
        return this.dict.keySet();
    }

    public HashMap get_dict() {
        return this.dict;
    }

    public String get_hint(String word) {
        return this.dict.get(word);
    }

    public void setNumber_of_words(Integer num) {
        this.number_of_words = num;
        this.resize();
    }

    private void resize() {
        Random rand = new Random();
        while (this.dict.size() != this.number_of_words) {
            //TODO: if loop continues for too long just stop with current #
            int index=rand.nextInt(file.size());
            String[] parts = file.get(index).split(":");
            if (!this.dict.containsKey(parts[0])) {
                if (parts[0].length() <= word_length) {
                    this.dict.put(parts[0], parts[1]);
                }
            }
        }

    }


}