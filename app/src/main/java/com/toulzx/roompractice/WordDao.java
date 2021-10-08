package com.toulzx.roompractice;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// 编辑器编译时根据 `@` 自动生成一些方法

@Dao        // Dao: Database access object 访问数据库操作的接口
public interface WordDao {

    @Insert
    void insertWords(Word... word);     // `...`表示可以插入多个参数

    @Update
    void updateWords(Word... word);

    @Delete
    void deleteWords(Word... word);

    @Query("DELETE FROM WORD")
    void deleteAllWords();

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    List<Word> getAllWords();

}
