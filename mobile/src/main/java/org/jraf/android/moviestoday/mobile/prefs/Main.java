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
package org.jraf.android.moviestoday.mobile.prefs;

import org.jraf.android.prefs.DefaultBoolean;
import org.jraf.android.prefs.DefaultString;
import org.jraf.android.prefs.Prefs;

@Prefs
public class Main {
    /**
     * Theater id.
     */
    @DefaultString("C2954")
    String theaterId;

    /**
     * Theater name.
     */
    @DefaultString("MK2 Bibliothèque")
    String theaterName;

    /**
     * Theater address.
     */
    @DefaultString("128-162 avenue de France\\n75013 Paris 13e arrondissement")
    String theaterAddress;

    /**
     * Last time an update was successfully called.
     */
    Long lastUpdateDate;

    /**
     * Show a notification on new movie release day.
     */
    @DefaultBoolean(true)
    Boolean showNewReleasesNotification;

}