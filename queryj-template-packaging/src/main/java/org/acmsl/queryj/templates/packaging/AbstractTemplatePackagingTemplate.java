/*
                        QueryJ Template Packaging

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
 * Filename: AbstractTemplatePackagingTemplate.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Abstract template for Template Packaging.
 *
 * Date: 2013/08/15
 * Time: 08:34
 *
 */
package org.acmsl.queryj.templates.packaging;

/*
 * Importing QueryJ Template Packaging classes.
 */
import org.acmsl.queryj.templates.packaging.exceptions.InvalidTemplatePackagingTemplateException;

/*
 * Importing QueryJ Core classes.
 */
import org.acmsl.queryj.api.AbstractTemplate;
import org.acmsl.queryj.api.STTemplate;
import org.acmsl.queryj.api.exceptions.InvalidTemplateException;

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
 * Importing StringTemplate classes.
 */
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/*
 * Importing JDK classes.
 */
import java.util.Arrays;

/**
 * Abstract template for Template Packaging.
 * @param <C> the context type.
 * @author <a href="mailto:queryj@acm-sl.org">Jose San Leandro</a>
 * @since 3.0
 * Created: 2013/08/15 08/34
 */
@ThreadSafe
public abstract class AbstractTemplatePackagingTemplate<C extends TemplatePackagingContext>
    extends AbstractTemplate<C>
    implements STTemplate<C>,
               TemplatePackagingSettings,
               TemplatePackagingTemplate<C>
{
    /**
     * The serial version id.
     */
    private static final long serialVersionUID = 1307562443874023658L;

    /**
     * Creates a new instance.
     * @param context the {@link TemplatePackagingContext context}.
     */
    protected AbstractTemplatePackagingTemplate(@NotNull final C context)
    {
        super(context, "org.acmsl.queryj.templates.packaging.placeholders");
    }

    /**
     * Retrieves the string template group.
     * @return such instance.
     */
    @Nullable
    @Override
    public STGroup retrieveGroup()
    {
        return
            retrieveGroup(
                TEMPLATE_PACKAGING_GROUP + getTemplateName() + ".stg",
                Arrays.asList(Literals.ORG_ACMSL_QUERYJ_TEMPLATES_PACKAGING));
    }

    /**
     * Builds the header for logging purposes.
     * @return such header.
     */
    @NotNull
    @Override
    protected String buildHeader()
    {
        return buildHeader(getTemplateName());
    }

    /**
     * Builds the header for logging purposes.
     * @param templateName the template name.
     * @return such header.
     */
    @NotNull
    protected String buildHeader(@NotNull final String templateName)
    {
        return org.acmsl.queryj.Literals.GENERATING + templateName;
    }

    /**
     * Builds a context-specific exception.
     * @param context the context.
     * @param template the {@link org.stringtemplate.v4.ST} instance.
     * @return the specific {@link InvalidTemplateException} for the template.
     */
    @Override
    @NotNull
    public InvalidTemplateException buildInvalidTemplateException(
        @NotNull final C context,
        @NotNull final ST template,
        @NotNull final Throwable actualException)
    {
        return
            new InvalidTemplatePackagingTemplateException(
                getTemplateName(),
                actualException);
    }

    /**
     * Retrieves the source code generated by this template.
     * @param header the header.
     * @param context the context.
     * @param relevantOnly whether to include only relevant placeholders.
     * @return such code.
     */
    @Nullable
    @SuppressWarnings("unused")
    protected String generateOutput1(
        @Nullable final String header, @NotNull final C context, final boolean relevantOnly)
        throws InvalidTemplateException
    {
        // TODO
        return null;
    }
}
