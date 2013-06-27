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

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

/**
 * Tests the URN class.
 *
 * @author Robert J. Buck
 */
public class URNTestCase {
    @Test
    public void testEquivalence() throws URNSyntaxException {
        final String S = "urn:nid:nss";
        URN u0 = new URN(S);
        URN u1 = new URN(S);
        Assert.assertEquals("URN equals", u0, u1);
    }

    @Test
    public void testHashSets() throws URNSyntaxException {
        final String S = "urn:nid:nss";
        URN u0 = new URN(S);
        URN u1 = new URN(S);
        HashSet<URN> set = new HashSet<URN>();
        set.add(u0);
        Assert.assertTrue("set contains", set.contains(u1));
    }

    @Test
    public void testURN() {
        {
            try {
                URN urn = new URN("urn:silly:blammo=default,chunky=jif,peanut=butter");
                Assert.assertEquals(urn.getNid(), "silly");
                Assert.assertEquals(urn.getNss(), "blammo=default,chunky=jif,peanut=butter");
            } catch (URNSyntaxException e) {
                e.printStackTrace();
            }
        }
        {
            try {
                URN urn1 = new URN("urn:silly:session");
                URN urn2 = new URN("uRn:silly:seSsion");
                Assert.assertTrue(urn1.compareTo(urn2) == 0);
            } catch (URNSyntaxException e) {
                Assert.fail();
            }
        }
        {
            try {
                final String urnLiteral = "urn:bar:foo";
                URN urn = new URN(urnLiteral);
                Assert.assertEquals(urnLiteral, urn.toString());
            } catch (URNSyntaxException e) {
                Assert.fail();
            }
        }
    }

    @Test
    public void testURNNegativeTests() {
        // urn's must be prefixed by "urn:"
        {
            boolean caught = false;
            try {
                new URN("pre:test:pre_must_be_urn");
            } catch (URNSyntaxException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        // missing urn prefix
        {
            boolean caught = false;
            try {
                new URN(":test:missing_urn_prefix");
            } catch (URNSyntaxException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        // urn's cannot have a comma before the first colon
        {
            final String urnLiteral = "urn,test:comma_cannot_be_first";
            boolean caught = false;
            try {
                new URN(urnLiteral);
            } catch (URNSyntaxException e) {
                caught = true;
                Assert.assertEquals(urnLiteral, e.getInput());
                Assert.assertTrue(e.getIndex() == 3);
            }
            Assert.assertTrue(caught);
        }
        // urn's cannot have a comma before the second colon, or nid cannot have commas
        {
            final String urnLiteral = "urn:test,value:comma_cannot_be_first";
            boolean caught = false;
            try {
                new URN(urnLiteral);
            } catch (URNSyntaxException e) {
                caught = true;
                Assert.assertEquals(urnLiteral, e.getInput());
                Assert.assertTrue(e.getIndex() == 8);
            }
            Assert.assertTrue(caught);
        }
        // urn nid too short
        {
            final String urnLiteral = "urn::urn_nid_too_short";
            boolean caught = false;
            try {
                new URN(urnLiteral);
            } catch (URNSyntaxException e) {
                caught = true;
                Assert.assertEquals(urnLiteral, e.getInput());
                Assert.assertTrue(e.getIndex() == 4);
            }
            Assert.assertTrue(caught);
        }
        // urn nid too long
        {
            boolean caught = false;
            try {
                new URN("urn:0123456789abcdef0123456789abcdef0:urn_nid_too_long");
            } catch (URNSyntaxException e) {
                caught = true;
            }
            Assert.assertTrue(caught);
        }
        // urn nss too short
        {
            boolean caught = false;
            final String urnLiteral = "urn:test:";
            try {
                new URN(urnLiteral);
            } catch (URNSyntaxException e) {
                caught = true;
                Assert.assertEquals(urnLiteral, e.getInput());
                Assert.assertTrue(e.getIndex() == 9);
            }
            Assert.assertTrue(caught);
        }
        // compare nid
        {
            try {
                URN urn1 = new URN("urn:bar:foo");
                URN urn2 = new URN("urn:bat:foo");
                Assert.assertTrue(urn1.compareTo(urn2) < 0);
            } catch (URNSyntaxException e) {
                Assert.fail();
            }
            try {
                URN urn1 = new URN("urn:bar:foo");
                URN urn2 = new URN("urn:bat:foo");
                Assert.assertTrue(urn2.compareTo(urn1) > 0);
            } catch (URNSyntaxException e) {
                Assert.fail();
            }
        }
        // compare nss
        {
            try {
                URN urn1 = new URN("urn:bar:foo");
                URN urn2 = new URN("urn:bar:fot");
                Assert.assertTrue(urn1.compareTo(urn2) < 0);
            } catch (URNSyntaxException e) {
                Assert.fail();
            }
            try {
                URN urn1 = new URN("urn:bar:foo");
                URN urn2 = new URN("urn:bar:fot");
                Assert.assertTrue(urn2.compareTo(urn1) > 0);
            } catch (URNSyntaxException e) {
                Assert.fail();
            }
        }
        // malformed escape in NID
        {
            boolean caught = false;
            final String urnLiteral = "urn:te%2z%25:bar";
            try {
                new URN(urnLiteral);
            } catch (URNSyntaxException e) {
                caught = true;
                Assert.assertEquals(urnLiteral, e.getInput());
                Assert.assertTrue(e.getIndex() == 6);
            }
            Assert.assertTrue(caught);
        }
        // malformed escape in NSS
        {
            boolean caught = false;
            final String urnLiteral = "urn:test:%25%0%32";
            try {
                new URN(urnLiteral);
            } catch (URNSyntaxException e) {
                caught = true;
                Assert.assertEquals(urnLiteral, e.getInput());
                Assert.assertTrue(e.getIndex() == 12);
            }
            Assert.assertTrue(caught);
        }
        {
            boolean caught = false;
            final String urnLiteral = "urn:nid:bad{syntax";
            try {
                new URN(urnLiteral);
            } catch (URNSyntaxException e) {
                caught = true;
                Assert.assertEquals(urnLiteral, e.getInput());
                Assert.assertEquals("bad syntax index", 11, e.getIndex());
            }
            Assert.assertTrue(caught);
        }
    }
}
