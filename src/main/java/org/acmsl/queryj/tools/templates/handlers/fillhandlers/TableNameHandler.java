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
 * Filename: TableNameHandler.java
 *
 * Author: Jose San Leandro Armendariz (chous)
 *
 * Description: Fills the "table_name" placeholder.
 *
 * Date: 5/19/12
 * Time: 6:29 PM
 *
 */
package org.acmsl.queryj.tools.templates.handlers.fillhandlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.templates.BasePerTableTemplateContext;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;

/**
 * Fills the "table_name" placeholder.
 * @author <a href="mailto:chous@acm-sl.org">chous</a>
 * @since 2012/05/19
 */
@SuppressWarnings("unused")
public class TableNameHandler
    extends AbstractTemplateContextFillHandler<BasePerTableTemplateContext, DecoratedString>
{
    /**
     * Creates a {@link TableNameHandler} for given {@link BasePerTableTemplateContext}.
     * @param context the template context.
     */
    @SuppressWarnings("unused")
    public TableNameHandler(@NotNull final BasePerTableTemplateContext context)
    {
        super(context);
    }

    /**
     * Returns "table_name".
     * @return such placeholder.
     */
    @NotNull
    @Override
    public String getPlaceHolder()
    {
        return "table_name";
    }

    /**
     * Retrieves the table name from given template.
     * @param context the {@link BasePerTableTemplateContext} instance.
     * @return the table name.
     */
    @NotNull
    protected DecoratedString getValue(@NotNull final BasePerTableTemplateContext context)
    {
        return new DecoratedString(context.getTableName());
    }
}