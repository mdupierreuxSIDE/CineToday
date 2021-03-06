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
package org.jraf.android.cinetoday.mobile.app.theater.search;

import java.util.List;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jraf.android.cinetoday.R;
import org.jraf.android.cinetoday.common.model.theater.Theater;
import org.jraf.android.cinetoday.databinding.TheaterSearchListBinding;
import org.jraf.android.util.app.base.BaseFragment;

public class TheaterSearchFragment extends BaseFragment<TheaterCallbacks> implements LoaderManager.LoaderCallbacks<List<Theater>> {
    private TheaterAdapter mAdapter;
    private TheaterSearchListBinding mBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.theater_search_list, container, false);
        mBinder.rclList.setHasFixedSize(true);
        mBinder.rclList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBinder.txtEmpty.setVisibility(View.GONE);
        mBinder.pgbLoading.setVisibility(View.GONE);
        return mBinder.getRoot();
    }

    public void search(String query) {
        if (mAdapter != null) mAdapter.clear();

        if (query.length() == 0) {
            mBinder.pgbLoading.setVisibility(View.GONE);
            mBinder.txtEmpty.setVisibility(View.GONE);
        } else {
            mBinder.pgbLoading.setVisibility(View.VISIBLE);
            mBinder.txtEmpty.setVisibility(View.GONE);

            Bundle args = new Bundle();
            args.putString("query", query);
            getLoaderManager().restartLoader(0, args, this);
        }
    }


    //--------------------------------------------------------------------------
    // region LoaderCallbacks.
    //--------------------------------------------------------------------------

    @Override
    public Loader<List<Theater>> onCreateLoader(int id, Bundle args) {
        String query = args.getString("query");
        return new TheaterLoader(getActivity(), query);
    }

    @Override
    public void onLoadFinished(Loader<List<Theater>> loader, List<Theater> data) {
        mBinder.pgbLoading.setVisibility(View.GONE);

        if (mAdapter == null) {
            mAdapter = new TheaterAdapter(getActivity(), getCallbacks());
            mBinder.rclList.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
        }

        boolean empty = data == null || data.isEmpty();
        if (empty) {
            mBinder.txtEmpty.setVisibility(View.VISIBLE);
        } else {
            mBinder.txtEmpty.setVisibility(View.GONE);
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Theater>> loader) {
        mAdapter.clear();
    }

    // endregion
}
