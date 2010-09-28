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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests the NaturalOrderComparator class.
 *
 * @author Robert J. Buck
 */
public class NaturalComparatorTestCase {
    @Test
    public void testNaturalComparator() {
        {
            String[] wants = {"1.00001", "1.01", "1.45", "1.9", "1.95"};
            List<String> list = Arrays.asList(wants);
            Collections.shuffle(list);
            Collections.sort(list, TextComparatorFactory.getInstance(
                    TextComparatorFactory.Kind.NATURAL));
            for (int i = 0; i < wants.length; i++) {
                Assert.assertEquals("sort fails", wants[i], list.get(i));
            }
        }
        {
            String[] wants = {"pic02", "pic02a"};
            String[] fails = {"pic02", "pic02a"};
            List<String> list = Arrays.asList(fails);
            Collections.sort(list, TextComparatorFactory.getInstance(
                    TextComparatorFactory.Kind.NATURAL));
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
                    TextComparatorFactory.Kind.NATURAL));
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
                    TextComparatorFactory.Kind.NATURAL));
            Assert.assertTrue("sort order", scrambled.get(0).equals("s.xml"));
            Assert.assertTrue("sort order", scrambled.get(1).equals("s.xml.0"));
            Assert.assertTrue("sort order", scrambled.get(2).equals("s.xml.1"));
            Assert.assertTrue("sort order", scrambled.get(3).equals("s.xml.2"));
            Assert.assertTrue("sort order", scrambled.get(4).equals("s.xml.10"));
        }
    }
}
