package com.toulzx.roompractice;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(
        entities = {Word.class},     // 复数->集合，集合中元素用`,`隔开
        version = 1                  // 数据库版本
)

// 若有多个 Entity, 则改写多个 dao

public abstract class WordDatabase extends RoomDatabase {

    private static WordDatabase INSTANCE;

    // singleton: 懒汉单例模式，因为 database 实例化消耗较大资源
    // synchronized 保证多进程运行时排队进行，而不冲突
    public static synchronized WordDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    WordDatabase.class,
                    MainActivity.DATABASE_FILE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();

}

