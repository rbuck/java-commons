/*
 * Copyright 2010-2013 Robert J. Buck
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Provides conversions for ISO-8601 Extended Notation. The standard notation
 * supported by this class is "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'". An example date
 * formatted using this pattern is "2006-11-15T19:12:56.235Z".
 *
 * @author Robert J. Buck
 */
public class ISO8601DateTime {
    /**
     * The standard format pattern for ISO-8601 Extended Notation.
     */
    public static final String ISO_8601_EXTENDED_PATTERN_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Thread specific singleton of date formatter.
     */
    private static final ThreadLocal<SimpleDateFormat> tssStandardDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat(ISO_8601_EXTENDED_PATTERN_Z);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format;
        }
    };

    /**
     * Converts an ISO 8601 DateTime to a Java date object.
     *
     * @param iso8601Date the extended ISO 8601 date string
     * @return a Date object set to the specified time
     * @throws ParseException if the iso8601Date is incorrectly formatted
     */
    public static Date toDate(String iso8601Date) throws ParseException {
        return tssStandardDateFormat.get().parse(iso8601Date);
    }

    /**
     * Converts a java.util.Date object to its string representation. The date
     * is represented as a time relative to the default timezone; that is, the
     * string representation of the time in the current time zone.
     *
     * @param date the date to format
     * @return a String representation of the date formatted using the ISO 8601
     *         extended pattern
     */
    public static String fromDate(Date date) {
        return tssStandardDateFormat.get().format(date);
    }
}
