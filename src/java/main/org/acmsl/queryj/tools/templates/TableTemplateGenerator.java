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
 * Description: Is able to generate table repositories according to database
 *              metadata.
 *
 */
package org.acmsl.queryj.tools.templates;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.templates.TableTemplate;
import org.acmsl.queryj.tools.templates.TableTemplateFactory;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some Ant classes.
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
 * Is able to generate Table repositories according to database metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 */
public class TableTemplateGenerator
    implements  TableTemplateFactory
{
    /**
     * Singleton implemented as a weak reference.
     */
    private static WeakReference singleton;

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected TableTemplateGenerator() {};

    /**
     * Specifies a new weak reference.
     * @param generator the generator instance to use.
     */
    protected static void setReference(final TableTemplateGenerator generator)
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
     * Retrieves a TableTemplateGenerator instance.
     * @return such instance.
     */
    public static TableTemplateGenerator getInstance()
    {
        TableTemplateGenerator result = null;

        WeakReference reference = getReference();

        if  (reference != null) 
        {
            result = (TableTemplateGenerator) reference.get();
        }

        if  (result == null) 
        {
            result = new TableTemplateGenerator();

            setReference(result);
        }

        return result;
    }

    /**
     * Generates a table template.
     * @param packageName the package name.
     * @param tableName the table name.
     * @param project the project, for logging purposes.
     * @param task the task, for logging purposes.
     * @return a template.
     * @precondition packageName != null
     * @precondition tableName != null
     */
    public TableTemplate createTableTemplate(
        final String packageName,
        final String tableName,
        final Project project,
        final Task task)
    {
        return new TableTemplate(packageName, tableName, project, task);
    }

    /**
     * Writes a table template to disk.
     * @param tableTemplate the table template to write.
     * @param outputDir the output folder.
     * @throws IOException if the file cannot be created.
     * @precondition tableTemplate != null
     * @precondition outputDir != null
     */
    public void write(
        final TableTemplate tableTemplate, final File outputDir)
      throws  IOException
    {
        write(
            tableTemplate,
            outputDir,
            FileUtils.getInstance(),
            TableTemplateUtils.getInstance());
    }

    /**
     * Writes a table template to disk.
     * @param tableTemplate the table template to write.
     * @param outputDir the output folder.
     * @param fileUtils the <code>FileUtils</code> instance.
     * @param tableTemplateUtils the <code>TableTemplateUtils</code> instance.
     * @throws IOException if the file cannot be created.
     * @precondition tableTemplate != null
     * @precondition outputDir != null
     * @precondition fileUtils != null
     * @precondition tableTemplateUtils != null
     */
    protected void write(
        final TableTemplate tableTemplate,
        final File outputDir,
        final FileUtils fileUtils,
        final TableTemplateUtils tableTemplateUtils)
      throws  IOException
    {
        outputDir.mkdirs();

        fileUtils.writeFile(
              outputDir.getAbsolutePath()
            + File.separator
            + tableTemplateUtils.retrieveTableClassName(
                  tableTemplate.getTableName())
            + ".java",
            tableTemplate.generateOutput());
    }
}
