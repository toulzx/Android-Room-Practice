package com.toulzx.roompractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DATABASE_FILE_NAME = "word_database";

    private WordDatabase wordDatabase;
    private WordDao wordDao;

    private TextView textView;
    private Button btnInsert, btnClear, btnUpdate, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // database
        wordDatabase = Room.databaseBuilder(this, WordDatabase.class, DATABASE_FILE_NAME)
                .allowMainThreadQueries()                   // 强制允许在主线程运行（真正项目不要这样做）
                .build();
        wordDao = wordDatabase.getWordDao();

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

        // update textview
        updateView();

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
            wordDao.insertWords(word1, word2);
            updateView();
        } else if (id == R.id.btnClear) {
            wordDao.deleteAllWords();
            updateView();
        } else if (id == R.id.btnUpdate) {
            Word word = new Word("Hi", "你好啊");
            word.setId(getCurrentId());
            wordDao.updateWords(word);
            updateView();
        } else if (id == R.id.btnDelete) {
            Word word = new Word("Hi", "你好啊");
            word.setId(getCurrentId());
            wordDao.deleteWords(word);
            updateView();
        }

    }


    /**
     * Update textView
     */
    private void updateView() {

        List<Word> list = wordDao.getAllWords();

        String text = "";

        for (int i = 0; i < list.size(); i++) {
            Word word = list.get(i);
            text += word.getId() + ":" + word.getWord() + "=" + word.getChineseMeaning() + "\n";
        }

        textView.setText(text);

    }


    /**
     * get the current id which has been showed on textview
     * in a stupid way...
     */
    private int getCurrentId() {

        List<Word> list = wordDao.getAllWords();

        Word word = list.get(0);

        return word.getId();

    }
}