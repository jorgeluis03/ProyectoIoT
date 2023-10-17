package com.example.proyecto_iot.delegadoGeneral.entity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Actividades.class},version = 1)
public abstract class ActividadesDataBase extends RoomDatabase {
    public abstract ActividadesDao actividadesDao();
    public static ActividadesDataBase INSTANCE;
    public static ActividadesDataBase getDataBase(Context context){
        if(INSTANCE ==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ActividadesDataBase.class,"iot")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
