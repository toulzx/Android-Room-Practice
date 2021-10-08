package com.toulzx.roompractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_FILE_NAME = "word_database";

    private TextView textView;
    private Button btnInsert, btnClear, btnUpdate, btnDelete;

    private WordViewModel wordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // viewModel instantiation
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        wordViewModel.getAllWordsLive().observe(this, new Observer<>() {
            @Override
            public void onChanged(List<Word> words) {
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < words.size(); i++) {
                    Word word = words.get(i);
                    // text += word.getId() + ":" + word.getWord() + "=" + word.getChineseMeaning() + "\n";      // Modified
                    text.append(word.getId()).append(":").append(word.getWord()).append("=").append(word.getChineseMeaning()).append("\n");
                }
                textView.setText(text.toString());
            }
        });

        // bind
        textView = findViewById(R.id.textView);
        btnInsert = findViewById(R.id.btnInsert);
        btnClear = findViewById(R.id.btnClear);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        //listener
        btnInsert.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

    }


    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btnInsert) {
            Word word1 = new Word("Hello", "你好！");
            Word word2 = new Word("World", "世界！");
            wordViewModel.insertWords(word1, word2);
        } else if (id == R.id.btnClear) {
            wordViewModel.deleteAllWords();
        } else if (id == R.id.btnUpdate) {
            Word word = new Word("Hi", "你好啊");
            word.setId(getCurrentId());
            wordViewModel.updateWords(word);
        } else if (id == R.id.btnDelete) {
            Word word = new Word("Hi", "你好啊");
            word.setId(getCurrentId());
            wordViewModel.deleteWords(word);
        }

    }


    /**
     * get the current id which has been showed on textview
     * in a stupid way...
     */
    private int getCurrentId() {

        LiveData<List<Word>> liveData = wordViewModel.getAllWordsLive();

        Word word = liveData.getValue().get(0);

        return word.getId();

    }

}