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
 * Filename: QueryJChain.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Defines the chain flow of QueryJ.
 *
 */
package org.acmsl.queryj.tools;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.AbstractQueryJChain;
import org.acmsl.queryj.QueryJCommand;
import org.acmsl.queryj.QueryJSettings;
import org.acmsl.queryj.SingularPluralFormConverter;
import org.acmsl.queryj.api.exceptions.CannotFindTemplatesException;
import org.acmsl.queryj.api.exceptions.QueryJBuildException;
import org.acmsl.queryj.customsql.handlers.CustomSqlProviderRetrievalHandler;
import org.acmsl.queryj.customsql.handlers.CustomSqlValidationHandler;
import org.acmsl.queryj.api.handlers.TemplateHandler;
import org.acmsl.queryj.api.handlers.fillhandlers.FillHandler;
import org.acmsl.queryj.tools.handlers.Log4JInitializerHandler;
import org.acmsl.queryj.tools.handlers.ParameterValidationHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataCacheWritingHandler;
import org.acmsl.queryj.tools.handlers.JdbcMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.DatabaseMetaDataLoggingHandler;
import org.acmsl.queryj.tools.handlers.JdbcConnectionOpeningHandler;
import org.acmsl.queryj.tools.handlers.JdbcConnectionClosingHandler;
import org.acmsl.queryj.tools.handlers.ExternallyManagedFieldsRetrievalHandler;
import org.acmsl.queryj.tools.handlers.QueryJCommandHandler;
import org.acmsl.queryj.tools.handlers.mysql.MySQL4xMetaDataRetrievalHandler;
import org.acmsl.queryj.tools.handlers.oracle.OracleMetaDataRetrievalHandler;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Chain;
import org.acmsl.commons.logging.UniqueLogFactory;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

/*
 * Importing some Apache Commons Logging classes.
 */
import org.apache.commons.logging.Log;

/*
 * Importing jetbrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/**
 * Defines the steps performed by QueryJ.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
@ThreadSafe
public class QueryJChain<CH extends QueryJCommandHandler<QueryJCommand>>
    extends AbstractQueryJChain<CH, QueryJCommand>
    implements QueryJSettings
{
    /**
     * The settings.
     */
    private Properties m__Settings;

    /**
     * The properties file (to include all other settings).
     */
    private File m__SettingsFile;

    /**
     * The driver.
     */
    private String m__strDriver;

    /**
     * The url.
     */
    private String m__strUrl;

    /**
     * The username.
     */
    private String m__strUsername;

    /**
     * The password.
     */
    private String m__strPassword;

    /**
     * The catalog.
     */
    private String m__strCatalog;

    /**
     * The schema.
     */
    private String m__strSchema;

    /**
     * The repository.
     */
    private String m__strRepository;

    /**
     * The package.
     */
    private String m__strPackage;

    /**
     * The output folder.
     */
    private File m__Outputdir;

    /**
     * The optional header.
     */
    private File m__Header;

    /**
     * The "outputdirsubfolders" value.
     */
    private String m__strOutputdirsubfolders;

    /**
     * Whether to use sub folders or not.
     */
    private boolean m__bOutputdirsubfolders = false;

    /**
     * The "extract-tables" value.
     */
    private String m__strExtractTables;

    /**
     * The "extract-tables" flag.
     */
    private boolean m__bExtractTables = true;

    /**
     * The "extract-procedures" value.
     */
    private String m__strExtractProcedures;

    /**
     * The "extract-procedures" flag.
     */
    private boolean m__bExtractProcedures = true;

    /**
     * The "extract-functions" value.
     */
    private String m__strExtractFunctions;

    /**
     * The "extract-functions" flag.
     */
    private boolean m__bExtractFunctions = true;

    /**
     * The JNDI location for the data sources.
     */
    private String m__strJNDIDataSources;

    /**
     * The "generate-mock-dao-implementation" value.
     */
    private String m__strGenerateMockDAOImplementation;

    /**
     * The "generate-mock-dao-implementation" flag.
     */
    private boolean m__bGenerateMockDAOImplementation = true;

    /**
     * The "generate-xml-dao-implementation" value.
     */
    private String m__strGenerateXMLDAOImplementation;

    /**
     * The "generate-xml-dao-implementation" flag.
     */
    private boolean m__bGenerateXMLDAOImplementation = true;

    /**
     * The "generate-tests" value.
     */
    private String m__strGenerateTests;

    /**
     * The "generate-tests" flag.
     */
    private boolean m__bGenerateTests = false;

    /**
     * The "custom-sql-model" type.
     */
    @Nullable
    private String m__strCustomSqlModel = null;

    /**
     * The "sql-xml-file" path.
     */
    private File m__SqlXmlFile;

    /**
     * The "grammar-folder" property.
     */
    private File m__GrammarFolder;

    /**
     * The "grammar-name" property.
     */
    private String m__strGrammarName;

    /**
     * The "grammar-suffix" property.
     */
    private String m__strGrammarSuffix;

    /**
     * The <i>allowEmptyRepositoryDAO</i> flag.
     */
    private boolean m__bAllowEmptyRepositoryDAO = false;

    /**
     * The <i>allowEmptyRepositoryDAO</i> property.
     */
    private String m__strAllowEmptyRepositoryDAO;

    /**
     * The <i>implementMarkerInterfaces</i> flag.
     */
    private boolean m__bImplementMarkerInterfaces = false;

    /**
     * The <i>implementMarkerInterfaces</i> property.
     */
    private String m__strImplementMarkerInterfaces;

    /**
     * The <i>disableCustomSqlValidation</i> flag.
     */
    private boolean m__bDisableCustomSqlValidation;

    /**
     * The <i>disableCustomSqlValidation</i> property.
     */
    private String m__strDisableCustomSqlValidation;

    /**
     * The <i>encoding</i> property.
     */
    private String m__strEncoding;

    /**
     * Whether to use template caching.
     */
    private boolean m__bCaching;

    /**
     * Whether to disable caching or not.
     */
    private String m__strDisableCaching;
    private boolean m__bDisableCachingFlag;

    /**
     * The thread count.
     */
    private int m__iThreadCount;

    /**
     * Whether to generate file timestamps.
     */
    private String m__strDisableGenerationTimestamps;
    private boolean m__bDisableGenerationTimestampsFlag;

    /**
     * Whether to disable NotNull annotations.
     */
    private String m__strDisableNotNullAnnotations;
    private Boolean m__bDisableNotNullAnnotationsFlag;

    /**
     * Whether to disable checkthread.org annotations.
     */
    private String m__strDisableCheckthreadAnnotations;
    private Boolean m__bDisableCheckthreadAnnotationsFlag;

    /**
     * Creates a {@link QueryJChain} instance.
     */
    public QueryJChain() {}

    /**
     * Creates a {@link QueryJChain} with given information.
     * @param settings the configuration settings.
     */
    public QueryJChain(final Properties settings)
    {
        immutableSetSettings(settings);
    }

    /**
     * Creates a {@link QueryJChain} with given information.
     * @param settings the configuration settings.
     * @param driver the JDBC driver.
     * @param url the JDBC url.
     * @param username the JDBC username.
     * @param password the JDBC password.
     * @param catalog the JDBC catalog.
     * @param schema the JDBC schema.
     * @param repository the repository.
     * @param packageName the base package of the generated sources.
     * @param outputDir the output folder.
     * @param header the copyright header.
     * @param outputDirSubFolders whether to use main/ and test/ as
     * subfolders.
     * @param extractTables whether to extract tables or not.
     * @param extractProcedures whether to extract the procedures or not.
     * @param extractFunctions whether to extract the functions or not.
     * @param jndiDataSource the location in JNDI of the
     * {@link javax.sql.DataSource}.
     * @param generateMockDAOImplementation whether to generate Mock DAOs.
     * @param generateXmlDAOImplementation whether to generate XML DAOs.
     * @param generateTests whether to generate test cases or not.
     * @param allowEmptyRepositoryDAO whether to generate a repository
     * DAO even tough it'll contain no custom queries.
     * @param implementMarkerInterfaces whether to make some generated 
     * sources implement {@link org.acmsl.commons.patterns} Marker
     * interfaces.
     * @param customSqlModel the format of the custom SQL file.
     * @param disableCustomSqlValidation to disable validation of custom sql.
     * @param sqlXmlFile the file containing the custom SQL.
     * @param grammarFolder the grammar folder.
     * @param grammarName the grammar with irregular singular and plural
     * forms of the table names.
     * @param grammarSuffix the grammar suffix.
     * @param encoding the file encoding.
     * @param caching whether to use template caching.
     * @param threadCount the thread count.
     */
    @SuppressWarnings("unused")
    public QueryJChain(
        @NotNull final File settings,
        @NotNull final String driver,
        @NotNull final String url,
        @NotNull final String username,
        @NotNull final String password,
        @NotNull final String catalog,
        @NotNull final String schema,
        @NotNull final String repository,
        @NotNull final String packageName,
        @NotNull final File outputDir,
        @NotNull final File header,
        final boolean outputDirSubFolders,
        final boolean extractTables,
        final boolean extractProcedures,
        final boolean extractFunctions,
        @NotNull final String jndiDataSource,
        final boolean generateMockDAOImplementation,
        final boolean generateXmlDAOImplementation,
        final boolean generateTests,
        final boolean allowEmptyRepositoryDAO,
        final boolean implementMarkerInterfaces,
        @NotNull final String customSqlModel,
        final boolean disableCustomSqlValidation,
        @NotNull final File sqlXmlFile,
        @NotNull final File grammarFolder,
        @NotNull final String grammarName,
        @NotNull final String grammarSuffix,
        @NotNull final String encoding,
        final boolean caching,
        final int threadCount)
    {
        immutableSetSettingsFile(settings);
        immutableSetDriver(driver);
        immutableSetUrl(url);
        immutableSetUsername(username);
        immutableSetPassword(password);
        immutableSetCatalog(catalog);
        immutableSetSchema(schema);
        immutableSetRepository(repository);
        immutableSetPackage(packageName);
        immutableSetOutputdir(outputDir);
        immutableSetHeaderfile(header);
        immutableSetOutputdirsubfoldersFlag(outputDirSubFolders);
        immutableSetExtractTablesFlag(extractTables);
        immutableSetExtractProceduresFlag(extractProcedures);
        immutableSetExtractFunctionsFlag(extractFunctions);
        immutableSetJndiDataSource(jndiDataSource);
        immutableSetGenerateMockDAOImplementationFlag(
            generateMockDAOImplementation);
        immutableSetGenerateXMLDAOImplementationFlag(
            generateXmlDAOImplementation);
        immutableSetGenerateTestsFlag(generateTests);
        immutableSetAllowEmptyRepositoryDAOFlag(allowEmptyRepositoryDAO);
        immutableSetImplementMarkerInterfacesFlag(implementMarkerInterfaces);
        immutableSetCustomSqlModel(customSqlModel);
        immutableSetDisableCustomSqlValidationFlag(disableCustomSqlValidation);
        immutableSetSqlXmlFile(sqlXmlFile);
        immutableSetGrammarFolder(grammarFolder);
        immutableSetGrammarName(grammarName);
        immutableSetGrammarSuffix(grammarSuffix);
        immutableSetEncoding(encoding);
        immutableSetCaching(caching);
        immutableSetThreadCount(threadCount);
    }
    /**
     * Specifies the properties.
     * @param settings such settings.
     */
    protected final void immutableSetSettings(final Properties settings)
    {
        m__Settings = settings;
    }

    /**
     * Specifies the properties.
     * @param settings such settings.
     */
    @SuppressWarnings("unused")
    public void setSettings(final Properties settings)
    {
        immutableSetSettings(settings);
    }

    /**
     * Retrieves the properties file.
     * @return such file.
     */
    public Properties getSettings()
    {
        return m__Settings;
    }

    /**
     * Specifies the properties file.
     * @param file such file.
     */
    protected final void immutableSetSettingsFile(final File file)
    {
        m__SettingsFile = file;
    }

    /**
     * Specifies the properties file.
     * @param file such file.
     */
    public void setSettingsFile(final File file)
    {
        immutableSetSettingsFile(file);
    }

    /**
     * Retrieves the properties file.
     * @return such file.
     */
    public File getSettingsFile()
    {
        return m__SettingsFile;
    }

    /**
     * Specifies the driver.
     * @param driver the new driver.
     */
    protected final void immutableSetDriver(final String driver)
    {
        m__strDriver = driver;
    }

    /**
     * Specifies the driver.
     * @param driver the new driver.
     */
    public void setDriver(final String driver)
    {
        immutableSetDriver(driver);
    }

    /**
     * Retrieves the driver.
     * @return such information.
     */
    public String getDriver()
    {
        return m__strDriver;
    }

    /**
     * Retrieves the driver, using given properties if necessary.
     * @param properties the properties.
     * @return the JDBC driver.
     */
    protected String getDriver(@Nullable final Properties properties)
    {
        String result = getDriver();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(JDBC_DRIVER);
            setDriver(result);
        }

        return result;
    }

    /**
     * Specifies the url.
     * @param url the new url.
     */
    protected final void immutableSetUrl(final String url)
    {
        m__strUrl = url;
    }

    /**
     * Specifies the url.
     * @param url the new url.
     */
    public void setUrl(final String url)
    {
        immutableSetUrl(url);
    }

    /**
     * Retrieves the url.
     * @return such information.
     */
    public String getUrl()
    {
        return m__strUrl;
    }

    /**
     * Retrieves the url, using given properties if necessary.
     * @param properties the properties.
     * @return the JDBC url.
     */
    protected String getUrl(@Nullable final Properties properties)
    {
        String result = getUrl();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(JDBC_URL);
            setUrl(result);
        }

        return result;
    }

    /**
     * Specifies the username.
     * @param username the new username.
     */
    protected final void immutableSetUsername(final String username)
    {
        m__strUsername = username;
    }

    /**
     * Specifies the username.
     * @param username the new username.
     */
    public void setUsername(final String username)
    {
        immutableSetUsername(username);
    }

    /**
     * Retrieves the username.
     * @return such information.
     */
    public String getUsername()
    {
        return m__strUsername;
    }

    /**
     * Retrieves the username, using given properties if necessary.
     * @param properties the properties.
     * @return the JDBC username.
     */
    protected String getUsername(@Nullable final Properties properties)
    {
        String result = getUsername();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(JDBC_USERNAME);
            setUsername(result);
        }

        return result;
    }

    /**
     * Specifies the password.
     * @param password the new password.
     */
    protected final void immutableSetPassword(final String password)
    {
        m__strPassword = password;
    }

    /**
     * Specifies the password.
     * @param password the new password.
     */
    public void setPassword(final String password)
    {
        immutableSetPassword(password);
    }

    /**
     * Retrieves the password.
     * @return such information.
     */
    public String getPassword()
    {
        return m__strPassword;
    }

    /**
     * Retrieves the password, using given properties if necessary.
     * @param properties the properties.
     * @return the JDBC password.
     */
    protected String getPassword(@Nullable final Properties properties)
    {
        String result = getPassword();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(JDBC_PASSWORD);
            setPassword(result);
        }

        return result;
    }

    /**
     * Specifies the catalog.
     * @param catalog the new catalog.
     */
    protected final void immutableSetCatalog(final String catalog)
    {
        m__strCatalog = catalog;
    }

    /**
     * Specifies the catalog.
     * @param catalog the new catalog.
     */
    public void setCatalog(final String catalog)
    {
        immutableSetCatalog(catalog);
    }

    /**
     * Retrieves the catalog.
     * @return such information.
     */
    public String getCatalog()
    {
        return m__strCatalog;
    }

    /**
     * Retrieves the catalog, using given properties if necessary.
     * @param properties the properties.
     * @return the JDBC catalog.
     */
    protected String getCatalog(@Nullable final Properties properties)
    {
        String result = getCatalog();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(JDBC_CATALOG);
            setCatalog(result);
        }

        return result;
    }

    /**
     * Specifies the schema.
     * @param schema the new schema.
     */
    protected final void immutableSetSchema(final String schema)
    {
        m__strSchema = schema;
    }

    /**
     * Specifies the schema.
     * @param schema the new schema.
     */
    public void setSchema(final String schema)
    {
        immutableSetSchema(schema);
    }

    /**
     * Retrieves the schema.
     * @return such information.
     */
    public String getSchema()
    {
        return m__strSchema;
    }

    /**
     * Retrieves the schema, using given properties if necessary.
     * @param properties the properties.
     * @return the JDBC schema.
     */
    protected String getSchema(@Nullable final Properties properties)
    {
        String result = getSchema();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(JDBC_SCHEMA);
            setSchema(result);
        }

        return result;
    }

    /**
     * Specifies the repository.
     * @param repository the new repository.
     */
    protected final void immutableSetRepository(final String repository)
    {
        m__strRepository = repository;
    }

    /**
     * Specifies the repository.
     * @param repository the new repository.
     */
    public void setRepository(final String repository)
    {
        immutableSetRepository(repository);
    }

    /**
     * Retrieves the repository.
     * @return such information.
     */
    public String getRepository()
    {
        return m__strRepository;
    }

    /**
     * Retrieves the repository, using given properties if necessary.
     * @param properties the properties.
     * @return the repository.
     */
    protected String getRepository(@Nullable final Properties properties)
    {
        String result = getRepository();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(REPOSITORY);
            setRepository(result);
        }

        return result;
    }

    /**
     * Specifies the package.
     * @param packageName the new package.
     */
    protected final void immutableSetPackage(final String packageName)
    {
        m__strPackage = packageName;
    }

    /**
     * Specifies the package.
     * @param packageName the new package.
     */
    public void setPackage(final String packageName)
    {
        immutableSetPackage(packageName);
    }

    /**
     * Retrieves the package.
     * @return such information.
     */
    public String getPackage()
    {
        return m__strPackage;
    }

    /**
     * Retrieves the package, using given properties if necessary.
     * @param properties the properties.
     * @return the package.
     */
    protected String getPackage(@Nullable final Properties properties)
    {
        String result = getPackage();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(PACKAGE);
            setPackage(result);
        }

        return result;
    }

    /**
     * Specifies the outputdir.
     * @param outputdir the new outputdir.
     */
    protected final void immutableSetOutputdir(final File outputdir)
    {
        m__Outputdir = outputdir;
    }

    /**
     * Specifies the outputdir.
     * @param outputdir the new outputdir.
     */
    public void setOutputdir(final File outputdir)
    {
        immutableSetOutputdir(outputdir);
    }

    /**
     * Retrieves the outputdir.
     * @return such information.
     */
    public File getOutputdir()
    {
        return m__Outputdir;
    }

    /**
     * Retrieves the output folder, using given properties if necessary.
     * @param properties the properties.
     * @return the output folder.
     */
    @Nullable
    protected File getOutputdir(@Nullable final Properties properties)
    {
        @Nullable File result = getOutputdir();

        if  (   (result == null)
             && (properties != null))
        {
            @Nullable final String t_strOutputdir =
                properties.getProperty(OUTPUT_FOLDER);

            if  (t_strOutputdir != null)
            {
                result = new File(t_strOutputdir);
                setOutputdir(result);
            }
        }

        return result;
    }

    /**
     * Specifies the header.
     * @param header the new header.
     */
    protected final void immutableSetHeaderfile(@NotNull final File header)
    {
        m__Header = header;
    }

    /**
     * Specifies the header.
     * @param header the new header.
     */
    public void setHeaderfile(final File header)
    {
        immutableSetHeaderfile(header);
    }

    /**
     * Retrieves the header.
     * @return such information.
     */
    public File getHeaderfile()
    {
        return m__Header;
    }

    /**
     * Retrieves the header file, using given properties if necessary.
     * @param properties the properties.
     * @return the header file.
     */
    protected File getHeaderfile(@Nullable final Properties properties)
    {
        File result = getHeaderfile();

        if  (   (result == null)
             && (properties != null))
        {
            @Nullable final String t_strHeader = properties.getProperty(HEADER_FILE);

            if  (t_strHeader != null)
            {
                result = new File(t_strHeader);
                setHeaderfile(result);
            }
        }

        return result;
    }

    /**
     * Specifies whether to use subfolders.
     * @param outputDirSubFolders such setting.
     */
    protected final void immutableSetOutputdirsubfolders(
        final String outputDirSubFolders)
    {
        m__strOutputdirsubfolders = outputDirSubFolders;
    }

    /**
     * Specifies whether to use subfolders.
     * @param outputDirSubFolders such setting.
     */
    public void setOutputdirsubfolders(final String outputDirSubFolders)
    {
        immutableSetOutputdirsubfolders(outputDirSubFolders);

        setOutputdirsubfoldersFlag(toBoolean(outputDirSubFolders));
    }

    /**
     * Retrieves whether to use subfolders.
     * @return such setting.
     */
    public String getOutputdirsubfolders()
    {
        return m__strOutputdirsubfolders;
    }

    /**
     * Specifies the "outputdirsubfolders" flag.
     * @param flag such flag.
     */
    protected final void immutableSetOutputdirsubfoldersFlag(
        final boolean flag)
    {
        m__bOutputdirsubfolders = flag;
    }

    /**
     * Specifies the "outputdirsubfolders" flag.
     * @param flag such flag.
     */
    protected void setOutputdirsubfoldersFlag(final boolean flag)
    {
        immutableSetOutputdirsubfoldersFlag(flag);
    }

    /**
     * Retrieves the "outputdirsubfolders" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getOutputdirsubfoldersFlag()
    {
        return m__bOutputdirsubfolders;
    }

    /**
     * Retrieves the output dir subfolders flag, using given properties if
     * necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getOutputdirsubfoldersFlag(@Nullable final Properties properties)
    {
        String t_strResult = getOutputdirsubfolders();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(OUTPUT_DIR_SUBFOLDERS);
            setOutputdirsubfolders(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to extract the tables.
     * @param extractTables the procedure extraction setting.
     */
    protected final void immutableSetExtractTables(final String extractTables)
    {
        m__strExtractTables = extractTables;
    }

    /**
     * Specifies whether to extract the tables.
     * @param extractTables the procedure extraction setting.
     */
    public void setExtractTables(final String extractTables)
    {
        immutableSetExtractTables(extractTables);

        setExtractTablesFlag(toBoolean(extractTables));
    }

    /**
     * Retrieves whether to extract the tables.
     * @return such setting.
     */
    public String getExtractTables()
    {
        return m__strExtractTables;
    }

    /**
     * Specifies the "extract-tables" flag.
     * @param flag such flag.
     */
    protected final void immutableSetExtractTablesFlag(final boolean flag)
    {
        m__bExtractTables = flag;
    }

    /**
     * Specifies the "extract-tables" flag.
     * @param flag such flag.
     */
    protected void setExtractTablesFlag(final boolean flag)
    {
        immutableSetExtractTablesFlag(flag);
    }

    /**
     * Retrieves the "extract-tables" flag.
     * @return such flag.
     */
    public boolean getExtractTablesFlag()
    {
        return m__bExtractTables;
    }

    /**
     * Retrieves the extract-tables flag, using given properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getExtractTablesFlag(@Nullable final Properties properties)
    {
        String t_strResult = getExtractTables();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(EXTRACT_TABLES);
            setExtractTables(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to extract the procedures.
     * @param extractProcedures the procedure extraction setting.
     */
    protected final void immutableSetExtractProcedures(
        final String extractProcedures)
    {
        m__strExtractProcedures = extractProcedures;
    }

    /**
     * Specifies whether to extract the procedures.
     * @param extractProcedures the procedure extraction setting.
     */
    public void setExtractProcedures(final String extractProcedures)
    {
        immutableSetExtractProcedures(extractProcedures);

        setExtractProceduresFlag(toBoolean(extractProcedures));
    }

    /**
     * Retrieves whether to extract the procedures.
     * @return such setting.
     */
    public String getExtractProcedures()
    {
        return m__strExtractProcedures;
    }

    /**
     * Specifies the "extract-procedures" flag.
     * @param flag such flag.
     */
    protected final void immutableSetExtractProceduresFlag(final boolean flag)
    {
        m__bExtractProcedures = flag;
    }

    /**
     * Specifies the "extract-procedures" flag.
     * @param flag such flag.
     */
    protected void setExtractProceduresFlag(final boolean flag)
    {
        immutableSetExtractProceduresFlag(flag);
    }

    /**
     * Retrieves the "extract-procedures" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getExtractProceduresFlag()
    {
        return m__bExtractProcedures;
    }

    /**
     * Retrieves the extract-procedures flag, using given properties
     * if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getExtractProceduresFlag(@Nullable final Properties properties)
    {
        String t_strResult = getExtractProcedures();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(EXTRACT_PROCEDURES);
            setExtractProcedures(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to extract the functions.
     * @param extractFunctions the function extraction setting.
     */
    protected final void immutableSetExtractFunctions(
        final String extractFunctions)
    {
        m__strExtractFunctions = extractFunctions;
    }

    /**
     * Specifies whether to extract the functions.
     * @param extractFunctions the function extraction setting.
     */
    public void setExtractFunctions(final String extractFunctions)
    {
        immutableSetExtractFunctions(extractFunctions);

        setExtractFunctionsFlag(toBoolean(extractFunctions));
    }

    /**
     * Retrieves whether to extract the functions.
     * @return such setting.
     */
    public String getExtractFunctions()
    {
        return m__strExtractFunctions;
    }

    /**
     * Specifies the "extract-functions" flag.
     * @param flag such flag.
     */
    protected final void immutableSetExtractFunctionsFlag(final boolean flag)
    {
        m__bExtractFunctions = flag;
    }

    /**
     * Specifies the "extract-functions" flag.
     * @param flag such flag.
     */
    protected void setExtractFunctionsFlag(final boolean flag)
    {
        immutableSetExtractFunctionsFlag(flag);
    }

    /**
     * Retrieves the "extract-functions" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getExtractFunctionsFlag()
    {
        return m__bExtractFunctions;
    }

    /**
     * Retrieves the extract-functions flag, using given properties if
     * necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getExtractFunctionsFlag(@Nullable final Properties properties)
    {
        String t_strResult = getExtractFunctions();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(EXTRACT_FUNCTIONS);
            setExtractFunctions(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifices the JNDI location for the data sources.
     * @param jndiLocation the JNDI location.
     */
    protected final void immutableSetJndiDataSource(final String jndiLocation)
    {
        m__strJNDIDataSources = jndiLocation;
    }

    /**
     * Specifices the JNDI location for the data sources.
     * @param jndiLocation the JNDI location.
     */
    public void setJndiDataSource(final String jndiLocation)
    {
        immutableSetJndiDataSource(jndiLocation);
    }

    /**
     * Retrieves the JNDI location for the data sources.
     * @return such information.
     */
    public String getJndiDataSource()
    {
        return m__strJNDIDataSources;
    }

    /**
     * Retrieves the JNDI path, using given properties if necessary.
     * @param properties the properties.
     * @return the JNDI location of the DataSource..
     */
    protected String getJndiDataSource(@Nullable final Properties properties)
    {
        String result = getJndiDataSource();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(JNDI_DATASOURCE);
            setJndiDataSource(result);
        }

        return result;
    }

    /**
     * Specifies whether to generate Mock DAO implementations.
     * @param generate such setting.
     */
    protected final void immutableSetGenerateMockDAOImplementation(
        final String generate)
    {
        m__strGenerateMockDAOImplementation = generate;
    }

    /**
     * Specifies whether to generate Mock DAO implementations.
     * @param generate such setting.
     */
    public void setGenerateMockDAOImplementation(final String generate)
    {
        immutableSetGenerateMockDAOImplementation(generate);

        setGenerateMockDAOImplementationFlag(toBoolean(generate));
    }

    /**
     * Retrieves whether to generate Mock DAO implementations.
     * @return such setting.
     */
    public String getGenerateMockDAOImplementation()
    {
        return m__strGenerateMockDAOImplementation;
    }

    /**
     * Specifies the "generate-mock-dao-implementation" flag.
     * @param flag such flag.
     */
    protected final void immutableSetGenerateMockDAOImplementationFlag(
        final boolean flag)
    {
        m__bGenerateMockDAOImplementation = flag;
    }

    /**
     * Specifies the "generate-mock-dao-implementation" flag.
     * @param flag such flag.
     */
    protected void setGenerateMockDAOImplementationFlag(final boolean flag)
    {
        immutableSetGenerateMockDAOImplementationFlag(flag);
    }

    /**
     * Retrieves the "generate-mock-dao-implementation" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getGenerateMockDAOImplementationFlag()
    {
        return m__bGenerateMockDAOImplementation;
    }

    /**
     * Retrieves the generate-mock-dao-implementation flag, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getGenerateMockDAOImplementationFlag(
        @Nullable final Properties properties)
    {
        String t_strResult = getGenerateMockDAOImplementation();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult =
                properties.getProperty(GENERATE_MOCK_DAO_IMPLEMENTATION);
            setGenerateMockDAOImplementation(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to generate XML DAO implementations.
     * @param generate such setting.
     */
    protected final void immutableSetGenerateXMLDAOImplementation(
        final String generate)
    {
        m__strGenerateXMLDAOImplementation = generate;
    }

    /**
     * Specifies whether to generate XML DAO implementations.
     * @param generate such setting.
     */
    public void setGenerateXMLDAOImplementation(final String generate)
    {
        immutableSetGenerateXMLDAOImplementation(generate);

        setGenerateXMLDAOImplementationFlag(toBoolean(generate));
    }

    /**
     * Retrieves whether to generate XML DAO implementations.
     * @return such setting.
     */
    public String getGenerateXMLDAOImplementation()
    {
        return m__strGenerateXMLDAOImplementation;
    }

    /**
     * Specifies the "generate-xml-dao-implementation" flag.
     * @param flag such flag.
     */
    protected final void immutableSetGenerateXMLDAOImplementationFlag(
        final boolean flag)
    {
        m__bGenerateXMLDAOImplementation = flag;
    }

    /**
     * Specifies the "generate-xml-dao-implementation" flag.
     * @param flag such flag.
     */
    protected void setGenerateXMLDAOImplementationFlag(final boolean flag)
    {
        immutableSetGenerateXMLDAOImplementationFlag(flag);
    }

    /**
     * Retrieves the "generate-xml-dao-implementation" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getGenerateXMLDAOImplementationFlag()
    {
        return m__bGenerateXMLDAOImplementation;
    }

    /**
     * Retrieves the generate-xml-dao-implementation flag, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getGenerateXMLDAOImplementationFlag(
        @Nullable final Properties properties)
    {
        String t_strResult = getGenerateXMLDAOImplementation();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult =
                properties.getProperty(GENERATE_XML_DAO_IMPLEMENTATION);
            setGenerateXMLDAOImplementation(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to generate test cases.
     * @param generate such setting.
     */
    protected final void immutableSetGenerateTests(
        final String generate)
    {
        m__strGenerateTests = generate;
    }

    /**
     * Specifies whether to generate test cases.
     * @param generate such setting.
     */
    public void setGenerateTests(final String generate)
    {
        immutableSetGenerateTests(generate);

        setGenerateTestsFlag(toBoolean(generate));
    }

    /**
     * Retrieves whether to generate test cases.
     * @return such setting.
     */
    public String getGenerateTests()
    {
        return m__strGenerateTests;
    }

    /**
     * Specifies the "generate-tests" flag.
     * @param flag such flag.
     */
    protected final void immutableSetGenerateTestsFlag(
        final boolean flag)
    {
        m__bGenerateTests = flag;
    }

    /**
     * Specifies the "generate-tests" flag.
     * @param flag such flag.
     */
    protected void setGenerateTestsFlag(final boolean flag)
    {
        immutableSetGenerateTestsFlag(flag);
    }

    /**
     * Retrieves the "generate-tests" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getGenerateTestsFlag()
    {
        return m__bGenerateTests;
    }

    /**
     * Retrieves the generate-tests flag, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getGenerateTestsFlag(
        @Nullable final Properties properties)
    {
        String t_strResult = getGenerateTests();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult =
                properties.getProperty(GENERATE_TESTS);
            setGenerateTests(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to allow empty repository DAO.
     * @param allow such setting.
     */
    protected final void immutableSetAllowEmptyRepositoryDAO(
        final String allow)
    {
        m__strAllowEmptyRepositoryDAO = allow;
    }

    /**
     * Specifies whether to allow empty repository DAO.
     * @param allow such setting.
     */
    public void setAllowEmptyRepositoryDAO(final String allow)
    {
        immutableSetAllowEmptyRepositoryDAO(allow);

        setAllowEmptyRepositoryDAOFlag(toBoolean(allow));
    }

    /**
     * Retrieves whether to allow empty repository DAO.
     * @return such setting.
     */
    public String getAllowEmptyRepositoryDAO()
    {
        return m__strAllowEmptyRepositoryDAO;
    }

    /**
     * Specifies the "allow-empty-repository-dao" flag.
     * @param flag such flag.
     */
    protected final void immutableSetAllowEmptyRepositoryDAOFlag(
        final boolean flag)
    {
        m__bAllowEmptyRepositoryDAO = flag;
    }

    /**
     * Specifies the "allow-empty-repository-dao" flag.
     * @param flag such flag.
     */
    protected void setAllowEmptyRepositoryDAOFlag(final boolean flag)
    {
        immutableSetAllowEmptyRepositoryDAOFlag(flag);
    }

    /**
     * Retrieves the "allow-empty-repository-dao" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getAllowEmptyRepositoryDAOFlag()
    {
        return m__bAllowEmptyRepositoryDAO;
    }

    /**
     * Retrieves the allow-empty-repository-dao flag, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getAllowEmptyRepositoryDAOFlag(
        @Nullable final Properties properties)
    {
        String t_strResult = getAllowEmptyRepositoryDAO();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(ALLOW_EMPTY_REPOSITORY_DAO);
            setAllowEmptyRepositoryDAO(t_strResult);
        }

        return toBoolean(t_strResult, false);
    }

    /**
     * Specifies whether to implement marker interfaces.
     * @param allow such setting.
     */
    protected final void immutableSetImplementMarkerInterfaces(
        final String allow)
    {
        m__strImplementMarkerInterfaces = allow;
    }

    /**
     * Specifies whether to implement marker interfaces.
     * @param implement such setting.
     */
    public void setImplementMarkerInterfaces(final String implement)
    {
        immutableSetImplementMarkerInterfaces(implement);

        setImplementMarkerInterfacesFlag(toBoolean(implement));
    }

    /**
     * Retrieves whether to implement marker interfaces.
     * @return such setting.
     */
    public String getImplementMarkerInterfaces()
    {
        return m__strImplementMarkerInterfaces;
    }

    /**
     * Specifies the "implement-marker-interfaces" flag.
     * @param flag such flag.
     */
    protected final void immutableSetImplementMarkerInterfacesFlag(
        final boolean flag)
    {
        m__bImplementMarkerInterfaces = flag;
    }

    /**
     * Specifies the "implement-marker-interfaces" flag.
     * @param flag such flag.
     */
    protected void setImplementMarkerInterfacesFlag(final boolean flag)
    {
        immutableSetImplementMarkerInterfacesFlag(flag);
    }

    /**
     * Retrieves the "implement-marker-interfaces" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getImplementMarkerInterfacesFlag()
    {
        return m__bImplementMarkerInterfaces;
    }

    /**
     * Retrieves the implement-marker-interfaces flag, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getImplementMarkerInterfacesFlag(
        @Nullable final Properties properties)
    {
        String t_strResult = getImplementMarkerInterfaces();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(IMPLEMENT_MARKER_INTERFACES);
            setImplementMarkerInterfaces(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies the custom-sql model.
     * @param model the model.
     */
    protected final void immutableSetCustomSqlModel(final String model)
    {
        m__strCustomSqlModel = model;
    }

    /**
     * Specifies the custom-sql model.
     * @param model the model.
     */
    public void setCustomSqlModel(final String model)
    {
        immutableSetCustomSqlModel(model);
    }

    /**
     * Retrieves the custom-sql model.
     * @return such model.
     */
    @Nullable
    public String getCustomSqlModel()
    {
        return m__strCustomSqlModel;
    }

    /**
     * Retrieves the custom-sql model, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such model.
     */
    @Nullable
    protected String getCustomSqlModel(@Nullable final Properties properties)
    {
        @Nullable String result = getCustomSqlModel();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(CUSTOM_SQL_MODEL);
            setCustomSqlModel(result);
        }

        return result;
    }

    /**
     * Specifies whether to disable custom sql validation.
     * @param allow such setting.
     */
    protected final void immutableSetDisableCustomSqlValidation(
        final String allow)
    {
        m__strDisableCustomSqlValidation = allow;
    }

    /**
     * Specifies whether to disable custom sql validation.
     * @param disable such setting.
     */
    public void setDisableCustomSqlValidation(final String disable)
    {
        immutableSetDisableCustomSqlValidation(disable);

        setDisableCustomSqlValidationFlag(toBoolean(disable));
    }

    /**
     * Retrieves whether to disable custom sql validation.
     * @return such setting.
     */
    public String getDisableCustomSqlValidation()
    {
        return m__strDisableCustomSqlValidation;
    }

    /**
     * Specifies the "disable-custom-sql-validation" flag.
     * @param flag such flag.
     */
    protected final void immutableSetDisableCustomSqlValidationFlag(
        final boolean flag)
    {
        m__bDisableCustomSqlValidation = flag;
    }

    /**
     * Specifies the "disable-custom-sql-validation" flag.
     * @param flag such flag.
     */
    protected void setDisableCustomSqlValidationFlag(final boolean flag)
    {
        immutableSetDisableCustomSqlValidationFlag(flag);
    }

    /**
     * Retrieves the "disable-custom-sql-validation" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    protected boolean getDisableCustomSqlValidationFlag()
    {
        return m__bDisableCustomSqlValidation;
    }

    /**
     * Retrieves the disable-custom-sql-validation flag, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getDisableCustomSqlValidationFlag(
        @Nullable final Properties properties)
    {
        String t_strResult = getDisableCustomSqlValidation();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult =
                properties.getProperty(DISABLE_CUSTOM_SQL_VALIDATION);
            setDisableCustomSqlValidation(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies the sql.xml file.
     * @param file the new file.
     */
    protected final void immutableSetSqlXmlFile(final File file)
    {
        m__SqlXmlFile = file;
    }

    /**
     * Specifies the sql.xml file.
     * @param file the new file.
     */
    public void setSqlXmlFile(final File file)
    {
        immutableSetSqlXmlFile(file);
    }

    /**
     * Retrieves the sql.xml file.
     * @return such information.
     */
    public File getSqlXmlFile()
    {
        return m__SqlXmlFile;
    }

    /**
     * Retrieves the sql.xml file, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such information.
     */
    @Nullable
    protected File getSqlXmlFile(@Nullable final Properties properties)
    {
        @Nullable File result = getSqlXmlFile();

        if  (   (result == null)
             && (properties != null))
        {
            @Nullable final String t_strSqlXml = properties.getProperty(SQL_XML_FILE);

            if  (t_strSqlXml != null)
            {
                result = new File(t_strSqlXml);
                setSqlXmlFile(result);
            }
        }

        return result;
    }

    /**
     * Specifies the grammar folder.
     * @param grammarFolder the new grammar folder.
     */
    protected final void immutableSetGrammarFolder(
        @NotNull final File grammarFolder)
    {
        m__GrammarFolder = grammarFolder;
    }

    /**
     * Specifies the grammar folder.
     * @param grammarFolder the new grammar folder.
     */
    public void setGrammarFolder(final File grammarFolder)
    {
        immutableSetGrammarFolder(grammarFolder);
    }

    /**
     * Retrieves the grammar name.
     * @return such information.
     */
    @Nullable
    public File getGrammarFolder()
    {
        return m__GrammarFolder;
    }

    /**
     * Retrieves the grammar folder, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such information.
     */
    @Nullable
    protected File getGrammarFolder(@Nullable final Properties properties)
    {
        File result = getGrammarFolder();

        if  (   (result == null)
             && (properties != null))
        {
            @Nullable final String t_strGrammarFolder = properties.getProperty(GRAMMAR_FOLDER);

            if (t_strGrammarFolder != null)
            {
                result = new File(t_strGrammarFolder);
                setGrammarFolder(result);
            }
        }

        return result;
    }

    /**
     * Specifies the grammar name.
     * @param grammarBundle the new grammar bundle.
     */
    protected final void immutableSetGrammarName(@NotNull final String grammarBundle)
    {
        m__strGrammarName = grammarBundle;
    }

    /**
     * Specifies the grammar name.
     * @param grammarBundle the new grammar bundle.
     */
    public void setGrammarName(@NotNull final String grammarBundle)
    {
        immutableSetGrammarName(grammarBundle);
    }

    /**
     * Retrieves the grammar name.
     * @return such information.
     */
    @Nullable
    public String getGrammarName()
    {
        return m__strGrammarName;
    }

    /**
     * Retrieves the grammar bundle, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such information.
     */
    @SuppressWarnings("unused")
    @Nullable
    protected String getGrammarName(@Nullable final Properties properties)
    {
        @Nullable String result = getGrammarName();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(GRAMMAR_NAME);

            if (result != null)
            {
                setGrammarName(result);
            }
        }

        return result;
    }

    /**
     * Specifies the grammar suffix.
     * @param suffix the new grammar suffix.
     */
    protected final void immutableSetGrammarSuffix(
        @NotNull final String suffix)
    {
        m__strGrammarSuffix = suffix;
    }

    /**
     * Specifies the grammar suffix.
     * @param suffix the new grammar suffix.
     */
    public void setGrammarSuffix(@NotNull final String suffix)
    {
        immutableSetGrammarSuffix(suffix);
    }

    /**
     * Retrieves the grammar suffix.
     * @return such information.
     */
    @Nullable
    public String getGrammarSuffix()
    {
        return m__strGrammarSuffix;
    }

    /**
     * Retrieves the grammar suffix, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such information.
     */
    @SuppressWarnings("unused")
    @Nullable
    protected String getGrammarSuffix(@Nullable final Properties properties)
    {
        @Nullable String result = getGrammarSuffix();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(GRAMMAR_SUFFIX);

            if (result != null)
            {
                setGrammarSuffix(result);
            }
        }

        return result;
    }

    /**
     * Specifies the file encoding.
     * @param encoding the encoding.
     */
    protected final void immutableSetEncoding(@NotNull final String encoding)
    {
        m__strEncoding = encoding;
    }

    /**
     * Specifies the file encoding.
     * @param encoding the encoding.
     */
    public void setEncoding(@NotNull final String encoding)
    {
        immutableSetEncoding(encoding);
    }

    /**
     * Retrieves the file encoding.
     * @return such encoding.
     */
    @Nullable
    public String getEncoding()
    {
        return m__strEncoding;
    }

    /**
     * Retrieves the encoding, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such information.
     */
    @Nullable
    protected String getEncoding(@Nullable final Properties properties)
    {
        @Nullable String result = getEncoding();

        if  (   (result == null)
             && (properties != null))
        {
            result = properties.getProperty(ENCODING);

            if (result != null)
            {
                setEncoding(result);
            }
        }

        return result;
    }

    /**
     * Specifies whether to disable caching.
     * @param allow such setting.
     */
    protected final void immutableSetDisableCaching(@NotNull final String allow)
    {
        m__strDisableCaching = allow;
    }

    /**
     * Specifies whether to disable caching.
     * @param disable such setting.
     */
    public void setDisableCaching(@NotNull final String disable)
    {
        immutableSetDisableCaching(disable);

        setDisableCachingFlag(toBoolean(disable));
    }

    /**
     * Specifies whether to disable caching.
     * @param disable such setting.
     */
    @SuppressWarnings("unused")
    public void setDisableCaching(final boolean disable)
    {
        immutableSetDisableCaching(Boolean.valueOf(disable).toString());

        setDisableCachingFlag(disable);
    }

    /**
     * Retrieves whether to disable caching.
     * @return such setting.
     */
    @Nullable
    public String getDisableCaching()
    {
        return m__strDisableCaching;
    }

    /**
     * Specifies the "disable-caching" flag.
     * @param flag such flag.
     */
    protected final void immutableSetDisableCachingFlag(final boolean flag)
    {
        m__bDisableCachingFlag = flag;
    }

    /**
     * Specifies the "disable-caching" flag.
     * @param flag such flag.
     */
    protected void setDisableCachingFlag(final boolean flag)
    {
        immutableSetDisableCachingFlag(flag);
    }

    /**
     * Retrieves the "disable-caching" flag.
     * @return such flag.
     */
    @SuppressWarnings("unused")
    public boolean getDisableCachingFlag()
    {
        return m__bDisableCachingFlag;
    }

    /**
     * Retrieves the disable-caching flag, using given
     * properties if necessary.
     * @param properties the properties.
     * @return such flag.
     */
    protected boolean getDisableCachingFlag(@Nullable final Properties properties)
    {
        @Nullable String t_strResult = getDisableCaching();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(DISABLE_CACHING);

            setDisableCaching(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies the thread count.
     * @param threadCount such value.
     */
    protected final void immutableSetThreadCount(final int threadCount)
    {
        this.m__iThreadCount = threadCount;
    }

    /**
     * Specifies the thread count.
     * @param threadCount such value.
     */
    @SuppressWarnings("unused")
    public void setThreadCount(final int threadCount)
    {
        immutableSetThreadCount(threadCount);
    }

    /**
     * Retrieves the thread count.
     * @return such value.
     */
    public int getThreadCount()
    {
        return m__iThreadCount;
    }

    /**
     * Retrieves the thread count, from given properties if necessary.
     * @param properties the properties.
     * @return such information.
     */
    @SuppressWarnings("unused")
    protected int getThreadCount(@Nullable final Properties properties)
    {
        int result = getThreadCount();

        if  (   (result == 0)
             && (properties != null))
        {
            @Nullable final String t_strValue = properties.getProperty(THREAD_COUNT);

            if (t_strValue != null)
            {
                try
                {
                    result = Integer.parseInt(t_strValue);
                }
                catch (@NotNull final NumberFormatException invalidValue)
                {
                    @Nullable final Log t_Log = UniqueLogFactory.getLog(QueryJChain.class);

                    if (t_Log != null)
                    {
                        t_Log.warn("Invalid thread count: " + t_strValue);
                    }
                }
            }

            setThreadCount(result);
        }

        return result;
    }

    /**
     * Specifies whether to use template caching.
     * @param caching such condition.
     */
    protected final void immutableSetCaching(final boolean caching)
    {
        m__bCaching = caching;
    }

    /**
     * Specifies whether to use template caching.
     * @param caching such condition.
     */
    @SuppressWarnings("unused")
    protected void setCaching(final boolean caching)
    {
        immutableSetCaching(caching);
    }

    /**
     * Retrieves whether to use template caching.
     * @return such condition.
     */
    @SuppressWarnings("unused")
    public boolean isCaching()
    {
        return m__bCaching;
    }

    /**
     * Retrieves whether to use template caching.
     * @param properties the properties.
     * @return such information.
     */
    @SuppressWarnings("unused")
    protected boolean isCaching(@Nullable final Properties properties)
    {
        boolean result = isCaching();

        if  (   (!result)
             && (properties != null))
        {
            @Nullable final String t_strValue = properties.getProperty(DISABLE_CACHING);

            if (t_strValue != null)
            {
                result = !Boolean.parseBoolean(t_strValue);
            }

            setCaching(result);
        }

        return result;
    }

    /**
     * Specifies whether to use generation timestamps.
     * @param disable the choice.
     */
    protected final void immutableSetDisableGenerationTimestamps(@NotNull final String disable)
    {
        m__strDisableGenerationTimestamps = disable;
    }

    /**
     * Specifies whether to use generation timestamps.
     * @param disable the choice.
     */
    protected void setDisableGenerationTimestamps(@NotNull final String disable)
    {
        immutableSetDisableGenerationTimestamps(disable);
    }

    /**
     * Retrieves whether to use generation timestamps.
     * @return such setting.
     */
    @Nullable
    protected final String getDisableGenerationTimestamps()
    {
        return m__strDisableGenerationTimestamps;
    }

    /**
     * Specifies whether to use generation timestamps.
     * @param flag the choice.
     */
    protected final void immutableSetDisableGenerationTimestampsFlag(final boolean flag)
    {
        m__bDisableGenerationTimestampsFlag = flag;
    }

    /**
     * Specifies whether to use generation timestamps.
     * @param flag the choice.
     */
    @SuppressWarnings("unused")
    public void setDisableGenerationTimestampsFlag(final boolean flag)
    {
        immutableSetDisableGenerationTimestamps(Boolean.valueOf(flag).toString());

        immutableSetDisableGenerationTimestampsFlag(flag);
    }

    /**
     * Retrieves whether to use generation timestamps.
     * @return such setting.
     */
    @SuppressWarnings("unused")
    public boolean getDisableGenerationTimestampsFlag()
    {
        return m__bDisableGenerationTimestampsFlag;
    }

    /**
     * Retrieves whether to use notnull annotations.
     * @return such setting.
     */
    @SuppressWarnings("unused")
    public boolean getDisableGenerationTimestamps(@Nullable final Properties properties)
    {
        @Nullable String t_strResult = getDisableGenerationTimestamps();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(DISABLE_TIMESTAMPS);

            setDisableGenerationTimestamps(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to use notnull annotations.
     * @param disable the choice.
     */
    protected final void immutableSetDisableNotNullAnnotations(@NotNull final String disable)
    {
        m__strDisableNotNullAnnotations = disable;
    }

    /**
     * Specifies whether to use notnull annotations.
     * @param disable the choice.
     */
    protected void setDisableNotNullAnnotations(@NotNull final String disable)
    {
        immutableSetDisableNotNullAnnotations(disable);
    }

    /**
     * Retrieves whether to use notnull annotations.
     * @return such setting.
     */
    protected final String getDisableNotNullAnnotations()
    {
        return m__strDisableNotNullAnnotations;
    }

    /**
     * Specifies whether to use notnull annotations.
     * @param flag the choice.
     */
    protected final void immutableSetDisableNotNullAnnotationsFlag(final boolean flag)
    {
        m__bDisableNotNullAnnotationsFlag = flag;
    }

    /**
     * Specifies whether to use notnull annotations.
     * @param flag the choice.
     */
    public void setDisableNotNullAnnotationsFlag(final boolean flag)
    {
        immutableSetDisableNotNullAnnotations(Boolean.valueOf(flag).toString());

        immutableSetDisableNotNullAnnotationsFlag(flag);
    }

    /**
     * Retrieves whether to use notnull annotations.
     * @return such setting.
     */
    @SuppressWarnings("unused")
    public boolean getDisableNotNullAnnotationsFlag()
    {
        return m__bDisableNotNullAnnotationsFlag;
    }

    /**
     * Retrieves whether to use notnull annotations.
     * @return such setting.
     */
    @SuppressWarnings("unused")
    public boolean getDisableNotNullAnnotations(@Nullable final Properties properties)
    {
        @Nullable String t_strResult = getDisableNotNullAnnotations();

        if  (   (t_strResult == null)
                && (properties != null))
        {
            t_strResult = properties.getProperty(DISABLE_NOTNULL_ANNOTATIONS);

            setDisableNotNullAnnotations(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Specifies whether to use checkthread.org annotations.
     * @param disable the choice.
     */
    protected final void immutableSetDisableCheckthreadAnnotations(@NotNull final String disable)
    {
        m__strDisableCheckthreadAnnotations = disable;
    }

    /**
     * Specifies whether to use checkthread.org annotations.
     * @param disable the choice.
     */
    protected void setDisableCheckthreadAnnotations(@NotNull final String disable)
    {
        immutableSetDisableCheckthreadAnnotations(disable);
    }

    /**
     * Retrieves whether to use checkthread.org annotations.
     * @return such setting.
     */
    protected final String getDisableCheckthreadAnnotations()
    {
        return m__strDisableCheckthreadAnnotations;
    }

    /**
     * Specifies whether to use checkthread.org annotations.
     * @param flag the choice.
     */
    protected final void immutableSetDisableCheckthreadAnnotationsFlag(final boolean flag)
    {
        m__bDisableCheckthreadAnnotationsFlag = flag;
    }

    /**
     * Specifies whether to use checkthread.org annotations.
     * @param flag the choice.
     */
    public void setDisableCheckthreadAnnotationsFlag(final boolean flag)
    {
        immutableSetDisableCheckthreadAnnotations(Boolean.valueOf(flag).toString());

        immutableSetDisableCheckthreadAnnotationsFlag(flag);
    }

    /**
     * Retrieves whether to use checkthread.org annotations.
     * @return such setting.
     */
    @SuppressWarnings("unused")
    public boolean getDisableCheckthreadAnnotationsFlag()
    {
        return m__bDisableCheckthreadAnnotationsFlag;
    }

    /**
     * Retrieves whether to use checkthread.org annotations.
     * @return such setting.
     */
    @SuppressWarnings("unused")
    public boolean getDisableCheckthreadAnnotations(@Nullable final Properties properties)
    {
        @Nullable String t_strResult = getDisableCheckthreadAnnotations();

        if  (   (t_strResult == null)
             && (properties != null))
        {
            t_strResult = properties.getProperty(DISABLE_CHECKTHREAD_ANNOTATIONS);

            setDisableCheckthreadAnnotations(t_strResult);
        }

        return toBoolean(t_strResult);
    }

    /**
     * Builds the chain.
     * @param chain the chain to be configured.
     * @return the updated chain.
     * @throws QueryJBuildException if the chain cannot be built successfully.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    protected Chain<CH> buildChain(@NotNull final Chain<CH> chain)
       throws QueryJBuildException
    {
        chain.add((CH) new ParameterValidationHandler());

        chain.add((CH) new Log4JInitializerHandler());

        chain.add((CH) new JdbcConnectionOpeningHandler());
        chain.add((CH) new CustomSqlProviderRetrievalHandler());

        // chain.add(new DatabaseMetaDataCacheReadingHandler());

        chain.add((CH) new MySQL4xMetaDataRetrievalHandler());
        chain.add((CH) new OracleMetaDataRetrievalHandler());
        chain.add((CH) new JdbcMetaDataRetrievalHandler());
        chain.add((CH) new CustomSqlValidationHandler());

        chain.add((CH) new DatabaseMetaDataCacheWritingHandler());

        chain.add((CH) new DatabaseMetaDataLoggingHandler());

        chain.add((CH) new ExternallyManagedFieldsRetrievalHandler());

        fillTemplateHandlers(chain);

        chain.add((CH) new JdbcConnectionClosingHandler());

        return chain;
    }

    /**
     * Fills given chain with external template bundles.
     * @param chain the chain.
     * @throws QueryJBuildException if the chain cannot be built successfully.
     */
    @SuppressWarnings("unchecked")
    protected void fillTemplateHandlers(@NotNull final Chain<CH> chain)
        throws QueryJBuildException
    {
         @NotNull final ServiceLoader<TemplateChainProvider> loader =
             ServiceLoader.load(TemplateChainProvider.class);

        if (loader.iterator().hasNext())
        {
            @NotNull final TemplateChainProvider<TemplateHandler> provider = loader.iterator().next();

            for (@Nullable final TemplateHandler handler : provider.getHandlers())
            {
                if (handler != null)
                {
                    chain.add((CH) handler);
                }
            }
        }
        else
        {
            throw new CannotFindTemplatesException(TemplateChainProvider.class);
        }
    }

    /**
     * Performs any clean up whenever an error occurs.
     * @param buildException the error that triggers this clean-up.
     * @param command the command.
     */
    protected void cleanUpOnError(
        @NotNull final QueryJBuildException buildException, @NotNull final QueryJCommand command)
    {
        @Nullable final Log t_Log = UniqueLogFactory.getLog(QueryJChain.class);

        if  (t_Log != null)
        {
             t_Log.info("Performing clean up");
        }

        try
        {
            new JdbcConnectionClosingHandler().handle(command);
        }
        catch  (@NotNull final QueryJBuildException closingException)
        {
            if  (t_Log != null)
            {
                t_Log.error("Error closing the connection", closingException);
            }
        }
    }

    /**
     * Builds the command.
     * @param command the command to be initialized.
     * @return the initialized command.
     */
    @NotNull
    protected QueryJCommand buildCommand(@NotNull final QueryJCommand command)
    {
        return buildCommand(command, getSettings());
    }

    /**
     * Builds the command.
     * @param command the command to be initialized.
     * @param settings the already-loaded settings.
     * @return the initialized command.
     */
    @NotNull
    protected QueryJCommand buildCommand(
        @NotNull final QueryJCommand command, @Nullable final Properties settings)
    {
        @Nullable Properties t_Settings = settings;

        if  (t_Settings == null)
        {
            t_Settings = readSettings(getSettingsFile());
        }

        return buildCommandFromSettingsIfPossible(command, t_Settings);
    }

    /**
     * Builds the command.
     * @param command the command to be initialized.
     * @param settings the properties file.
     * @return the initialized command.
     */
    @NotNull
    protected QueryJCommand buildCommandFromSettingsIfPossible(
        @NotNull final QueryJCommand command, @Nullable final Properties settings)
    {
        mapAttributes(
            command.getAttributeMap(),
            getDriver(settings),
            getUrl(settings),
            getUsername(settings),
            getPassword(settings),
            getCatalog(settings),
            getSchema(settings),
            getRepository(settings),
            getPackage(settings),
            getOutputdir(settings),
            getHeaderfile(settings),
            getOutputdirsubfoldersFlag(settings),
            getExtractTablesFlag(settings),
            getExtractProceduresFlag(settings),
            getExtractFunctionsFlag(settings),
            getJndiDataSource(settings),
            getGenerateMockDAOImplementationFlag(settings),
            getGenerateXMLDAOImplementationFlag(settings),
            getGenerateTestsFlag(settings),
            getAllowEmptyRepositoryDAOFlag(settings),
            getImplementMarkerInterfacesFlag(settings),
            getCustomSqlModel(settings),
            getDisableCustomSqlValidationFlag(settings),
            getSqlXmlFile(settings),
            getGrammarFolder(settings),
            getGrammarName(settings),
            getGrammarSuffix(settings),
            getEncoding(settings),
            !getDisableCachingFlag(settings),
            getThreadCount(settings),
            getDisableGenerationTimestamps(settings),
            getDisableNotNullAnnotations(settings),
            getDisableCheckthreadAnnotations(settings));

        return command;
    }
                
    /**
     * Maps the attributes in the command map.
     * @param attributes the command attributes.
     * @param driver the JDBC driver.
     * @param url the JDBC url.
     * @param username the JDBC username.
     * @param password the JDBC password.
     * @param catalog the JDBC catalog.
     * @param schema the JDBC schema.
     * @param repository the repository.
     * @param packageName the base package of the generated sources.
     * @param outputDir the output folder.
     * @param header the copyright header.
     * @param outputDirSubFolders whether to use main/ and test/ as
     * sub folders.
     * @param extractTables whether to extract tables or not.
     * @param extractProcedures whether to extract the procedures or not.
     * @param extractFunctions whether to extract the functions or not.
     * @param jndiDataSource the location in JNDI of the
     * {@link javax.sql.DataSource}.
     * @param generateMockDAOImplementation whether to generate Mock DAOs.
     * @param generateXmlDAOImplementation whether to generate XML DAOs.
     * @param generateTests whether to generate test cases.
     * @param allowEmptyRepositoryDAO whether to generate a repository
     * DAO even tough it'll contain no custom queries..
     * @param implementMarkerInterfaces whether to make some generated 
     * sources implement <code></code>org.acmsl.commons.patterns</code> <i>Marker</i>
     * interfaces.
     * @param customSqlModel the format of the custom SQL file.
     * @param disableCustomSqlValidation whether to disable custom sql
     * validation.
     * @param sqlXmlFile the file containing the custom SQL.
     * @param grammarFolder the grammar folder.
     * @param grammarName the grammar with irregular singular and plural
     * forms of the table names.
     * @param grammarSuffix the suffix grammar.
     * @param encoding the encoding.
     * @param caching whether to enable template caching.
     * @param threadCount the thread count.
     * @param disableGenerationTimestamps whether to disable timestamps.
     * @param disableNotNullAnnotations whether to disable NotNull annotations.
     * @param disableCheckthreadAnnotations whether to disable Checkthread.org annotations.
     */
    @SuppressWarnings("unchecked")
    protected void mapAttributes(
        @Nullable final Map attributes,
        @Nullable final String driver,
        @Nullable final String url,
        @Nullable final String username,
        @Nullable final String password,
        @Nullable final String catalog,
        @Nullable final String schema,
        @Nullable final String repository,
        @Nullable final String packageName,
        @Nullable final File outputDir,
        @Nullable final File header,
        final boolean outputDirSubFolders,
        final boolean extractTables,
        final boolean extractProcedures,
        final boolean extractFunctions,
        @Nullable final String jndiDataSource,
        final boolean generateMockDAOImplementation,
        final boolean generateXmlDAOImplementation,
        final boolean generateTests,
        final boolean allowEmptyRepositoryDAO,
        final boolean implementMarkerInterfaces,
        @Nullable final String customSqlModel,
        final boolean disableCustomSqlValidation,
        @Nullable final File sqlXmlFile,
        @Nullable final File grammarFolder,
        @Nullable final String grammarName,
        @Nullable final String grammarSuffix,
        @Nullable final String encoding,
        final boolean caching,
        final int threadCount,
        final boolean disableGenerationTimestamps,
        final boolean disableNotNullAnnotations,
        final boolean disableCheckthreadAnnotations)
    {
        if  (attributes != null)
        {
            if  (driver != null)
            {
                attributes.put(
                    ParameterValidationHandler.JDBC_DRIVER,
                    driver);
            }

            if  (url != null)
            {
                attributes.put(
                    ParameterValidationHandler.JDBC_URL,
                    url);
            }

            if  (username != null)
            {
                attributes.put(
                    ParameterValidationHandler.JDBC_USERNAME,
                    username);
            }

            if  (password != null)
            {
                attributes.put(
                    ParameterValidationHandler.JDBC_PASSWORD,
                    password);
            }

            if  (catalog != null)
            {
                attributes.put(
                    ParameterValidationHandler.JDBC_CATALOG,
                    catalog);
            }

            if  (schema != null)
            {
                attributes.put(
                    ParameterValidationHandler.JDBC_SCHEMA,
                    schema);
            }

            if  (repository != null)
            {
                attributes.put(
                    ParameterValidationHandler.REPOSITORY,
                    repository);
            }

            if  (packageName != null)
            {
                attributes.put(
                    ParameterValidationHandler.PACKAGE,
                    packageName);
            }

            if  (outputDir != null)
            {
                attributes.put(
                    ParameterValidationHandler.OUTPUT_DIR,
                    outputDir);
            }

            if  (header != null)
            {
                attributes.put(
                    ParameterValidationHandler.HEADER_FILE,
                    header);
            }

            attributes.put(
                ParameterValidationHandler.OUTPUT_DIR_SUBFOLDERS,
                (outputDirSubFolders
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            attributes.put(
                ParameterValidationHandler.EXTRACT_TABLES,
                (extractTables
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            attributes.put(
                ParameterValidationHandler.EXTRACT_PROCEDURES,
                (extractProcedures
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            attributes.put(
                ParameterValidationHandler.EXTRACT_FUNCTIONS,
                (extractFunctions
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            if  (jndiDataSource != null)
            {
                attributes.put(
                    ParameterValidationHandler.JNDI_DATASOURCES,
                    jndiDataSource);
            }

            attributes.put(
                ParameterValidationHandler.GENERATE_MOCK_DAO,
                (generateMockDAOImplementation
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            attributes.put(
                ParameterValidationHandler.GENERATE_XML_DAO,
                (generateXmlDAOImplementation
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            attributes.put(
                ParameterValidationHandler.GENERATE_TESTS,
                (generateTests
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            attributes.put(
                ParameterValidationHandler.ALLOW_EMPTY_REPOSITORY_DAO,
                (allowEmptyRepositoryDAO
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            attributes.put(
                ParameterValidationHandler.IMPLEMENT_MARKER_INTERFACES,
                (implementMarkerInterfaces
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            if  (customSqlModel != null)
            {
                attributes.put(
                    ParameterValidationHandler.CUSTOM_SQL_MODEL,
                    customSqlModel);
            }

            attributes.put(
                ParameterValidationHandler.DISABLE_CUSTOM_SQL_VALIDATION,
                (disableCustomSqlValidation
                 ?  Boolean.TRUE
                 :  Boolean.FALSE));

            if  (sqlXmlFile != null)
            {
                attributes.put(
                    ParameterValidationHandler.SQL_XML_FILE,
                    sqlXmlFile);
            }

            if  (grammarFolder != null)
            {
                attributes.put(
                    ParameterValidationHandler.GRAMMAR_FOLDER,
                    grammarFolder);
            }

            if  (grammarName != null)
            {
                attributes.put(
                    ParameterValidationHandler.GRAMMAR_NAME,
                    grammarName);
            }

            if  (grammarSuffix != null)
            {
                attributes.put(
                        ParameterValidationHandler.GRAMMAR_SUFFIX,
                        grammarSuffix);
            }

            if (   (grammarFolder != null)
                && (grammarName != null)
                && (grammarSuffix != null))
            {
                @NotNull final File t_GrammarFile =
                    new File(
                          grammarFolder + File.separator + grammarName
                        + "_" + Locale.US.getLanguage().toLowerCase(Locale.US)
                        + grammarSuffix);

                SingularPluralFormConverter.setGrammarBundle(t_GrammarFile);
            }

            if  (encoding != null)
            {
                attributes.put(
                    ParameterValidationHandler.ENCODING,
                    encoding);
            }

            attributes.put(
                ParameterValidationHandler.CACHING,
                caching);

            attributes.put(
                ParameterValidationHandler.THREAD_COUNT,
                threadCount);

            attributes.put(
                ParameterValidationHandler.DISABLE_TIMESTAMPS,
                disableGenerationTimestamps);

            attributes.put(
                ParameterValidationHandler.DISABLE_NOTNULL_ANNOTATIONS,
                disableNotNullAnnotations);

            attributes.put(
                ParameterValidationHandler.DISABLE_CHECKTHREAD_ANNOTATIONS,
                disableCheckthreadAnnotations);
        }
    }

    /**
     * Converts given value to boolean.
     * @param value the value.
     * @return the converted value.
     */
    protected boolean toBoolean(@Nullable final String value)
    {
        return toBoolean(value, true);
    }

    /**
     * Converts given value to boolean.
     * @param value the value.
     * @param defaultValue the default value if input is null.
     * @return the converted value.
     */
    protected boolean toBoolean(
        @Nullable final String value, final boolean defaultValue)
    {
        boolean result = defaultValue;

        if  (value != null)
        {
            @NotNull final String t_strTrimmedValue = value.trim().toLowerCase();

            result =
                (   (t_strTrimmedValue.equals("yes"))
                 || (t_strTrimmedValue.equals("true")));
        }

        return result;
    }

    /**
     * Reads the settings from given file.
     * @param file the file.
     * @return the {@link Properties} instance, or <code>null</code>
     * if the file doesn't exist.
     */
    @Nullable
    protected Properties readSettings(@Nullable final File file)
    {
        @Nullable Properties result = new Properties();

        if  (file != null)
        {
            @Nullable InputStream t_isSettings = null;

            try
            {
                t_isSettings = new FileInputStream(file);

                result.load(t_isSettings);
            }
            catch  (@NotNull final IOException ioException)
            {
                result = null;

                @Nullable final Log t_Log = UniqueLogFactory.getLog(QueryJChain.class);

                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot read the configuration properties "
                        + "file.",
                        ioException);
                }
            }
            finally
            {
                if  (t_isSettings != null)
                {
                    try
                    {
                        t_isSettings.close();
                    }
                    catch  (@NotNull final IOException ioException)
                    {
                        @Nullable final Log t_Log = UniqueLogFactory.getLog(QueryJChain.class);

                        if  (t_Log != null)
                        {
                            t_Log.error(
                                  "Cannot close the configuration properties "
                                + "file.",
                                ioException);
                        }
                    }
                }
            }
        }

        return result;
    }

    @NotNull
    @Override
    public String toString()
    {
        @NotNull final StringBuilder result =
            new StringBuilder("QueryJChain{AllowEmptyRepositoryDAO=");

        result.append(getAllowEmptyRepositoryDAO());
        result.append(", Settings =");
        result.append(getSettings());
        result.append(", SettingsFile=");
        result.append(getSettingsFile().getAbsolutePath());
        result.append(", Driver='");
        result.append(getDriver());
        result.append("', Url='");
        result.append(getUrl());
        result.append("', Username='");
        result.append(getUsername());
        result.append("', Password='");
        result.append(getPassword());
        result.append("', Catalog='");
        result.append(getCatalog());
        result.append("', Schema='");
        result.append(getSchema());
        result.append("', Repository='");
        result.append(getRepository());
        result.append("', Package='");
        result.append(getPackage());
        result.append("', Outputdir=");
        result.append(getOutputdir().getAbsolutePath());
        result.append(", Header=");
        result.append(getHeaderfile());
        result.append(", Outputdirsubfolders='");
        result.append(getOutputdirsubfolders());
        result.append(", ExtractTables='");
        result.append(getExtractTables());
        result.append("', ExtractProcedures=");
        result.append(getExtractProcedures());
        result.append("', ExtractFunctions=");
        result.append(getExtractFunctions());
        result.append(", JNDIDataSources='");
        result.append(getJndiDataSource());
        result.append("', GenerateMockDAOImplementation='");
        result.append(getGenerateMockDAOImplementation());
        result.append(", GenerateXMLDAOImplementation='");
        result.append(getGenerateXMLDAOImplementation());
        result.append(", GenerateTests='");
        result.append(getGenerateTests());
        result.append(", CustomSqlModel='");
        result.append(getCustomSqlModel());
        result.append("', SqlXmlFile=");
        result.append(getSqlXmlFile());
        result.append(", GrammarFolder=");
        result.append(getGrammarFolder());
        result.append(", GrammarName='");
        result.append(getGrammarName());
        result.append("', GrammarSuffix='");
        result.append(getGrammarSuffix());
        result.append("', AllowEmptyRepositoryDAO='");
        result.append(getAllowEmptyRepositoryDAO());
        result.append(", ImplementMarkerInterfaces='");
        result.append(getImplementMarkerInterfaces());
        result.append(", DisableCustomSqlValidation='");
        result.append(getDisableCustomSqlValidation());
        result.append("', Encoding='");
        result.append(getEncoding());
        result.append("', Caching=");
        result.append(isCaching());
        result.append(", DisableCaching='");
        result.append(getDisableCaching());
        result.append(", ThreadCount=");
        result.append(getThreadCount());
        result.append("', DisableGenerationTimestampsFlag=");
        result.append(getDisableGenerationTimestamps());
        result.append(", DisableNotNullAnnotations='");
        result.append(getDisableNotNullAnnotations());
        result.append(", DisableCheckthreadAnnotations='");
        result.append(getDisableCheckthreadAnnotations());
        result.append('}');

        return result.toString();
    }
}
