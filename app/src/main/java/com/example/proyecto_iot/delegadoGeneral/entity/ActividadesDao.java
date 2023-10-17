package com.example.proyecto_iot.delegadoGeneral.entity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ActividadesDao {
    @Insert
    public void insert(Actividades actividades);

    @Update
    public void update(Actividades actividades);

    @Query("delete from actividades where id=:id")
    public void delete(Integer id);

    @Query("select * from actividades")
    public List<Actividades> listarActividades();

}
