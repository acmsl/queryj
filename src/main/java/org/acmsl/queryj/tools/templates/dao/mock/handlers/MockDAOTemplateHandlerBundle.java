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
 * Filename: MockDAOTemplateHandlerBundle.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Bundles a pair of MockDAO template build and writing
 *              handlers.
 *
 */
package org.acmsl.queryj.tools.templates.dao.mock.handlers;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.templates.dao.mock.handlers.MockDAOTemplateBuildHandler;
import org.acmsl.queryj.tools.templates.dao.mock.handlers.MockDAOTemplateWritingHandler;
import org.acmsl.queryj.tools.templates.handlers.TemplateHandlerBundle;

/**
 * Bundles a pair of template MockDAO template build and writing
 * handlers.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
public class MockDAOTemplateHandlerBundle
    extends  TemplateHandlerBundle
{
    /**
     * Builds a bundle with given handlers.
     * @param buildHandler the template build handler.
     * @param writingHandler the writing handler.
     * @precondition buildHandler != null
     * @precondition writingHandler != null
     */
    public MockDAOTemplateHandlerBundle()
    {
        super(
            new MockDAOTemplateBuildHandler(),
            new MockDAOTemplateWritingHandler());
    }
}
