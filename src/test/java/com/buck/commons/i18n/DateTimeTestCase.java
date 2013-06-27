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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Tests the DateTime class.
 *
 * @author Robert J. Buck
 */
public class DateTimeTestCase {
    @Test
    public void testGetTimeZoneOffset() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Calendar enUsCalendar = Calendar.getInstance(timeZone, new Locale("en", "US"));
        {
            enUsCalendar.set(2005, Calendar.JULY, 15);
            Date date = enUsCalendar.getTime();
            long timeZoneOffset = DateTime.getTimeZoneOffset(date, timeZone);
            Assert.assertEquals(((long) (-4 * 60 * 60 * 1000)), timeZoneOffset);
        }
        {
            enUsCalendar.set(2005, Calendar.DECEMBER, 15);
            Date date = enUsCalendar.getTime();
            long timeZoneOffset = DateTime.getTimeZoneOffset(date, timeZone);
            Assert.assertEquals(((long) (-5 * 60 * 60 * 1000)), timeZoneOffset);
        }
    }
}
