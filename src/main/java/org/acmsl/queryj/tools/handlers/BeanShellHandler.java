//;-*- mode: java -*-
/*
                        QueryJ

    Copyright (C) 2002-2007  Jose San Leandro Armendariz
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
 * Filename: BeanShellHandler.java
 *
 * Author: Jose San Leandro Armendariz
 *
 * Description: Conditionally launches a BeanShell to allow the user
 *              interact with the model and perform acceptance tests.
 *
 */
package org.acmsl.queryj.tools.handlers;

/*
 * Importing some BeanShell classes.
 */
import bsh.BshClassManager;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;

/*
 * Importing some project classes.
 */
import org.acmsl.queryj.tools.AntCommand;
import org.acmsl.queryj.tools.handlers.AbstractAntCommandHandler;

/*
 * Importing some ACM-SL Commons classes.
 */
import org.acmsl.commons.logging.UniqueLogFactory;
import org.acmsl.commons.patterns.Command;
import org.acmsl.commons.utils.ClassLoaderUtils;

/*
 * Importing some Ant classes.
 */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.apache.tools.ant.types.Path;

/*
 * Importing some Commons-Logging classes.
 */
import org.apache.commons.logging.Log;

/*
 * Importing some JDK classes.
 */
import java.io.IOException;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Conditionally launches a BeanShell to allow the user
 * interact with the model and perform acceptance tests.
 * @author <a href="mailto:chous@acm-sl.org"
           >Jose San Leandro</a>
 */
public class BeanShellHandler
    extends  AbstractAntCommandHandler
{
    /**
     * The initial messages.
     */
    static final String[] INITIAL_MESSAGES =
        {
            "Important notice:",
            "-All QueryJ packages are automatically imported.",
            "-Type 'quit' when done."
        };

    /**
     * The available packages.
     */
    static final String[] AVAILABLE_PACKAGES =
        {
            "org.acmsl.queryj",
            "org.acmsl.queryj.dao",
            "org.acmsl.queryj.tools",
            "org.acmsl.queryj.tools.antlr",
            "org.acmsl.queryj.tools.customsql",
            "org.acmsl.queryj.tools.customsql.handlers",
            "org.acmsl.queryj.tools.customsql.xml",
            "org.acmsl.queryj.tools.handlers",
            "org.acmsl.queryj.tools.handlers.oracle",
            "org.acmsl.queryj.tools.templates",
            "org.acmsl.queryj.tools.templates.dao",
            "org.acmsl.queryj.tools.templates.dao.handlers",
            "org.acmsl.queryj.tools.templates.dao.mock",
            "org.acmsl.queryj.tools.templates.dao.mock.handlers",
            "org.acmsl.queryj.tools.templates.dao.xml",
            "org.acmsl.queryj.tools.templates.dao.xml.handlers",
            "org.acmsl.queryj.tools.templates.functions",
            "org.acmsl.queryj.tools.templates.functions.numeric",
            "org.acmsl.queryj.tools.templates.functions.numeric.handlers",
            "org.acmsl.queryj.tools.templates.functions.numeric.mysql",
            "org.acmsl.queryj.tools.templates.functions.numeric.oracle",
            "org.acmsl.queryj.tools.templates.functions.system",
            "org.acmsl.queryj.tools.templates.functions.system.handlers",
            "org.acmsl.queryj.tools.templates.functions.system.mysql",
            "org.acmsl.queryj.tools.templates.functions.system.oracle",
            "org.acmsl.queryj.tools.templates.functions.text",
            "org.acmsl.queryj.tools.templates.functions.text.handlers",
            "org.acmsl.queryj.tools.templates.functions.text.mysql",
            "org.acmsl.queryj.tools.templates.functions.text.oracle",
            "org.acmsl.queryj.tools.templates.functions.time",
            "org.acmsl.queryj.tools.templates.functions.time.handlers",
            "org.acmsl.queryj.tools.templates.functions.time.mysql",
            "org.acmsl.queryj.tools.templates.functions.time.oracle",
            "org.acmsl.queryj.tools.templates.handlers",
            "org.acmsl.queryj.tools.templates.valueobject",
            "org.acmsl.queryj.tools.templates.valueobject.handlers"
        };

    /**
     * The QueryJ prompt.
     */
    public static final String PROMPT = "(queryj-bsh)";

    /**
     * Creates a BeanShellHandler.
     */
    public BeanShellHandler() {};

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     */
    public boolean handle(final Command command)
    {
        boolean result = false;

        if  (command instanceof AntCommand) 
        {
            AntCommand t_AntCommand = (AntCommand) command;
            
            result = handle(t_AntCommand);
        }
        
        return result;
    }

    /**
     * Handles given command.
     * @param command the command to handle.
     * @return <code>true</code> if the chain should be stopped.
     * @precondition command != null
     */
    public boolean handle(final AntCommand command)
    {
        return handle(command.getAttributeMap());
    }

    /**
     * Handles given information.
     * @param parameters the parameters.
     * @return <code>true</code> if the chain should be stopped.
     * @precondition parameters != null
     */
    protected boolean handle(final Map parameters)
    {
        Log t_Log = UniqueLogFactory.getLog(BeanShellHandler.class);
                
        Boolean t_bShell =
            (Boolean) parameters.get(ParameterValidationHandler.SHELL);

        if  (Boolean.TRUE.equals(t_bShell))
        {
            try
            {
                Interpreter t_Interpreter = setUpInterpreter(parameters);

                int t_iCount =
                    INITIAL_MESSAGES != null ? INITIAL_MESSAGES.length : 0;

                StringBuffer t_sbInitialMessage = new StringBuffer();

                for  (int t_iIndex = 0; t_iIndex < t_iCount; t_iIndex++)
                {
                    t_sbInitialMessage.append(INITIAL_MESSAGES[t_iIndex] + "\n");
                }

                t_Log.info(t_sbInitialMessage.toString());

                boolean t_bFinished = false;

                while (!t_bFinished)
                {
                    InputRequest t_Request =
                        new BshInputRequest(PROMPT, t_Interpreter);

                    new DefaultInputHandler().handleInput(t_Request);

                    t_bFinished = t_Request.isInputValid();
                }
            }
            catch  (final EvalError evalError)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot launch BeanShell.",
                        evalError);
                }
            }
            catch  (final MalformedURLException malformedUrlException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot launch BeanShell.",
                        malformedUrlException);
                }
            }
            catch  (final IOException ioException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot launch BeanShell.",
                        ioException);
                }
            }
            catch  (final BuildException buildException)
            {
                if  (t_Log != null)
                {
                    t_Log.error(
                        "Cannot launch BeanShell.",
                        buildException);
                }
            }
        }

        return false;
    }

    /**
     * Sets up a new interpreter.
     * @param parameters the command parameters.
     * @return the configured Interpreter instance.
     * @throws EvalError if the interpreter cannot be configured.
     * @throws MalformedURLException if the classpath cannot be configured.
     * @throws IOException if the Interpreter cannot be configured.
     */
    protected Interpreter setUpInterpreter(final Map parameters)
        throws  EvalError,
                MalformedURLException,
                IOException
    {
        Interpreter result = new Interpreter();

        result.setClassLoader(BeanShellHandler.class.getClassLoader());

        result.set("bsh.prompt", "(queryj) %");
        result.set("bsh.interactive", true);
        result.set("bsh.eval", true);
        NameSpace t_NameSpace = result.getNameSpace();
        BshClassManager t_ClassManager = result.getClassManager();

        if  (t_NameSpace == null)
        {
            t_NameSpace = new NameSpace(t_ClassManager, "queryj");
            result.setNameSpace(t_NameSpace);
        }

        result.set("bsh.ns", t_NameSpace);
        result.set("queryj.parameters", parameters);
        result.set("queryj.metadata", retrieveMetadataManager(parameters));
        Path t_Path =
            (Path) parameters.get(ParameterValidationHandler.CLASSPATH);

        int t_iCount = 0;

        int t_iIndex = 0;

        if  (t_Path != null)
        {
            String[] t_astrPaths = t_Path.list();

            String t_strPath;

            t_iCount = (t_astrPaths != null) ? t_astrPaths.length : 0;

            for  (t_iIndex = 0; t_iIndex < t_iCount; t_iIndex++)
            {
                t_strPath = t_astrPaths[t_iIndex];

                if  (   (t_strPath != null)
                     && (t_strPath.trim().length() > 0))
                {
                    t_ClassManager.addClassPath(
                        new URL("file://" + t_strPath));
                }
            }
        }

        t_iCount =
            AVAILABLE_PACKAGES != null ? AVAILABLE_PACKAGES.length : 0;

        for  (t_iIndex = 0; t_iIndex < t_iCount; t_iIndex++)
        {
            t_NameSpace.importPackage(AVAILABLE_PACKAGES[t_iIndex]);
        }

//        sanityCheck();

        return result;
    }

    /**
     * Performs a sanity check.
     * @param interpreter the interpreter.
     * @throws  EvalError if the interpreter does not see QueryJ classes.
     * @precondition interpreter != null
     */
    protected void sanityCheck(final Interpreter interpreter)
        throws  EvalError
    {
        PrintStream t_psPreviousOut = interpreter.getOut();
        PrintStream t_psPreviousErr = interpreter.getErr();
        PrintStream t_psOut = new PrintStream(new ByteArrayOutputStream());

        try
        {
            PrintStream t_psErr = t_psOut;

            interpreter.setOut(t_psOut);
            interpreter.setErr(t_psErr);

            // do stuff
            interpreter.set("beanShellHandler", this);
            interpreter.eval("javap(beanShellHandler.getClass());\n");
        }
        finally
        {
            interpreter.setOut(t_psPreviousOut);
            interpreter.setErr(t_psPreviousErr);

            t_psOut.close();
        }
    }

    /**
     * Retrieves the relative weight of this handler.
     * @param parameters the parameters.
     * @return a value between <code>MIN_WEIGHT</code>
     * and <code>MAX_WEIGHT</code>.
     */
    public double getRelativeWeight(final Map parameters)
    {
        return MIN_WEIGHT;
    }

    /**
     * Manages the input from Ant.
     * @author <a href="mailto:jose.sanleandro@ventura24.es">Jose San Leandro</a>
     */
    protected static class BshInputRequest
        extends  InputRequest
    {
        /**
         * The BeanShell interpreter.
         */
        private Interpreter m__Interpreter;

        /**
         * Creates a <code>BshInputRequest</code> for given interpreter.
         * @param interpreter such instance.
         */
        public BshInputRequest(
            final String prompt, final Interpreter interpreter)
        {
            super(prompt);
            immutableSetInterpreter(interpreter);
        }

        /**
         * Specifies the interpreter.
         * @param interpreter such instance.
         */
        protected final void immutableSetInterpreter(
            final Interpreter interpreter)
        {
            m__Interpreter = interpreter;
        }

        /**
         * Specifies the interpreter.
         * @param interpreter such instance.
         */
        protected void setInterpreter(final Interpreter interpreter)
        {
            immutableSetInterpreter(interpreter);
        }

        /**
         * Retrieves the interpreter.
         * @return such instance.
         */
        public Interpreter getInterpreter()
        {
            return m__Interpreter;
        }

        /**
         * Receives given input.
         * @param input such input.
         */
        public void setInput(final String input)
        {
            setInput(
                input,
                getInterpreter(),
                UniqueLogFactory.getLog(BeanShellHandler.class));
                
        }

        /**
         * Receives given input.
         * @param input such input.
         * @param interpreter the BeanShell interpreter.
         * @param log the log.
         */
        protected void setInput(
            final String input,
            final Interpreter interpreter,
            final Log log)
          throws  BuildException
        {
            try
            {
                String t_strInput = input;

                super.setInput(t_strInput);

                if  (!isInputValid())
                {
                    if  (t_strInput == null)
                    {
                        t_strInput = "int index; print(\"\" + index++);\n";
                    }

                    Object t_Result = interpreter.eval(t_strInput);

                    if  (t_Result != null)
                    {
                        log("" + t_Result, log);
                    }
                }
            }
            catch  (final EvalError evalError)
            {
                log(evalError.getMessage(), log);
            }
        }

        /**
         * Logs given message.
         * @param message the message.
         * @param log the <code>Log</code> instance.
         */
        protected void log(final String message, final Log log)
        {
            if  (log != null)
            {
                log.info(message);
            }
            else
            {
                System.out.println(message);
            }
        }

        /**
         * Checks whether the input is valid.
         * @return <code>true</code> in such case.
         */
        public boolean isInputValid()
        {
            boolean result = false;

            String t_strInput = getInput();

            if  (   (t_strInput == null)
                 || (t_strInput.trim().length() == 0))
            {
                result = false;
            }
            else if  (   (t_strInput.trim().equalsIgnoreCase("quit"))
                      || (t_strInput.trim().equalsIgnoreCase("continue")))
            {
                result = true;
            }

            return result;
        }
    }
}