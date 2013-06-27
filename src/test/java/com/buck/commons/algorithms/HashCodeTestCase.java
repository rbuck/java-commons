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

package com.buck.commons.algorithms;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the HashCode class.
 *
 * @author Robert J. Buck
 */
public class HashCodeTestCase {
    @Test
    public void testStrings() {
        String s0 = "jack";
        String s1 = "jill";
        String s3 = s0 + s1;
        String[] sa = {s0, s1};
        Assert.assertEquals("Identical String.hashCode", s0.hashCode(), HashCode.hashCode(0, s0));
        Assert.assertEquals("Identical String.hashCode Concatenated", (s0 + s1).hashCode(), HashCode.hashCode(0, (s0 + s1)));
        Assert.assertEquals("Concatenated HashCode Identical to Constituent (Ellipsis)", s3.hashCode(), HashCode.hashCode(s0, s1));
        Assert.assertEquals("Concatenated HashCode Identical to Constituent (Array)", s3.hashCode(), HashCode.hashCode(sa));
    }
}
