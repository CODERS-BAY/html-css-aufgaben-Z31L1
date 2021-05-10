package com.z31l1.heimHelfer;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class DbDao_Impl implements DbDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMeasure;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfMeasure;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfMeasure;

  public DbDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMeasure = new EntityInsertionAdapter<Measure>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Measure`(`measureId`,`name`,`description`,`filterTag`,`img`,`timeStamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Measure value) {
        stmt.bindLong(1, value.measureId);
        if (value.name == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.name);
        }
        if (value.description == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.description);
        }
        if (value.filterTag == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.filterTag);
        }
        if (value.img == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindBlob(5, value.img);
        }
        if (value.timeStamp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.timeStamp);
        }
      }
    };
    this.__deletionAdapterOfMeasure = new EntityDeletionOrUpdateAdapter<Measure>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Measure` WHERE `measureId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Measure value) {
        stmt.bindLong(1, value.measureId);
      }
    };
    this.__updateAdapterOfMeasure = new EntityDeletionOrUpdateAdapter<Measure>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Measure` SET `measureId` = ?,`name` = ?,`description` = ?,`filterTag` = ?,`img` = ?,`timeStamp` = ? WHERE `measureId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Measure value) {
        stmt.bindLong(1, value.measureId);
        if (value.name == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.name);
        }
        if (value.description == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.description);
        }
        if (value.filterTag == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.filterTag);
        }
        if (value.img == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindBlob(5, value.img);
        }
        if (value.timeStamp == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.timeStamp);
        }
        stmt.bindLong(7, value.measureId);
      }
    };
  }

  @Override
  public long insertMeasure(Measure measure) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfMeasure.insertAndReturnId(measure);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMeasureList(List<Measure> measureList) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMeasure.insert(measureList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int deleteMeasure(Measure measure) {
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfMeasure.handle(measure);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int updateMeasure(Measure measure) {
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfMeasure.handle(measure);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Measure> fetchAllMeasures() {
    final String _sql = "SELECT * FROM Measure WHERE NOT filterTag LIKE 'Archiv'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMeasureId = _cursor.getColumnIndexOrThrow("measureId");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfFilterTag = _cursor.getColumnIndexOrThrow("filterTag");
      final int _cursorIndexOfImg = _cursor.getColumnIndexOrThrow("img");
      final int _cursorIndexOfTimeStamp = _cursor.getColumnIndexOrThrow("timeStamp");
      final List<Measure> _result = new ArrayList<Measure>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Measure _item;
        _item = new Measure();
        _item.measureId = _cursor.getInt(_cursorIndexOfMeasureId);
        _item.name = _cursor.getString(_cursorIndexOfName);
        _item.description = _cursor.getString(_cursorIndexOfDescription);
        _item.filterTag = _cursor.getString(_cursorIndexOfFilterTag);
        _item.img = _cursor.getBlob(_cursorIndexOfImg);
        _item.timeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Measure> fetchMeasureListByFilterTag(String filterTag) {
    final String _sql = "SELECT * FROM Measure WHERE filterTag = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (filterTag == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterTag);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMeasureId = _cursor.getColumnIndexOrThrow("measureId");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfFilterTag = _cursor.getColumnIndexOrThrow("filterTag");
      final int _cursorIndexOfImg = _cursor.getColumnIndexOrThrow("img");
      final int _cursorIndexOfTimeStamp = _cursor.getColumnIndexOrThrow("timeStamp");
      final List<Measure> _result = new ArrayList<Measure>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Measure _item;
        _item = new Measure();
        _item.measureId = _cursor.getInt(_cursorIndexOfMeasureId);
        _item.name = _cursor.getString(_cursorIndexOfName);
        _item.description = _cursor.getString(_cursorIndexOfDescription);
        _item.filterTag = _cursor.getString(_cursorIndexOfFilterTag);
        _item.img = _cursor.getBlob(_cursorIndexOfImg);
        _item.timeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Measure fetchMeasureListById(int measureId) {
    final String _sql = "SELECT * FROM Measure WHERE measureId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, measureId);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMeasureId = _cursor.getColumnIndexOrThrow("measureId");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfFilterTag = _cursor.getColumnIndexOrThrow("filterTag");
      final int _cursorIndexOfImg = _cursor.getColumnIndexOrThrow("img");
      final int _cursorIndexOfTimeStamp = _cursor.getColumnIndexOrThrow("timeStamp");
      final Measure _result;
      if(_cursor.moveToFirst()) {
        _result = new Measure();
        _result.measureId = _cursor.getInt(_cursorIndexOfMeasureId);
        _result.name = _cursor.getString(_cursorIndexOfName);
        _result.description = _cursor.getString(_cursorIndexOfDescription);
        _result.filterTag = _cursor.getString(_cursorIndexOfFilterTag);
        _result.img = _cursor.getBlob(_cursorIndexOfImg);
        _result.timeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Measure> fetchMeasureListByName(String name) {
    final String _sql = "SELECT * FROM Measure WHERE name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMeasureId = _cursor.getColumnIndexOrThrow("measureId");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfFilterTag = _cursor.getColumnIndexOrThrow("filterTag");
      final int _cursorIndexOfImg = _cursor.getColumnIndexOrThrow("img");
      final int _cursorIndexOfTimeStamp = _cursor.getColumnIndexOrThrow("timeStamp");
      final List<Measure> _result = new ArrayList<Measure>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Measure _item;
        _item = new Measure();
        _item.measureId = _cursor.getInt(_cursorIndexOfMeasureId);
        _item.name = _cursor.getString(_cursorIndexOfName);
        _item.description = _cursor.getString(_cursorIndexOfDescription);
        _item.filterTag = _cursor.getString(_cursorIndexOfFilterTag);
        _item.img = _cursor.getBlob(_cursorIndexOfImg);
        _item.timeStamp = _cursor.getString(_cursorIndexOfTimeStamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public String[] fetchMeasureName() {
    final String _sql = "SELECT name FROM Measure";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final String[] _result = new String[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
