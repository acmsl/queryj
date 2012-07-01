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
package org.acmsl.queryj.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.customsql.CustomSqlProvider;
import org.acmsl.queryj.metadata.DecoratorFactory;
import org.acmsl.queryj.metadata.MetadataManager;
import org.acmsl.queryj.metadata.vo.Row;
import org.acmsl.queryj.templates.AbstractTemplateGenerator;
import org.acmsl.queryj.templates.BasePerTableTemplateContext;
import org.acmsl.queryj.templates.BasePerTableTemplateFactory;
import org.acmsl.queryj.templates.BasePerTableTemplateGenerator;
import org.acmsl.queryj.templates.MetaLanguageUtils;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.util.List;
import java.util.Locale;

/**
 * Is able to generate base abstract DAO implementations according to database
 * metadata.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class BaseAbstractDAOTemplateGenerator
    extends AbstractTemplateGenerator<BaseAbstractDAOTemplate, BasePerTableTemplateContext>
    implements BasePerTableTemplateFactory<BaseAbstractDAOTemplate>,
               BasePerTableTemplateGenerator<BaseAbstractDAOTemplate, BasePerTableTemplateContext>,
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
    protected BaseAbstractDAOTemplateGenerator() {}

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
     * {@inheritDoc}
     */
    @Nullable
    public BaseAbstractDAOTemplate createTemplate(
        @NotNull final MetadataManager metadataManager,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final String packageName,
        @NotNull final String basePackageName,
        @NotNull final String repositoryName,
        @NotNull final String header,
        final boolean implementMarkerInterfaces,
        final boolean jmx,
        @NotNull final String jndiLocation,
        @NotNull final String tableName,
        @Nullable List<Row> staticContents)
    {
        @Nullable BaseAbstractDAOTemplate result = null;

        if  (isStaticTable(tableName, metadataManager))
        {
            result =
                new BaseAbstractDAOTemplate(
                    new BasePerTableTemplateContext(
                        metadataManager,
                        customSqlProvider,
                        header,
                        getDecoratorFactory(),
                        packageName,
                        basePackageName,
                        repositoryName,
                        implementMarkerInterfaces,
                        jmx,
                        jndiLocation,
                        tableName,
                        staticContents));
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
     */
    protected boolean isStaticTable(
        @NotNull final String tableName, @NotNull final MetadataManager metadataManager)
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
     */
    protected boolean isStaticTable(
        @NotNull final String tableName,
        @NotNull final MetadataManager metadataManager,
        @NotNull final MetaLanguageUtils metaLanguageUtils)
    {
         return
            metaLanguageUtils.containsStaticValues(
                tableName, metadataManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String retrieveTemplateFileName(@NotNull BasePerTableTemplateContext context)
    {
        return retrieveTemplateFileName(context, StringUtils.getInstance(), EnglishGrammarUtils.getInstance());
    }

    /**
     * Retrieves given template's file name.
     * @param context the template context.
     * @param stringUtils the {@link StringUtils} instance.
     * @param englishGrammarUtils the {@link EnglishGrammarUtils} instance.
     * @return such name.
     */
    @NotNull
    public String retrieveTemplateFileName(
        @NotNull final BasePerTableTemplateContext context,
        @NotNull final StringUtils stringUtils,
        @NotNull final EnglishGrammarUtils englishGrammarUtils)
    {
        return
              "Abstract"
            + stringUtils.capitalize(
                  englishGrammarUtils.getSingular(
                      context.getTableName().toLowerCase(Locale.US)),
                  '_')
            + "DAO.java";
    }
}