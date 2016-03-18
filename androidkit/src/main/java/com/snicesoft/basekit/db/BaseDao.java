package com.snicesoft.basekit.db;

import android.content.Context;

import com.google.gson.internal.$Gson$Types;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zhuzhe on 16/3/18.
 */
public class BaseDao<M> {
    protected DbUtils dbUtils;
    protected Context context;

    public BaseDao(Context context) {
        this.context = context;
        this.dbUtils = DbUtils.create(context, "snicesoft.db");
    }

    public BaseDao(Context context, String dbName) {
        this.context = context;
        this.dbUtils = DbUtils.create(context, dbName);
    }

    protected Class<?> getMClass() {
        return $Gson$Types.getRawType(getType(0));
    }

    public M findById(final Object id) {
        M m = null;
        try {
            m = (M) dbUtils.findById(getMClass(), id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return m;
    }

    public List<M> findAll() {
        List<M> list = null;
        try {
            list = (List<M>) dbUtils.findAll(getMClass());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<M> findAll(Selector selector) {
        List<M> list = null;
        try {
            list = (List<M>) dbUtils.findAll(selector);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public M findFirst() {
        M m = null;
        try {
            m = (M) dbUtils.findFirst(getMClass());
        } catch (DbException e) {
            e.printStackTrace();
        }
        return m;
    }

    public M findFirst(Selector selector) {
        M m = null;
        try {
            m = (M) dbUtils.findFirst(selector);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return m;
    }

    public void save(final M m) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.save(m);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveAll(final List<M> list) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.saveAll(list);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveOrUpdate(final M m) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.saveOrUpdate(m);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveOrUpdate(final List<M> list) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.saveOrUpdate(list);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void delete(final M m) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.delete(m);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void delete(final WhereBuilder whereBuilder) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.delete(getMClass(), whereBuilder);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteAll(final List<M> list) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.deleteAll(list);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteAll() {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.deleteAll(getMClass());
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void update(final M m, final WhereBuilder whereBuilder) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.update(m, whereBuilder);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void update(final M m, final String... colume) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.update(m, colume);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateAll(final List<M> list, final WhereBuilder whereBuilder) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.updateAll(list, whereBuilder);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateAll(final List<M> list, final String... colume) {
        startExe(new Runnable() {
            @Override
            public void run() {
                try {
                    dbUtils.updateAll(list, colume);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Type getType(int index) {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[index]);
    }

    protected void startExe(Runnable runnable) {
        new Thread(runnable).start();
    }
}
