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

package com.buck.commons.algorithms;

import com.buck.commons.i18n.ResourceBundle;

import java.io.File;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Algorithm to generate some entropy based upon some system values. This is
 * commonly used to initialize random number generators.
 *
 * @author Robert J. Buck
 */
public final class SystemEntropy {

    private static byte[] longToByteArray(long l) {
        byte[] retVal = new byte[8];
        for (int i = 0; i < 8; i++) {
            retVal[i] = (byte) l;
            l >>= 8;
        }
        return retVal;
    }

    public static byte[] getSystemEntropy() {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException nsae) {
            Object[] args = {};
            String message = ResourceBundle.formatResourceBundleMessage(SystemEntropy.class,
                    "SHA1_UNAVAILABLE", args);
            throw new InternalError(message);
        }

        // The current time in nanoseconds; we cannot use System.currentTimeMillis()
        // because of Sun Bugs # 4478186.
        byte[] time = new byte[Long.SIZE / Byte.SIZE];
        BinaryData.storeLongAtOffset(time, 0, System.nanoTime());
        md.update(time);

        // The current thread id
        byte[] tid = new byte[Long.SIZE / Byte.SIZE];
        BinaryData.storeLongAtOffset(tid, 0, Thread.currentThread().getId());
        md.update(tid);

        java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
            public Void run() {
                try {
                    // System properties can change from machine to machine
                    String s;
                    Properties p = System.getProperties();
                    Enumeration e = p.propertyNames();
                    while (e.hasMoreElements()) {
                        s = (String) e.nextElement();
                        md.update(s.getBytes());
                        md.update(p.getProperty(s).getBytes());
                    }

                    md.update(InetAddress.getLocalHost().toString().getBytes());

                    // The temporary dir
                    File f = new File(p.getProperty("java.io.tmpdir"));
                    String[] sa = f.list();
                    for (String aSa : sa) {
                        md.update(aSa.getBytes());
                    }

                } catch (Exception ex) {
                    md.update((byte) ex.hashCode());
                }

                // get Runtime memory stats
                Runtime rt = Runtime.getRuntime();
                byte[] memBytes = longToByteArray(rt.totalMemory());
                md.update(memBytes, 0, memBytes.length);
                memBytes = longToByteArray(rt.freeMemory());
                md.update(memBytes, 0, memBytes.length);

                return null;
            }
        });
        return md.digest();
    }
}
