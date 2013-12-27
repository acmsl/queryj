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
 * Filename: BasePerForeignKeyFillTemplateChain.java
 *
 * Author: Jose San Leandro Armendariz (chous)
 *
 * Description: Sets up the chain required to provide placeholder replacements
 *              for per-foreign-key templates.
 *
 * Date: 6/3/12
 * Time: 12:38 PM
 *
 */
package org.acmsl.queryj.placeholders;

/*
 *Importing project classes.
*/
import org.acmsl.queryj.QueryJCommand;
import org.acmsl.queryj.api.exceptions.QueryJBuildException;
import org.acmsl.queryj.api.AbstractFillTemplateChain;
import org.acmsl.queryj.api.PerForeignKeyTemplateContext;
import org.acmsl.queryj.api.handlers.TemplateContextFillAdapterHandler;
import org.acmsl.queryj.api.handlers.fillhandlers.FillHandler;
import org.acmsl.queryj.metadata.ForeignKeyDecorator;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;

/*
 * Importing JDK classes.
 */
import java.util.ArrayList;
import java.util.List;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/**
 * Sets up the chain required to provide placeholder replacements for
 * {@link org.acmsl.queryj.api.PerForeignKeyTemplate per-foreign-key templates}.
 * @author <a href="mailto:chous@acm-sl.org">chous</a>
 * @since 3.0
 * Created: 2012/06/18
 */
@ThreadSafe
public class BasePerForeignKeyFillTemplateChain
    extends AbstractFillTemplateChain<PerForeignKeyTemplateContext>
{
    /**
     * Creates a {@link BasePerForeignKeyFillTemplateChain} using given context.
     * @param context the {@link org.acmsl.queryj.api.PerForeignKeyTemplateContext context}.
     */
    public BasePerForeignKeyFillTemplateChain(@NotNull final PerForeignKeyTemplateContext context)
    {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public QueryJCommand providePlaceholders(final boolean relevantOnly)
        throws QueryJBuildException
    {
        return new FillTemplateChainWrapper<PerForeignKeyTemplateContext>(this).providePlaceholders(relevantOnly);
    }

    /**
     * Retrieves the additional per-custom-foreignkey handlers.
     * @param context the {@link org.acmsl.queryj.api.PerForeignKeyTemplateContext context}.
     * @return such handlers.
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected List<FillHandler<?>> getHandlers(@NotNull final PerForeignKeyTemplateContext context)
    {
        @NotNull final List<FillHandler<?>> result = new ArrayList<FillHandler<?>>(2);

        result.add(
            new TemplateContextFillAdapterHandler
                <PerForeignKeyTemplateContext, ForeignKeyHandler,ForeignKeyDecorator>(
                    new ForeignKeyHandler(context)));

        result.add(
            new TemplateContextFillAdapterHandler
                <PerForeignKeyTemplateContext, ForeignKeyAttributeTypeImportsHandler,List<String>>(
                    new ForeignKeyAttributeTypeImportsHandler(context)));

        return result;
    }
}
