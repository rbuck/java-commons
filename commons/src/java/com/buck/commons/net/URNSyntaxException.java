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

package com.buck.commons.net;

/**
 * Checked exception thrown to indicate that a string could not be parsed as a
 * URN reference.
 *
 * @author Robert J. Buck
 */
public class URNSyntaxException extends Exception {

    /**
     * The input URN string that has the syntax issue.
     */
    private final String input;

    /**
     * The index where the syntax issue occurred.
     */
    private final int index;

    /**
     * Serialization version number.
     */
    private static final long serialVersionUID = 9038341204011423155L;

    /**
     * Constructs an instance from the given input string and reason.  The
     * resulting object will have an error index hashCode <tt>-1</tt>.
     *
     * @param input  The input string
     * @param reason A string explaining why the input could not be parsed
     * @throws NullPointerException If either the input or reason strings are
     *                              <tt>null</tt>
     */
    public URNSyntaxException(String input, String reason) {
        this(input, reason, -1);
    }

    /**
     * Constructs an instance from the given input string, reason, and error
     * index.
     *
     * @param input  The input string
     * @param reason A string explaining why the input could not be parsed
     * @param index  The index at which the parse error occurred, or <tt>-1</tt>
     *               if the index is not known
     * @throws NullPointerException     If either the input or reason strings
     *                                  are <tt>null</tt>
     * @throws IllegalArgumentException If the error index is less than
     *                                  <tt>-1</tt>
     */
    public URNSyntaxException(String input, String reason, int index) {
        super(reason);
        this.input = input;
        this.index = index;
    }

    /**
     * Returns the input string.
     *
     * @return The input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Returns an index into the input string hashCode the position at which the
     * parse error occurred, or <tt>-1</tt> if this position is not known.
     *
     * @return The error index
     */
    public int getIndex() {
        return index;
    }
}
