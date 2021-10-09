package com.toulzx.roompractice;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "english_word")
    private String word;
    @ColumnInfo(name = "chinese_meaning")
    private String chineseMeaning;
    @ColumnInfo(name = "chinese_invisible")
    private boolean chineseInvisible;

    // `Alt` + `Insert` => Generate

    // id 不需要，系统会自动生成
    public Word(String word, String chineseMeaning) {
        this.word = word;
        this.chineseMeaning = chineseMeaning;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public boolean isChineseInvisible() { return chineseInvisible; }

    // Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
    }

    public void setChineseInvisible(boolean chineseInvisible) { this.chineseInvisible = chineseInvisible; }

}
