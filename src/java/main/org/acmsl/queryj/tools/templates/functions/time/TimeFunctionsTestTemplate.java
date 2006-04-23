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
 * Description: Is able to generate tests on Database's time functions.
 *              database metadata.
 *
 */
package org.acmsl.queryj.tools.templates.functions.time;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.tools.metadata.DecoratorFactory;
import org.acmsl.queryj.tools.templates.functions.FunctionsTestTemplate;

/**
 * Is able to generate tests on Database's time functions.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public abstract class TimeFunctionsTestTemplate
    extends  FunctionsTestTemplate
{
    /**
     * Builds a <code>TimeFunctionsTestTemplate</code> using given information.
     * @param decoratorFactory the <code>DecoratorFactory</code> instance.
     * @param packageName the package name.
     * @param testedPackageName the tested package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     */
    public TimeFunctionsTestTemplate(
        final DecoratorFactory decoratorFactory,
        final String  packageName,
        final String  testedPackageName,
        final String  engineName,
        final String  engineVersion,
        final String  quote)
    {
        super(
            decoratorFactory,
            "Time/Date",
            "Time",
            packageName,
            testedPackageName,
            engineName,
            engineVersion,
            quote);
    }

    /**
     * Retrieves the test name.
     * @return such name.
     */
    public String getTestName()
    {
        return "TimeFunctionsTest";
    }
}
