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

 *****************************************************************************
 *
 * Filename: AttributesStatementSetterTemplate.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Generates AttributeStatementSetter sources for a concrete
 *              entity in the persistence model.
 *
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.templates.BasePerTableTemplate;
import org.acmsl.queryj.tools.templates.BasePerTableTemplateContext;

/*
 * Importing StringTemplate classes.
 */
import org.antlr.stringtemplate.StringTemplateGroup;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generates <code>AttributeStatementSetter</code> sources for a concrete
 * entity in the persistence model.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class AttributesStatementSetterTemplate
    extends  BasePerTableTemplate<BasePerTableTemplateContext>
{
    private static final long serialVersionUID = -702786604326883063L;

    /**
     * Builds an {@link AttributesStatementSetterTemplate} instance with given context.
     * @param context the {@link BasePerTableTemplateContext} instance.
     */
    public AttributesStatementSetterTemplate(@NotNull final BasePerTableTemplateContext context)
    {
        super(context);
    }

    /**
     * Retrieves the string template group.
     * @return such instance.
     */
    @Nullable
    @Override
    public StringTemplateGroup retrieveGroup()
    {
        return
            retrieveGroup(
                "/org/acmsl/queryj/dao/" + getTemplateName() + ".stg");
    }

    /**
     * Returns "AttributesStatementSetter".
     * @return such information.
     */
    @NotNull
    public String getTemplateName()
    {
        return "AttributesStatementSetter";
    }
}
