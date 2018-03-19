package com.kwan.base.mvp.model.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.kwan.base.common.bean.DownLoadFileBlockBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWN_LOAD_FILE_BLOCK_BEAN".
*/
public class DownLoadFileBlockBeanDao extends AbstractDao<DownLoadFileBlockBean, Long> {

    public static final String TABLENAME = "DOWN_LOAD_FILE_BLOCK_BEAN";

    /**
     * Properties of entity DownLoadFileBlockBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Url = new Property(1, String.class, "url", false, "URL");
        public final static Property Start = new Property(2, long.class, "start", false, "START");
        public final static Property End = new Property(3, long.class, "end", false, "END");
        public final static Property Finished = new Property(4, long.class, "finished", false, "FINISHED");
    }


    public DownLoadFileBlockBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DownLoadFileBlockBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWN_LOAD_FILE_BLOCK_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"URL\" TEXT," + // 1: url
                "\"START\" INTEGER NOT NULL ," + // 2: start
                "\"END\" INTEGER NOT NULL ," + // 3: end
                "\"FINISHED\" INTEGER NOT NULL );"); // 4: finished
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWN_LOAD_FILE_BLOCK_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownLoadFileBlockBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
        stmt.bindLong(3, entity.getStart());
        stmt.bindLong(4, entity.getEnd());
        stmt.bindLong(5, entity.getFinished());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownLoadFileBlockBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
        stmt.bindLong(3, entity.getStart());
        stmt.bindLong(4, entity.getEnd());
        stmt.bindLong(5, entity.getFinished());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public DownLoadFileBlockBean readEntity(Cursor cursor, int offset) {
        DownLoadFileBlockBean entity = new DownLoadFileBlockBean( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // url
            cursor.getLong(offset + 2), // start
            cursor.getLong(offset + 3), // end
            cursor.getLong(offset + 4) // finished
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownLoadFileBlockBean entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStart(cursor.getLong(offset + 2));
        entity.setEnd(cursor.getLong(offset + 3));
        entity.setFinished(cursor.getLong(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DownLoadFileBlockBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DownLoadFileBlockBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownLoadFileBlockBean entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}