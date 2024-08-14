package com.pnuppp.pplusplus;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase { // Room이 실제 DB 구현체를 생성
    public abstract EventDao eventDao();
}
