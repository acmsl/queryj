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
 * Filename: TimeFunctionsTestTemplateGenerator.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate the JUnit classes to test the Database's
 *              time functions.
 *
 */
package org.acmsl.queryj.tools.templates.functions.time;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.templates.TemplateMappingManager;
import org.acmsl.queryj.tools.templates.functions.time
    .TimeFunctionsTestTemplate;
import org.acmsl.queryj.tools.templates.functions.time
    .TimeFunctionsTestTemplateFactory;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.Singleton;
import org.acmsl.commons.logging.UniqueLogFactory;
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/*
 * Importing some Apache Commons Logging classes.
 */
import org.apache.commons.logging.Log;

/**
 * Is able to generate the JUnit classes to test the Database's time functions.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class TimeFunctionsTestTemplateGenerator
    implements  TimeFunctionsTestTemplateFactory,
                Singleton
{
    /**
     * Singleton implemented to avoid the double-checked locking.
     */
    private static class TimeFunctionsTestTemplateGeneratorSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final TimeFunctionsTestTemplateGenerator SINGLETON =
            new TimeFunctionsTestTemplateGenerator();
    }

    /**
     * Public constructor to allow reflective instantiation.
     */
    public TimeFunctionsTestTemplateGenerator() {};

    /**
     * Retrieves a {@link TimeFunctionsTestTemplateGenerator} instance.
     * @return such instance.
     */
    public static TimeFunctionsTestTemplateGenerator getInstance()
    {
        return TimeFunctionsTestTemplateGeneratorSingletonContainer.SINGLETON;
    }

    /**
     * Retrieves the template factory instance.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @return the template factory class name.
     * @throws QueryJBuildException if the factory class is invalid.
     * @precondition engineName != null
     */
    protected TimeFunctionsTestTemplateFactory getTemplateFactory(
        final String engineName, final String engineVersion)
      throws  QueryJBuildException
    {
        return
            getTemplateFactory(
                engineName,
                engineVersion,
                TemplateMappingManager.getInstance());
    }

    /**
     * Retrieves the template factory instance.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param templateMappingManager the {@link TemplateMappingManager}
     * instance.
     * @return the template factory class name.
     * @throws QueryJBuildException if the factory class is invalid.
     * @precondition engineName != null
     * @precondition templateMappingManager != null
     */
    protected TimeFunctionsTestTemplateFactory getTemplateFactory(
        final String engineName,
        final String engineVersion,
        final TemplateMappingManager templateMappingManager)
      throws  QueryJBuildException
    {
        TimeFunctionsTestTemplateFactory result = null;

        Object t_TemplateFactory =
            templateMappingManager.getTemplateFactory(
                TemplateMappingManager.TIME_FUNCTIONS_TEST_TEMPLATE,
                engineName,
                engineVersion);

        if  (t_TemplateFactory != null)
        {
            if  (!(t_TemplateFactory instanceof TimeFunctionsTestTemplateFactory))
            {
                throw
                    new QueryJBuildException(
                        "invalid.time.function.test.template.factory");
            }
            else 
            {
                result = (TimeFunctionsTestTemplateFactory) t_TemplateFactory;
            }
        }

        return result;
    }

    /**
     * Generates a time functions test template.
     * @param packageName the package name.
     * @param testedPackageName the tested package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @return a template.
     * @precondition packageName != null
     * @precondition engineName != null
     * @precondition engineVersion != null
     * @precondition quote != null
     */
    public TimeFunctionsTestTemplate createTimeFunctionsTestTemplate(
        final String packageName,
        final String testedPackageName,
        final String engineName,
        final String engineVersion,
        final String quote)
    {
        TimeFunctionsTestTemplate result = null;

        TimeFunctionsTestTemplateFactory t_TemplateFactory = null;

        try
        {
            t_TemplateFactory =
                getTemplateFactory(engineName, engineVersion);
        }
        catch  (final QueryJBuildException buildException)
        {
            Log t_Log =
                UniqueLogFactory.getLog(
                    TimeFunctionsTestTemplateGenerator.class);

            if  (t_Log != null)
            {
                t_Log.warn(
                    "Cannot retrieve time-functions test template",
                    buildException);
            }
        }

        if  (t_TemplateFactory != null)
        {
            result =
                t_TemplateFactory.createTimeFunctionsTestTemplate(
                    packageName,
                    testedPackageName,
                    engineName,
                    engineVersion,
                    quote);
        }

        return result;
    }

    /**
     * Writes a time functions test template to disk.
     * @param template the time functions template to write.
     * @param outputDir the output folder.
     * @param charset the file encoding.
     * @throws IOException if the file cannot be created.
     * @precondition template != null
     * @precondition outputDir != null
     */
    public void write(
        final TimeFunctionsTestTemplate template,
        final File outputDir,
        final Charset charset)
      throws  IOException
    {
        write(
            template,
            outputDir,
            charset,
            StringUtils.getInstance(),
            FileUtils.getInstance());
    }

    /**
     * Writes a time functions test template to disk.
     * @param timeFunctionsTestTemplate the time functions template to write.
     * @param outputDir the output folder.
     * @param charset the file encoding.
     * @param stringUtils the {@link StringUtils} instance.
     * @param fileUtils the {@link FileUtils} instance.
     * @throws IOException if the file cannot be created.
     * @precondition template != null
     * @precondition outputDir != null
     * @precondition stringUtils != null
     * @precondition fileUtils != null
     */
    protected void write(
        final TimeFunctionsTestTemplate template,
        final File outputDir,
        final Charset charset,
        final StringUtils stringUtils,
        final FileUtils fileUtils)
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
                + "TimeFunctionsTest.java",
                template.generate(),
                charset);
        }
    }
}
