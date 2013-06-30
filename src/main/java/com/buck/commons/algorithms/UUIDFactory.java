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

package com.buck.commons.algorithms;

import java.util.Random;
import java.util.UUID;

/**
 * Constructs UUID objects 15x faster than what is provided with Java Util.
 *
 * @author Robert J. Buck
 */
public final class UUIDFactory {

    /**
     * Thread specific singleton of date formatter.
     */
    private static final ThreadLocal<Random> tssPRNG = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new MersenneTwister();
        }
    };

    /**
     * Makes a Type 4 UUID.
     *
     * @return a uuid
     */
    public static UUID randomUUID() {
        byte[] randomBytes = new byte[16];
        tssPRNG.get().nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;  /* clear version        */
        randomBytes[6] |= 0x40;  /* set to version 4     */
        randomBytes[8] &= 0x3f;  /* clear variant        */
        randomBytes[8] |= 0x80;  /* set to IETF variant  */
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (randomBytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (randomBytes[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }
}
