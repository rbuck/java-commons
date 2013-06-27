/*
 * Copyright 2010 Robert J. Buck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buck.commons.i18n;

import java.util.Date;
import java.util.TimeZone;

/**
 * Provides basic date and time support.
 *
 * @author Robert J. Buck
 */
public class DateTime {

    /**
     * Gets the total default timezone offset, taking into account the raw
     * offset and the daylight savings time offset.
     *
     * @param date the date from which to calculate the dst offset relative to
     * @return the total default timezone offset
     */
    public static long getDefaultTimeZoneOffset(Date date) {
        return getTimeZoneOffset(date, TimeZone.getDefault());
    }

    /**
     * Gets the total timezone offset, taking into account the raw offset and
     * the daylight savings time offset.
     *
     * @param date     the date from which to calculate the dst offset relative
     *                 to
     * @param timeZone the timezone to get the total offset for
     * @return the total timezone offset
     */
    public static long getTimeZoneOffset(Date date, TimeZone timeZone) {
        long dstSavings = 0;
        if (timeZone.inDaylightTime(date)) {
            dstSavings = timeZone.getDSTSavings();
        }
        return timeZone.getRawOffset() + dstSavings;
    }
}
