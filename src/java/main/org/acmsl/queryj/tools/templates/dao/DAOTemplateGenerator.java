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
 * Description: Is able to generate DAO implementations according to
 *              database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.QueryJException;
import org.acmsl.queryj.tools.customsql.CustomSqlProvider;
import org.acmsl.queryj.tools.DatabaseMetaDataManager;
import org.acmsl.queryj.tools.templates.dao.DAOTemplate;
import org.acmsl.queryj.tools.templates.dao.DAOTemplateFactory;
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.EnglishGrammarUtils;
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing Ant classes.
 */
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Is able to generate DAO implementations according to database
 * metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
           >Jose San Leandro</a>
 */
public class DAOTemplateGenerator
    implements  DAOTemplateFactory
{
    /**
     * Singleton implemented as a weak reference.
     */
    private static WeakReference singleton;

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected DAOTemplateGenerator() {};

    /**
     * Specifies a new weak reference.
     * @param generator the generator instance to use.
     */
    protected static void setReference(final DAOTemplateGenerator generator)
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
     * Retrieves a DAOTemplateGenerator instance.
     * @return such instance.
     */
    public static DAOTemplateGenerator getInstance()
    {
        DAOTemplateGenerator result = null;

        WeakReference reference = getReference();

        if  (reference != null) 
        {
            result = (DAOTemplateGenerator) reference.get();
        }

        if  (result == null) 
        {
            result = new DAOTemplateGenerator();

            setReference(result);
        }

        return result;
    }

    /**
     * Adds a new template factory class.
     * @param daoName the DAO name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param templateFactoryClass the template factory.
     * @precondition engineName != null
     * @precondition templateFactoryClass != null
     */
    public void addTemplateFactoryClass(
        final String daoName,
        final String engineName,
        final String engineVersion,
        final String templateFactoryClass)
    {
        addTemplateFactoryClass(
            daoName,
            engineName,
            engineVersion,
            templateFactoryClass,
            TemplateMappingManager.getInstance());
    }

    /**
     * Adds a new template factory class.
     * @param daoName the DAO name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param templateFactoryClass the template factory.
     * @param templateMappingManager the <code>TemplateMappingManager</code
     * instance.
     * @precondition engineName != null
     * @precondition templateFactoryClass != null
     * @precondition templateMappingManager != null
     */
    protected void addTemplateFactoryClass(
        final String daoName,
        final String engineName,
        final String engineVersion,
        final String templateFactoryClass,
        final TemplateMappingManager templateMappingManager)
    {
        templateMappingManager.addTemplateFactoryClass(
            TemplateMappingManager.DAO_TEMPLATE_PREFIX + daoName,
            engineName,
            engineVersion,
            templateFactoryClass);
    }

    /**
     * Retrieves the template factory class.
     * @param daoName the DAO name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @return the template factory class name.
     * @precondition engineName != null
     */
    protected String getTemplateFactoryClass(
        final String daoName,
        final String engineName,
        final String engineVersion)
    {
        return
            getTemplateFactoryClass(
                daoName,
                engineName,
                engineVersion,
                TemplateMappingManager.getInstance());
    }

    /**
     * Retrieves the template factory class.
     * @param daoName the DAO name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param templateMappingManager the <code>TemplateMappingManager</code
     * instance.
     * @return the template factory class name.
     * @precondition engineName != null
     * @precondition templateMappingManager != null
     */
    protected String getTemplateFactoryClass(
        final String daoName,
        final String engineName,
        final String engineVersion,
        final TemplateMappingManager templateMappingManager)
    {
        return
            templateMappingManager.getTemplateFactoryClass(
                TemplateMappingManager.DAO_TEMPLATE_PREFIX + daoName,
                engineName,
                engineVersion);
    }

    /**
     * Retrieves the template factory instance.
     * @param daoName the DAO name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @return the template factory class name.
     * @throws QueryJException if the factory class is invalid.
     */
    protected DAOTemplateFactory getTemplateFactory(
        final String daoName,
        final String engineName,
        final String engineVersion)
      throws  QueryJException
    {
        return
            getTemplateFactory(
                daoName,
                engineName,
                engineVersion,
                TemplateMappingManager.getInstance());
    }

    /**
     * Retrieves the template factory instance.
     * @param daoName the DAO name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param templateMappingManager the <code>TemplateMappingManager</code
     * instance.
     * @return the template factory class name.
     * @throws QueryJException if the factory class is invalid.
     * @precondition templateMappingManager != null
     */
    protected DAOTemplateFactory getTemplateFactory(
        final String daoName,
        final String engineName,
        final String engineVersion,
        final TemplateMappingManager templateMappingManager)
      throws  QueryJException
    {
        DAOTemplateFactory result = null;

        Object t_TemplateFactory =
            templateMappingManager.getTemplateFactoryClass(
                TemplateMappingManager.DAO_TEMPLATE_PREFIX + daoName,
                engineName,
                engineVersion);

        if  (t_TemplateFactory != null)
        {
            if  (!(t_TemplateFactory instanceof DAOTemplateFactory))
            {
                throw
                    new QueryJException(
                        "invalid.dao.template.factory");
            }
            else 
            {
                result = (DAOTemplateFactory) t_TemplateFactory;
            }
        }

        return result;
    }

    /**
     * Generates a DAO template.
     * @param tableTemplate the table template.
     * @param metaDataManager the metadata manager.
     * @param customSqlProvider the CustomSqlProvider instance.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @param basePackageName the base package name.
     * @param repositoryName the name of the repository.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return a template.
     * @throws QueryJException if the factory class is invalid.
     * @precondition tableTemplate != null
     * @precondition metaDataManager != null
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition engineVersion != null
     * @precondition quote != null
     * @precondition basePackageName != null
     * @precondition repositoryName != null
     */
    public DAOTemplate createDAOTemplate(
        final TableTemplate tableTemplate,
        final DatabaseMetaDataManager metaDataManager,
        final CustomSqlProvider customSqlProvider,
        final String packageName,
        final String engineName,
        final String engineVersion,
        final String quote,
        final String basePackageName,
        final String repositoryName,
        final Project project,
        final Task task)
      throws  QueryJException
    {
        DAOTemplate result = null;

        DAOTemplateFactory t_TemplateFactory =
            getTemplateFactory(
                tableTemplate.getTableName(), engineName, engineVersion);

        if  (t_TemplateFactory != null)
        {
            result =
                t_TemplateFactory.createDAOTemplate(
                    tableTemplate,
                    metaDataManager,
                    customSqlProvider,
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    basePackageName,
                    repositoryName,
                    project,
                    task);
        }
        else 
        {
            result =
                new DAOTemplate(
                    tableTemplate,
                    metaDataManager,
                    customSqlProvider,
                    packageName,
                    engineName,
                    engineVersion,
                    quote,
                    basePackageName,
                    repositoryName,
                    project,
                    task);
        }

        return result;
    }

    /**
     * Writes a DAO template to disk.
     * @param daoTemplate the DAO template to write.
     * @param outputDir the output folder.
     * @throws IOException if the file cannot be created.
     * @precondition daoTemplate != null
     * @precondition outputDir != null
     */
    public void write(
        final DAOTemplate daoTemplate,
        final File outputDir)
      throws  IOException
    {
        write(
            daoTemplate,
            outputDir, 
            StringUtils.getInstance(),
            EnglishGrammarUtils.getInstance(),
            FileUtils.getInstance());
    }

    /**
     * Writes a DAO template to disk.
     * @param daoTemplate the DAO template to write.
     * @param outputDir the output folder.
     * @param stringUtils the <code>StringUtils</code> instance.
     * @param englishGrammarUtils the <code>EnglishGrammarUtils</code>
     * instance.
     * @param fileUtils the <code>FileUtils</code> instance.
     * @throws IOException if the file cannot be created.
     * @precondition daoTemplate != null
     * @precondition outputDir != null
     * @precondition stringUtils != null
     * @precondition englishGrammarUtils != null
     * @precondition fileUtils != null
     */
    protected void write(
        final DAOTemplate daoTemplate,
        final File outputDir,
        final StringUtils stringUtils,
        final EnglishGrammarUtils englishGrammarUtils,
        final FileUtils fileUtils)
      throws  IOException
    {
        outputDir.mkdirs();

        fileUtils.writeFile(
            outputDir.getAbsolutePath()
            + File.separator
            + daoTemplate.getEngineName()
            + stringUtils.capitalize(
                englishGrammarUtils.getSingular(
                    daoTemplate
                        .getTableTemplate()
                            .getTableName().toLowerCase()),
                '_')
            + "DAO.java",
            daoTemplate.generate());
    }
}
