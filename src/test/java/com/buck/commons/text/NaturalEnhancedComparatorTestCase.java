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

package com.buck.commons.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tests the NaturalEnhancedComparator class.
 *
 * @author Robert J. Buck
 */
public class NaturalEnhancedComparatorTestCase {
    @Test
    public void testNaturalComparator() {
        {
            String[] wants = {"1.00001", "1.01", "1.45", "1.9", "1.95"};
            List<String> list = Arrays.asList(wants);
            Collections.shuffle(list);
            Collections.sort(list, TextComparatorFactory.getInstance(
                    TextComparatorFactory.Kind.NATURAL_ENHANCED));
            for (int i = 0; i < wants.length; i++) {
                Assert.assertEquals("sort fails", wants[i], list.get(i));
            }
        }
        {
            String[] wants = {"1-2", "1-02"};
            String[] fails = {"1-02", "1-2"};
            List<String> list = Arrays.asList(fails);
            Collections.sort(list, TextComparatorFactory.getInstance(
                    TextComparatorFactory.Kind.NATURAL_ENHANCED));
            for (int i = 0; i < wants.length; i++) {
                Assert.assertEquals("sort fails", wants[i], list.get(i));
            }
        }
        {
            String[] wants = {"pic02", "pic02a"};
            String[] fails = {"pic02", "pic02a"};
            List<String> list = Arrays.asList(fails);
            Collections.sort(list, TextComparatorFactory.getInstance(
                    TextComparatorFactory.Kind.NATURAL_ENHANCED));
            for (int i = 0; i < wants.length; i++) {
                Assert.assertEquals("sort fails", wants[i], list.get(i));
            }
        }
        {
            String[] wants = {
                    "1-2", "1-02", "1-20", "10-20", "fred", "jane", "pic01",
                    "pic2", "pic02", "pic02a", "pic3", "pic4", "pic 4 else",
                    "pic 5", "pic 5 something", "pic05", "pic 6",
                    "pic   7", "pic100", "pic100a", "pic120", "pic121",
                    "pic02000", "tom", "x2-g8", "x2-y7", "x2-y08", "x8-y8"

            };
            List<String> scrambled = Arrays.asList(wants);
            Collections.shuffle(scrambled);
            Collections.sort(scrambled, TextComparatorFactory.getInstance(
                    TextComparatorFactory.Kind.NATURAL_ENHANCED));
            for (int i = 0; i < wants.length; i++) {
                Assert.assertEquals("sort fails", wants[i], scrambled.get(i));
            }
        }
        {
            String[] strings1 = new String[]{
                    "s.xml.2", "s.xml", "s.xml.1", "s.xml.0", "s.xml.10"
            };
            List<String> scrambled = Arrays.asList(strings1);
            Collections.shuffle(scrambled);
            Collections.sort(scrambled, TextComparatorFactory.getInstance(
                    TextComparatorFactory.Kind.NATURAL_ENHANCED));
            Assert.assertTrue("sort order", scrambled.get(0).equals("s.xml"));
            Assert.assertTrue("sort order", scrambled.get(1).equals("s.xml.0"));
            Assert.assertTrue("sort order", scrambled.get(2).equals("s.xml.1"));
            Assert.assertTrue("sort order", scrambled.get(3).equals("s.xml.2"));
            Assert.assertTrue("sort order", scrambled.get(4).equals("s.xml.10"));
        }
    }

    @Test
    public void testRightCompareDigits() {
        String[][] vectors = {
                {"", "1"},
                {"1", "1"},
                {"2", "1"},
                {"1", "2"},
                {"10", "2"},
                {"2", "22"},
                {"22", "0002"},
                {"2", "0002"}
        };
        int[] orders = {
                -1,
                0,
                1,
                -1,
                1,
                -1,
                1,
                -1
        };
        NaturalEnhancedComparator comparator = new NaturalEnhancedComparator();
        for (int i = 0; i < vectors.length; i++) {
            int order = comparator.rightCompareDigits(
                    vectors[i][0], new NaturalEnhancedComparator.ScanState(),
                    vectors[i][1], new NaturalEnhancedComparator.ScanState());
            Assert.assertEquals("right sort order", orders[i], order);
        }
    }

    @Test
    public void testLeftCompareDigits() {
        String[][] vectors = {
                {"", "1"},
                {"1", "1"},
                {"01", "2"},
        };
        int[] orders = {
                -1,
                0,
                -1,
        };
        NaturalEnhancedComparator comparator = new NaturalEnhancedComparator();
        for (int i = 0; i < vectors.length; i++) {
            int order = comparator.leftCompareDigits(
                    vectors[i][0], new NaturalEnhancedComparator.ScanState(),
                    vectors[i][1], new NaturalEnhancedComparator.ScanState());
            Assert.assertEquals("left sort order", orders[i], order);
        }
    }

    @Test
    public void testFractional() {
        String[][] vectors = {
                {"1.0", "01.0"},
                {"1.1", "1.12"},
                {"2.1", "1.2"},
                {"A 3.14 A", "A 3.14 B"},
                {"1.02.3", "1.3.1"},
        };
        int[] orders = {
                -1,
                -1,
                1,
                -1,
                -1
        };
        NaturalEnhancedComparator comparator = new NaturalEnhancedComparator();
        for (int i = 0; i < vectors.length; i++) {
            int order = comparator.compare(vectors[i][0], vectors[i][1]);
            Assert.assertEquals("sort order", orders[i], order);
        }
    }

    @Test
    public void testLeftCompare() {
        String[][] vectors = {
                {"10a", "10.1a"},
        };
        int[] orders = {
                -1,
        };
        Comparator<String> comparator = TextComparatorFactory.getInstance(TextComparatorFactory.Kind.NATURAL_ENHANCED);
        for (int i = 0; i < vectors.length; i++) {
            int order = comparator.compare(vectors[i][0], vectors[i][1]);
            Assert.assertEquals("sort order", orders[i], order);
        }
    }

    @Test
    public void testEndVersionNumbers() {
        String[][] vectors = {
                {"a.x", "a.x.1"},
                {"a.x.1", "a.x.2"},
                {"a.x.10", "a.x.2"},
        };
        int[] orders = {
                -1,
                -1,
                1,
        };
        NaturalEnhancedComparator comparator = new NaturalEnhancedComparator();
        for (int i = 0; i < vectors.length; i++) {
            int order = comparator.compare(vectors[i][0], vectors[i][1]);
            Assert.assertEquals("sort order", orders[i], order);
        }
    }
}
