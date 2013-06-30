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
 * Filename: BasePerCustomResultFillTemplateChain.java
 *
 * Author: Jose San Leandro Armendariz (chous)
 *
 * Description: Sets up the chain required to provide placeholder replacements
 *              for per-custom-result templates.
 *
 * Date: 6/17/12
 * Time: 12:38 PM
 *
 */
package org.acmsl.queryj.placeholders;

/*
 *Importing project classes.
*/
import org.acmsl.queryj.metadata.ResultDecorator;
import org.acmsl.queryj.api.AbstractFillTemplateChain;
import org.acmsl.queryj.api.PerCustomResultTemplateContext;
import org.acmsl.queryj.api.handlers.TemplateContextFillAdapterHandler;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.patterns.Chain;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/*
 * Importing JDK classes.
 */
import java.util.List;

/**
 * Sets up the chain required to provide placeholder replacements for
 * {@link org.acmsl.queryj.api.PerCustomResultTemplate per-custom-result templates}.
 * @author <a href="mailto:chous@acm-sl.org">chous</a>
 * @since 2012/06/17
 */
@ThreadSafe
@SuppressWarnings("unused")
public class BasePerCustomResultFillTemplateChain
    extends AbstractFillTemplateChain<PerCustomResultTemplateContext, CustomResultHandler>
{
    /**
     * Creates a {@link BasePerCustomResultFillTemplateChain} using given context.
     * @param context the {@link org.acmsl.queryj.api.PerCustomResultTemplateContext context}.
     */
    public BasePerCustomResultFillTemplateChain(@NotNull final PerCustomResultTemplateContext context)
    {
        super(context);
    }

    /**
     * Adds additional per-custom-result handlers.
     * @param chain the chain to be configured.
     * @param context the {@link org.acmsl.queryj.api.PerCustomResultTemplateContext context}.
     * @param relevantOnly whether to include only relevant placeholders.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void addHandlers(
        @NotNull final Chain<CustomResultHandler> chain,
        @NotNull final PerCustomResultTemplateContext context,
        final boolean relevantOnly)
    {
        add(
            chain,
            new TemplateContextFillAdapterHandler<PerCustomResultTemplateContext, CustomResultHandler, ResultDecorator>(
                new CustomResultHandler(context)),
            relevantOnly);
        add(
            chain,
            new TemplateContextFillAdapterHandler<PerCustomResultTemplateContext, CustomResultTypeImportsHandler, List<String>>(
                new CustomResultTypeImportsHandler(context)),
            relevantOnly);

        add(
            chain,
            new TemplateContextFillAdapterHandler<PerCustomResultTemplateContext, ResultIdHandler, DecoratedString>(
                new ResultIdHandler(context)),
            relevantOnly);
    }
}