package com.p8.common.mvvm.model

import android.database.Cursor
import com.p8.common.App
import com.p8.common.db.DBManager
import com.p8.common.net.RetrofitHelper
import com.p8.common.net.RxAdapter
import io.reactivex.Observable
import org.greenrobot.greendao.Property
import org.greenrobot.greendao.query.WhereCondition

/**
 *  @author : WX.Y
 *  date : 2021/3/23 11:16
 *  description :
 */
open class BaseModel(
    var app: App = App.getInstance(),
    var dbManager: DBManager = DBManager.getInstance(),
    var netManager: RetrofitHelper = RetrofitHelper.getInstance()
) {

    /**
     * 条件查询
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T> list(cls: Class<T>?): Observable<List<T>?>? {
        return dbManager.list(cls, 0, 0, null, null, null)
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun <T> list(
        cls: Class<T>?,
        page: Int,
        pageSize: Int
    ): Observable<List<T>?>? {
        return dbManager.list(cls, page, pageSize, null, null, null)
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun <T> listDesc(
        cls: Class<T>?,
        page: Int,
        pageSize: Int,
        desc: Property?
    ): Observable<List<T>?>? {
        return dbManager.list(cls, page, pageSize, null, desc, null)
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun <T> listDesc(
        cls: Class<T>?, page: Int, pageSize: Int, desc: Property?,
        cond: WhereCondition?, vararg condMore: WhereCondition?
    ): Observable<List<T>?>? {
        return dbManager.list(cls, page, pageSize, null, desc, cond, *condMore)
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun <T> list(
        cls: Class<T>?,
        cond: WhereCondition?,
        vararg condMore: WhereCondition?
    ): Observable<List<T>?>? {
        return dbManager.list(cls, 0, 0, null, null, cond, *condMore)
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }


    fun rawQuery(
        sql: String?,
        selectionArgs: Array<String?>?
    ): Observable<Cursor?>? {
        return dbManager.rawQuery(sql, selectionArgs)
            .compose(RxAdapter.exceptionTransformer<Cursor>())
            .compose(RxAdapter.schedulersTransformer<Cursor>())
    }

    /**
     * 清空所有记录
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T> clearAll(cls: Class<T>?): Observable<Boolean?>? {
        return dbManager.clearAll(cls)
            .compose(RxAdapter.exceptionTransformer<Boolean>())
            .compose(RxAdapter.schedulersTransformer<Boolean>())
    }

    /**
     * 删除一条记录
     *
     * @param <T>
     * @return
    </T> */
    fun <T, K> remove(
        cls: Class<T>?,
        key: K
    ): Observable<Boolean?>? {
        return dbManager.remove(cls, key)
            .compose(RxAdapter.exceptionTransformer<Boolean>())
            .compose(RxAdapter.schedulersTransformer<Boolean>())
    }

    /**
     * 更新或插入一条记录
     *
     * @param entity
     * @param <T>
     * @return
    </T> */
    fun <T> insert(entity: T): Observable<T>? {
        return dbManager.insert(entity)
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun getSPString(key: String?): Observable<String?>? {
        return dbManager.getSPString(key)
            .compose(RxAdapter.exceptionTransformer<String>())
            .compose(RxAdapter.schedulersTransformer<String>())
    }

    fun getSPString(
        key: String?,
        defaultValue: String?
    ): Observable<String?>? {
        return dbManager.getSPString(key, defaultValue)
            .compose(RxAdapter.exceptionTransformer<String>())
            .compose(RxAdapter.schedulersTransformer<String>())
    }
}