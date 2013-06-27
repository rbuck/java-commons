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

package com.buck.commons.text;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

/**
 * Strictly compares two strings using the current locale's default collator.
 *
 * @author Robert J. Buck
 */
class StrictCollationComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = -9169124899305453592L;

    public int compare(String s0, String s1) {
        Collator collator = Collator.getInstance();
        return collator.compare(s0, s1);
    }
}
