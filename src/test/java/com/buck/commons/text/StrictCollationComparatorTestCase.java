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

package com.buck.commons.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Locale;

/**
 * Tests the StrictCollationComparator class.
 *
 * @author Robert J. Buck
 */
public class StrictCollationComparatorTestCase {
    private void runTest(String s0, String s1, String language, String country, String rank) {
        Locale def = Locale.getDefault();
        Locale.setDefault(new Locale(language, country));
        try {
            Comparator<String> collator = TextComparatorFactory.getInstance(TextComparatorFactory.Kind.STRICT_COLLATION);
            int result = collator.compare(s0, s1);
            if ("GT".equals(rank)) {
                Assert.assertTrue("rank gt", result > 0);
            } else if ("LT".equals(rank)) {
                Assert.assertTrue("rank lt", result < 0);
            }
        } finally {
            Locale.setDefault(def);
        }
    }

    @Test
    public void testCollation() {
        String[][] testVectors = {
                {"\u007A", "\u00F6", "sv", "SE", "LT"},
                {"\u007A", "\u00F6", "de", "DE", "GT"},
        };
        for (String[] testVector : testVectors) {
            runTest(testVector[0], testVector[1], testVector[2], testVector[3], testVector[4]);
        }
    }
}
