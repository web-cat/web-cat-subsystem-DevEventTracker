/*==========================================================================*\
 |  $Id: PluginError.java,v 1.2 2015/11/30 15:45:37 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2015 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.deveventtracker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.webcat.core.messaging.UnexpectedExceptionMessage;
import org.webcat.woextensions.ECAction;
import org.webcat.woextensions.WCEC;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

// -------------------------------------------------------------------------
/**
 * TODO: place a real description here.
 *
 * @author
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2015/11/30 15:45:37 $
 */
public class PluginError
    extends _PluginError
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new PluginError object.
     */
    public PluginError()
    {
        super();
    }


    // ~ Methods ...............................................................

    // ----------------------------------------------------------
    private static String stringFor(NSArray<Object> v)
    {
        if (v == null || v.size() == 0)
        {
            return null;
        }
        Object o = v.objectAtIndex(0);
        return (o == null)
          ? null
          : o.toString();
    }


    // ----------------------------------------------------------
    private static void logError(
        EOEditingContext ec,
        NSDictionary<String, NSArray<Object>> values)
    {
        String userUuid = stringFor(values.get("userUuid"));
        String exceptionCls = stringFor(values.get("exceptionClass"));
        String exceptionMsg = stringFor(values.get("exceptionMessage"));
        String clsName = stringFor(values.get("className"));
        String methdName = stringFor(values.get("methodName"));
        String fName = stringFor(values.get("fileName"));
        String lineNo = stringFor(values.get("lineNumber"));
        String stack = stringFor(values.get("stackTrace"));

        logError(
            ec,
            userUuid,
            exceptionCls,
            exceptionMsg,
            clsName,
            methdName,
            fName,
            lineNo,
            stack);
    }


    // ----------------------------------------------------------
    private static void logError(
        EOEditingContext ec,
        String userUuid,
        String exceptionCls,
        String exceptionMsg,
        String clsName,
        String methdName,
        String fName,
        String lineNo,
        String stack)
    {
        try
        {
            UuidForUser uuid =
                UuidForUser.uniqueObjectMatchingQualifier(
                    ec, UuidForUser.uuid.is(userUuid));

            PluginError error = PluginError.create(ec);
            if (exceptionClass != null)
            {
                error.setExceptionClass(exceptionCls);
            }
            if (exceptionMessage != null)
            {
                error.setExceptionMessage(exceptionMsg);
            }
            if (className != null)
            {
                error.setClassName(clsName);
            }
            if (methodName != null)
            {
                error.setMethodName(methdName);
            }
            if (fileName != null)
            {
                error.setFileName(fName);
            }
            if (lineNumber != null)
            {
                error.setLineNumber(Integer.parseInt(lineNo));
            }
            if (stackTrace != null)
            {
                error.setStackTrace(stack);
            }
            error.setUuidForUserRelationship(uuid);

            ec.saveChanges();
        }
        catch (Exception e)
        {
            log.error("Unexpected exception logging PluginError with:\n"
                + "userUuid: " + userUuid + "\n"
                + "exceptionClass" + exceptionClass + "\n"
                + "exceptionMessage" + exceptionMessage + "\n"
                + "className" + className + "\n"
                + "methodName" + methodName + "\n"
                + "fileName" + fileName + "\n"
                + "lineNumber" + lineNumber + "\n"
                + "stackTrace" + stackTrace + "\n", e);
            new UnexpectedExceptionMessage(e, null, null, null).send();
        }
    }


    // ----------------------------------------------------------
    public static void logError(
        NSDictionary<String, NSArray<Object>> values)
    {
        while (true)
        {
            try
            {
                errorQueue.put(values);
                return;
            }
            catch (InterruptedException e)
            {
                // ignore
            }
        }
    }


    // ----------------------------------------------------------
    private static class ErrorWorker
        extends ECAction
    {
        // ----------------------------------------------------------
        private int posts = 0;
        private int totalPosts;
        private boolean active = true;
        private BlockingQueue<NSDictionary<String, NSArray<Object>>> errorQueue;
        private static Logger log = Logger.getLogger(ErrorWorker.class);


        // ----------------------------------------------------------
        public ErrorWorker(
            BlockingQueue<NSDictionary<String, NSArray<Object>>> queue)
        {
            errorQueue = queue;
        }


        // ----------------------------------------------------------
        @Override
        public void action()
        {
            ec.unlock();
            try
            {
            while (active)
            {
                NSDictionary<String, NSArray<Object>> values = null;
                try
                {
                    values = errorQueue.take();
                    posts++;
                    totalPosts++;
                    try
                    {
                        ec.lock();
                        logError(ec, values);
                    }
                    finally
                    {
                        ec.unlock();
                    }
                    if (posts >= 1024)
                    {
                        try
                        {
                            ec.dispose();
                        }
                        catch (Exception e)
                        {
                            log.error("Unexpected exception disposing ec", e);
                        }
                        ec = WCEC.newEditingContext();
                    }
                }
                catch (Exception e)
                {
                    log.error("Unexpected exception storing plugin error, "
                        + "values = " + values, e);
                }
            }
            }
            finally
            {
                ec.lock();
            }
        }
    }


    //~ Instance/static variables .............................................

    private static BlockingQueue<NSDictionary<String, NSArray<Object>>>
        errorQueue = new
        ArrayBlockingQueue<NSDictionary<String, NSArray<Object>>>(512);
    private static ExecutorService pool = Executors.newSingleThreadExecutor();
    static
    {
        pool.execute(new ErrorWorker(errorQueue));
    }
}
