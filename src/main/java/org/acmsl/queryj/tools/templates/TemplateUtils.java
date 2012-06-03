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
 * Filename: TemplateUtils.java
 *
 * Author: Jose San Leandro Armend&aacute;riz
 *
 * Description: Provides some useful methods when filling up templates.
 *
 */
package org.acmsl.queryj.tools.templates;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.customsql.Result;
import org.acmsl.queryj.tools.customsql.Sql;
import org.acmsl.queryj.tools.customsql.ResultRefElement;
import org.acmsl.queryj.tools.metadata.CachingResultDecorator;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.dao.DAOTemplateUtils;

/*
 * Importing ACM-SL Commons classes.
 */
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.patterns.Utils;

/*
 * Importing some Apache Commons-Logging classes.
 */
import org.apache.commons.logging.LogFactory;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Provides some useful methods when filling up templates.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class TemplateUtils
    implements  Singleton, 
                Utils
{
    /**
     * Singleton implemented to avoid the double-checked locking.
     */
    private static class TemplateUtilsSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final TemplateUtils SINGLETON = new TemplateUtils();
    }

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected TemplateUtils() {}

    /**
     * Retrieves a <code>TemplateUtils</code> instance.
     * @return such instance.
     */
    @NotNull
    public static TemplateUtils getInstance()
    {
        return TemplateUtilsSingletonContainer.SINGLETON;
    }

    /**
     * Retrieves the custom selects.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom selects.
     */
    @SuppressWarnings("unused")
    @NotNull
    public List<Sql> retrieveCustomSelects(
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        return
            retrieveCustomSelects(
                null,
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils);
    }

    /**
     * Retrieves the custom selects.
     * @param tableName the table name, or <code>null</code> for
     * repository-wide results.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom selects.
     */
    @NotNull
    public List<Sql> retrieveCustomSelects(
        @Nullable final String tableName,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        return
            retrieveCustomSql(
                new String[]
                {
                    Sql.SELECT,
                },
                tableName,
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils);
    }

    /**
     * Retrieves the custom updates or inserts.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom sql.
     */
    @SuppressWarnings("unused")
    @NotNull
    public List<Sql> retrieveCustomUpdatesOrInserts(
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        return
            retrieveCustomUpdatesOrInserts(
                null,
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils);
    }

    /**
     * Retrieves the custom updates or inserts.
     * @param tableName the table name.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom sql.
     */
    @NotNull
    public List<Sql> retrieveCustomUpdatesOrInserts(
        @Nullable final String tableName,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        return
            retrieveCustomSql(
                new String[]
                {
                    Sql.INSERT,
                    Sql.UPDATE,
                    Sql.DELETE
                },
                tableName,
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils);
    }

    /**
     * Retrieves the custom selects.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom selects.
     */
    @SuppressWarnings("unused")
    @NotNull
    public List<Sql> retrieveCustomSelectsForUpdate(
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        return
            retrieveCustomSelectsForUpdate(
                null,
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils);
    }

    /**
     * Retrieves the custom selects.
     * @param tableName the table name.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom selects.
     */
    @NotNull
    public List<Sql> retrieveCustomSelectsForUpdate(
        @Nullable final String tableName,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        return
            retrieveCustomSql(
                new String[]
                {
                    Sql.SELECT_FOR_UPDATE,
                },
                tableName,
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils);
    }

    /**
     * Retrieves the custom sql.
     * @param types the sql types.
     * @param tableName the table name, or <code>null</code> for
     * repository-wide results.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom sql.
     */
    @NotNull
    public List<Sql> retrieveCustomSql(
        @Nullable final String[] types,
        @Nullable final String tableName,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        @NotNull List<Sql> result = new ArrayList<Sql>();

        Collection t_cContents = customSqlProvider.getCollection();

        if  (t_cContents != null)
        {
            Iterator t_itContentIterator = t_cContents.iterator();

            if  (t_itContentIterator != null)
            {
                int t_iTypes = (types != null) ? types.length : 0;

                while  (t_itContentIterator.hasNext())
                {
                    Object t_Content = t_itContentIterator.next();

                    boolean t_bMatches;

                    if  (t_Content instanceof Sql)
                    {
                        @NotNull Sql t_Sql = (Sql) t_Content;

                        String t_strDao = t_Sql.getDao();

                        if  (   (t_strDao != null)
                             && (tableName != null))
                        {
                            t_bMatches =
                                daoTemplateUtils.matches(
                                    tableName, t_strDao);
                        }
                        else
                        {
                            t_bMatches = (t_Sql.getRepositoryScope() != null);
                        }

                        if  (t_bMatches)
                        {
                            boolean t_bAdd = false;

                            @Nullable String t_strCurrentType;

                            for  (int t_iIndex = 0;
                                      t_iIndex < t_iTypes;
                                      t_iIndex++)
                            {
                                t_strCurrentType = types[t_iIndex];

                                if  (   (t_strCurrentType != null)
                                     && (t_strCurrentType.equals(
                                             t_Sql.getType())))
                                {
                                    t_bAdd = true;
                                }
                            }

                            if  (t_bAdd)
                            {
                                result.add(
                                    decoratorFactory.createDecorator(
                                        t_Sql,
                                        customSqlProvider,
                                        metadataManager));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Retrieves the custom results.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom results.
     * @precondition customSqlProvider != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     * @precondition daoTemplateUtils != null
     */
    @NotNull
    public List<Result> retrieveCustomResults(
        @NotNull final CustomSqlProvider customSqlProvider,
        final MetadataManager metadataManager,
        final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        return
            retrieveCustomResults(
                null,
                customSqlProvider,
                metadataManager,
                decoratorFactory,
                daoTemplateUtils);
    }

    /**
     * Retrieves the custom results.
     * @param tableName the table name, or <code>null</code> for
     * repository-wide results.
     * @param customSqlProvider the provider.
     * @param metadataManager the database metadata manager.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param daoTemplateUtils the <code>DAOTemplateUtils</code> instance.
     * @return the custom results.
     * @precondition customSqlProvider != null
     * @precondition metadataManager != null
     * @precondition decoratorFactory != null
     * @precondition daoTemplateUtils != null
     */
    @NotNull
    public List<Result> retrieveCustomResults(
        @Nullable final String tableName,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final MetadataManager metadataManager,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final DAOTemplateUtils daoTemplateUtils)
    {
        @NotNull List<Result> result = new ArrayList<Result>();

        Collection t_cContents = customSqlProvider.getCollection();

        if  (t_cContents != null)
        {
            @Nullable Object t_Content;
            @Nullable Sql t_Sql;
            @Nullable ResultRefElement t_ResultRefElement;
            @Nullable Result t_ResultElement;
            String t_strDao;
            boolean t_bMatches;

            Iterator t_itContentIterator = t_cContents.iterator();

            if  (t_itContentIterator != null)
            {
                while  (t_itContentIterator.hasNext())
                {
                    t_Content = t_itContentIterator.next();

                    if  (t_Content instanceof Sql)
                    {
                        t_Sql = (Sql) t_Content;

                        t_strDao = t_Sql.getDao();

                        if  (   (t_strDao != null)
                             && (tableName != null))
                        {
                            t_bMatches =
                                daoTemplateUtils.matches(
                                    tableName, t_strDao);
                        }
                        else
                        {
                            t_bMatches = (t_Sql.getRepositoryScope() != null);
                        }

                        if  (t_bMatches)
                        {
                            t_ResultRefElement = t_Sql.getResultRef();

                            if  (t_ResultRefElement != null)
                            {
                                t_ResultElement =
                                    customSqlProvider.resolveReference(
                                        t_ResultRefElement);

                                if  (t_ResultElement != null)
                                {
                                    if  (!result.contains(t_ResultElement))
                                    {
                                        result.add(
                                            new CachingResultDecorator(
                                                t_ResultElement,
                                                customSqlProvider,
                                                metadataManager,
                                                decoratorFactory));
                                    }
                                }
                                else
                                {
                                    try
                                    {
                                        // todo throw something.
                                        LogFactory.getLog(TemplateUtils.class)
                                            .warn(
                                                "Referenced result not found:"
                                                + t_ResultRefElement.getId());
                                    }
                                    catch  (@NotNull final Throwable throwable)
                                    {
                                        // class-loading problem.
                                        System.err.println(
                                              "Referenced result not found:"
                                            + t_ResultRefElement.getId());
                                    }
                                }
                            }
                            else
                            {
                                try
                                {
                                    // todo throw something.
                                    LogFactory.getLog(TemplateUtils.class).warn(
                                          "Referenced result not found for sql:"
                                        + t_Sql.getId());
                                }
                                catch  (@NotNull final Throwable throwable)
                                {
                                    // class-loading problem.
                                    System.err.println(
                                          "Referenced result not found for sql:"
                                        + t_Sql.getId());
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}
