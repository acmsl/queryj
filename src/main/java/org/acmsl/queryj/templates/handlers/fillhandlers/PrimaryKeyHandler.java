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
 * Filename: PrimaryKeyHandler.java
 *
 * Author: Jose San Leandro Armendariz (chous)
 *
 * Description: Resolves "pk_attributes" placeholders.
 *
 * Date: 5/31/12
 * Time: 2:14 AM
 *
 */
package org.acmsl.queryj.templates.handlers.fillhandlers;

/*
 * Importing project classes.
 */
import org.acmsl.queryj.metadata.MetadataManager;
import org.acmsl.queryj.metadata.MetadataUtils;
import org.acmsl.queryj.metadata.vo.Attribute;
import org.acmsl.queryj.templates.BasePerTableTemplateContext;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;

/*
 * Importing some JDK classes.
 */
import java.util.List;

/**
 * Resolves "pk_attributes" placeholders with the primary key information from given {@link org.acmsl.queryj.templates.BasePerTableTemplateContext}
 * @author <a href="mailto:chous@acm-sl.org">chous</a>
 * @since 2012/05/31
 */
@SuppressWarnings("unused")
public class PrimaryKeyHandler
    extends AbstractTemplateContextFillHandler<BasePerTableTemplateContext, List<Attribute>>
{
    /**
     *  Creates a {@link PrimaryKeyHandler} with given {@link BasePerTableTemplateContext context}.
     * @param context the context
     */
    @SuppressWarnings("unused")
    public PrimaryKeyHandler(@NotNull final BasePerTableTemplateContext context)
    {
        super(context);
    }

    /**
     * Retrieves the template value for this placeholder.
     *
     * @return such value.
     */
    @NotNull
    @Override
    protected List<Attribute> getValue(@NotNull final BasePerTableTemplateContext context)
    {
        return
            retrievePrimaryKeyAttributes(
                context.getTableName(),
                context.getMetadataManager(),
                MetadataUtils.getInstance());
    }

    /**
     * Returns "pk_attributes".
     * @return such placeholder.
     */
    @NotNull
    @Override
    public String getPlaceHolder()
    {
        return "pk_attributes";
    }

    /**
     * Retrieves the primary key attributes.
     * @return such attributes.
     */
    @NotNull
    protected List<Attribute> retrievePrimaryKeyAttributes(
        @NotNull final String tableName,
        @NotNull final MetadataManager metadataManager,
        @NotNull final MetadataUtils metadataUtils)
    {
        return
            metadataUtils.retrievePrimaryKeyAttributes(
                tableName, metadataManager);
    }

}