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
 * Filename: DAOTestTemplateGenerator.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate DAO test implementations according to
 *              database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.SingularPluralFormConverter;
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.metadata.CachingDecoratorFactory;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.dao.DAOTestTemplate;
import org.acmsl.queryj.tools.templates.dao.DAOTestTemplateFactory;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;

/**
 * Is able to generate DAO test implementations according to database
 * metadata.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public class DAOTestTemplateGenerator
    implements  BasePerTableTemplateFactory,
                BasePerTableTemplateGenerator
{
    /**
     * Singleton implemented to avoid the double-checked locking.
     */
    private static class DAOTestTemplateGeneratorSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final DAOTestTemplateGenerator SINGLETON =
            new DAOTestTemplateGenerator();
    }

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected DAOTestTemplateGenerator() {};

    /**
     * Retrieves a <code>DAOTestTemplateGenerator</code> instance.
     * @return such instance.
     */
    public static DAOTestTemplateGenerator getInstance()
    {
        return DAOTestTemplateGeneratorSingletonContainer.SINGLETON;
    }

    /**
     * Generates a DAO template.
     * @param tableName the table name.
     * @param metadataManager the metadata manager.
     * @param customSqlProvider the CustomSqlProvider instance.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param basePackageName the base package name.
     * @param repositoryName the name of the repository.
     * @param jmx whether to support JMX.
     * @param header the header.
     * @param jndiLocation the location of the datasource in JNDI.
     * @return a template.
     * @throws QueryJException if the factory class is invalid.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition engineVersion != null
     * @precondition quote != null
     * @precondition basePackageName != null
     * @precondition repositoryName != null
     */
    public BasePerTableTemplate createTemplate(
        final String tableName,
        final MetadataManager metadataManager,
        final CustomSqlProvider customSqlProvider,
        final String packageName,
        final String engineName,
        final String engineVersion,
        final String quote,
        final String basePackageName,
        final String repositoryName,
        final boolean jmx,
        final String header,
        final String jndiLocation)
      throws  QueryJException
    {
        return
            new DAOTestTemplate(
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
                jmx,
                jndiLocation);
    }

    /**
     * Retrieves the decorator factory.
     * @return such instance.
     */
    public DecoratorFactory getDecoratorFactory()
    {
        return CachingDecoratorFactory.getInstance();
    }

    /**
     * Writes a DAO test template to disk.
     * @param daoTestTemplate the DAO test template to write.
     * @param outputDir the output folder.
     * @throws IOException if the file cannot be created.
     * @precondition daoTestTemplate != null
     * @precondition outputDir != null
     */
    public void write(
        final DAOTestTemplate daoTestTemplate,
        final File outputDir)
      throws  IOException
    {
        write(
            daoTestTemplate,
            outputDir,
            FileUtils.getInstance(),
            StringUtils.getInstance(),
            SingularPluralFormConverter.getInstance());
    }

    /**
     * Writes a DAO test template to disk.
     * @param daoTestTemplate the DAO test template to write.
     * @param outputDir the output folder.
     * @param fileUtils the <code>FileUtils</code> instance.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @param singularPluralFormConverter the <code>SingularPluralFormConverter</code>
     * instance.
     * @throws IOException if the file cannot be created.
     * @precondition daoTestTemplate != null
     * @precondition outputDir != null
     * @precondition fileUtils != null
     * @precondition stringUtils != null
     * @precondition singularPluralFormConverter != null
     */
    protected void write(
        final DAOTestTemplate daoTestTemplate,
        final File outputDir,
        final FileUtils fileUtils,
        final StringUtils stringUtils,
        final EnglishGrammarUtils singularPluralFormConverter)
      throws  IOException
    {
        outputDir.mkdirs();

        fileUtils.writeFile(
              outputDir.getAbsolutePath()
            + File.separator
            + daoTestTemplate.getEngineName()
            + stringUtils.capitalize(
                singularPluralFormConverter.getSingular(
                    daoTestTemplate
                        .getTableTemplate().getTableName().toLowerCase()),
                '_')
            + "DAOTest.java",
            daoTestTemplate.generate());
    }
}
