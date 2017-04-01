package com.android.base.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.android.base.domain.DaoTest;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig daoTestDaoConfig;

    private final DaoTestDao daoTestDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        daoTestDaoConfig = daoConfigMap.get(DaoTestDao.class).clone();
        daoTestDaoConfig.initIdentityScope(type);

        daoTestDao = new DaoTestDao(daoTestDaoConfig, this);

        registerDao(DaoTest.class, daoTestDao);
    }
    
    public void clear() {
        daoTestDaoConfig.clearIdentityScope();
    }

    public DaoTestDao getDaoTestDao() {
        return daoTestDao;
    }

}
