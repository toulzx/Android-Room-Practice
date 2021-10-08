package com.toulzx.roompractice;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Word.class},     // 复数->集合，集合中元素用`,`隔开
        version = 1                  // 数据库版本
)

// 若有多个 Entity, 则改写多个 dao

public abstract class WordDatabase extends RoomDatabase {

    public abstract WordDao getWordDao();

}

