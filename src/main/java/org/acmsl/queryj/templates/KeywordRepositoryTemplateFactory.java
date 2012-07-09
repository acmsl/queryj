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
 * Filename: KeywordRepositoryTemplateGenerator.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate keyword repository templates according to
 *              keyword definition.
 *
 */
package org.acmsl.queryj.templates;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.customsql.CustomSqlProvider;
import org.acmsl.queryj.metadata.DecoratorFactory;
import org.acmsl.queryj.metadata.MetadataManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Singleton;

/*
 * Importing some JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Importing some JDK classes.
 */
import java.util.List;

/**
 * Is able to generate keyword repositories template according to
 * keyword definition.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class KeywordRepositoryTemplateFactory
    implements  BasePerRepositoryTemplateFactory<KeywordRepositoryTemplate>,
                Singleton
{
    /**
     * Singleton implemented to avoid double-locking check.
     */
    protected static final class KeywordRepositoryTemplateFactorySingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final KeywordRepositoryTemplateFactory SINGLETON = new KeywordRepositoryTemplateFactory();
    }

    /**
     * Retrieves the singleton.
     * @return such instance.
     */
    public static KeywordRepositoryTemplateFactory getInstance()
    {
        return KeywordRepositoryTemplateFactorySingletonContainer.SINGLETON;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public KeywordRepositoryTemplate createTemplate(
        @NotNull final MetadataManager metadataManager,
        @NotNull final CustomSqlProvider customSqlProvider,
        @NotNull final DecoratorFactory decoratorFactory,
        @NotNull final String packageName,
        @NotNull final String projectPackage,
        @NotNull final String repository,
        @NotNull final String header,
        final boolean implementMarkerInterfaces,
        final boolean jmx,
        @NotNull final List<String> tableNames,
        @NotNull final String jndiLocation)
    {
        return
            new KeywordRepositoryTemplate(
                new BasePerRepositoryTemplateContext(
                    metadataManager,
                    customSqlProvider,
                    header,
                    decoratorFactory,
                    packageName,
                    projectPackage,
                    repository,
                    implementMarkerInterfaces,
                    jmx,
                    tableNames,
                    jndiLocation));
    }
}
