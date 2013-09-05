/*
                        queryj

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
 * Filename: TemplatePackagingClassNameHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Resolves "class_name" placeholders, for Template Packaging.
 *
 * Date: 2013/09/04
 * Time: 20:05
 *
 */
package org.acmsl.queryj.templates.packaging.placeholders;

/*
 * Importing QueryJ-Placeholders classes.
 */
import org.acmsl.queryj.placeholders.AbstractDecoratedStringHandler;
import org.acmsl.queryj.placeholders.DecoratedString;

/*
 * Importing QueryJ-Template-Packaging classes.
 */
import org.acmsl.queryj.templates.packaging.DefaultTemplatePackagingContext;

/*
 * Importing JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/*
 * Importing JDK classes.
 */
import java.util.regex.Pattern;

/**
 * Resolves "class_name" placeholders, for Template Packaging.
 * @author <a href="mailto:queryj@acm-sl.org">Jose San Leandro</a>
 * @since 3.0
 * Created: 2013/09/04 20:05
 */
@ThreadSafe
public class TemplatePackagingClassNameHandler
    extends AbstractDecoratedStringHandler<DefaultTemplatePackagingContext>
{
    @NotNull
    private static final Pattern STG_EXT = Pattern.compile("\\.stg$");

    /**
     * Creates a new instance to resolve "class_name" placeholders in Template Packaging templates.
     * @param context the {@link DefaultTemplatePackagingContext context}.
     */
    public TemplatePackagingClassNameHandler(@NotNull final DefaultTemplatePackagingContext context)
    {
        super(context);
    }

    /**
     * Returns "class_name".
     * @return such variable name.
     */
    @NotNull
    @Override
    public String getPlaceHolder()
    {
        return "class_name";
    }

    /**
     * Resolves "class_name" values.
     * @param context the {@link DefaultTemplatePackagingContext context}.
     * @return such value.
     */
    @NotNull
    @Override
    protected String resolveContextValue(
        @NotNull final DefaultTemplatePackagingContext context)
    {
        @NotNull final String result =
              new DecoratedString(STG_EXT.matcher(context.getTemplateDef().getName()).replaceAll("")).getCapitalized();

        return result + context.getTemplateName();
    }
}
