/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2015 Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jraf.android.moviestoday.mobile.app.api;

import org.jraf.android.util.listeners.Listeners;

public class LoadMoviesHelper implements LoadMoviesListener {
    private static final LoadMoviesHelper INSTANCE = new LoadMoviesHelper();

    private boolean mStarted;
    private Listeners<LoadMoviesListener> mListeners = new Listeners<>();

    private LoadMoviesHelper() {
        mListeners.setNewListenerDispatcher(new Listeners.Dispatcher<LoadMoviesListener>() {
            @Override
            public void dispatch(LoadMoviesListener listener) {
                if (mStarted) {
                    listener.onLoadMoviesStarted();
                } else {
                    listener.onLoadMoviesFinished();
                }
            }
        });
    }

    public static LoadMoviesHelper get() {
        return INSTANCE;
    }

    public void addListener(LoadMoviesListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(LoadMoviesListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onLoadMoviesStarted() {
        mStarted = true;
        mListeners.dispatch(new Listeners.Dispatcher<LoadMoviesListener>() {
            @Override
            public void dispatch(LoadMoviesListener listener) {
                listener.onLoadMoviesStarted();
            }
        });
    }

    @Override
    public void onLoadMoviesProgress(final int currentMovie, final int totalMovies) {
        mListeners.dispatch(new Listeners.Dispatcher<LoadMoviesListener>() {
            @Override
            public void dispatch(LoadMoviesListener listener) {
                listener.onLoadMoviesProgress(currentMovie, totalMovies);
            }
        });
    }

    @Override
    public void onLoadMoviesFinished() {
        mStarted = false;
        mListeners.dispatch(new Listeners.Dispatcher<LoadMoviesListener>() {
            @Override
            public void dispatch(LoadMoviesListener listener) {
                listener.onLoadMoviesFinished();
            }
        });
    }
}
