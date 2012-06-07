/*
                        QueryJ

    Copyright (C) 2002-today  Jose San Leandro Armendariz
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
    Contact info: jose.sanleandro@acm-sl.com

 ******************************************************************************
 *
 * Filename: ValueObjectUtils.java
 *
 * Author: Jose San Leandro Armendariz (chous)
 *
 * Description: Provides some useful methods for ValueObject-related templates.
 *
 */
package org.acmsl.queryj.tools.templates.valueobject;

import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.StringUtils;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.vo.Table;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides some useful methods for ValueObject-related templates.
 * @author <a href="mailto:jose.sanleandro@ventura24.es">chous</a>
 * @since 05/05/2012 16:26
 */
public class ValueObjectUtils
    implements Singleton
{
    /**
     * Singleton implemented to prevent against double-check locking.
     */
    private static final class ValueObjectUtilsSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final ValueObjectUtils SINGLETON = new ValueObjectUtils();
    }

    /**
     * Default constructor.
     */
    public ValueObjectUtils() {}

    /**
     * Retrieves the {@link ValueObjectUtils} instance.
     * @return such instance.
     */
    @NotNull
    public static ValueObjectUtils getInstance()
    {
        return ValueObjectUtilsSingletonContainer.SINGLETON;
    }
    /**
     * Checks whether the given class name corresponds to
     * a standard value object or not.
     * @param className the class name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @return <code>true</code> in such case.
     * @precondition className != null
     * @precondition metadataManager != null
     */
    public boolean isStandard(
        @NotNull final String className, @NotNull final MetadataManager metadataManager)
    {
        boolean result = false;

        List<Table> t_lTables = metadataManager.getTableDAO().findAllTables();

        String t_strVoClassName;

        for (Table t_Table : t_lTables)
        {
            if (t_Table != null)
            {
                t_strVoClassName = getVoClassName(t_Table.getName());

                if (   (t_strVoClassName != null)
                    && (   (t_strVoClassName.equals(className)))
                        || ((t_strVoClassName + "ValueObject").equals(className))
                        || ((className + "ValueObject").equals(t_strVoClassName)))
                {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Extracts the class name of given fully-qualified class.
     * @param fqcn such information.
     * @return the class name.
     * @precondition fqcn != null
     */
    @NotNull
    public String extractClassName(@NotNull final String fqcn)
    {
        return extractClassName(fqcn, PackageUtils.getInstance());
    }

    /**
     * Extracts the class name of given fully-qualified class.
     * @param fqcn such information.
     * @param packageUtils the {@link PackageUtils} instance.
     * @return the class name.
     * @precondition fqcn != null
     * @precondition packageUtils != null
     */
    @NotNull
    protected String extractClassName(
        @NotNull final String fqcn, @NotNull final PackageUtils packageUtils)
    {
        return packageUtils.extractClassName(fqcn);
    }

    /**
     * Retrieves the class name of the value object associated to
     * given table name.
     * @param tableName the table name.
     * @return the class name.
     */
    public String getVoClassName(@NotNull final String tableName)
    {
        return
            getVoClassName(
                tableName,
                EnglishGrammarUtils.getInstance(),
                StringUtils.getInstance());
    }

    /**
     * Retrieves the class name of the value object associated to
     * given table name.
     * @param tableName the table name.
     * @param englishGrammarUtils the {@link EnglishGrammarUtils}
     * instance.
     * @param stringUtils the {@link StringUtils} instance.
     * @return the class name.
     * @precondition englishGrammarUtils != null
     * @precondition stringUtils != null
     */
    protected String getVoClassName(
        @NotNull final String tableName,
        @NotNull final EnglishGrammarUtils englishGrammarUtils,
        @NotNull final StringUtils stringUtils)
    {
        return
            stringUtils.capitalize(
                englishGrammarUtils.getSingular(tableName.toLowerCase()),
                '_');
    }

}
