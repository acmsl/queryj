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
 * Description: Is able to generate procedure repository templates according to
 *              database metadata.
 *
 * Last modified by: $Author$ at $Date$
 *
 * File version: $Revision$
 *
 * Project version: $Name$
 *
 * $Id$
 *
 */
package org.acmsl.queryj.tools.templates;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.templates.ProcedureRepositoryTemplate;
import org.acmsl.queryj.tools.templates.ProcedureRepositoryTemplateFactory;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.utils.io.FileUtils;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Is able to generate procedure repositories template according to
 * database metadata.
 * @author <a href="mailto:jsanleandro@yahoo.es"
 *         >Jose San Leandro</a>
 * @version $Revision$
 */
public class ProcedureRepositoryTemplateGenerator
    implements  ProcedureRepositoryTemplateFactory
{
    /**
     * Singleton implemented as a weak reference.
     */
    private static WeakReference singleton;

    /**
     * Protected constructor to avoid accidental instantiation.
     */
    protected ProcedureRepositoryTemplateGenerator() {};

    /**
     * Specifies a new weak reference.
     * @param generator the generator instance to use.
     */
    protected static void setReference(
        final ProcedureRepositoryTemplateGenerator generator)
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
     * Retrieves a ProcedureRepositoryTemplateGenerator instance.
     * @return such instance.
     */
    public static ProcedureRepositoryTemplateGenerator getInstance()
    {
        ProcedureRepositoryTemplateGenerator result = null;

        WeakReference reference = getReference();

        if  (reference != null) 
        {
            result = (ProcedureRepositoryTemplateGenerator) reference.get();
        }

        if  (result == null) 
        {
            result = new ProcedureRepositoryTemplateGenerator();

            setReference(result);
        }

        return result;
    }

    /**
     * Generates a procedure repository template.
     * @param packageName the package name.
     * @param repository the repository.
     * @return such template.
     * @throws IOException if the file cannot be created.
     * @precondition packageName != null
     * @precondition repository != null
     */
    public ProcedureRepositoryTemplate createProcedureRepositoryTemplate(
        final String packageName, final String repository)
    {
        return
            new ProcedureRepositoryTemplate(packageName, repository);
    }

    /**
     * Writes a procedure repository template to disk.
     * @param procedureRepositoryTemplate the procedure repository to write.
     * @param outputDir the output folder.
     * @throws IOException if the file cannot be created.
     */
    public void write(
        final ProcedureRepositoryTemplate procedureRepositoryTemplate,
        final File outputDir)
      throws  IOException
    {
        write(
            procedureRepositoryTemplate,
            outputDir,
            ProcedureRepositoryTemplateUtils.getInstance(),
            FileUtils.getInstance());
    }

    /**
     * Writes a procedure repository template to disk.
     * @param procedureRepositoryTemplate the procedure repository to write.
     * @param outputDir the output folder.
     * @param procedureRepositoryTemplateUtils the
     * <code>ProcedureRepositoryTemplateUtils</code> instance.
     * @param fileUtils the <code>FileUtils</code> instance.
     * @throws IOException if the file cannot be created.
     * @precondition procedureRepositoryTemplate != null
     * @precondition outputDir != null
     * @precondition procedureRepositoryTemplateUtils != null
     * @precondition fileUtils != null
     */
    protected void write(
        final ProcedureRepositoryTemplate procedureRepositoryTemplate,
        final File outputDir,
        final ProcedureRepositoryTemplateUtils procedureRepositoryTemplateUtils,
        final FileUtils fileUtils)
      throws  IOException
    {
        if  (!procedureRepositoryTemplate.isEmpty())
        {
            File t_FinalOutputDir =
                new File(
                      outputDir.getAbsolutePath()
                    + File.separator
                    + fileUtils.packageToPath(
                          procedureRepositoryTemplate.getPackageName()));

            t_FinalOutputDir.mkdirs();

            fileUtils.writeFile(
                  t_FinalOutputDir.getAbsolutePath()
                + File.separator
                + procedureRepositoryTemplateUtils
                      .retrieveProcedureRepositoryClassName(
                          procedureRepositoryTemplate.getRepository()),
                procedureRepositoryTemplate.toString());
        }
    }
}
