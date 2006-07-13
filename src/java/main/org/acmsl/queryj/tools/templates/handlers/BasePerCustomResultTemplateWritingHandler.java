//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-2006  Jose San Leandro Armendariz
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
    Contact info: chous@acm-sl.org
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Filename: $RCSfile: $
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Writes per-custom-result templates.
 *
 */
package org.acmsl.queryj.tools.templates.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.QueryJCommand;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.customsql.Result;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.templates.BasePerCustomResultTemplate;
import org.acmsl.queryj.tools.templates.BasePerCustomResultTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.BasePerCustomResultTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateWritingHandler;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * Writes <i>per-custom-result</i> templates.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public abstract class BasePerCustomResultTemplateWritingHandler
    extends    AbstractQueryJCommandHandler
    implements TemplateWritingHandler
{
    /**
     * Creates a <code>BasePerCustomResultTemplateWritingHandler</code>
     * instance.
     */
    public BasePerCustomResultTemplateWritingHandler() {};

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters)
      throws  QueryJBuildException
    {
        writeTemplates(parameters, retrieveDatabaseMetaData(parameters));

        return false;
    }

    /**
     * Writes the templates.
     * @param parameters the parameters.
     * @param metaData the database metadata.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     */
    protected void writeTemplates(
        final Map parameters, final DatabaseMetaData metaData)
      throws  QueryJBuildException
    {
        try
        {
            writeTemplates(parameters, metaData.getDatabaseProductName());
        }
        catch  (final SQLException resultException)
        {
            throw
                new QueryJBuildException(
                    "Cannot retrieve database product name", resultException);
        }
    }

    /**
     * Writes the templates.
     * @param parameters the parameters.
     * @param engineName the engine name.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition engineName != null
     */
    protected void writeTemplates(
        final Map parameters, final String engineName)
      throws  QueryJBuildException
    {
        writeTemplates(
            retrieveTemplates(parameters),
            retrieveTemplateGenerator(),
            retrieveCustomSqlProvider(parameters),
            retrieveMetadataManager(parameters),
            engineName,
            parameters);
    }

    /**
     * Writes the templates.
     * @param template the template.
     * @param templateGenerator the template generator.
     * @param customSqlProvider the custom sql provider.
     * @param metadataManager the metadata manager.
     * @param engineName the engine name.
     * @param parameters the parameter map.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition template != null
     * @precondition templateGenerator != null
     * @precondition engineName != null
     * @precondition parameters != null
     */
    protected void writeTemplates(
        final BasePerCustomResultTemplate[] templates,
        final BasePerCustomResultTemplateGenerator templateGenerator,
        final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final String engineName,
        final Map parameters)
      throws  QueryJBuildException
    {
        int t_iCount = (templates != null) ? templates.length : 0;

        BasePerCustomResultTemplate t_Template = null;

        try
        {
            for  (int t_iIndex = 0; t_iIndex < t_iCount; t_iIndex++)
            {
                t_Template = templates[t_iIndex];

                if  (t_Template != null)
                {
                    templateGenerator.write(
                        t_Template,
                        retrieveOutputDir(
                            t_Template.getResult(),
                            customSqlProvider,
                            metadataManager,
                            engineName,
                            parameters));
                }
            }
        }
        catch  (final IOException ioException)
        {
            throw
                new QueryJBuildException(
                    "Cannot write the templates", ioException);
        }
    }

    /**
     * Retrieves the template generator.
     * @return such instance.
     */
    protected abstract BasePerCustomResultTemplateGenerator retrieveTemplateGenerator();

    /**
     * Retrieves the templates from the attribute map.
     * @param parameters the parameter map.
     * @return the templates.
     * @throws QueryJBuildException if the template retrieval process if faulty.
     */
    protected abstract BasePerCustomResultTemplate[] retrieveTemplates(
        final Map parameters)
      throws  QueryJBuildException;

    /**
     * Retrieves the output dir from the attribute map.
     * @param resultElement the result element.
     * @param customSqlProvider the custom sql provider.
     * @param metadataManager the metadata manager.
     * @param engineName the engine name.
     * @param parameters the parameter map.
     * @return such folder.
     * @throws QueryJBuildException if the output-dir retrieval process if faulty.
     * @precondition parameters != null
     * @precondition resultElement != null
     */
    protected File retrieveOutputDir(
        final Result result,
        final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final String engineName,
        final Map parameters)
      throws  QueryJBuildException
    {
        return
            retrieveOutputDir(
                result,
                customSqlProvider,
                metadataManager,
                retrieveProjectOutputDir(parameters),
                retrieveProjectPackage(parameters),
                retrieveUseSubfoldersFlag(parameters),
                engineName,
                parameters,
                PackageUtils.getInstance());
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param resultElement the result element.
     * @param customSqlProvider the custom sql provider.
     * @param metadataManager the metadata manager.
     * @param projectFolder the project folder.
     * @param projectPackage the project base package.
     * @param useSubfolders whether to use subfolders for tests, or
     * using a different package naming scheme.
     * @param engineName the engine name.
     * @param parameters the parameter map.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return such folder.
     * @throws QueryJBuildException if the output-dir retrieval process if faulty.
     */
    protected abstract File retrieveOutputDir(
        final Result result,
        final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final File projectFolder,
        final String projectPackage,
        final boolean useSubfolders,
        final String engineName,
        final Map parameters,
        final PackageUtils packageUtils)
      throws  QueryJBuildException;
}
