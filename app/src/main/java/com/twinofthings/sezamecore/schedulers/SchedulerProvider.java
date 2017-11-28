package com.twinofthings.sezamecore.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 *         <p>
 *         Provides different types of schedulers.
 */
public class SchedulerProvider implements BaseSchedulerProvider {
    private static SchedulerProvider instance;

    @Override
    @NonNull
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    @NonNull
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    @NonNull
    public Scheduler ui() {
//        return AndroidSchedulers.mainThread();
        return null;
    }

    public static BaseSchedulerProvider instance() {
        if (instance == null) {
            instance = new SchedulerProvider();
        }
        return instance;
    }
}
