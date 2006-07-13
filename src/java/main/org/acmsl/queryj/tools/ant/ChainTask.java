//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-2006  Jose San Leandro Armendariz
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
 * Description: Manages a sequential chain of Ant actions.
 *
 */
package org.acmsl.queryj.tools.ant;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.QueryJBuildException;
import org.acmsl.queryj.tools.QueryJCommand;
import org.acmsl.queryj.tools.handlers.QueryJCommandHandler;
import org.acmsl.queryj.tools.logging.QueryJAntLog;

/*
 * Importing some ACM-SL classes.
 */
import org.acmsl.commons.patterns.ArrayListChainAdapter;
import org.acmsl.commons.patterns.Chain;
import org.acmsl.commons.utils.StringUtils;

/*
 * Importing some JDK classes.
 */
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/**
 * Manages a sequential chain of Ant actions.
 * @author <a href="mailto:chous@acm-sl.org"
 *         >Jose San Leandro</a>
 */
public abstract class ChainTask
    extends  Task
{
    /**
     * The task chain.
     */
    private Chain m__Chain;

    /**
     * Constructs a Chain task.
     */
    public ChainTask()
    {
        super(); // redundant
        immutableSetChain(new ArrayListChainAdapter());
    }

    /**
     * Specifies the chain.
     * @param chain the new chain.
     */
    private void immutableSetChain(final Chain chain)
    {
        m__Chain = chain;
    }

    /**
     * Specifies the chain.
     * @param chain the new chain.
     */
    protected void setChain(final Chain chain)
    {
        immutableSetChain(chain);
    }

    /**
     * Retrieves the chain.
     * @return such chain.
     */
    public Chain getChain()
    {
        return m__Chain;
    }

    /**
     * Requests the chained logic to be performed.
     * @throws BuildException whenever the required
     * parameters are not present or valid.
     */
    public void execute()
        throws  BuildException
    {
        try
        {
            process(
                buildChain(getChain()),
                buildCommand(
                    new QueryJCommand(new QueryJAntLog(getProject()))));
        }
        catch  (final QueryJBuildException buildException)
        {
            throw
                new BuildException(
                    buildException.getMessage(), buildException);
        }
    }

    /**
     * Builds the chain.
     * @param chain the chain to be configured.
     * @return the updated chain.
     */
    protected abstract Chain buildChain(final Chain chain);

    /**
     * Builds the command.
     * @param command the command to be initialized.
     * @return the initialized command.
     */
    protected abstract QueryJCommand buildCommand(final QueryJCommand command);

    /**
     * Retrieves the link of the chain just after the one given command
     * handlar takes.
     * @param chain the concrete chain.
     * @param commandHandler the handler just before the desired link.
     * @return the next hanlder in the chain.
     * @precondition chain != null
     */
    public QueryJCommandHandler getNextChainLink(
        final Chain chain, final QueryJCommandHandler commandHandler)
    {
        QueryJCommandHandler result = null;

        if  (   (chain != null)
             && (!chain.isEmpty()))
        {
            if  (   (commandHandler == null)
                 || (!chain.contains(commandHandler)))
            {
                result = (QueryJCommandHandler) chain.get(0);
            }
            else
            {
                int t_iCurrentIndex = chain.indexOf(commandHandler);

                if  (   (t_iCurrentIndex >= 0)
                     && (t_iCurrentIndex < chain.size() - 1))
                {
                    result =
                        (QueryJCommandHandler) chain.get(t_iCurrentIndex + 1);
                }
            }
        }

        return result;
    }
    /**
     * Sends given command to a concrete chain.
     * @param chain the concrete chain.
     * @param command the command that represents which actions should be done.
     * @return <code>true</code> if the command is processed by the chain.
     * @throws QueryJBuildException if the build process cannot be performed.
     */
    protected boolean process(
        final Chain chain, final QueryJCommand command)
      throws QueryJBuildException
    {
        boolean result = false;

        try 
        {
            QueryJCommandHandler t_CurrentCommandHandler = null;

            do
            {
                t_CurrentCommandHandler =
                    getNextChainLink(chain, t_CurrentCommandHandler);

                if  (t_CurrentCommandHandler != null)
                {
                    result = t_CurrentCommandHandler.handle(command);
                }
            }
            while  (   (!result)
                    && (t_CurrentCommandHandler != null));
        }
        catch  (QueryJBuildException buildException)
        {
            cleanUpOnError(buildException, command);

            throw buildException;
        }

        return result;
    }

    /**
     * Performs any clean up whenever an error occurs.
     * @param buildException the error that triggers this clean-up.
     * @param command the command.
     */
    protected abstract void cleanUpOnError(
        final QueryJBuildException buildException,
        final QueryJCommand command);
}
