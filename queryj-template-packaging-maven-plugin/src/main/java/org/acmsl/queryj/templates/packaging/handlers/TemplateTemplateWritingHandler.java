/*
                        QueryJ Template Packaging Plugin

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
 * Filename: TemplateTemplateWritingHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is responsible of writing the content generated by
 *              TemplateTemplates.
 *
 * Date: 2013/08/21
 * Time: 21:18
 *
 */
package org.acmsl.queryj.templates.packaging.handlers;

/*
 * Importing QueryJ-API classes.
 */
import org.acmsl.queryj.QueryJCommand;
import org.acmsl.queryj.QueryJCommandWrapper;
import org.acmsl.queryj.api.exceptions.QueryJBuildException;

/*
 * Importing QueryJ Template Packaging classes.
 */
import org.acmsl.queryj.templates.packaging.DefaultTemplatePackagingContext;
import org.acmsl.queryj.templates.packaging.exceptions.MissingTemplatesException;
import org.acmsl.queryj.templates.packaging.TemplatePackagingTemplateGenerator;
import org.acmsl.queryj.templates.packaging.TemplateTemplate;

/*
 * Importing JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/*
 * Importing JDK classes.
 */
import java.util.List;

/**
 * Is responsible of writing the content generated by {@link TemplateTemplate}s.
 * @author <a href="mailto:queryj@acm-sl.org">Jose San Leandro</a>
 * @since 3.0
 * Created: 2013/08/21 21:18
 */
@ThreadSafe
public class TemplateTemplateWritingHandler
    extends TemplatePackagingWritingHandler
        <TemplateTemplate<DefaultTemplatePackagingContext>,
            DefaultTemplatePackagingContext,
            TemplatePackagingTemplateGenerator<TemplateTemplate<DefaultTemplatePackagingContext>, DefaultTemplatePackagingContext>>
{
    /**
     * Retrieves the template generator.
     * @param caching whether to enable template caching.
     * @param threadCount the number of threads to use.
     * @return such instance.
     */
    @NotNull
    @Override
    protected TemplatePackagingTemplateGenerator<TemplateTemplate<DefaultTemplatePackagingContext>, DefaultTemplatePackagingContext>
        retrieveTemplateGenerator(final boolean caching, final int threadCount)
    {
        return new TemplatePackagingTemplateGenerator<TemplateTemplate<DefaultTemplatePackagingContext>, DefaultTemplatePackagingContext>(caching, threadCount);
    }

    /**
     * Retrieves the templates from the attribute map.
     * @param parameters the parameter map.
     * @return the template.
     * @throws QueryJBuildException if the template retrieval process if faulty.
     */
    @NotNull
    @Override
    protected List<TemplateTemplate<DefaultTemplatePackagingContext>> retrieveTemplates(
        @NotNull final QueryJCommand parameters)
        throws QueryJBuildException
    {
        @NotNull final List<TemplateTemplate<DefaultTemplatePackagingContext>> result;

        @Nullable final List<TemplateTemplate<DefaultTemplatePackagingContext>> aux =
            new QueryJCommandWrapper<TemplateTemplate<DefaultTemplatePackagingContext>>(parameters).getListSetting(TEMPLATE_TEMPLATES);

        if (aux == null)
        {
            throw new MissingTemplatesException("template");
        }
        else
        {
            result = aux;
        }

        return result;
    }
}
