/*
                        QueryJ

    Copyright (C) 2002  Jose San Leandro Armendariz
                        jsanleandro@yahoo.es
                        chousz@yahoo.com

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
    Contact info: jsanleandro@yahoo.es
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabanas
                    Boadilla del monte
                    28660 Madrid
                    Spain

 ******************************************************************************
 *
 * Filename: $RCSfile$
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Builds a DAO factory template using database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.dao.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.DatabaseMetaDataManager;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.PackageUtils;
import org.acmsl.queryj.tools.MetaDataUtils;
import org.acmsl.queryj.tools.templates.dao.DAOFactoryTemplate;
import org.acmsl.queryj.tools.templates.dao.DAOFactoryTemplateFactory;
import org.acmsl.queryj.tools.templates.dao.DAOFactoryTemplateGenerator;
import org.acmsl.queryj.tools.templates.handlers.TableTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateBuildHandler;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Command;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * Builds a DAO factory template using database metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 */
public class DAOFactoryTemplateBuildHandler
    extends    AbstractAntCommandHandler
    implements TemplateBuildHandler
{
    /**
     * A cached empty table template array.
     */
    public static final TableTemplate[] EMPTY_TABLE_TEMPLATE_ARRAY =
        new TableTemplate[0];

    /**
     * Creates a DAOFactoryTemplateBuildHandler.
     */
    public DAOFactoryTemplateBuildHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition command != null
     */
    public boolean handle(final AntCommand command)
        throws  BuildException
    {
        return
            handle(
                command.getAttributeMap(),
                command.getProject(),
                command.getTask());
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(
        final Map parameters,
        final Project project,
        final Task task)
      throws  BuildException
    {
        return
            handle(
                parameters,
                retrieveDatabaseMetaData(parameters),
                retrieveBasePackage(parameters),
                project,
                task);
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param metaData the database metadata.
     * @param basePackage the base package.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaData != null
     * @precondition basePackage != null
     */
    protected boolean handle(
        final Map parameters,
        final DatabaseMetaData metaData,
        final String basePackage,
        final Project project,
        final Task task)
      throws  BuildException
    {
        boolean result = false;

        try
        {
            handle(
                parameters,
                retrieveDatabaseMetaDataManager(parameters),
                retrieveJNDIDataSource(parameters),
                retrievePackage(
                    basePackage,
                    metaData.getDatabaseProductName().toLowerCase()),
                basePackage,
                metaData.getDatabaseProductName(),
                metaData.getDatabaseProductVersion(),
                DAOTemplateBuildHandler.fixQuote(
                    metaData.getIdentifierQuoteString()),
                retrieveTableTemplates(parameters),
                DAOFactoryTemplateGenerator.getInstance(),
                project,
                task);
        }
        catch  (final SQLException sqlException)
        {
            throw new BuildException(sqlException);
        }

        return result;
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @param metaDataManager the database metadata manager.
     * @param jndiDataSource the JNDI location of the data source.
     * @param packageName the package name.
     * @param basePackage the base package.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the quote.
     * @param tableTemplates the table templates.
     * @param templateFactory the template factory.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return <code>true</code> if the chain should be stopped.
     * @throws BuildException if the build process cannot be performed.
     * @precondition parameters != null
     * @precondition metaDataManager != null
     * @precondition jndiDataSource != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition quote != null
     * @precondition tableTemplates != null
     * @precondition templateFactory != null
     */
    protected void handle(
        final Map parameters,
        final DatabaseMetaDataManager metaDataManager,
        final String jndiDataSource,
        final String packageName,
        final String basePackage,
        final String engineName,
        final String engineVersion,
        final String quote,
        final TableTemplate[] tableTemplates,
        final DAOFactoryTemplateFactory templateFactory,
        final Project project,
        final Task task)
      throws  BuildException
    {
        try
        {
            DAOFactoryTemplate[] t_aDAOFactoryTemplates =
                new DAOFactoryTemplate[tableTemplates.length];

            for  (int t_iDAOFactoryIndex = 0;
                      t_iDAOFactoryIndex < t_aDAOFactoryTemplates.length;
                      t_iDAOFactoryIndex++) 
            {
                t_aDAOFactoryTemplates[t_iDAOFactoryIndex] =
                    templateFactory.createDAOFactoryTemplate(
                        tableTemplates[t_iDAOFactoryIndex],
                        metaDataManager,
                        packageName,
                        engineName,
                        engineVersion,
                        quote,
                        basePackage,
                        jndiDataSource,
                        project,
                        task);
            }

            storeDAOFactoryTemplates(t_aDAOFactoryTemplates, parameters);
        }
        catch  (final QueryJException queryjException)
        {
            throw new BuildException(queryjException);
        }
    }

    /**
     * Retrieves the database metadata from the attribute map.
     * @param parameters the parameter map.
     * @return the metadata.
     * @throws BuildException if the metadata retrieval process if faulty.
     * @precondition parameters != null
     */
    protected DatabaseMetaData retrieveDatabaseMetaData(
        final Map parameters)
      throws  BuildException
    {
        return
            (DatabaseMetaData)
                parameters.get(
                    DatabaseMetaDataRetrievalHandler.DATABASE_METADATA);
    }

    /**
     * Retrieves the database metadata manager from the attribute map.
     * @param parameters the parameter map.
     * @return the manager.
     * @throws BuildException if the manager retrieval process if faulty.
     * @precondition parameters != null
     */
    protected DatabaseMetaDataManager retrieveDatabaseMetaDataManager(
        final Map parameters)
      throws  BuildException
    {
        return
            (DatabaseMetaDataManager)
                parameters.get(
                    DatabaseMetaDataRetrievalHandler.DATABASE_METADATA_MANAGER);
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    protected String retrieveBasePackage(final Map parameters)
        throws  BuildException
    {
        return (String) parameters.get(ParameterValidationHandler.PACKAGE);
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param basePackage the base package.
     * @param engineName the engine name.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    protected String retrievePackage(
        final String basePackage, final String engineName)
      throws  BuildException
    {
        return
            retrievePackage(
                basePackage, engineName, PackageUtils.getInstance());
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param basePackage the base package.
     * @param engineName the engine name.
     * @param packageUtils the <code>PackageUtils</code> instance.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    protected String retrievePackage(
        final String basePackage,
        final String engineName,
        final PackageUtils packageUtils)
      throws  BuildException
    {
        return
            packageUtils.retrieveDAOFactoryPackage(
                basePackage, engineName);
    }

    /**
     * Retrieves the output dir from the attribute map.
     * @param engineName the engine name.
     * @param parameters the parameter map.
     * @return such folder.
     * @throws BuildException if the output-dir retrieval process if faulty.
     * @precondition engineName != null
     * @precondition parameters != null
     */
    protected File retrieveOutputDir(
        final String engineName, final Map parameters)
      throws  BuildException
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
     * @throws BuildException if the output-dir retrieval process if faulty.
     * @precondition engineName != null
     * @precondition parameters != null
     * @precondition packageUtils != null
     */
    protected File retrieveOutputDir(
        final String engineName,
        final Map parameters,
        final PackageUtils packageUtils)
      throws  BuildException
    {
        return
            packageUtils.retrieveDAOFactoryFolder(
                (File)
                    parameters.get(
                        ParameterValidationHandler.OUTPUT_DIR),
                retrieveProjectPackage(parameters),
                engineName);
    }

    /**
     * Retrieves the JNDI location of the data source from the attribute map.
     * @param parameters the parameter map.
     * @return the JNDI location.
     * @throws BuildException if the JNDI location retrieval process if faulty.
     * @precondition parameters != null
     */
    protected String retrieveJNDIDataSource(final Map parameters)
        throws  BuildException
    {
        return
            (String)
                parameters.get(
                    ParameterValidationHandler.JNDI_DATASOURCES);
    }

    /**
     * Stores the DAO factory template collection in given attribute map.
     * @param daoFactoryTemplates the DAO factory templates.
     * @param parameters the parameter map.
     * @throws BuildException if the templates cannot be stored for any reason.
     * @precondition daoFactoryTemplates != null
     * @precondition parameters != null
     */
    protected void storeDAOFactoryTemplates(
        final DAOFactoryTemplate[] daoFactoryTemplates,
        final Map parameters)
      throws  BuildException
    {
        parameters.put(
            TemplateMappingManager.DAO_FACTORY_TEMPLATES,
            daoFactoryTemplates);
    }

    /**
     * Retrieves the table templates.
     * @param parameters the parameter map.
     * @return such templates.
     * @throws BuildException if the templates cannot be stored for any reason.
     * @precondition parameters != null
     */
    protected TableTemplate[] retrieveTableTemplates(
        final Map parameters)
      throws  BuildException
    {
        return
            (TableTemplate[])
                parameters.get(TableTemplateBuildHandler.TABLE_TEMPLATES);
    }

    /**
     * Retrieves the package name from the attribute map.
     * @param parameters the parameter map.
     * @return the package name.
     * @throws BuildException if the package retrieval process if faulty.
     * @precondition parameters != null
     */
    protected String retrieveProjectPackage(final Map parameters)
        throws  BuildException
    {
        return (String) parameters.get(ParameterValidationHandler.PACKAGE);
    }
}
