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
package org.jraf.android.cinetoday.wear.app.configure;

import android.app.IntentService;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;

import org.jraf.android.cinetoday.R;
import org.jraf.android.cinetoday.common.wear.WearHelper;
import org.jraf.android.util.log.Log;
import org.jraf.android.util.string.StringUtil;

public class ConfigureIntentService extends IntentService {
    public static final String ACTION_CONFIGURE = "ACTION_CONFIGURE";

    public ConfigureIntentService() {
        super(ConfigureIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("intent=%s", StringUtil.toString(intent));

        String action = intent.getAction();
        switch (action) {
            case ACTION_CONFIGURE:
                WearHelper wearHelper = WearHelper.get();
                wearHelper.connect(this);
                wearHelper.sendMessageOpenConfigureActivity();
                wearHelper.disconnect();
                showOpenOnPhoneAnimation();
                break;
        }
    }

    private void showOpenOnPhoneAnimation() {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.configure_openedOnPhone));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
