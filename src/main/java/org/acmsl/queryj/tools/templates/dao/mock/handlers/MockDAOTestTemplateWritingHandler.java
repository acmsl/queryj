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
 * Filename: MockDAOTestTemplateWritingHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Writes Mock DAO test templates.
 *
 */
package org.acmsl.queryj.tools.templates.dao.mock.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.dao.mock.MockDAOTestTemplate;
import org.acmsl.queryj.tools.templates.dao.mock.MockDAOTestTemplateGenerator;
import org.acmsl.queryj.tools.templates.dao.mock.handlers.MockDAOTestTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateWritingHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * Writes Mock DAO test templates.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class MockDAOTestTemplateWritingHandler
    extends    AbstractQueryJCommandHandler
    implements TemplateWritingHandler
{
    /**
     * Creates a <code>MockDAOTestTemplateWritingHandler</code> instance.
     */
    public MockDAOTestTemplateWritingHandler() {};

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    @Override
    protected boolean handle(@NotNull final Map parameters)
        throws  QueryJBuildException
    {
        writeTemplates(
            parameters,
            retrieveMockDAOTestTemplates(parameters),
            MockDAOTestTemplateGenerator.getInstance(),
            retrieveOutputDir(parameters),
            retrieveCharset(parameters));

        return false;
    }

    /**
     * Writes the <code>MockDAOTest</code> templates..
     * @param parameters the parameters.
     * @param templates the templates.
     * @param generator the generator.
     * @param outputDir the output dir.
     * @param charset the file encoding.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition templates != null
     * @precondition generator != null
     * @precondition outputDir != null
     */
    protected void writeTemplates(
        final Map parameters,
        @Nullable final MockDAOTestTemplate[] templates,
        @NotNull final MockDAOTestTemplateGenerator generator,
        @NotNull final File outputDir,
        final Charset charset)
      throws  QueryJBuildException
    {
        try 
        {
            int t_iLength = (templates != null) ? templates.length : 0;

            for  (int t_iMockDAOTestIndex = 0;
                      t_iMockDAOTestIndex < t_iLength;
                      t_iMockDAOTestIndex++)
            {
                generator.write(
                    templates[t_iMockDAOTestIndex],
                    outputDir,
                    charset);
            }
        }
        catch  (@NotNull final IOException ioException)
        {
            throw
                new QueryJBuildException(
                    "Cannot write the templates", ioException);
        }
    }

    /**
     * Retrieves the dao test templates from the attribute map.
     * @param parameters the parameter map.
     * @return the templates.
     * @precondition parameters != null
     */
    @NotNull
    protected MockDAOTestTemplate[] retrieveMockDAOTestTemplates(
        @NotNull final Map parameters)
    {
        return
            (MockDAOTestTemplate[])
                parameters.get(
                    TemplateMappingManager.MOCK_DAO_TEST_TEMPLATES);
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param parameters the parameter map.
     * @return such folder.
     * @precondition parameters != null
     */
    @NotNull
    protected File retrieveOutputDir(@NotNull final Map parameters)
    {
        return retrieveOutputDir(parameters, PackageUtils.getInstance());
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return such folder.
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    @NotNull
    protected File retrieveOutputDir(
        @NotNull final Map parameters, @NotNull final PackageUtils packageUtils)
    {
        return
            packageUtils.retrieveMockDAOTestFolder(
                retrieveProjectOutputDir(parameters),
                retrieveProjectPackage(parameters),
                retrieveUseSubfoldersFlag(parameters));
    }
}
