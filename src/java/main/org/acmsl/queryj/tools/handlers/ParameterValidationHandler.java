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
    Contact info: jose.sanleandro@acm-sl.com
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
 * Description: Validates the parameters of an Ant task.
 *
 */
package org.acmsl.queryj.tools.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.ant.AntExternallyManagedFieldsElement;
import org.acmsl.queryj.tools.ant.AntTablesElement;
import org.acmsl.queryj.tools.handlers.AbstractQueryJCommandHandler;
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.QueryJCommand;
import org.acmsl.queryj.tools.logging.QueryJAntLog;
import org.acmsl.queryj.tools.logging.QueryJLog;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.logging.UniqueLogFactory;
import org.acmsl.commons.utils.io.FileUtils;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.types.Path;

/*
 * Importing Commons-Logging classes.
 */
import org.apache.commons.logging.Log;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Validates the parameters of an Ant task.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public class ParameterValidationHandler
    extends  AbstractQueryJCommandHandler
{
    /**
     * The JDBC driver attribute name.
     */
    public static final String JDBC_DRIVER = "jdbc.driver";

    /**
     * The missing driver error message.
     */
    public static final String DRIVER_MISSING = "JDBC driver is missing.";

    /**
     * The JDBC URL attribute name.
     */
    public static final String JDBC_URL = "jdbc.url";

    /**
     * The missing url error message.
     */
    public static final String URL_MISSING = "JDBC url is missing.";

    /**
     * The JDBC username attribute name.
     */
    public static final String JDBC_USERNAME = "jdbc.username";

    /**
     * The missing username error message.
     */
    public static final String USERNAME_MISSING = "JDBC username is missing.";

    /**
     * The JDBC password attribute name.
     */
    public static final String JDBC_PASSWORD = "jdbc.password";

    /**
     * The missing password error message.
     */
    public static final String PASSWORD_MISSING = "JDBC password is missing.";

    /**
     * The JDBC catalog attribute name.
     */
    public static final String JDBC_CATALOG = "jdbc.catalog";

    /**
     * The missing catalog error message.
     */
    public static final String CATALOG_MISSING = "JDBC catalog is missing.";

    /**
     * The JDBC schema attribute name.
     */
    public static final String JDBC_SCHEMA = "jdbc.schema";

    /**
     * The missing schema error message.
     */
    public static final String SCHEMA_MISSING = "JDBC schema is missing.";

    /**
     * The repository attribute name.
     */
    public static final String REPOSITORY = "repository";

    /**
     * The missing repository error message.
     */
    public static final String REPOSITORY_MISSING = "Repository name is missing.";

    /**
     * The package attribute name.
     */
    public static final String PACKAGE = "package";

    /**
     * The missing package error message.
     */
    public static final String PACKAGE_MISSING = "Package is missing.";

    /**
     * The classpath attribute name.
     */
    public static final String CLASSPATH = "classpath";

    /**
     * The missing classpath error message.
     */
    public static final String CLASSPATH_MISSING = "Classpath not specified.";

    /**
     * The output-dir attribute name.
     */
    public static final String OUTPUT_DIR = "outputdir";

    /**
     * The header-file attribute name.
     */
    public static final String HEADER_FILE = "header-file";

    /**
     * The header attribute name.
     */
    public static final String HEADER = "header";

    /**
     * The header unreadable error message.
     */
    public static final String HEADER_NOT_READABLE =
        "Specified header is not readable";

    /**
     * The missing output dir error message.
     */
    public static final String OUTPUTDIR_MISSING =
         "Output directory not specified.";

    /**
     * The missing output dir not folder error message.
     */
    public static final String OUTPUTDIR_NOT_FOLDER =
         "Specified Outputdir is not a folder.";

    /**
     * The output-dir-subfolders attribute name.
     */
    public static final String OUTPUT_DIR_SUBFOLDERS = "outputdirsubfolders";

    /**
     * The extract-tables attribute name.
     */
    public static final String EXTRACT_TABLES = "extract.tables";

    /**
     * The extract-functions attribute name.
     */
    public static final String EXTRACT_FUNCTIONS = "extract.functions";

    /**
     * The extract-procedures attribute name.
     */
    public static final String EXTRACT_PROCEDURES = "extract.procedures";

    /**
     * The JNDI location for data sources  attribute name.
     */
    public static final String JNDI_DATASOURCES = "jndi.datasources";

    /**
     * The missing JNDI data sources error message.
     */
    public static final String JNDI_DATASOURCES_MISSING =
        "No JNDI location specified for retrieving DataSource instances.";

    /**
     * The invalid JNDI data sources error message.
     */
    public static final String JNDI_DATASOURCES_INVALID =
        "Invalid JNDI location for retrieving DataSource instances.";

    /**
     * The generate-mock-dao attribute name.
     */
    public static final String GENERATE_MOCK_DAO = "generate.mock.dao";

    /**
     * The generate-xml-dao attribute name.
     */
    public static final String GENERATE_XML_DAO = "generate.xml.dao";

    /**
     * The allow-empty-repository-dao attribute name.
     */
    public static final String ALLOW_EMPTY_REPOSITORY_DAO =
        "allow.empty.repository.dao";

    /**
     * The implement-marker-interfaces attribute name.
     */
    public static final String IMPLEMENT_MARKER_INTERFACES =
        "implement.marker.interfaces";

    /**
     * The tables element name.
     */
    public static final String TABLES = "tables";

    /**
     * The missing tables error message.
     */
    public static final String TABLES_MISSING =
         "Empty table information specified.";

    /**
     * The externally-managed-fields element name.
     */
    public static final String EXTERNALLY_MANAGED_FIELDS =
        "externally.managed.fields";

    /**
     * The missing externally-managed-fields error message.
     */
    public static final String EXTERNALLY_MANAGED_FIELDS_MISSING =
         "Empty externally-managed fields information specified.";

    /**
     * The custom-sql model.
     */
    public static final String CUSTOM_SQL_MODEL = "customSqlModel";

    /**
     * Whether to disable custom sql validation.
     */
    public static final String DISABLE_CUSTOM_SQL_VALIDATION =
        "disableCustomSqlValidation";

    /**
     * The sql.xml file.
     */
    public static final String SQL_XML_FILE = "sqlXmlFile";

    /**
     * The missing tables error message.
     */
    public static final String SQL_XML_FILE_MISSING =
         "No sql.xml file specified or it cannot be read.";

    /**
     * The custom-sql XML model.
     */
    public static final String CUSTOM_SQL_MODEL_XML = "xml";

    /**
     * The grammar bundle name.
     */
    public static final String GRAMMAR_BUNDLE_NAME = "grammarBundle";
    
    /**
     * The missing grammar bundle error message.
     */
    public static final String GRAMMAR_BUNDLE_NOT_FOUND =
        "Specified grammar bundle cannot be found";
    
    /**
     * Creates a <code>ParameterValidationHandler</code> instance.
     */
    public ParameterValidationHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition command != null
     */
    public boolean handle(final QueryJCommand command)
        throws  QueryJBuildException
    {
        return handle(command.getAttributeMap(), command.getLog());
    }

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @param log the log.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters, final QueryJLog log)
        throws  QueryJBuildException
    {
        validateParameters(parameters, (log instanceof QueryJAntLog));

        return false;
    }

    /**
     * Handles given parameters.
     * @param parameters the parameters to handle.
     * @param log the log.
     * @return <code>true</code> if the chain should be stopped.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters)
        throws  QueryJBuildException
    {
        validateParameters(parameters, false);

        return false;
    }

    /**
     * Validates the parameters.
     * @param parameters the parameter map.
     * @param usingAnt whether QueryJ is executed within Ant.
     * @return <code>false</code> if the chain should be stopped because
     * of invalid parameters.
     * @throws QueryJBuildException if the build process cannot be performed.
     * @precondition parameters != null
     */
    public void validateParameters(
        final Map parameters, final boolean usingAnt)
      throws  QueryJBuildException
    {
        validateParameters(
            (String) parameters.get(JDBC_DRIVER),
            (String) parameters.get(JDBC_URL),
            (String) parameters.get(JDBC_USERNAME),
            (String) parameters.get(JDBC_PASSWORD),
            (String) parameters.get(JDBC_CATALOG),
            (String) parameters.get(JDBC_SCHEMA),
            (String) parameters.get(REPOSITORY),
            (String) parameters.get(PACKAGE),
            (File) parameters.get(OUTPUT_DIR),
            (File) parameters.get(HEADER_FILE),
            (Boolean) parameters.get(OUTPUT_DIR_SUBFOLDERS),
            (Boolean) parameters.get(EXTRACT_PROCEDURES),
            (Boolean) parameters.get(EXTRACT_FUNCTIONS),
            (String) parameters.get(JNDI_DATASOURCES),
            (Boolean) parameters.get(GENERATE_MOCK_DAO),
            (String) parameters.get(CUSTOM_SQL_MODEL),
            (File) parameters.get(SQL_XML_FILE),
            (String) parameters.get(GRAMMAR_BUNDLE_NAME),
            parameters,
            usingAnt);

        if  (usingAnt)
        {
            validateAntParameters(
                (AntTablesElement) parameters.get(TABLES),
                (AntExternallyManagedFieldsElement)
                    parameters.get(EXTERNALLY_MANAGED_FIELDS),
                (Path) parameters.get(CLASSPATH),
                parameters);
        }
    }

    /**
     * Validates the parameters.
     * @param driver the JDBC driver.
     * @param url the url.
     * @param username the username.
     * @param password the password.
     * @param catalog the catalog.
     * @param schema the schema.
     * @param repository the repository.
     * @param packageName the package name.
     * @param outputdir the output folder.
     * @param header the header.
     * @param outputdirsubfolders whether to use subfolders.
     * @param extractProcedures the extract-procedures setting.
     * @param extractFunctions the extract-functions setting.
     * @param jndiDataSources the JNDI location for data sources.
     * @param generateMockDAO the generate-mock-dao-implementation setting.
     * @param customSqlModel the model for custom-sql information.
     * @param sqlXmlFile the sql.xml file.
     * @param grammarBundleName the grammar bundle name.
     * @param parameters the parameter map, to store processed information
     * such as the header contents.
     * @param usingAnt whether QueryJ is executed within Ant.
     * @throws QueryJBuildException whenever the required
     * parameters are not present or valid.
     */
    protected void validateParameters(
        final String driver,
        final String url,
        final String username,
        final String password,
        final String catalog,
        final String schema,
        final String repository,
        final String packageName,
        final File outputdir,
        final File header,
        final Boolean outputdirsubfolders,
        final Boolean extractProcedures,
        final Boolean extractFunctions,
        final String jndiDataSources,
        final Boolean generateMockDAO,
        final String customSqlModel,
        final File sqlXmlFile,
        final String grammarBundleName,
        final Map parameters,
        final boolean usingAnt)
      throws  QueryJBuildException
    {
        Log t_Log =
            UniqueLogFactory.getLog(ParameterValidationHandler.class);
                
        if  (driver == null) 
        {
            throw new QueryJBuildException(DRIVER_MISSING);
        }

        if  (url == null) 
        {
            throw new QueryJBuildException(URL_MISSING);
        }

        if  (username == null) 
        {
            throw new QueryJBuildException(USERNAME_MISSING);
        }

        /* Not mandatory.
        if  (password == null)
        {
            throw new QueryJBuildException(PASSWORD_MISSING);
        }
        */

        /* Not mandatory.
        if  (catalog == null) 
        {
            throw new QueryJBuildException(CATALOG_MISSING);
        }
        */

        if  (schema == null) 
        {
            throw new QueryJBuildException(SCHEMA_MISSING);
        }

        if  (repository == null) 
        {
            throw new QueryJBuildException(REPOSITORY_MISSING);
        }

        if  (packageName == null) 
        {
            throw new QueryJBuildException(PACKAGE_MISSING);
        }

        if  (outputdir == null) 
        {
            throw new QueryJBuildException(OUTPUTDIR_MISSING);
        }

        if  (header == null) 
        {
            if  (t_Log != null)
            {
                t_Log.info(
                      "No header specified. Using "
                    + "GPLed QueryJ's instead.");
            }
        }
        else
        {
            boolean t_bExceptionReadingHeader = false;
            
            try
            {
                parameters.put(HEADER, readFile(header));
            }
            catch  (final FileNotFoundException fileNotFoundException)
            {
                t_bExceptionReadingHeader = true;
                
                if  (t_Log != null)
                {
                    t_Log.warn(
                        "Header file not found.",
                        fileNotFoundException);
                }
            }
            catch  (final SecurityException securityException)
            {
                t_bExceptionReadingHeader = true;
                
                if  (t_Log != null)
                {
                    t_Log.warn(
                        "No permission to read header file.",
                        securityException);
                }
            }
            catch  (final IOException ioException)
            {
                t_bExceptionReadingHeader = true;
                
                if  (t_Log != null)
                {
                    t_Log.warn(
                        "Could not read header file.",
                        ioException);
                }
            }
            finally
            {
                if  (t_bExceptionReadingHeader)
                {
                    throw new QueryJBuildException(HEADER_NOT_READABLE);
                }
            }
        }
        
        if  (!outputdir.isDirectory())
        {
            throw new QueryJBuildException(OUTPUTDIR_NOT_FOLDER);
        }

        if  (jndiDataSources == null)
        {
            throw new QueryJBuildException(JNDI_DATASOURCES_MISSING);
        }

        if  (   (jndiDataSources.indexOf("\"") != -1)
             || (jndiDataSources.indexOf("\n") != -1))
        {
            throw new QueryJBuildException(JNDI_DATASOURCES_INVALID);
        }

        /* Not mandatory
        if  (customSqlModel == null)
        {
            throw new QueryJBuildException(CUSTOM_SQL_MODEL_MISSING);
        }
        */
        if  (   (CUSTOM_SQL_MODEL_XML.equals(customSqlModel))
             && (   (sqlXmlFile == null)
                 || (!sqlXmlFile.exists())
                 || (!sqlXmlFile.canRead())))
        {
            throw new QueryJBuildException(SQL_XML_FILE_MISSING);
        }

        // Not mandatory
        if  (grammarBundleName != null)
        {
            try
            {
                ResourceBundle.getBundle(grammarBundleName);
            }
            catch  (final MissingResourceException missingResourceException)
            {
                throw new QueryJBuildException(GRAMMAR_BUNDLE_NOT_FOUND);
            }
        }
    }

    /**
     * Validates the parameters explicitly coming from Ant.
     * @param tables the table information.
     * @param externallyManagedFields the externally-managed fields
     * information.
     * @param classpath the classpath.
     * @param parameters the parameter map, to store processed information
     * such as the header contents.
     * @throws QueryJBuildException whenever the required
     * parameters are not present or valid.
     */
    protected void validateAntParameters(
        final AntTablesElement tables,
        final AntExternallyManagedFieldsElement externallyManagedFields,
        final Path classpath,
        final Map parameters)
      throws  QueryJBuildException
    {
        if  (classpath == null) 
        {
            throw new QueryJBuildException(CLASSPATH_MISSING);
        }

        if  (   (tables != null)
             && (   (tables.getTables() == null)
                 || (tables.getTables().size() == 0)))
        {
            throw new QueryJBuildException(TABLES_MISSING);
        }

        if  (   (externallyManagedFields != null)
             && (   (externallyManagedFields.getFields() == null)
                 || (externallyManagedFields.getFields().size() == 0)))
        {
            throw new QueryJBuildException(EXTERNALLY_MANAGED_FIELDS_MISSING);
        }
    }

    /**
     * Reads the contents of given file.
     * @param file the file.
     * @return the file contents.
     * @throws FileNotFoundException if the file is not found.
     * @throws SecurityException if the environment prevents
     * reading the file.
     * @throws IOException if the file cannot be read for any
     * other reason.
     * @precondition file != null
     */
    protected String readFile(final File file)
        throws  FileNotFoundException,
                SecurityException,
                IOException
    {
        return readFile(file, FileUtils.getInstance());
    }

    /**
     * Reads the contents of given file.
     * @param file the file.
     * @param fileUtils the <code>FileUtils</code> instance.
     * @return the file contents.
     * @throws FileNotFoundException if the file is not found.
     * @throws SecurityException if the environment prevents
     * reading the file.
     * @throws IOException if the file cannot be read for any
     * other reason.
     * @precondition file != null
     * @precondition fileUtils != null
     */
    protected String readFile(final File file, final FileUtils fileUtils)
        throws  FileNotFoundException,
                SecurityException,
                IOException
    {
        return fileUtils.readFile(file);
    }
}
