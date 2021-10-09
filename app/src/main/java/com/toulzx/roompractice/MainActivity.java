package com.toulzx.roompractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch aSwitch;
    private Button btnInsert, btnClear, btnUpdate, btnDelete;

    private WordViewModel wordViewModel;

    String[] english = {
            "Hello", "World", "Android", "Google", "Studio", "Project",
            "Database", "Recycler", "View", "String", "Value", "Integer"
    };
    String[] chinese = {
            "你好", "世界", "安卓系统", "谷歌公司", "工作室", "项目",
            "数据库", "回收站", "视图", "字符串", "价值", "整数类型"
    };

    private RecyclerView recyclerView;
    private MyAdapter myAdapterNormal, myAdapterCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  adapter & recycleView
        myAdapterNormal = new MyAdapter(false);
        myAdapterCard = new MyAdapter(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterNormal);

        // viewModel instantiation
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        wordViewModel.getAllWordsLive().observe(this, new Observer<>() {
            @Override
            public void onChanged(List<Word> words) {
                // 设置数据
                myAdapterNormal.setAllWords(words);
                myAdapterCard.setAllWords(words);
                // 通知它刷新
                myAdapterNormal.notifyDataSetChanged();
                myAdapterCard.notifyDataSetChanged();
            }
        });

        // bind
        aSwitch = findViewById(R.id.switcher);
        btnInsert = findViewById(R.id.btnInsert);
        btnClear = findViewById(R.id.btnClear);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        //listener
        aSwitch.setOnCheckedChangeListener(this);
        btnInsert.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

    }


    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            recyclerView.setAdapter(myAdapterCard);
        } else {
            recyclerView.setAdapter(myAdapterNormal);
        }

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
            for (int i = 0; i < english.length; i++) {
                wordViewModel.insertWords(new Word(english[i], chinese[i]));
            }
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