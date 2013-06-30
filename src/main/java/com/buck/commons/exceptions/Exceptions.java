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

package com.buck.commons.exceptions;

/**
 * Provides general exception utility methods.
 *
 * @author Robert J. Buck
 */
public final class Exceptions {
    /**
     * Prepares an exception for display by concatenating all causation messages
     * into one string. This method could be used for logs where stack traces
     * are to be avoided.
     *
     * @param what the exception for which to prepare a concatenated message
     * @return the concatenated causation message
     */
    public static String toStringAllCauses(Throwable what) {
        return toStringAllCauses(what, Integer.MAX_VALUE);
    }

    /**
     * Prepares an exception for display by concatenating all causation messages
     * into one string. This method could be used for logs where stack traces
     * are to be avoided.
     *
     * @param what  the exception for which to prepare a concatenated message
     * @param depth the stack depth to which causation messages are
     *              concatenated
     * @return the concatenated causation message
     */
    public static String toStringAllCauses(Throwable what, int depth) {
        String message = what.getMessage();
        if (depth > 0) {
            Throwable cause = what.getCause();
            if (cause != null) {
                final String reason = toStringAllCauses(cause, depth - 1);
                if (reason != null) {
                    message = message + ": " + reason;
                }
            }
        }
        return message;
    }
}