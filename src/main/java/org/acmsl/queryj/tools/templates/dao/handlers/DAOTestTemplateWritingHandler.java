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
 * Filename: DAOTestTemplateWritingHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Writes DAO test templates.
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.handlers.TemplateWritingHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some JetBrains annotations.
 */
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
 * Writes DAO test templates.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class DAOTestTemplateWritingHandler
    extends    AbstractQueryJCommandHandler
    implements TemplateWritingHandler
{
    /**
     * Creates a <code>DAOTestTemplateWritingHandler</code> instance.
     */
    public DAOTestTemplateWritingHandler() {}

    /**
     * Handles given parameters.
     *
     *
     * @param parameters the parameters to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    @Override
    protected boolean handle(@NotNull final Map parameters)
        throws  QueryJBuildException
    {
        writeTemplates(parameters, retrieveDatabaseMetaData(parameters));

        return false;
    }
                
    /**
     * Writes the templates.
     * @param parameters the parameters to handle.
     * @param metadata the database metadata.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metadata != null
     */
    protected void writeTemplates(
        @NotNull final Map parameters, @Nullable final DatabaseMetaData metadata)
      throws  QueryJBuildException
    {
        if (metadata != null)
        {
            try
            {
                writeTemplates(
                    retrieveDAOTestTemplates(parameters),
                    retrieveOutputDir(
                        metadata.getDatabaseProductName(),
                        parameters),
                    retrieveCharset(parameters),
                    DAOTestTemplateGenerator.getInstance());
            }
            catch  (@NotNull final SQLException sqlException)
            {
                throw
                    new QueryJBuildException(
                        "Cannot retrieve database product name", sqlException);
            }
        }
    }
                
    /**
     * Writes the <code>DAOTest</code> templates.
     * @param templates the templates.
     * @param outputDir the output dir.
     * @param charset the file encoding.
     * @param generator the <code>DAOTestTemplateGenerator</code> instance.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition templates != null
     * @precondition outputDir != null
     * @precondition generator != null
     */
    protected void writeTemplates(
        @Nullable final DAOTestTemplate[] templates,
        @NotNull final File outputDir,
        final Charset charset,
        @NotNull final DAOTestTemplateGenerator generator)
      throws  QueryJBuildException
    {
        if (templates != null)
        {
            try
            {
                for (DAOTestTemplate t_Template : templates)
                {
                    generator.write(t_Template, outputDir, charset);
                }
            }
            catch  (@NotNull final IOException ioException)
            {
                throw
                    new QueryJBuildException(
                        "Cannot write the templates", ioException);
            }
        }
    }

    /**
     * Retrieves the dao test templates from the attribute map.
     * @param parameters the parameter map.
     * @return the templates.
     * @precondition parameters != null
     */
    @NotNull
    protected DAOTestTemplate[] retrieveDAOTestTemplates(@NotNull final Map parameters)
    {
        return
            (DAOTestTemplate[])
                parameters.get(
                    TemplateMappingManager.DAO_TEST_TEMPLATES);
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param engineName the engine name.
     * @param parameters the parameter map.
     * @return such folder.
     * @precondition engineName != null
     * @precondition parameters != null
     */
    @NotNull
    protected File retrieveOutputDir(
        @NotNull final String engineName, @NotNull final Map parameters)
    {
        return
            retrieveOutputDir(
                engineName, parameters, PackageUtils.getInstance());
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param engineName the engine name.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return such folder.
     * @precondition engineName != null
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    @NotNull
    protected File retrieveOutputDir(
        @NotNull final String engineName,
        @NotNull final Map parameters,
        @NotNull final PackageUtils packageUtils)
    {
        return
            packageUtils.retrieveDAOTestFolder(
                retrieveProjectOutputDir(parameters),
                retrieveProjectPackage(parameters),
                engineName,
                retrieveUseSubfoldersFlag(parameters));
    }
}
