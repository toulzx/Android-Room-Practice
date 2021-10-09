package com.toulzx.roompractice;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// Android Studio 4.x 中使用底栏的 App Inspection 工具可查看数据库

@Database(
        entities = {Word.class},     // 复数->集合，集合中元素用`,`隔开
        version = 3                  // 数据库版本
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
//                    .fallbackToDestructiveMigration()       // 破坏式迁移，会清空原有数据
                    .addMigrations(MIGRATION_2_3)        // 使用自己创建的迁移策略
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();

    private static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 我们没有定义 table 名称，默认缺省值为类名 `word`
            // sql 中没有 boolean，因此我们用 integer 存储 `bar_data`
            // 定义 `bar_data` 的缺省值为 1
            database.execSQL("ALTER TABLE word ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 1");
            // 补充：仅 SQLite 中有 `DROP` 字段可以删除内容，删除的实现比较麻烦，需要重新建新表迁移
        }
    };

}

