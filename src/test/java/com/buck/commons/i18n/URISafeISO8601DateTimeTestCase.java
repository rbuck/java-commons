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
public class URISafeISO8601DateTimeTestCase {

    private void internalTestURISafeISO8601DateTime(String timeZone) {
        TimeZone tzDefault = TimeZone.getDefault();
        try {
            // normalize so tests work on all platforms, all timezones
            TimeZone.setDefault(TimeZone.getTimeZone(timeZone));

            // the real test
            Date dateTimeL0 = new Date();
            try {
                String dateTimeS = URISafeISO8601DateTime.fromDate(dateTimeL0); // fastTime: long = 1163617976235
                Date date1 = URISafeISO8601DateTime.toDate(dateTimeS); //
                String dateTimeS2 = URISafeISO8601DateTime.fromDate(date1); // fastTime: long = 1163617976235
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
        internalTestURISafeISO8601DateTime("America/New_York");
        internalTestURISafeISO8601DateTime("UTC");
        internalTestURISafeISO8601DateTime("America/Los_Angeles");
    }

    @Test
    public void testParseableISO8601() {
        try {
            URISafeISO8601DateTime.toDate("20000101T060000000Z");
        } catch (ParseException e) {
            Assert.fail("Failure parsing: 20000101T060000000Z");
        }
    }
}
