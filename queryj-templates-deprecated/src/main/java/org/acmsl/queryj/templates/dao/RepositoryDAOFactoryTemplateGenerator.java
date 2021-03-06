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
 * Filename: RepositoryDAOFactoryTemplateGenerator.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Is able to generate the repository DAO factory implementation.
 *
 */
package org.acmsl.queryj.templates.dao;

/*
 * Importing some project-specific classes.
 */
import org.acmsl.queryj.api.AbstractTemplateGenerator;
import org.acmsl.queryj.api.PerRepositoryTemplateGenerator;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/**
 * Is able to generate the repository DAO factory implementation.
 * @author <a href="mailto:chous@acm-sl.org">Jose San Leandro Armendariz</a>
 */
@ThreadSafe
public class RepositoryDAOFactoryTemplateGenerator
    extends AbstractTemplateGenerator<RepositoryDAOFactoryTemplate>
    implements PerRepositoryTemplateGenerator<RepositoryDAOFactoryTemplate>
{
    /**
     * Creates a new {@link RepositoryDAOFactoryTemplateGenerator} with given settings.
     * @param caching whether to enable caching.
     * @param threadCount the threads to use.
     */
    @ThreadSafe
    public RepositoryDAOFactoryTemplateGenerator(final boolean caching, final int threadCount)
    {
        super(caching, threadCount);
    }
}
