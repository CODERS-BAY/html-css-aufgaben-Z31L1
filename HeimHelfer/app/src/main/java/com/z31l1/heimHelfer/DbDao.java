package com.z31l1.heimHelfer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DbDao {

    @Insert
    long insertMeasure(Measure measure);

    @Insert
    void insertMeasureList(List<Measure> measureList);

    @Query("SELECT * FROM " + DataBank.TABLE_NAME_MEASURE + " WHERE NOT filterTag LIKE 'Archiv'")
    List<Measure> fetchAllMeasures();

    @Query("SELECT * FROM " + DataBank.TABLE_NAME_MEASURE + " WHERE filterTag = :filterTag")
    List<Measure> fetchMeasureListByFilterTag(String filterTag);

    @Query("SELECT * FROM " + DataBank.TABLE_NAME_MEASURE + " WHERE measureId = :measureId")
    Measure fetchMeasureListById(int measureId);

    @Query("SELECT * FROM " + DataBank.TABLE_NAME_MEASURE + " WHERE name = :name")
    List<Measure> fetchMeasureListByName(String name);

    @Query("SELECT name FROM " + DataBank.TABLE_NAME_MEASURE)
    String[] fetchMeasureName();

    @Update
    int updateMeasure(Measure measure);

    @Delete
    int deleteMeasure(Measure measure);
}