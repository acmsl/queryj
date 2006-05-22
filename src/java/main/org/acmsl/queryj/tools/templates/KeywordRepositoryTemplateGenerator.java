//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-2005  Jose San Leandro Armendariz
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
 * Description: Is able to generate keyword repository templates according to
 *              keyword definition.
 *
 */
package org.acmsl.queryj.tools.templates;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.metadata.CachingDecoratorFactory;
import org.acmsl.queryj.tools.metadata.DecorationUtils;
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.metadata.MetadataManager;
import org.acmsl.queryj.tools.metadata.MetadataTypeManager;
import org.acmsl.queryj.tools.templates.KeywordRepositoryTemplate;
import org.acmsl.queryj.tools.templates.KeywordRepositoryTemplateFactory;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.io.FileUtils;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Is able to generate keyword repositories template according to
 * keyword definition.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public class KeywordRepositoryTemplateGenerator
    implements  KeywordRepositoryTemplateFactory,
                BasePerRepositoryTemplateGenerator
{
    /**
     * Singleton implemented as a weak reference.
     */
    private static WeakReference singleton;

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected KeywordRepositoryTemplateGenerator() {};

    /**
     * Specifies a new weak reference.
     * @param generator the generator instance to use.
     */
    protected static void setReference(
        KeywordRepositoryTemplateGenerator generator)
    {
        singleton = new WeakReference(generator);
    }

    /**
     * Retrieves the weak reference.
     * @return such reference.
     */
    protected static WeakReference getReference()
    {
        return singleton;
    }

    /**
     * Retrieves a KeywordRepositoryTemplateGenerator instance.
     * @return such instance.
     */
    public static KeywordRepositoryTemplateGenerator getInstance()
    {
        KeywordRepositoryTemplateGenerator result = null;

        WeakReference reference = getReference();

        if  (reference != null) 
        {
            result = (KeywordRepositoryTemplateGenerator) reference.get();
        }

        if  (result == null) 
        {
            result = new KeywordRepositoryTemplateGenerator();

            setReference(result);
        }

        return result;
    }

    /**
     * Generates a <i>per-repository</i> template.
     * @param metadataManager the metadata manager.
     * @param metadataTypeManager the database metadata type manager.
     * @param customSqlProvider the <code>CustomSqlProvider</code> instance.
     * @param packageName the package name.
     * @param basePackageName the base package name.
     * @param engineName the engine name.
     * @param repositoryName the name of the repository.
     * @param tables the tables.
     * @param header the header.
     * @return a template.
     * @throws QueryJException if the input values are invalid.
     */
    public BasePerRepositoryTemplate createTemplate(
        final MetadataManager metadataManager,
        final MetadataTypeManager metadataTypeManager,
        final CustomSqlProvider customSqlProvider,
        final String packageName,
        final String basePackageName,
        final String repositoryName,
        final String engineName,
        final String header)
      throws  QueryJException
    {
        return
            new KeywordRepositoryTemplate(
                metadataManager,
                metadataTypeManager,
                customSqlProvider,
                header,
                getDecoratorFactory(),
                packageName,
                basePackageName,
                repositoryName,
                engineName);
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
     * Writes a keyword repository template to disk.
     * @param template the keyword repository to write.
     * @param outputDir the output folder.
     * @throws IOException if the file cannot be created.
     */
    public void write(
        final BasePerRepositoryTemplate template,
        final File outputDir)
      throws  IOException
    {
        write(
            template,
            outputDir,
            DecorationUtils.getInstance(),
            FileUtils.getInstance());
    }

    /**
     * Writes a keyword repository template to disk.
     * @param template the keyword repository to write.
     * @param outputDir the output folder.
     * @param decorationUtils the <code>DecorationUtils</code> instance.
     * @param fileutils the <code>FileUtils</code> instance.
     * @throws IOException if the file cannot be created.
     * @precondition template != null
     * @precondition outputDir != null
     * @precondition decorationUtils != null
     * @precondition fileUtils != null
     */
    protected void write(
        final BasePerRepositoryTemplate template,
        final File outputDir,
        final DecorationUtils decorationUtils,
        final FileUtils fileUtils)
      throws  IOException
    {
        outputDir.mkdirs();

        fileUtils.writeFile(
              outputDir.getAbsolutePath()
            + File.separator
            + decoration.capitalize(
                  template.getRepositoryName())
            + "KeywordRepository.java",
            template.generate());
    }
}
