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
 * Filename: SystemFunctionsTemplateFactory.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to create system function templates.
 *
 */
package org.acmsl.queryj.tools.templates.functions.system;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.queryj.tools.templates.functions.system
    .SystemFunctionsTemplate;
import org.acmsl.queryj.tools.templates.TemplateFactory;
import org.jetbrains.annotations.Nullable;

/**
 * Is able to create system function templates.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public interface SystemFunctionsTemplateFactory
    extends  TemplateFactory
{
    /**
     * Generates a system functions template.
     * @param packageName the package name.
     * @param engineName the engine name.
     * @param engineVersion the engine version.
     * @param quote the identifier quote string.
     * @return a template.
     */
    @Nullable
    public SystemFunctionsTemplate createSystemFunctionsTemplate(
        final String packageName,
        final String engineName,
        final String engineVersion,
        final String quote);
}
