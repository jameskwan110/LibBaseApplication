package com.kwan.base.mvp.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kwan.base.mvp.model.db.gen.DaoMaster;
import com.kwan.base.mvp.model.db.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 进行数据库的管理
 * 1.创建数据库
 * 2.创建数据库表
 * 3.对数据库进行增删查改
 * 4.对数据库进行升级
 * Created by Mr.Kwan on 2017-8-22.
 */

public class DaoManager {

	private static final String TAG = DaoManager.class.getSimpleName();
	private static final String DB_NAME = "menyabv.db";//数据库名称
	private volatile static DaoManager mDaoManager;//多线程访问
	private static MySQLiteOpenHelper mHelper;
	private static DaoMaster mDaoMaster;
	private static DaoSession mDaoSession;
	public static SQLiteDatabase db;
	private Context context;

	/**
	 * 使用单例模式获得操作数据库的对象
	 *
	 * @return
	 */
	public static DaoManager getInstance() {

		if (mDaoManager == null) {
			synchronized (DaoManager.class) {
				mDaoManager = new DaoManager();
			}
		}
		return mDaoManager;
	}

	/**
	 * 初始化Context对象
	 *
	 * @param context
	 */
	public void init(Context context) {
		this.context = context;
	}

	/**
	 * 判断数据库是否存在，如果不存在则创建
	 *
	 * @return
	 */
	public DaoMaster getDaoMaster() {
		if (null == mDaoMaster) {
			MigrationHelper.DEBUG = true;
			mHelper = new MySQLiteOpenHelper(context, DB_NAME, null);
			mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
			db = mHelper.getWritableDatabase();
			//获取数据库对象
		}
		return mDaoMaster;
	}

	/**
	 * 完成对数据库的增删查找
	 *
	 * @return
	 */
	public DaoSession getDaoSession() {
		if (null == mDaoSession) {
			if (null == mDaoMaster) {
				mDaoMaster = getDaoMaster();
			}
			mDaoSession = mDaoMaster.newSession();
		}
		return mDaoSession;
	}

	/**
	 * 设置debug模式开启或关闭，默认关闭
	 *
	 * @param flag
	 */
	public void setDebug(boolean flag) {
		QueryBuilder.LOG_SQL = flag;
		QueryBuilder.LOG_VALUES = flag;
	}

	/**
	 * 关闭数据库
	 */
	public void closeDataBase() {
		closeHelper();
		closeDaoSession();
	}

	public void closeDaoSession() {
		if (null != mDaoSession) {
			mDaoSession.clear();
			mDaoSession = null;
		}
	}

	public void closeHelper() {
		if (mHelper != null) {
			mHelper.close();
			mHelper = null;
		}
	}
}
