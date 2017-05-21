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
package org.jraf.android.cinetoday.network.api.codec.showtime

import org.jraf.android.cinetoday.model.ParseException
import org.jraf.android.cinetoday.model.movie.Movie
import org.jraf.android.cinetoday.model.movie.Showtime
import org.jraf.android.util.log.Log
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ShowtimeCodec {

    @Throws(ParseException::class)
    fun fill(movie: Movie, jsonMovieShowtime: JSONObject, theaterId: String, date: Date) {
        // Example input:
        // Séances du dimanche 1 novembre 2015 : 10:00 (film à 10:15), 14:00 (film à 14:15), 16:00 (film à 16:15), 20:00 (film à 20:15), 21:45 (film à 22:00)
        try {
            val display = jsonMovieShowtime.getString("display")

            // Split per day
            val showtimesDays = display.split("\r\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

            // Find today's date in the list
            val todayFormatted = DAY_DATE_FORMAT.format(date)
            var todayShowtimesStr: String? = null
            for (showtimesDay in showtimesDays) {
                if (showtimesDay.contains(todayFormatted)) {
                    // Found
                    todayShowtimesStr = showtimesDay
                    break
                }
            }
            if (todayShowtimesStr == null) {
                Log.w("Could not find today in showtime days")
                return
            }

            // Is 3D?
            val screenFormat = jsonMovieShowtime.getJSONObject("screenFormat")
            val screenFormatStr = screenFormat.getString("$")
            val is3d = "3D".equals(screenFormatStr, ignoreCase = true)

            // Remove first part
            todayShowtimesStr = todayShowtimesStr.substring(todayShowtimesStr.indexOf(':') + 2)

            // Split times
            val todayShowtimesStrElem = todayShowtimesStr.split(", ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

            // Clean up
            for (i in todayShowtimesStrElem.indices) {
                var todayShowtime = todayShowtimesStrElem[i]
                val spaceIdx = todayShowtime.indexOf(' ')
                if (spaceIdx != -1) {
                    todayShowtime = todayShowtime.substring(0, spaceIdx)
                }
                todayShowtimesStrElem[i] = todayShowtime
            }

            // Make a set and merge it with the previous one if any
            val todayShowtimesSet = TreeSet<Showtime>()
            for (timeStr in todayShowtimesStrElem) {
                val showtime = Showtime()
                showtime.is3d = is3d
                showtime.time = stringTimeToDate(timeStr)
                todayShowtimesSet.add(showtime)
            }
            if (movie.todayShowtimes == null) {
                movie.todayShowtimes = TreeMap<String, List<Showtime>>()
            }
            val showTimesForThisTheater = movie.todayShowtimes!![theaterId]
            if (showTimesForThisTheater != null) {
                // The movie already has showtimes for *this* theater: merge them into the new ones
                todayShowtimesSet.addAll(showTimesForThisTheater)
            }
            val todayShowtimesList = ArrayList(todayShowtimesSet)
            movie.todayShowtimes!!.put(theaterId, todayShowtimesList)

        } catch (e: JSONException) {
            throw ParseException(e)
        }

    }

    companion object {
        private val DAY_DATE_FORMAT = SimpleDateFormat("d MMMM", Locale.FRENCH)

        private fun stringTimeToDate(time: String): Date {
            val split = time.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val hourStr = split[0]
            val minuteStr = split[1]
            val hour = Integer.valueOf(hourStr)!!
            val minute = Integer.valueOf(minuteStr)!!
            return Date(TimeUnit.HOURS.toMillis(hour!!.toLong()) + TimeUnit.MINUTES.toMillis(minute!!.toLong()))
        }
    }
}