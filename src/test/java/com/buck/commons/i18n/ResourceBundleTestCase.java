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

package com.buck.commons.i18n;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the ResourceBundle class.
 *
 * @author Robert J. Buck
 */
public class ResourceBundleTestCase {
    @Test
    public void testResourceBundleFactory() {
        java.util.ResourceBundle rb = ResourceBundle.getResourceBundle(ResourceBundleTestCase.class);
        String s1 = rb.getString("TEST_VALUE_INTEGER_ONE");
        Assert.assertEquals("1", s1);
    }

    @Test
    public void testMissingResource() {
        boolean failed = false;
        try {
            Object[] arguments = {};
            ResourceBundle.formatResourceBundleMessage(ResourceBundleTestCase.class,
                    "THIS_IS_A_MISSING_RESOURCE", arguments);
        } catch (Throwable t) {
            failed = true;
        }
        Assert.assertFalse(failed);
    }

    @Test
    public void testArrayThrows() {
        boolean thrown = false;
        try {
            ResourceBundle.getResourceBundle(Object[].class);
        } catch (Throwable e) {
            //e.printStackTrace();
            thrown = true;
        }
        Assert.assertTrue(thrown);
    }

    @Test
    public void testParameterization() {
        Object[] arguments = {"ZERO"};
        String message = ResourceBundle.formatResourceBundleMessage(ResourceBundleTestCase.class,
                "TEST_VALUE_STRING_FORMAT", arguments);
        Assert.assertEquals("Text (ZERO) Text", message);
    }
}
