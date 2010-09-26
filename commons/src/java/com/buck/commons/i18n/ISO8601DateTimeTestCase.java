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

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Tests the ISO8601DateTime class
 *
 * @author Robert J. Buck
 */
public class ISO8601DateTimeTestCase {

    private void internalTestISO8601DateTime(String timeZone) {
        TimeZone tzDefault = TimeZone.getDefault();
        try {
            // normalize so tests work on all platforms, all timezones
            TimeZone.setDefault(TimeZone.getTimeZone(timeZone));

            // the real test
            Date dateTimeL0 = new Date();
            try {
                String dateTimeS = ISO8601DateTime.fromDate(dateTimeL0); // fastTime: long = 1163617976235
                Date date1 = ISO8601DateTime.toDate(dateTimeS); //
                String dateTimeS2 = ISO8601DateTime.fromDate(date1); // fastTime: long = 1163617976235
                Assert.assertEquals(dateTimeL0, date1);
                Assert.assertEquals(dateTimeS, dateTimeS2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } finally {
            TimeZone.setDefault(tzDefault);
        }
    }

    @Test
    public void testISO8601DateTime() {
        internalTestISO8601DateTime("America/New_York");
        internalTestISO8601DateTime("UTC");
        internalTestISO8601DateTime("America/Los_Angeles");
    }

    @Test
    public void testISO8601RoundTrip() {
        final String date = "2000-01-01T06:00:00.000Z";
        try {
            Assert.assertEquals(date, ISO8601DateTime.fromDate(ISO8601DateTime.toDate(date)));
        } catch (ParseException e) {
            Assert.fail("Failure parsing: 2000-01-01T06:00:00.000Z");
        }
    }

    @Test
    public void testISO8601FromPerformance() {
        final Date date = new Date();
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            ISO8601DateTime.fromDate(date);
        }
        long e = System.currentTimeMillis();
        System.out.println("ISO fromDate Perf: " + (e - s));
    }

    @Test
    public void testISO8601ToPerformance() {
        final String date = "2000-01-01T06:00:00.000Z";
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            try {
                ISO8601DateTime.toDate(date);
            } catch (ParseException e) {
                Assert.fail("Failure parsing: " + date);
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("ISO toDate Perf: " + (e - s));
    }
}
