/*
                  QueryJ's Template Packaging

    Copyright (C) 2013-today Jose San Leandro Armendariz
                              queryj@acm-sl.org

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
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Author: QueryJ's Template Packaging
 *
 * Filename: PerTableTemplatesFeatureTemplateBuildHandler.java
 *
 * Description: Build handler for PerTableTemplatesFeatureTemplates.
 *
 * Generated by QueryJ Template Packaging's
 * org/acmsl/templates/packaging/TemplateBuildHandler.stg
 * at timestamp: 2013/12/07 12:29
 *
 */
package org.acmsl.queryj.templates.packaging.handlers;

/*
 * Importing QueryJ-API classes.
 */
import org.acmsl.queryj.QueryJCommand;
import org.acmsl.queryj.QueryJCommandWrapper;
import org.acmsl.queryj.metadata.DecoratedString;

/*
 * Importing QueryJ Template Packaging classes.
 */
import org.acmsl.queryj.templates.packaging.GlobalTemplateContext;
import org.acmsl.queryj.templates.packaging.Literals;
import org.acmsl.queryj.templates.packaging.TemplateDef;
import org.acmsl.queryj.templates.packaging.PerTableTemplatesFeatureTemplate;
import org.acmsl.queryj.templates.packaging.PerTableTemplatesFeatureTemplateFactory;

/*
 * Importing StringTemplate classes.
 */
import org.stringtemplate.v4.ST;

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
import java.util.List;

/**
 * Build handler for {@link PerTableTemplatesFeatureTemplate}s.
 * @author <a href="http://www.acm-sl.org/projects/queryj">QueryJ's Template Packaging</a>
 * Generation template: org/acmsl/templates/packaging/TemplateBuildHandler.stg
 * Created: 2013/12/07 12:29
 */
@ThreadSafe
public class PerTableTemplatesFeatureTemplateBuildHandler
    extends TemplatePackagingTestBuildHandler
                <PerTableTemplatesFeatureTemplate,
                    PerTableTemplatesFeatureTemplateFactory,
                    GlobalTemplateContext>
{
    /**
     * The key to access the templates in the command.
     */
    @NotNull static final String TEMPLATE_KEY = "PerTableTemplatesFeature_template";

    /**
     * Creates a {@code PerTableTemplatesFeatureTemplateBuildHandler}.
     */
    public PerTableTemplatesFeatureTemplateBuildHandler() {}

    /**
     * Retrieves the template factory.
     * @return the {@link PerTableTemplatesFeatureTemplateFactory} instance.
     */
    @Override
    @NotNull
    protected PerTableTemplatesFeatureTemplateFactory retrieveTemplateFactory()
    {
        return PerTableTemplatesFeatureTemplateFactory.getInstance();
    }

    /**
     * Builds the context from given parameters.
     *
     * @param templateDefs the template defs.
     * @param parameters   the command with the parameters.
     * @return the template context.
     */
    @NotNull
    @Override
    protected GlobalTemplateContext buildContext(
        @NotNull final List<TemplateDef<String>> templateDefs, @NotNull final QueryJCommand parameters)
    {
        return buildGlobalContext(templateDefs, parameters);
    }

    /**
     * Builds the final file name.
     * @param templateDefs the {@link TemplateDef} instances.
     * @param templateName the template name.
     * @return such file name.
     */
    @NotNull
    protected String buildFilename(
        @SuppressWarnings("unused") @NotNull final List<TemplateDef<String>> templateDefs,
        @NotNull final String templateName)
    {
        return
              new DecoratedString(templateName).getCapitalized().getValue().replaceAll("Feature$", "")
            + ".feature";
    }

    /**
     * Retrieves the output package for the generated file.
     * @param parameters the parameters.
     * @return such package.
     */
    @NotNull
    @Override
    protected String retrieveOutputPackage(@NotNull final QueryJCommand parameters)
    {
        return Literals.CUCUMBER_TEMPLATES;
    }

    /**
     * Retrieves the template name, using the parameters if necessary.
     * @param parameters the parameters.
     * @return the template name.
     */
    @NotNull
    @Override
    protected String retrieveTemplateName(@NotNull final QueryJCommand parameters)
    {
        return Literals.PER_TABLE_TEMPLATES_FEATURE;
    }

    /**
     * Builds the actual package name.
     * @param engineName the engine name.
     * @param projectPackage the project package.
     * @return the package name.
     */
    @NotNull
    static String buildPackageName(
        @NotNull final String engineName,
        @NotNull final String projectPackage)
    {
        @NotNull final String result;

        @NotNull final ST packageTemplate = new ST(Literals.CUCUMBER_TEMPLATES);

        packageTemplate.add(Literals.PACKAGE_NAME1, new DecoratedString(projectPackage));
        packageTemplate.add(Literals.ENGINE_NAME, new DecoratedString(engineName));

        result = packageTemplate.render();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void storeTemplate(
        @NotNull final PerTableTemplatesFeatureTemplate template,
        @NotNull final QueryJCommand parameters)
    {
        new QueryJCommandWrapper<PerTableTemplatesFeatureTemplate>(parameters)
            .setSetting(TEMPLATE_KEY, template);
    }
}
