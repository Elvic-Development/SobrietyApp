package com.orangeelephant.sobriety;

import android.app.Application;
import android.content.Context;

import com.orangeelephant.sobriety.logging.PersistentLogger;
import com.orangeelephant.sobriety.storage.database.SobrietyDatabase;
import com.orangeelephant.sobriety.storage.database.SqlCipherKey;

/**
 * A class to store and retrieve other singletons required
 * by the application
 */
public class ApplicationDependencies {
    private static volatile Application      application;
    private static volatile SqlCipherKey     sqlcipherKey;
    private static volatile PersistentLogger logger;
    private static volatile SobrietyDatabase sobrietyDatabase;

    private ApplicationDependencies() {}

    public static void init(Application application) {
        if (ApplicationDependencies.application == null) {
            ApplicationDependencies.application = application;
        }
    }

    public static boolean isInitialised() {
        return application != null;
    }

    public static void setSqlcipherKey(SqlCipherKey sqlcipherKey) {
        ApplicationDependencies.sqlcipherKey = sqlcipherKey;
    }

    public static SqlCipherKey getSqlCipherKey() {
        if (sqlcipherKey == null) {
            throw new IllegalStateException("SQLCipherKey has not been loaded");
        }
        return sqlcipherKey;
    }


    public static PersistentLogger getLogger() {
        if (logger == null) {
            logger = new PersistentLogger();
            logger.startWriteThread();
        }
        return logger;
    }

    public static SobrietyDatabase getDatabase() {
        if (sobrietyDatabase == null) {
            sobrietyDatabase = new SobrietyDatabase(getApplicationContext());
        }
        return sobrietyDatabase;
    }

    public static Context getApplicationContext() {
        return application.getApplicationContext();
    }
}
