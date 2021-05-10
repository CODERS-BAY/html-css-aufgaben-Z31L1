package com.z31l1.heimHelfer;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = DataBank.TABLE_NAME_MEASURE)
public class Measure implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int measureId;

    public String name;

    public String description;

    public String filterTag;

    public byte[] img;

    public String timeStamp;

}
