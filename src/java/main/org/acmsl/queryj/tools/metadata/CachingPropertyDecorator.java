/*
                        QueryJ

    Copyright (C) 2002-2005  Jose San Leandro Armendariz
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
 * Filename: $RCSfile: $
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Decorates <property> elements in custom-sql models.
 *
 */
package org.acmsl.queryj.tools.metadata;

/*
 * Importing project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.Property;
import org.acmsl.queryj.tools.metadata.AbstractPropertyDecorator;
import org.acmsl.queryj.tools.metadata.engines.JdbcMetadataTypeManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;

/**
 * Decorates &lt;property&gt; elements in <i>custom-sql</i> models.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class CachingPropertyDecorator
    extends  AbstractPropertyDecorator
{
    /**
     * The cached Java type.
     */
    private String m__strCachedJavaType;

    /**
     * The cached name lowercased.
     */
    private String m__strCachedNameLowercased;

    /**
     * Whether the type refers to a number smaller than int.
     */
    private Boolean m__bCachedNumberSmallerThanInt;

    /**
     * The cached capitalized name.
     */
    private String m__strCachedColumnNameNormalizedCapitalized;

    /**
     * Creates a <code>CachingPropertyDecorator</code> to decorate given property.
     * @param property the property to decorate.
     * @precondition property != null
     * @precondition metadataManager != null
     */
    public CachingPropertyDecorator(
        final Property property, final MetadataManager metadataManager)
    {
        super(property, metadataManager);
    }

    /**
     * Specifies the cached Java type.
     * @param type such type.
     */
    protected final void immutableSetCachedJavaType(final String type)
    {
        m__strCachedJavaType = type;
    }

    /**
     * Specifies the cached Java type.
     * @param type such type.
     */
    protected void setCachedJavaType(final String type)
    {
        immutableSetCachedJavaType(type);
    }

    /**
     * Retrieves the cached Java type.
     * @return such type.
     */
    public String getCachedJavaType()
    {
        return m__strCachedJavaType;
    }

    /**
     * Specifies the cached name lowercased.
     * @param nameLowercased such value.
     */
    protected final void immutableSetCachedNameLowercased(
        final String nameLowercased)
    {
        m__strCachedNameLowercased = nameLowercased;
    }

    /**
     * Specifies the cached name lowercased.
     * @param nameLowercased such value.
     */
    protected void setCachedNameLowercased(
        final String nameLowercased)
    {
        immutableSetCachedNameLowercased(nameLowercased);
    }

    /**
     * Retrieves the cached name lowercased.
     * @return such value.
     */
    public String getCachedNameLowercased()
    {
        return m__strCachedNameLowercased;
    }

    /**
     * Retrieves the Java type of the property.
     * @return such information.
     */
    public String getJavaType()
    {
        String result = getCachedJavaType();

        if  (result == null)
        {
            result = super.getJavaType();
            setCachedJavaType(result);
        }

        return result;
    }

    /**
     * Retrieves the name, in lower case.
     * @return such information.
     */
    public String getNameLowercased()
    {
        String result = getCachedNameLowercased();

        if  (result == null)
        {
            result = super.getNameLowercased();
            setCachedNameLowercased(result);
        }

        return result;
    }

    /**
     * Specifies whether the type means the attribute is a
     * number smaller than an int.
     * @param flag such condition.
     */
    protected final void immutableSetCachedNumberSmallerThanInt(
        final Boolean flag)
    {
        m__bCachedNumberSmallerThanInt = flag;
    }

    /**
     * Specifies whether the type means the attribute is a
     * number smaller than an int.
     * @param flag such condition.
     */
    protected void setCachedNumberSmallerThanInt(
        final Boolean flag)
    {
        immutableSetCachedNumberSmallerThanInt(flag);
    }

    /**
     * Retrieves whether the type means the attribute is a
     * number smaller than an int.
     * @return such condition.
     */
    protected Boolean getCachedNumberSmallerThanInt()
    {
        return m__bCachedNumberSmallerThanInt;
    }

    /**
     * Retrieves whether the type means the attribute is a
     * number smaller than an int.
     * @return such condition.
     */
    public boolean getNumberSmallerThanInt()
    {
        Boolean result = getCachedNumberSmallerThanInt();

        if  (result == null)
        {
            result =
                (super.isNumberSmallerThanInt())
                ?  Boolean.TRUE
                :  Boolean.FALSE;

            setCachedNumberSmallerThanInt(result);
        }

        return result.booleanValue();
    }

    /**
     * Specifies the cached capitalized name.
     * @param name such name.
     */
    protected final void immutableSetCachedColumnNameNormalizedCapitalized(
        final String name)
    {
        m__strCachedColumnNameNormalizedCapitalized = name;
    }

    /**
     * Specifies the cached capitalized name.
     * @param name such name.
     */
    protected void setCachedColumnNameNormalizedCapitalized(
        final String name)
    {
        immutableSetCachedColumnNameNormalizedCapitalized(name);
    }

    /**
     * Retrieves the cached capitalized name.
     * @return such name.
     */
    public String getCachedColumnNameNormalizedCapitalized()
    {
        return m__strCachedColumnNameNormalizedCapitalized;
    }

    /**
     * Retrieves the capitalized name.
     * @return such name.
     */
    public String getColumnNameNormalizedCapitalized()
    {
        String result = getCachedColumnNameNormalizedCapitalized();

        if  (result == null)
        {
            result = super.getColumnNameNormalizedCapitalized();
            setCachedColumnNameNormalizedCapitalized(result);
        }

        return result;
    }
}
