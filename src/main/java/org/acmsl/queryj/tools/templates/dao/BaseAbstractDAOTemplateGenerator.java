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
 * Filename: BaseAbstractDAOTemplateGenerator.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate base abstract DAO implementations according
 *              to database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.CachingDecoratorFactory;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.BasePerTableTemplate;
import org.acmsl.queryj.tools.templates.BasePerTableTemplateFactory;
import org.acmsl.queryj.tools.templates.BasePerTableTemplateGenerator;
import org.acmsl.queryj.tools.templates.dao.BaseDAODecoratorFactory;
import org.acmsl.queryj.tools.templates.dao.BaseDAOTemplate;
import org.acmsl.queryj.tools.templates.MetaLanguageUtils;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * Is able to generate base abstract DAO implementations according to database
 * metadata.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class BaseAbstractDAOTemplateGenerator
    implements  BasePerTableTemplateFactory,
                BasePerTableTemplateGenerator,
                Singleton
{
    /**
     * Singleton implemented to avoid the double-checked locking.
     */
    private static class BaseAbstractDAOTemplateGeneratorSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final BaseAbstractDAOTemplateGenerator SINGLETON =
            new BaseAbstractDAOTemplateGenerator();
    }

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected BaseAbstractDAOTemplateGenerator() {};

    /**
     * Retrieves a {@link BaseAbstractDAOTemplateGenerator} instance.
     * @return such instance.
     */
    @NotNull
    public static BaseAbstractDAOTemplateGenerator getInstance()
    {
        return BaseAbstractDAOTemplateGeneratorSingletonContainer.SINGLETON;
    }

    /**
     * Generates a {@link BaseAbstractDAOTemplate} instance.
     * @param tableName the table name.
     * @param metadataManager the metadata manager.
     * @param customSqlProvider the CustomSqlProvider instance.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param basePackageName the base package name.
     * @param repositoryName the name of the repository.
     * @param header the header.
     * @param implementMarkerInterfaces whether to implement marker
     * interfaces.
     * @return a template.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition engineVersion != null
     * @precondition quote != null
     * @precondition basePackageName != null
     * @precondition repositoryName != null
     */
    @Nullable
    public BasePerTableTemplate createTemplate(
        final String tableName,
        @NotNull final MetadataManager metadataManager,
        final CustomSqlProvider customSqlProvider,
        final String packageName,
        final String engineName,
        final String engineVersion,
        final String quote,
        final String basePackageName,
        final String repositoryName,
        final String header,
        final boolean implementMarkerInterfaces)
    {
        @Nullable BasePerTableTemplate result = null;

        if  (isStaticTable(tableName, metadataManager))
        {
            result =
                new BaseAbstractDAOTemplate(
                    tableName,
                    metadataManager,
                    customSqlProvider,
                    header,
                    getDecoratorFactory(),
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    basePackageName,
                    repositoryName,
                    implementMarkerInterfaces);
        }

        return result;
    }

    /**
     * Generates a {@link BaseAbstractDAOTemplate} instance.
     * @param tableName the table name.
     * @param metadataManager the metadata manager.
     * @param customSqlProvider the CustomSqlProvider instance.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param basePackageName the base package name.
     * @param repositoryName the name of the repository.
     * @param header the header.
     * @param implementMarkerInterfaces whether to implement marker
     * interfaces.
     * @param staticValues the static values.
     * @return a template.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition engineVersion != null
     * @precondition quote != null
     * @precondition basePackageName != null
     * @precondition repositoryName != null
     * @precondition staticValues != null
     */
    @Nullable
    public BasePerTableTemplate createTemplate(
        final String tableName,
        @NotNull final MetadataManager metadataManager,
        final CustomSqlProvider customSqlProvider,
        final String packageName,
        final String engineName,
        final String engineVersion,
        final String quote,
        final String basePackageName,
        final String repositoryName,
        final String header,
        final boolean implementMarkerInterfaces,
        @Nullable final Collection staticValues)
    {
        @Nullable BasePerTableTemplate result = null;

        if  (staticValues != null)
        {
            result =
                new BaseAbstractDAOTemplate(
                    tableName,
                    metadataManager,
                    customSqlProvider,
                    header,
                    getDecoratorFactory(),
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    basePackageName,
                    repositoryName,
                    implementMarkerInterfaces,
                    staticValues);
        }
        else
        {
            result =
                createTemplate(
                    tableName,
                    metadataManager,
                    customSqlProvider,
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    basePackageName,
                    repositoryName,
                    header,
                    implementMarkerInterfaces);
        }
                
        return result;
    }

    /**
     * Retrieves the decorator factory.
     * @return such instance.
     */
    @NotNull
    public DecoratorFactory getDecoratorFactory()
    {
        // Reusing to avoid copy/paste.
        return BaseDAODecoratorFactory.getInstance();
    }


    /**
     * Checks whether the table contains static values or not.
     * @param tableName the table name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @return such information.
     * @precondition tableName != null
     * @precondition metadataManager != null
     */
    protected boolean isStaticTable(
        final String tableName, @NotNull final MetadataManager metadataManager)
    {
        return
            isStaticTable(
                tableName, metadataManager, MetaLanguageUtils.getInstance());
    }

    /**
     * Checks whether the table contains static values or not.
     * @param tableName the table name.
     * @param metadataManager the {@link MetadataManager} instance.
     * @param metaLanguageUtils the {@link MetaLanguageUtils} instance.
     * @return such information.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition metaLanguageUtils != null
     */
    protected boolean isStaticTable(
        final String tableName,
        @NotNull final MetadataManager metadataManager,
        @NotNull final MetaLanguageUtils metaLanguageUtils)
    {
         return
            metaLanguageUtils.containsStaticValues(
                tableName, metadataManager);
    }

    /**
     * Writes a {@link BaseAbstractDAOTemplate} to disk.
     * @param template the template to write.
     * @param outputDir the output folder.
     * @param charset the file encoding.
     * @throws IOException if the file cannot be created.
     * @precondition template instanceof BaseDAOTemplate
     * @precondition outputDir != null
     */
    public void write(
        @NotNull final BasePerTableTemplate template,
        @NotNull final File outputDir,
        final Charset charset)
      throws  IOException
    {
        write(
            template,
            outputDir,
            charset,
            StringUtils.getInstance(),
            EnglishGrammarUtils.getInstance(),
            FileUtils.getInstance());
    }

    /**
     * Writes a BaseDAO template to disk.
     * @param template template to write.
     * @param outputDir the output folder.
     * @param charset the file encoding.
     * @param stringUtils the {@link StringUtils} instance.
     * @param englishGrammarUtils the {@link EnglishGrammarUtils}
     * instance.
     * @param fileUtils the {@link FileUtils} instance.
     * @throws IOException if the file cannot be created.
     * @precondition template instanceof BaseDAOTemplate
     * @precondition outputDir != null
     * @precondition stringUtils != null
     * @precondition englishGrammarUtils != null
     * @precondition fileUtils != null
     */
    protected void write(
        @NotNull final BasePerTableTemplate template,
        @NotNull final File outputDir,
        final Charset charset,
        @NotNull final StringUtils stringUtils,
        @NotNull final EnglishGrammarUtils englishGrammarUtils,
        @NotNull final FileUtils fileUtils)
      throws  IOException
    {
        boolean folderCreated = outputDir.mkdirs();

        if (   (!folderCreated)
            && (!outputDir.exists()))
        {
            throw
                new IOException("Cannot create output dir: " + outputDir);
        }
        else
        {
            fileUtils.writeFile(
                  outputDir.getAbsolutePath()
                + File.separator
                + "Abstract"
                + stringUtils.capitalize(
                    englishGrammarUtils.getSingular(
                        template.getTableName().toLowerCase()),
                    '_')
                + "DAO.java",
                template.generate(),
                charset);
        }
    }
}
