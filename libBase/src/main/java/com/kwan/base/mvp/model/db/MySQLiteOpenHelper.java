package com.kwan.base.mvp.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kwan.base.mvp.model.db.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Growth on 2016/3/3.
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

	public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory);
	}

	@Override
	public void onUpgrade(Database db, int oldVersion, int newVersion) {

		Log.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
		if (oldVersion < newVersion) {
			Log.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);

//			MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
//						@Override
//						public void onCreateAllTables(Database db, boolean ifNotExists) {
//							DaoMaster.createAllTables(db, ifNotExists);
//						}
//
//						@Override
//						public void onDropAllTables(Database db, boolean ifExists) {
//							DaoMaster.dropAllTables(db, ifExists);
//						}
//					}, BVEffectDao.class, BVFragmentDao.class, BVResourcesDao.class, BVStickerDao.class
//					, BVTemplateDao.class, FragmentJoinStickerDao.class);

			//更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
//             MigrationHelper.getInstance().migrate(db, UserDao.class,XXDao.class);
		}
	}
}
