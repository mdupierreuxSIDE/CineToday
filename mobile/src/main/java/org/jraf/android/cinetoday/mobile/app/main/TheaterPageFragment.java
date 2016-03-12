/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2016 Benoit 'BoD' Lubek (BoD@JRAF.org)
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
package org.jraf.android.cinetoday.mobile.app.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jraf.android.cinetoday.R;
import org.jraf.android.cinetoday.mobile.provider.theater.TheaterCursor;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TheaterPageFragment extends Fragment {
    @Bind(R.id.fabPickTheater)
    protected FloatingActionButton mFabPickTheater;

    @Bind(R.id.txtTheaterName)
    protected TextView mTxtTheaterName;

    @Bind(R.id.txtTheaterAddress)
    protected TextView mTxtTheaterAddress;

    @Bind(R.id.imgTheaterPicture)
    protected ImageView mImgTheaterPicture;

    public static Fragment newInstance(TheaterCursor theaterCursor) {
        TheaterPageFragment res = new TheaterPageFragment();
        Bundle args = new Bundle();
        args.putString("name", theaterCursor.getName());
        args.putString("address", theaterCursor.getAddress());
        args.putString("pictureUri", theaterCursor.getPictureUri());
        res.setArguments(args);
        return res;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.page_theater, container, false);
        ButterKnife.bind(this, res);
        Bundle args = getArguments();
        mTxtTheaterName.setText(args.getString("name"));
        mTxtTheaterAddress.setText(args.getString("address"));
        String pictureUri = args.getString("pictureUri");
        Picasso.with(getContext()).load(pictureUri).placeholder(R.drawable.theater_list_item_placeholder).error(
                R.drawable.theater_list_item_placeholder).fit().centerCrop().noFade().into(mImgTheaterPicture);
        return res;
    }

    @OnClick(R.id.fabPickTheater)
    protected void onPickTheaterClicked() {
//        Intent intent = new Intent(this, TheaterSearchActivity.class);
//        intent.putExtra(TheaterSearchActivity.EXTRA_FIRST_USE, !MainPrefs.get(this).containsTheaterId());
//        startActivityForResult(intent, REQUEST_PICK_THEATER);
    }

    @OnClick(R.id.btnNavigate)
    protected void onNavigateClick() {
        String address = getArguments().getString("address");
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException ignored) {}
        Uri uri = Uri.parse("http://maps.google.com/maps?f=d&daddr=" + address);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.btnWebSite)
    protected void onWebSiteClick() {
        String name = getArguments().getString("name");
        // Try to improve "I'm feeling ducky" results
        name = "cinema " + name;
        try {
            name = URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException ignored) {}
        Uri uri = Uri.parse("https://www.google.com/search?sourceid=navclient&btnI=I&q=" + name);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
