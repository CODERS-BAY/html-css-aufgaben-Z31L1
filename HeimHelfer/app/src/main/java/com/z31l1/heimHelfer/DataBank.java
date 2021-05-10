package com.z31l1.heimHelfer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Measure.class}, version = 1, exportSchema = false)
public abstract class DataBank extends RoomDatabase {
    public static final String MEASURE_DB = "MeasureDb";
    public static final String TABLE_NAME_MEASURE = "Measure";
    public abstract DbDao dbDao();
}
