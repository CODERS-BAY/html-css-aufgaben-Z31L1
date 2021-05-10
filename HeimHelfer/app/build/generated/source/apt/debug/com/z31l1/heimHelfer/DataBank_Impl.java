package com.z31l1.heimHelfer;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class DataBank_Impl extends DataBank {
  private volatile DbDao _dbDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Measure` (`measureId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `description` TEXT, `filterTag` TEXT, `img` BLOB, `timeStamp` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"ba2da7ca9ae1c8118e3fee945c90fcca\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Measure`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsMeasure = new HashMap<String, TableInfo.Column>(6);
        _columnsMeasure.put("measureId", new TableInfo.Column("measureId", "INTEGER", true, 1));
        _columnsMeasure.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsMeasure.put("description", new TableInfo.Column("description", "TEXT", false, 0));
        _columnsMeasure.put("filterTag", new TableInfo.Column("filterTag", "TEXT", false, 0));
        _columnsMeasure.put("img", new TableInfo.Column("img", "BLOB", false, 0));
        _columnsMeasure.put("timeStamp", new TableInfo.Column("timeStamp", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeasure = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMeasure = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeasure = new TableInfo("Measure", _columnsMeasure, _foreignKeysMeasure, _indicesMeasure);
        final TableInfo _existingMeasure = TableInfo.read(_db, "Measure");
        if (! _infoMeasure.equals(_existingMeasure)) {
          throw new IllegalStateException("Migration didn't properly handle Measure(com.z31l1.heimHelfer.Measure).\n"
                  + " Expected:\n" + _infoMeasure + "\n"
                  + " Found:\n" + _existingMeasure);
        }
      }
    }, "ba2da7ca9ae1c8118e3fee945c90fcca");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Measure");
  }

  @Override
  public DbDao dbDao() {
    if (_dbDao != null) {
      return _dbDao;
    } else {
      synchronized(this) {
        if(_dbDao == null) {
          _dbDao = new DbDao_Impl(this);
        }
        return _dbDao;
      }
    }
  }
}
