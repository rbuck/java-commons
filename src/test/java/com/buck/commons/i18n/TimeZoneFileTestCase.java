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

import java.io.File;
import java.io.InputStream;
import java.util.TimeZone;

/**
 * Tests the TimeZoneFile class.
 *
 * @author Robert J. Buck
 */
public class TimeZoneFileTestCase {
    @Test
    public void testEmptyTimeZoneFileParser() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("etc" + File.separator + "notimezone");
        TimeZone[] timeZones = TimeZoneFile.getTimeZoneIds(inputStream);
        Assert.assertTrue(timeZones.length == 1);
        Assert.assertTrue(TimeZone.getDefault().equals(timeZones[0]));
    }

    @Test
    public void testBadTimeZoneFileParser() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("etc" + File.separator + "badtimezone");
        TimeZone[] timeZones = TimeZoneFile.getTimeZoneIds(inputStream);
        Assert.assertTrue(timeZones.length == 1);
        Assert.assertTrue(TimeZone.getDefault().equals(timeZones[0]));
    }

    @Test
    public void testNonExistentTimeZoneFileParser() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("etc" + File.separator + "nonexistenttimezonefile");
        TimeZone[] timeZones = TimeZoneFile.getTimeZoneIds(inputStream);
        Assert.assertTrue(timeZones.length == 1);
        Assert.assertTrue(TimeZone.getDefault().equals(timeZones[0]));
    }

    @Test
    public void testTimeZoneFileParser() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("etc" + File.separator + "timezone");
        String[] expectedTimeZones = {
                "GMT",
                "UTC",
                "America/Montreal",
                "America/New_York",
                "America/Chicago",
                "America/Denver",
                "America/Los_Angeles"
        };
        TimeZone[] timeZones = TimeZoneFile.getTimeZoneIds(inputStream);
        for (int i = 0; i < timeZones.length; i++) {
            Assert.assertEquals(TimeZone.getTimeZone(expectedTimeZones[i]), timeZones[i]);
        }
    }
}
