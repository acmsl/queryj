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
 * Filename: $RCSfile: $
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Models the definition of table fields declared by the user.
 *
 */
package org.acmsl.queryj.tools.metadata.vo;

/*
 * Importing some JDK classes.
 */
import java.util.ArrayList;
import java.util.Collection;

/**
 * Models the definition of table fields declared by the user.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public abstract class AbstractField
    extends  AbstractAttribute
    implements  Field
{
    /**
     * The field type.
     */
    private String m__strType;

    /**
     * The field pk nature.
     */
    private boolean m__bPk = false;

    /**
     * The keyword.
     */
    private String m__strKeyword;

    /**
     * The query to retrieve the keyword value.
     */
    private String m__strRetrievalQuery;

    /**
     * The field fk collection.
     */
    private Collection m__cFieldFks;

    /**
     * Creates an empty <code>AbstractField</code> instance.
     */
    public AbstractField() {};

    /**
     * Creates an <code>AbstractField</code> with given information.
     * @param name the field name.
     * @param type the field type.
     * @param pk whether it participates in the table's pk.
     * @param tableName the name of the table.
     */
    public AbstractField(
        final String name,
        final String type,
        final boolean pk,
        final String tableName)
    {
        super(name, type, tableName, false);
        immutableSetPk(pk);
    }

    /**
     * Creates an <code>AbstractField</code> with given information.
     * @param name the field name.
     * @param type the field type.
     * @param pk whether it participates in the table's pk.
     * @param tableName the name of the table.
     * @param keyword the keyword.
     * @param retrievalQuery the query to retrieval field's values.
     */
    public AbstractField(
        final String name,
        final String type,
        final boolean pk,
        final String tableName,
        final String keyword,
        final String retrievalQuery)
    {
        this(name, type, pk, tableName);
        immutableSetKeyword(keyword);
        immutableSetRetrievalQuery(retrievalQuery);
    }

    /**
     * Specifies if the field is part of the primary key.
     * @param pk such information.
     */
    protected final void immutableSetPk(final boolean pk)
    {
        m__bPk = pk;
    }

    /**
     * Specifies if the field is a primary key.
     * @param pk such information.
     */
    protected void setPk(final boolean pk)
    {
        immutableSetPk(pk);
    }

    /**
     * Retrieves if the field is a primary key.
     * @return such information.
     */
    public boolean isPk()
    {
        return m__bPk;
    }

    /**
     * Specifies the field's keyword.
     * @param keyword the keyword.
     */
    protected final void immutableSetKeyword(final String keyword)
    {
        m__strKeyword = keyword;
    }

    /**
     * Specifies the field's keyword.
     * @param keyword the keyword.
     */
    protected void setKeyword(final String keyword)
    {
        immutableSetKeyword(keyword);
    }

    /**
     * Retrieves the field's keyword.
     * @return such keyword.
     */
    public String getKeyword()
    {
        return m__strKeyword;
    }

    /**
     * Specifies the query to retrieve the field value.
     * @param query such query.
     */
    protected final void immutableSetRetrievalQuery(final String query)
    {
        m__strRetrievalQuery = query;
    }

    /**
     * Specifies the query to retrieve the field value.
     * @param query such query.
     */
    protected void setRetrievalQuery(final String query)
    {
        immutableSetRetrievalQuery(query);
    }

    /**
     * Retrieves the query to retrieve the field value.
     * @return such information.
     */
    public String getRetrievalQuery()
    {
        return m__strRetrievalQuery;
    }

    /**
     * Specifies the field fk collection.
     * @param fieldFks the collection
     */
    private void immutableSetFieldFks(final Collection fieldFks)
    {
        m__cFieldFks = fieldFks;
    }

    /**
     * Specifies the field fk collection.
     * @param fieldFks the collection
     */
    protected void setFieldFks(final Collection fieldFks)
    {
        immutableSetFieldFks(fieldFks);
    }

    /**
     * Retrieves the field fk collection.
     * @return such collection.
     */
    public Collection getFieldFks()
    {
        return m__cFieldFks;
    }
}