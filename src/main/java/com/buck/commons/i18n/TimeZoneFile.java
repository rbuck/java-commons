/*
 * Copyright 2010-2013 Robert J. Buck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buck.commons.i18n;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * The implementation parses a POSIX style timezone file. The time zone file
 * generally should be located at ${application.data.dir}/etc/timezone. There is
 * one difference between our timezone file format and the POSIX format; we
 * permit multiple time zones being listed in the file -- the timezone file
 * lists the time zones available for the application to use.
 * <p/>
 * The timezone file may have comment lines; comment lines are preceded by a '#'
 * character. Each timezone is listed on a separate line. Multiple time zones
 * per raw offset are permitted.
 *
 * @author Robert J. Buck
 */
public class TimeZoneFile {

    /**
     * Logger used to record diagnostic messages.
     */
    @SuppressWarnings({"FieldCanBeLocal"})
    private static final Logger logger;

    static {
        logger = Logger.getLogger(TimeZoneFile.class);
    }

    /**
     * This method returns a list of supported time zone ids that are read from
     * a time zone file (generally etc/timezone). If any entry in the time zone
     * file contains an invalid id, the implementation will provide a fallback
     * time zone id for GMT. If multiple invalid values exist, only one GMT
     * entry will appear in the list.
     * <p/>
     * The time zone array is sorted first by raw time zone offset, second by
     * the time zone id.
     *
     * @param timeZoneFile path to timezone file
     * @return a sorted list of timezones, sorted in timezone order
     */
    public static TimeZone[] getTimeZoneIds(String timeZoneFile) {
        File file = new File(timeZoneFile);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(timeZoneFile);
                try {
                    return getTimeZoneIds(inputStream);
                } finally {
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                Object[] formatArgs = {timeZoneFile, e.getMessage()};
                String message = ResourceBundle.formatResourceBundleMessage(TimeZoneFile.class,
                        "MISSING_TIMEZONE_FILE_EXCEPTION", formatArgs);
                logger.error(message);
            } catch (IOException e) {
                Object[] formatArgs = {timeZoneFile, e.getMessage()};
                String message = ResourceBundle.formatResourceBundleMessage(TimeZoneFile.class,
                        "UNKNOWN_TIMEZONE_FILE_EXCEPTION", formatArgs);
                logger.error(message);
            }
        }
        return null;
    }

    /**
     * This method returns a list of supported time zone ids that are read from
     * a time zone file (generally etc/timezone). If any entry in the time zone
     * file contains an invalid id, the implementation will provide a fallback
     * time zone id for GMT. If multiple invalid values exist, only one GMT
     * entry will appear in the list.
     * <p/>
     * The time zone array is sorted first by raw time zone offset, second by
     * the time zone id.
     *
     * @param tzInputStream input stream for the timezone file
     * @return a sorted list of timezones, sorted in timezone order
     */
    public static TimeZone[] getTimeZoneIds(InputStream tzInputStream) {
        TimeZone[] timeZones;

        /**
         * Compares TimeZone objects by id.
         */
        class TimeZoneIdComparator implements Comparator<TimeZone>, Serializable {

            private static final long serialVersionUID = -899787199207258879L;

            public int compare(TimeZone lhs, TimeZone rhs) {
                return lhs.getID().compareTo(rhs.getID());
            }
        }

        /**
         * Compares TimeZone objects by raw offset.
         */
        class TimeZoneRawOffsetComparator implements Comparator<TimeZone>, Serializable {

            private static final long serialVersionUID = 786814863252198070L;

            public int compare(TimeZone lhs, TimeZone rhs) {
                int lhsRawOffset = lhs.getRawOffset();
                int rhsRawOffset = rhs.getRawOffset();
                // reverse comparison so they sort from east to west
                return (lhsRawOffset > rhsRawOffset ? -1 : (lhsRawOffset == rhsRawOffset ? 0 : 1));
            }
        }

        String[] timeZoneIds = parseTimeZoneFile(tzInputStream);

        // get a unique set of time zone ids
        TreeSet<TimeZone> timeZoneSet = new TreeSet<TimeZone>(new TimeZoneIdComparator());
        if (timeZoneIds != null && timeZoneIds.length != 0) {
            for (String timeZoneId : timeZoneIds) {
                timeZoneSet.add(TimeZone.getTimeZone(timeZoneId));
            }
        } else {
            timeZoneSet.add(TimeZone.getDefault());
        }

        // sort by time zone offset
        ArrayList<TimeZone> list = new ArrayList<TimeZone>(timeZoneSet);
        Collections.sort(list, new TimeZoneRawOffsetComparator());

        // that's all folks!
        timeZones = new TimeZone[list.size()];
        return list.toArray(timeZones);
    }

    /**
     * Tries to read the time zone names from a file. Only the first consecutive
     * letters, digits, slashes, dashes and underscores are read from the file.
     * If the file cannot be read or an IOException occurs null is returned.
     *
     * @param tzInputStream the timezone file
     * @return the list of timezones in the timezone file
     */
    private static String[] parseTimeZoneFile(InputStream tzInputStream) {
        if (tzInputStream == null) {
            return null;
        }
        // enforce no duplicates
        TreeSet<String> validTimeZones = new TreeSet<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(tzInputStream));
        try {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    line = line.trim();
                    if (line.length() == 0 || '#' == line.charAt(0)) {
                        // comment or blank line
                        continue;
                    }
                    if (isTimeZoneWellFormed(line)) {
                        validTimeZones.add(line);
                    }
                }
            } while (line != null);
        } catch (IOException ioe) {
            // Parse error, not a proper timezone file.
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException ioe) {
                // Error while close, nothing we can do.
            }
        }

        int countTimeZones = validTimeZones.size();
        String[] timeZones = new String[countTimeZones];
        int i = 0;
        for (String timeZone : validTimeZones) {
            timeZones[i++] = timeZone;
        }
        return timeZones;
    }

    /**
     * Check to see if the timezone string is well formed. The method does not
     * check to see if the timezone is a legal zone name.
     *
     * @param text string to check
     * @return true if the text is a well formed timezone name
     */
    private static boolean isTimeZoneWellFormed(String text) {
        boolean isValid = true;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!(Character.isLetter(c) || Character.isDigit(c)
                    || c == '/' || c == '-' || c == '_')) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }
}
