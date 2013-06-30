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

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Exceptions utility class.
 *
 * @author Robert J. Buck
 */
public class ExceptionsTestCase {

    @Test
    public void checksHoles() {
        final Throwable t0;
        try {
            throw new Throwable("t0");
        } catch (Throwable t) {
            t0 = t;
        }
        final Throwable t1;
        try {
            throw new Throwable(t0);
        } catch (Throwable t) {
            t1 = t;
        }
        final Throwable t2;
        try {
            throw new Throwable("t2", t1);
        } catch (Throwable t) {
            t2 = t;
        }
        final String reason = Exceptions.toStringAllCauses(t2);
        System.out.println(reason);
    }

    @Test
    public void checksDepth() {
        final Throwable t0;
        try {
            throw new Throwable("t0");
        } catch (Throwable t) {
            t0 = t;
        }
        final Throwable t1;
        try {
            throw new Throwable(t0);
        } catch (Throwable t) {
            t1 = t;
        }
        final Throwable t2;
        try {
            throw new Throwable("t2", t1);
        } catch (Throwable t) {
            t2 = t;
        }
        final String reason = Exceptions.toStringAllCauses(t2, 0);
        Assert.assertEquals("depth 1", "t2", reason);
    }
}