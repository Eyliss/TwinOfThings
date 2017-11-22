package com.twinofthings.sezamecore.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 *         <p>
 *         Allow providing different types of {@link Scheduler}s.
 */
public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
