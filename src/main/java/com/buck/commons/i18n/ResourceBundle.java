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

package com.buck.commons.i18n;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Class to hide details of getting a resource bundle, properly; the class also
 * provides facilities to format resource bundle messages.
 * <p/>
 * The general form to format resource bundle messages is:
 * <p/>
 * <pre>
 *  try {
 *    // ...
 *  } catch (Exception e) {
 *      Object[] fmtargs = {yourValue};
 *      String message = ResourceBundle.formatResourceBundleMessage(
 *                         YourLexicallyEnclosingClassName.class,
 *                         "YOUR_RESOURCE_BUNDLE_MESSAGE_MNEMONIC", fmtargs);
 *      throw new Exception(message, e);
 *  }
 * </pre>
 * <p/>
 * In the package containing <tt>YourLexicallyEnclosingClassName.class</tt> you
 * would add a file named <tt>resource.properties</tt>, which may have the
 * following content, where CAFEBABE is a Hex error code:
 * <p/>
 * <pre>
 * YOUR_RESOURCE_BUNDLE_MESSAGE_MNEMONIC=(ERROR:CAFEBABE) An error occurred
 * processing ({0}).
 * </pre>
 *
 * @author Robert J. Buck
 */
public final class ResourceBundle {

    /**
     * Logger used to record diagnostic messages.
     */
    @SuppressWarnings({"FieldCanBeLocal"})
    private static final Logger logger;

    static {
        logger = Logger.getLogger(ResourceBundle.class);
    }

    /**
     * Get a resource bundle using the context class loader and default locale.
     *
     * @param baseName the name of the resource bundle
     * @return the resource bundle for baseName
     */
    public static java.util.ResourceBundle getResourceBundle(String baseName) {
        return java.util.ResourceBundle.getBundle(baseName, Locale.getDefault(),
                Thread.currentThread().getContextClassLoader());
    }

    /**
     * Get the resource bundle in the package of the specified class. The name
     * for this resource bundle in the package must be "resource.properties".
     *
     * @param c the class in whose package a resource.properties file exists.
     * @return the resource bundle
     */
    public static java.util.ResourceBundle getResourceBundle(Class c) {
        if (c.isArray()) {
            Object[] arguments = {};
            String message = ResourceBundle.formatResourceBundleMessage(ResourceBundle.class,
                    "ARRAYS_HAVE_NO_ASSOCIATED_PACKAGE", arguments);
            throw new IllegalArgumentException(message);
        }
        String className = c.getName();
        int pkgEndIndex = className.lastIndexOf('.');
        String packageName;
        if (pkgEndIndex < 0) {
            packageName = "";
        } else {
            packageName = className.substring(0, pkgEndIndex);
        }
        String baseName = packageName.replace('.', '/') + "/resource";
        return getResourceBundle(baseName);
    }

    /**
     * A helper function to format a message using a resource identified by its
     * mnemonic, located in a resource bundle located in the package of the
     * given class.
     *
     * @param c         the class in whose package we will load a resource
     *                  bundle
     * @param mnemonic  the message mnemonic to load from the bundle
     * @param arguments formatting arguments
     * @return the formatted message
     */
    public static String formatResourceBundleMessage(Class c, String mnemonic, Object[] arguments) {
        final java.util.ResourceBundle rb = ResourceBundle.getResourceBundle(c);
        try {
            String pattern = rb.getString(mnemonic);
            return MessageFormat.format(pattern, arguments);
        } catch (MissingResourceException e) {
            Object[] fmtargs = {mnemonic, c.getPackage().getName()};
            String message = ResourceBundle.formatResourceBundleMessage(ResourceBundle.class,
                    "MISSING_RESOURCE_EXCEPTION", fmtargs);
            logger.error(message);
            return mnemonic;
        }
    }
}
