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
    but WITHOUT ANY WARRANTY; without even the factoryied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Thanks to ACM S.L. for distributing this library under the GPL license.
    Contact info: jose.sanleandro@acm-sl.com

 ******************************************************************************
 *
 * Filename: ValueObjectFactoryTemplateGenerator.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate base DAO factories.
 *
 */
package org.acmsl.queryj.tools.templates.valueobject;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.CachingDecoratorFactory;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.templates.valueobject.ValueObjectFactoryTemplate;
import org.acmsl.queryj.tools.templates.BasePerTableTemplate;
import org.acmsl.queryj.tools.templates.BasePerTableTemplateFactory;
import org.acmsl.queryj.tools.templates.BasePerTableTemplateGenerator;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Is able to generate base DAO factories.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class ValueObjectFactoryTemplateGenerator
    implements  BasePerTableTemplateFactory,
                BasePerTableTemplateGenerator,
                Singleton
{
    /**
     * Singleton implemented to avoid the double-checked locking.
     */
    private static class ValueObjectFactoryTemplateGeneratorSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final ValueObjectFactoryTemplateGenerator SINGLETON =
            new ValueObjectFactoryTemplateGenerator();
    }

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected ValueObjectFactoryTemplateGenerator() {};

    /**
     * Retrieves a <code>ValueObjectFactoryTemplateGenerator</code> instance.
     * @return such instance.
     */
    public static ValueObjectFactoryTemplateGenerator getInstance()
    {
        return ValueObjectFactoryTemplateGeneratorSingletonContainer.SINGLETON;
    }

    /**
     * Creates a <code>ValueObjectFactoryTemplate</code> using given
     * information.
     * @param tableName the table name.
     * @param metadataManager the database metadata manager.
     * @param customSqlProvider the CustomSqlProvider instance.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param basePackageName the base package name.
     * @param repositoryName the repository name.
     * @param header the header.
     * @param implementMarkerInterfaces whether to implement marker
     * interfaces.
     * @return the fresh new template.
     * @precondition tableName != null
     * @precondition metadataManager != null
     * @precondition customSqlProvider != null
     * @precondition packageName != null
     * @precondition engineName != null
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
        final String header,
        final boolean implementMarkerInterfaces)
    {
        return
            new ValueObjectFactoryTemplate(
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

    /**
     * Retrieves the decorator factory.
     * @return such instance.
     */
    public DecoratorFactory getDecoratorFactory()
    {
        return VODecoratorFactory.getInstance();
    }

    /**
     * Writes a <code>ValueObject</code> template to disk.
     * @param template the template to write.
     * @param outputDir the output folder.
     * @param charset the file encoding.
     * @throws IOException if the file cannot be created.
     * @precondition template instanceof BaseDAOFactoryTemplate
     * @precondition outputDir != null
     */
    public void write(
        final BasePerTableTemplate template,
        final File outputDir,
        final Charset charset)
      throws  IOException
    {
        write(
            template,
            outputDir,
            charset,
            ValueObjectTemplateGenerator.getInstance(),
            StringUtils.getInstance(),
            EnglishGrammarUtils.getInstance(),
            FileUtils.getInstance());
    }

    /**
     * Writes a value object template to disk.
     * @param valueObjectFactoryTemplate the value object factoryementation template
     * to write.
     * @param outputDir the output folder.
     * @param charset the file encoding.
     * @param valueObjectTemplateGenerator the
     * <code>ValueObjectTemplateGenerator</code> instance.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @param englishGrammarUtils the <code>EnglishGrammarUtils</code>
     * instance.
     * @param fileUtils the <code>FileUtils</code> instance.
     * @throws IOException if the file cannot be created.
     * @precondition valueObjectFactoryTemplate != null
     * @precondition outputDir != null
     * @precondition stringUtils != null
     * @precondition englishGrammarUtils != null
     * @precondition fileUtils != null
     */
    protected void write(
        final BasePerTableTemplate valueObjectFactoryTemplate,
        final File outputDir,
        final Charset charset,
        final ValueObjectTemplateGenerator valueObjectTemplateGenerator,
        final StringUtils stringUtils,
        final EnglishGrammarUtils englishGrammarUtils,
        final FileUtils fileUtils)
      throws  IOException
    {
        outputDir.mkdirs();

        fileUtils.writeFile(
              outputDir.getAbsolutePath()
            + File.separator
            + valueObjectTemplateGenerator.getVoClassName(
                  valueObjectFactoryTemplate.getTableName(),
                  englishGrammarUtils,
                  stringUtils)
            + "ValueObjectFactory.java",
            valueObjectFactoryTemplate.generate(),
            charset);
    }
}
