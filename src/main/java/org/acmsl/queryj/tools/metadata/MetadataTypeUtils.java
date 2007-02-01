//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-2006  Jose San Leandro Armendariz
                             chous@acm-sl.org

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Thanks to ACM S.L. for distributing this library under the GPL license.
    Contact info: chous@acm-sl.org
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Filename: MetadataTypeUtils.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Provides some methods commonly-reused when working with
 *              types of metadata.
 */
package org.acmsl.queryj.tools.metadata;

/*
 * Importing ACM-SL Commons classes.
 */
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.patterns.Utils;

/**
 * Provides some methods commonly-used when working with types of metadata.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class MetadataTypeUtils
    implements  Singleton,
                Utils
{
    /**
     * Singleton implemented to avoid the double-checked locking.
     */
    private static class MetadataTypeUtilsSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final MetadataTypeUtils SINGLETON =
            new MetadataTypeUtils();
    }

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected MetadataTypeUtils() {};

    /**
     * Retrieves a <code>MetadataTypeUtils</code> instance.
     * @return such instance.
     */
    public static MetadataTypeUtils getInstance()
    {
        return MetadataTypeUtilsSingletonContainer.SINGLETON;
    }

    /**
     * Retrieves the wrapper class associated to given Java type.
     * @param type the Java type.
     * @return such class name (not fully-qualified).
     * @precondtion type != null
     */
    public String getWrapperClass(final String type)
    {
        String result = type;

        if  (   ("boolean".equals(type))
             || ("Boolean".equals(type))
             || ("java.lang.Boolean".equals(type)))
        {
            result = "Boolean";
        }
        else if  (   ("byte".equals(type))
                  || ("Byte".equals(type))
                  || ("java.lang.Byte".equals(type)))
        {
            result = "Byte";
        }
        else if  (   ("short".equals(type))
                  || ("Short".equals(type))
                  || ("java.lang.Short".equals(type)))
        {
            result = "Short";
        }
        else if  (   ("char".equals(type))
                  || ("Character".equals(type))
                  || ("java.lang.Character".equals(type)))
        {
            result = "Character";
        }
        else if  (   ("int".equals(type))
                  || ("Integer".equals(type))
                  || ("java.lang.Long".equals(type)))
        {
            result = "Integer";
        }
        else if  (   ("long".equals(type))
                  || ("Long".equals(type))
                  || ("java.lang.Long".equals(type)))
        {
            result = "Long";
        }
        else if  (   ("float".equals(type))
                  || ("Float".equals(type))
                  || ("java.lang.Float".equals(type)))
        {
            result = "Float";
        }
        else if  (   ("double".equals(type))
                  || ("Double".equals(type))
                  || ("java.lang.Double".equals(type)))
        {
            result = "Double";
        }

        return result;
    }
}