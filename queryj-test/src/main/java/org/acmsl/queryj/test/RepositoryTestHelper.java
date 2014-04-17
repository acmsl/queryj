/*
                        QueryJ Test

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
 * Filename: RepositoryTestHelper.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Helper class for per-foreign key Cucumber tests.
 *
 * Date: 2014/04/17
 * Time: 11:33
 *
 */
package org.acmsl.queryj.test;

/*
 * Importing JetBrains annotations.
 */
import org.jetbrains.annotations.NotNull;

/*
 * Importing checkthread.org annotations.
 */
import org.checkthread.annotations.ThreadSafe;

/**
 * Helper class for per-foreign key Cucumber tests.
 * @author <a href="mailto:queryj@acm-sl.org">Jose San Leandro</a>
 * @since 3.0
 * Created: 2014/04/17 11:33
 */
@SuppressWarnings("unused")
@ThreadSafe
public class RepositoryTestHelper
{
    /**
     * Singleton implementation to avoid double-locking check.
     */
    protected static final class RepositoryTestHelperSingletonContainer
    {
        /**
         * The actual singleton.
         */
        public static final RepositoryTestHelper SINGLETON = new RepositoryTestHelper();
    }

    /**
     * Retrieves the singleton instance.
     * @return such instance.
     */
    @NotNull
    public static RepositoryTestHelper getInstance()
    {
        return RepositoryTestHelperSingletonContainer.SINGLETON;
    }
}