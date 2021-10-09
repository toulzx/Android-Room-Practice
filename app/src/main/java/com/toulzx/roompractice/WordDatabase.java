package com.toulzx.roompractice;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Android Studio 4.x 中使用底栏的 App Inspection 工具可查看数据库

@Database(
        entities = {Word.class},     // 复数->集合，集合中元素用`,`隔开
        version = 2                  // 数据库版本
)

// 若有多个 Entity, 则改写多个 dao

public abstract class WordDatabase extends RoomDatabase {

    public static final String DATABASE_FILE_NAME = "word_database";

    private static WordDatabase INSTANCE;

    // singleton: 懒汉单例模式，因为 database 实例化消耗较大资源
    // synchronized 保证多进程运行时排队进行，而不冲突
    public static synchronized WordDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    WordDatabase.class,
                    DATABASE_FILE_NAME)
                    .fallbackToDestructiveMigration()       // 破坏式迁移，会清空原有数据
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();
}

