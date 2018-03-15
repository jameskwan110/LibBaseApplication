package com.kwan.base.mvp.model.db;

import android.util.Log;

import com.kwan.base.BaseApplication;
import com.kwan.base.mvp.model.db.gen.DaoSession;

import java.util.List;

/**
 * Created by Mr.Kwan on 2017-8-22.
 */

public class BaseDao<T> {

	public static final String TAG = BaseDao.class.getSimpleName();
	public static final boolean DEBUG = true;
	public DaoManager manager;
	public DaoSession daoSession;

	private static BaseDao instance;

	public static BaseDao getInstance() {
		return instance;
	}

	public BaseDao() {
		instance = this;
		manager = DaoManager.getInstance();
		manager.init(BaseApplication.getInstance());
		daoSession = manager.getDaoSession();
		manager.setDebug(DEBUG);
	}

	/**************************数据库插入操作***********************/
	/**
	 * 插入单个对象
	 *
	 * @param object
	 * @return
	 */
	public long insertObject(T object) {

		try {
			return manager.getDaoSession().insert(object);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return -1;
	}

	/**
	 * 插入多个对象，并开启新的线程
	 *
	 * @param objects
	 * @return
	 */
	public boolean insertMultObject(final List<T> objects) {
		boolean flag = false;
		if (null == objects || objects.isEmpty()) {
			return false;
		}
		try {
			manager.getDaoSession().runInTx(new Runnable() {
				@Override
				public void run() {
					for (T object : objects) {
						Log.e("kwan", "insert---" + manager.getDaoSession().insertOrReplace(object));
					}
				}
			});
			flag = true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			flag = false;
		} finally {
//            manager.CloseDataBase();
		}
		return flag;
	}

	/**************************数据库更新操作***********************/
	/**
	 * 以对象形式进行数据修改
	 * 其中必须要知道对象的主键ID
	 *
	 * @param object
	 * @return
	 */
	public void updateObject(T object) {

		if (null == object) {
			return;
		}
		try {
			manager.getDaoSession().update(object);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	/**
	 * 批量更新数据
	 *
	 * @param objects
	 * @return
	 */
	public void updateMultObject(final List<T> objects, Class clz) {
		if (null == objects || objects.isEmpty()) {
			return;
		}
		try {

			daoSession.getDao(clz).updateInTx(new Runnable() {
				@Override
				public void run() {
					for (T object : objects) {
						daoSession.update(object);
					}
				}
			});
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}


	/**************************数据库删除操作***********************/
	/**
	 * 删除某个数据库表
	 *
	 * @param clz
	 * @return
	 */
	public boolean deleteAll(Class clz) {
		boolean flag = false;
		try {
			manager.getDaoSession().deleteAll(clz);
			flag = true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			flag = false;
		}
		return flag;
	}

	/**
	 * 删除某个对象
	 *
	 * @param object
	 * @return
	 */
	public void deleteObject(T object) {
		try {
			daoSession.delete(object);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	/**
	 * 异步批量删除数据
	 *
	 * @param objects
	 * @return
	 */
	public boolean deleteMultObject(final List<T> objects, Class clz) {
		boolean flag = false;
		if (null == objects || objects.isEmpty()) {
			return false;
		}
		try {

			daoSession.getDao(clz).deleteInTx(new Runnable() {
				@Override
				public void run() {
					for (T object : objects) {
						daoSession.delete(object);
					}
				}
			});
			flag = true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			flag = false;
		}
		return flag;
	}

	/**************************数据库查询操作***********************/

	/**
	 * 获得某个表名
	 *
	 * @return
	 */
	public String getTablename(Class<T> object) {
		return daoSession.getDao(object).getTablename();
	}

	/**
	 * 查询某个ID的对象是否存在
	 *
	 * @param
	 * @return
	 */
//	public boolean isExitObject(long id, Class<T> object) {
//		QueryBuilder<T> qb = (QueryBuilder<T>) daoSession.getDao(object).queryBuilder();
//		qb.where(CustomerDao.Properties.Id.eq(id));
//		long length = qb.buildCount().count();
//		return length > 0 ;
//	}

	/**
	 * 根据主键ID来查询
	 *
	 * @param id
	 * @return
	 */
	public T QueryById(long id, Class<T> object) {
		return (T) daoSession.getDao(object).loadByRowId(id);
	}

	/**
	 * 查询某条件下的对象
	 *
	 * @param object
	 * @return
	 */
	public List<T> QueryObject(Class object, String where, String... params) {
		Object obj;
		List<T> objects = null;
		try {
			obj = daoSession.getDao(object);
			if (null == obj) {
				return null;
			}
			objects = daoSession.getDao(object).queryRaw(where, params);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		return objects;
	}

	/**
	 * 查询所有对象
	 *
	 * @param object
	 * @return
	 */
	public List<T> QueryAll(Class<T> object) {
		List<T> objects = null;
		try {
			objects = (List<T>) daoSession.getDao(object).loadAll();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return objects;
	}

	/***************************关闭数据库*************************/
	/**
	 * 关闭数据库一般在Odestory中使用
	 */
	public void CloseDataBase() {
		manager.closeDataBase();
	}

}
