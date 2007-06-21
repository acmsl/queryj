/*
                        QueryJ

    Copyright (C) 2002-2007  Jose San Leandro Armendariz
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
 * Filename: ResultSetFlagsRefElement.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Models <resultset-flags-ref> elements in custom-sql models.
 *
 */
package org.acmsl.queryj.tools.customsql;

/*
 * Importing project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.AbstractIdElement;

/**
 * Models &lt;resultset-flags-ref&gt; elements in <i>custom-sql</i> models, which
 * satisfy the following DTD extract (to describe the model even in
 * non-xml implementations):
 *  <!ELEMENT resultset-ref EMPTY>
 *  <!ATTLIST resultset-flags-ref
 *    id IDREF #REQUIRED>
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public class ResultSetFlagsRefElement
    extends  AbstractIdElement
    implements  ResultSetFlagsRef
{
    /**
     * Creates a ResultSetFlagsRefElement with given information.
     * @param id the <i>id</i> attribute.
     * @precondition id != null
     */
    public ResultSetFlagsRefElement(final String id)
    {
        super(id);
    }
}