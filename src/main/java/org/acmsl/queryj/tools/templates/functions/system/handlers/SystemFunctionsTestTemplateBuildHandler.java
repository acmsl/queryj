//;-*- mode: java -*-
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
 * Filename: SystemFunctionsTestTemplateBuildHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a system functions test template using database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.functions.system.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.templates.functions.system
    .SystemFunctionsTestTemplate;
import org.acmsl.queryj.tools.templates.functions.system
    .SystemFunctionsTestTemplateFactory;
import org.acmsl.queryj.tools.templates.functions.system
    .SystemFunctionsTestTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;
import org.acmsl.queryj.tools.templates.TestTemplate;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Builds a system functions test template using database metadata.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class SystemFunctionsTestTemplateBuildHandler
    extends    AbstractQueryJCommandHandler
    implements TemplateBuildHandler
{
    /**
     * Creates a <code>SystemFunctionsTestTemplateBuildHandler</code>
     * instance.
     */
    public SystemFunctionsTestTemplateBuildHandler() {};

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(@NotNull final Map parameters)
        throws  QueryJBuildException
    {
        if  (retrieveExtractFunctions(parameters))
        {
            buildTemplate(
                retrieveDatabaseMetaData(parameters),
                retrievePackage(parameters),
                retrieveTestedPackage(parameters),
                SystemFunctionsTestTemplateGenerator.getInstance(),
                parameters,
                StringUtils.getInstance());
        }

        return false;
    }

    /**
     * Builds the <code>SystemFunctionsTest</code> template.
     * @param metadata the <code>DatabaseMetaData</code> instance.
     * @param packageName the package name.
     * @param testedPackage the tested package.
     * @param generator the <code>SystemFuynctionsTestTemplateGenerator</code>
     * instance.
     * @param parameters the map to store the template into.
     * @param stringUtils the >code>StringUtils</code> instance.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition metadata != null
     * @precondition packageName != null
     * @precondition testedPackage != null
     * @precondition generator != null
     * @precondition parameters != null
     * @preocndition strungUtils != null
     */
    protected void buildTemplate(
        @NotNull final DatabaseMetaData metadata,
        final String packageName,
        final String testedPackage,
        @NotNull final SystemFunctionsTestTemplateGenerator generator,
        @NotNull final Map parameters,
        @NotNull final StringUtils stringUtils)
      throws  QueryJBuildException
    {
        try 
        {
            @Nullable SystemFunctionsTestTemplate t_Template =
                generator.createSystemFunctionsTestTemplate(
                    packageName,
                    testedPackage,
                    metadata.getDatabaseProductName(),
                    metadata.getDatabaseProductVersion(),
                    fixQuote(metadata.getIdentifierQuoteString()));

            Collection t_cFunctions =
                stringUtils.tokenize(metadata.getSystemFunctions(), ",");

            @Nullable Iterator t_itFunctions =
                (t_cFunctions != null) ? t_cFunctions.iterator() : null;

            if  (t_itFunctions != null) 
            {
                String t_strFunction;

                while  (t_itFunctions.hasNext())
                {
                    t_strFunction = (String) t_itFunctions.next();

                    t_Template.addFunction(t_strFunction);
                }
            }

            storeSystemFunctionsTestTemplate(t_Template, parameters);
        }
        catch  (@NotNull final SQLException sqlException)
        {
            throw
                new QueryJBuildException(
                      "Cannot retrieve database product name, "
                    + "version or quote string",
                    sqlException);
        }
    }

    /**
     * Retrieves whether the functions should be extracted or not.
     * @param parameters the parameter map.
     * @return such information.
     * @precondition parameters != null
     */
    protected boolean retrieveExtractFunctions(@NotNull final Map parameters)
    {
        boolean result = true;

        @NotNull Boolean t_bResult =
            (Boolean)
                parameters.get(
                    ParameterValidationHandler.EXTRACT_FUNCTIONS);

        if  (t_bResult != null)
        {
            result = t_bResult.booleanValue();
        }

        return result;
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @precondition parameters != null
     */
    protected String retrievePackage(@NotNull final Map parameters)
    {
        return retrievePackage(parameters, PackageUtils.getInstance());
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    protected String retrievePackage(
        @NotNull final Map parameters, @NotNull final PackageUtils packageUtils)
    {
        return
            packageUtils.retrieveTestFunctionsPackage(
                retrieveProjectPackage(parameters),
                retrieveUseSubfoldersFlag(parameters));
    }

    /**
     * Retrieves the tested package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @precondition parameters != null
     */
    protected String retrieveTestedPackage(@NotNull final Map parameters)
    {
        return retrieveTestedPackage(parameters, PackageUtils.getInstance());
    }

    /**
     * Retrieves the tested package name from the attribute map.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    protected String retrieveTestedPackage(
        @NotNull final Map parameters, @NotNull final PackageUtils packageUtils)
    {
        return
            packageUtils.retrieveFunctionsPackage(
                retrieveProjectPackage(parameters));
    }

    /**
     * Retrieves the test template collection.
     * @param parameters the parameter map.
     * @return the test templates.
     * @precondition parameters != null
     */
    @NotNull
    protected Collection retrieveTestTemplates(@NotNull final Map parameters)
    {
        return
            (Collection)
               parameters.get(TemplateMappingManager.TEST_TEMPLATES);
    }

    /**
     * Stores the system functions test template.
     * @param template the test template.
     * @param parameters the parameter map.
     * @precondition template != null
     * @precondition parameters != null
     */
    protected void storeSystemFunctionsTestTemplate(
        final SystemFunctionsTestTemplate template,
        @NotNull final Map parameters)
    {
        parameters.put(
            TemplateMappingManager.SYSTEM_FUNCTIONS_TEST_TEMPLATE,
            template);

        storeTestTemplate(template, parameters);
    }

    /**
     * Stores the test template collection.
     * @param templates the test templates.
     * @param parameters the parameter map.
     * @return the test templates.
     * @precondition templates != null
     * @precondition parameters != null
     */
    protected void storeTestTemplates(
        final Collection templates, @NotNull final Map parameters)
    {
        parameters.put(TemplateMappingManager.TEST_TEMPLATES, templates);
    }

    /**
     * Stores a new test template.
     * @param template the test template.
     * @param parameters the parameter map.
     * @precondition template != null
     * @precondition parameters != null
     */
    protected void storeTestTemplate(
        final TestTemplate template, @NotNull final Map parameters)
    {
        @NotNull Collection t_cTestTemplates = retrieveTestTemplates(parameters);

        if  (t_cTestTemplates == null) 
        {
            t_cTestTemplates = new ArrayList();
            storeTestTemplates(t_cTestTemplates, parameters);
        }

        t_cTestTemplates.add(template);
    }
}
