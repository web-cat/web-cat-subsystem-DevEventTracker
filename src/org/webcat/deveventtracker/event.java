/*==========================================================================*\
 |  $Id: event.java,v 1.8 2015/11/30 15:45:37 stedwar2 Exp $
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.webcat.core.AuthenticationDomain;
import org.webcat.core.CourseOffering;
import org.webcat.core.User;
import org.webcat.grader.Assignment;
import org.webcat.grader.AssignmentOffering;
import org.webcat.grader.Submission;
import org.webcat.woextensions.ECActionWithResult;
import static org.webcat.woextensions.ECActionWithResult.call;
import org.webcat.core.git.*;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.appserver.ERXDirectAction;

//-------------------------------------------------------------------------
/**
 * This direct action class handles all response actions for this subsystem.
 *
 * @author edwards
 * @author Joseph Luke
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.8 $, $Date: 2015/11/30 15:45:37 $
 */
public class event
    extends ERXDirectAction
{

    // ~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new DirectAction object.
     *
     * @param aRequest
     *            The request to respond to
     */
    public event(WORequest aRequest)
    {
        super(aRequest);
    }


    // ~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * The default action simply returns an invalid request response.
     *
     * @return The session response
     */
    public WOActionResults defaultAction()
    {
        log.debug("defaultAction()");
        return response("Invalid request");
    }


    // ----------------------------------------------------------
    /**
     * Returns a user's UUID if one exists for the given email. If the user
     * exists, but does not have a UUID, create one. If no user exists, inform
     * the requester.
     *
     * @return The page containing the UUID of the user or a message stating
     *         that no such user exists.
     */
    public WOActionResults retrieveUserAction()
    {
        return call(new ECActionWithResult<WOActionResults>() {

            public WOActionResults action()
            {
                long start = System.currentTimeMillis();
                WORequest request = request();

                // Get parameters.
                String email = request.stringFormValueForKey("email");
                log.debug("retrieveUserAction(): email = " + email);

                // If we weren't given an email, we create a UUIDForUser with
                // no associated user, then fill the user in later (via
                // confirmUUID action).
                if (email == null || email.equals(""))
                {
                    UuidForUser noUserUuid = UuidForUser.create(
                        ec, UUID.randomUUID().toString());

                    RetrieveUserResponse page =
                        pageWithName(RetrieveUserResponse.class);
                    page.uuid = noUserUuid.uuid();
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double time = (end - start)/1000.0;
                        log.debug("retrieveUserAction(): email = " + email
                            + ", " + time + "s");
                    }
                    return page;
                }

                // TODO: Fix this (API change the lookupUserByEMail method)
                // Incorrect signature (confirmed by Edwards)
                AuthenticationDomain domain =
                    AuthenticationDomain.forId(ec, 1);
                User user = null;
                try
                {
                    user = User.lookupUserByEmail(ec, email, domain);
                }
                catch (Exception e)
                {
                    log.error(e);
                }

                // No base User found, so we can't have or make a UuidForUser.
                if (user == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double time = (end - start)/1000.0;
                        log.debug("retrieveUserAction(): email = " + email
                            + ", " + time + "s");
                    }
                    return response("No user found for that email.");
                }

                UuidForUser uuidForUser =
                    UuidForUser.uniqueObjectMatchingQualifier(
                        ec, UuidForUser.user.is(user));

                // This user doesn't have a UUID, so we need to make one.
                if (uuidForUser == null)
                {
                    uuidForUser = UuidForUser.create(
                        ec, UUID.randomUUID().toString());
                    uuidForUser.setUserRelationship(user);
                    ec.saveChanges();
                }

                // Return the page listing the String representation of
                // this user's UUID.
                RetrieveUserResponse page =
                    pageWithName(RetrieveUserResponse.class);
                page.uuid = uuidForUser.uuid();

                long end = System.currentTimeMillis();
                if (trace.isDebugEnabled())
                {
                    double time = (end - start)/1000.0;
                    log.debug("retrieveUserAction(): email = " + email
                        + ", " + time + "s");
                }
                return page;
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Given a projectUri and a userUUID, return the UUID of the StudentProject
     * associated with these, creating one if necessary.
     *
     * @return The page containing the UUID for the StudentProject, or a
     *         message indicating no such StudentProject exists.
     */
    public WOActionResults retrieveStudentProjectAction()
    {
        return call(new ECActionWithResult<WOActionResults>() {

            public WOActionResults action()
            {
                long start = System.currentTimeMillis();
                WORequest request = request();

                // Get parameters.
                log.debug("request: " + request.uri());
                String projectUri =
                    request.stringFormValueForKey("projectUri");
                String userUuid = request.stringFormValueForKey("userUuid");
                log.debug("retrieveStudentProjectAction(): projectUri = "
                    + projectUri + ", userUuid = " + userUuid);

                // Look up UuidForUser from userUUID, then look up User
                UuidForUser uuidForUser =
                    UuidForUser.uniqueObjectMatchingQualifier(
                        ec, UuidForUser.uuid.is(userUuid));

                // No base UuidForUser found, so we can't use it to make a new
                // StudentProject.
                if (uuidForUser == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double time = (end - start)/1000.0;
                        log.debug("retrieveStudentProjectAction(): "
                            + "projectUri = " + projectUri
                            + ", userUuid = " + userUuid
                            + ", " + time + "s");
                    }
                    return response("No UuidForUser found for that UUID.");
                }
                /*
                 * NSArray<StudentProject> studentProjects =
                 * StudentProject.forUserUuid(ec, projectUri, uuidForUser);
                 * StudentProject studentProject = null; if(studentProjects.size() > 0)
                 * { studentProject = studentProjects.get(0); }
                 */
                NSArray<StudentProject> studentProjects = StudentProject.
                    objectsMatchingQualifier(
                        ec,
                        StudentProject.uri.is(projectUri)
                        .and(StudentProject.studentUuids.is(uuidForUser)));
                StudentProject studentProject = null;
                if (studentProjects.size() > 0)
                {
                    // TODO there needs to be tracking/check here to deal with
                    // same projectUri for same user on different computers/
                    // Eclipse installations.
                    studentProject = studentProjects.get(0);
                }
                else
                {
                    // No StudentProject that matches projectUri and userUuid
                    studentProject = StudentProject.create(ec);
                    studentProject.setUri(projectUri);
                    studentProject.setUuid(UUID.randomUUID().toString());
                    studentProject.addToStudentUuidsRelationship(uuidForUser);

                    ec.saveChanges();

                    // Create a new repository for this student project.
                    GitRepository.repositoryForObject(studentProject);
                }

                Boolean userInActiveCourse = false;

                User user = uuidForUser.user();
                if (user != null)
                {
                    NSArray<CourseOffering> offeringsForUser =
                        user.enrolledIn();
                    for (CourseOffering co : offeringsForUser)
                    {
                        if (co.semester().semesterEndDate()
                            .after(new NSTimestamp()))
                        {
                            userInActiveCourse = true;
                            break;
                        }
                    }
                }
                RetrieveStudentProjectResponse page =
                    pageWithName(RetrieveStudentProjectResponse.class);
                page.uuid = studentProject.uuid();
                page.pushLogs = userInActiveCourse.toString();

                long end = System.currentTimeMillis();
                if (trace.isDebugEnabled())
                {
                    double time = (end - start)/1000.0;
                    log.debug("retrieveStudentProjectAction(): projectUri = "
                        + projectUri + ", userUuid = " + userUuid
                        + ", " + time + "s");
                }
                return page;
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Stores the given SensorData information, assuming all parameters are
     * valid.
     *
     * @return The page stating success or failure for storing the SensorData.
     */
    public WOActionResults postSensorDataAction()
    {
        if (true)
        {
            sdLog.println(request().uri());
            return response("Invalid SensorDataType");
        }
        return call(new ECActionWithResult<WOActionResults>() {

            public WOActionResults action()
            {
                long start = System.currentTimeMillis();
                WORequest request = request();

                long t1 = System.currentTimeMillis();
                // Get parameters.
                String studentProjectUuid = request
                        .stringFormValueForKey("studentProjectUuid");
                String userUuid = request.stringFormValueForKey("userUuid");
                String timeString = request.stringFormValueForKey("time");
                String runtimeString =
                    request.stringFormValueForKey("runtime");
                String tool = request.stringFormValueForKey("tool");
                String sensorDataTypeName =
                    request.stringFormValueForKey("sensorDataType");
                String uri = request.stringFormValueForKey("uri");
                String commitHash =
                    request.stringFormValueForKey("CommitHash");
                String content = request().contentString();

                if (log.isDebugEnabled())
                {
                    log.debug("postSensorDataAction() : " + request.uri());
                    log.debug("    studentProjectUuid = " + studentProjectUuid);
                    log.debug("    userUuid = " + userUuid);
                    log.debug("    timeString = " + timeString);
                    log.debug("    runtimeString = " + runtimeString);
                    log.debug("    tool = " + tool);
                    log.debug("    type = " + sensorDataTypeName);
                    log.debug("    uri = " + uri);
                    log.debug("    commitHash = " + commitHash);
                    log.debug("    form data = " + request.formValues());
                }

                long t2 = System.currentTimeMillis();
                // subtype
                // type
                // name1= ... value1= ...

                NSTimestamp time =
                    new NSTimestamp(Long.parseLong(timeString));
                NSTimestamp runtime =
                    new NSTimestamp(Long.parseLong(runtimeString));

                // Look up UuidForUser from userUUID, then look up User
                UuidForUser uuidForUser =
                    UuidForUser.uniqueObjectMatchingQualifier(
                        ec, UuidForUser.uuid.is(userUuid));

                // No base UuidForUser found, so we can't use it to make a new
                // StudentProject.
                if (uuidForUser == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("postSensorDataAction() : " + request.uri()
                            + "\n    total time = " + diff
                            + "s, t1 = " + ((t1 - start)/1000.0)
                            + "s, t2 = " + ((t2 - t1)/1000.0)
                            );
                    }
                    return response("NNo UuidForUser found for that UUID.");
                }

                long t3 = System.currentTimeMillis();
                User user = uuidForUser.user();
                if (user == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("postSensorDataAction() : " + request.uri()
                            + "\n    total time = " + diff
                            + "s, t1 = " + ((t1 - start)/1000.0)
                            + "s, t2 = " + ((t2 - t1)/1000.0)
                            + "s, t3 = " + ((t3 - t2)/1000.0)
                            );
                    }
                    return response("No user found for that UUID.");
                }

                long t4 = System.currentTimeMillis();
                // Look up StudentProject from the given UUID.
                StudentProject studentProject = StudentProject.
                    uniqueObjectMatchingQualifier(
                        ec, StudentProject.uuid.is(studentProjectUuid));

                // No StudentProject found for that UUID.
                if (studentProject == null) {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("postSensorDataAction() : " + request.uri()
                            + "\n    total time = " + diff
                            + "s, t1 = " + ((t1 - start)/1000.0)
                            + "s, t2 = " + ((t2 - t1)/1000.0)
                            + "s, t3 = " + ((t3 - t2)/1000.0)
                            + "s, t4 = " + ((t4 - t3)/1000.0)
                            );
                    }
                    return response("No student project found for that UUID");
                }

                long t5 = System.currentTimeMillis();
                // Look up dataType based on name from given parameter.
                SensorDataType dataType = SensorDataType.getSensorDataType(ec,
                        sensorDataTypeName);
                long t51 = System.currentTimeMillis();

                // No SensorDataType found for that name.
                if (dataType == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("postSensorDataAction() : " + request.uri()
                            + "\n    total time = " + diff
                            + "s, t1 = " + ((t1 - start)/1000.0)
                            + "s, t2 = " + ((t2 - t1)/1000.0)
                            + "s, t3 = " + ((t3 - t2)/1000.0)
                            + "s, t4 = " + ((t4 - t3)/1000.0)
                            + "s, t5 = " + ((t5 - t4)/1000.0)
                            );
                    }
                    return response("Invalid SensorDataType");
                }

                // create sensordata entry and populate from given data
                SensorData sensorData =
                    SensorData.create(ec, false, time, dataType, user);
                long t52 = System.currentTimeMillis();
                sensorData.setProjectRelationship(studentProject);
                sensorData.setRunTime(runtime);
                sensorData.setTool(tool);
                sensorData.setUri(uri);
                if (commitHash != null) {
                    sensorData.setCommitHash(commitHash);
                }
                long t53 = System.currentTimeMillis();
                ec.saveChanges();

                long t6 = System.currentTimeMillis();
                long b4 = t6;
                String msg = "";
                int p = 0;
                while (true)
                {
                    p++;
                    String prop = "name" + p;
                    String name = request.stringFormValueForKey(prop);
                    if (name == null)
                    {
                        break;
                    }
                    else if (name.isEmpty())
                    {
                        continue;
                    }

                    try
                    {
                    String value = request.stringFormValueForKey("value" + p);
        //            String attrName = name.replaceAll("[^\\p{Alnum}]", "");
        //            attrName = attrName.substring(0, 1).toLowerCase()
        //                + attrName.substring(1);
        //            com.webobjects.eoaccess.EOAttribute attr =
        //                sensorData.entity().attributeNamed(attrName);
                    String attrName = PROPERTY_KEYS.get(name);
        //            if (attr == null)
                    if (attrName == null)
                    {
                        log.debug("attribute \"" + attrName + "\" not found");
                        // Just place it in a separate property object
                        SensorDataProperty property =
                            SensorDataProperty.create(ec, name, sensorData);
                        property.setValue(value);
                    }
                    else if (value != null)
                    {
                        log.debug("attribute \"" + attrName + "\" found");
                        // If it is an attribute, stuff it into the sensorData obj.
                        // Need to check if conversion to int is needed first
        //                log.debug("attribute class name = " + attr.className());
        //                if ("java.lang.Number".equals(attr.className())
        //                    || "java.lang.Integer".equals(attr.className())
        //                    || "Integer".equals(attr.className())
        //                    || "int".equals(attr.className()))
                        if (attrName.startsWith("current"))
                        {
                            log.debug("attribute \"" + attrName
                                + "\" is an integer");
                            try
                            {
                                sensorData.takeValueForKey(
                                    Integer.parseInt(value), attrName);
                            }
                            catch (NumberFormatException e)
                            {
                                log.error("Number format exception on "
                                    + "SensorData field: name = \"" + name
                                    + "\", value = \"" + value + "\"");
                                sensorData.takeValueForKey(value, attrName);
                            }
                        }
                        else
                        {
                            sensorData.takeValueForKey(value, attrName);
                        }
                    }

                    // Finally, check to see if property indicates a test case
                    // TestCodeEdit = boolean
                    if (name.startsWith("Current-Test")
                        || name.startsWith("Test")
                        || (name.equals("LaunchType") && "Test".equals(value)))
                    {
                        if ("TestCodeEdit".equals(name))
                        {
                            sensorData.setOnTestCase("true".equals(value));
                        }
                        else
                        {
                            sensorData.setOnTestCase(true);
                        }
                    }
                    }
                    catch (Exception e)
                    {
                        log.error(
                            "Unexpected exception handling attribute", e);
                    }

                    if (trace.isDebugEnabled())
                    {
                        long tt = System.currentTimeMillis();
                        msg += "    name" + p
                            + " time = " + ((tt - b4)/1000.0) + "s";
                        b4 = tt;
                    }
                }
                ec.saveChanges();

                long end = System.currentTimeMillis();
                if (trace.isDebugEnabled())
                {
                    double diff = (end - start)/1000.0;
                    trace.debug("postSensorDataAction() : " + request.uri()
                        + "\n    total time = " + diff
                        + "s, t1 = " + ((t1 - start)/1000.0)
                        + "s, t2 = " + ((t2 - t1)/1000.0)
                        + "s, t3 = " + ((t3 - t2)/1000.0)
                        + "s, t4 = " + ((t4 - t3)/1000.0)
                        + "s, t5 = " + ((t5 - t4)/1000.0)
                        + "s, t5.1 = " + ((t51 - t5)/1000.0)
                        + "s, t5.2 = " + ((t52 - t51)/1000.0)
                        + "s, t5.3 = " + ((t53 - t51)/1000.0)
                        + "s, t6 = " + ((t6 - t53)/1000.0)
                        + msg
                        );
                }
                return pageWithName(PostSensorDataResponse.class);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Given that a submission happened for the given user for the given
     * project, look up the most recent submission for that user in the db and
     * create a ProjectForAssignment linking this StudentProject to the
     * AssignmentOffering (unless there already is a PFA doing this).
     *
     * @return The page indicating success or failure for creating a new
     *         ProjectForAssignment.
     */
    public WOActionResults submissionHappenedAction()
    {
        return call(new ECActionWithResult<WOActionResults>() {

            public WOActionResults action()
            {
                long start = System.currentTimeMillis();
                WORequest request = request();

                // Get parameters.
                String userUuid = request.stringFormValueForKey("userUuid");
                String studentProjectUuid = request
                    .stringFormValueForKey("studentProjectUuid");
                if (studentProjectUuid == null)
                {
                    studentProjectUuid =
                        request.stringFormValueForKey("projectUuid");
                }
                log.debug("submissionHappenedAction(): userUuid = " + userUuid
                    + ", studentProjectUuid = " + studentProjectUuid);

                UuidForUser uuidForUser =
                    UuidForUser.uniqueObjectMatchingQualifier(
                        ec, UuidForUser.uuid.is(userUuid));
                if (uuidForUser == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("submissionHappenedAction(): userUuid = "
                            + userUuid
                            + ", studentProjectUuid = " + studentProjectUuid
                            + ", " + diff + "s");
                    }
                    return response("No user found for that UUID.");
                }
                User user = uuidForUser.user();

                Submission latestSubmission =
                    Submission.firstObjectMatchingQualifier(ec,
                    Submission.user.is(user), Submission.submitTime.descs());
                if (latestSubmission == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("submissionHappenedAction(): userUuid = "
                            + userUuid
                            + ", studentProjectUuid = " + studentProjectUuid
                            + ", " + diff + "s");
                    }
                    return response("No submissions found for that user.");
                }
                AssignmentOffering offering =
                    latestSubmission.assignmentOffering();

                StudentProject project =
                    StudentProject.uniqueObjectMatchingQualifier(
                        ec, StudentProject.uuid.is(studentProjectUuid));
                if (project == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("submissionHappenedAction(): userUuid = "
                            + userUuid
                            + ", studentProjectUuid = " + studentProjectUuid
                            + ", " + diff + "s");
                    }
                    return response("No StudentProject found for that UUID.");
                }
                // If there is a PFA associated with this StudentProject that
                // matches the given User and AssignmentOffering, we don't
                // need to create one.
                NSArray<ProjectForAssignment> pfas =
                    project.projectsForAssignment();
                for (ProjectForAssignment p : pfas)
                {
                    if (p.students().contains(user)
                        && p.assignmentOffering().equals(offering))
                    {
                        return response("StudentProject already part of the "
                            + "correct ProjectForAssignment.");
                    }
                }
                // Otherwise, create a new PFA.
                ProjectForAssignment pfa =
                    ProjectForAssignment.create(ec, offering);
                pfa.addToStudentsRelationship(user);
                pfa.addToStudentProjectsRelationship(project);
                pfa.setStart(offering.availableFrom());
                pfa.setEnd(offering.lateDeadline());

                ec.saveChanges();

                long end = System.currentTimeMillis();
                if (trace.isDebugEnabled())
                {
                    double diff = (end - start)/1000.0;
                    trace.debug("submissionHappenedAction(): userUuid = "
                        + userUuid
                        + ", studentProjectUuid = " + studentProjectUuid
                        + ", " + diff + "s");
                }
                return response("ProjectForAssignment created successfully.");
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Given an email and a user UUID, checks to see if the given UUID
     * corresponds to a null user, and reassociates it with the User from the
     * email if so. This rematches all StudentProjects associated with the old
     * user UUID with the new one if necessary.
     *
     * @return The page indicating success or failure for the confirmation.
     */
    public WOActionResults confirmUuidAction()
    {
        return call(new ECActionWithResult<WOActionResults>() {

            public WOActionResults action()
            {
                long start = System.currentTimeMillis();
                WORequest request = request();

                // Get parameters.
                String email = request.stringFormValueForKey("email");
                String userUuid = request.stringFormValueForKey("userUuid");
                log.debug("confirmUuidAction(): email = " + email
                    + ", userUuid = " + userUuid);

                UuidForUser oldUuidForUser =
                    UuidForUser.uniqueObjectMatchingQualifier(
                        ec, UuidForUser.uuid.is(userUuid));
                if (oldUuidForUser == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("confirmUuidAction(): email = " + email
                            + ", userUuid = " + userUuid
                            + ", " + diff + "s");
                    }
                    return response("No UuidForUser found for that UUID.");
                }
                if (oldUuidForUser.user() == null)
                {
                    User user = User.uniqueObjectMatchingQualifier(
                        ec, User.email.is(email));
                    if (user == null)
                    {
                        long end = System.currentTimeMillis();
                        if (trace.isDebugEnabled())
                        {
                            double diff = (end - start)/1000.0;
                            trace.debug("confirmUuidAction(): email = " + email
                                + ", userUuid = " + userUuid
                                + ", " + diff + "s");
                        }
                        return response("No user found for that email.");
                    }
                    UuidForUser uuidForConfirmedUser = UuidForUser.
                        uniqueObjectMatchingQualifier(
                            ec, UuidForUser.user.is(user));
                    // If there is another UUID for this user that has been
                    // used, we need to pull all student projects to that
                    // UuidForUser, and delete the old UUID (which had no user
                    // associated with it).
                    if (uuidForConfirmedUser != null
                        && !uuidForConfirmedUser.equals(oldUuidForUser))
                    {
                        NSArray<StudentProject> projects = StudentProject.
                            objectsMatchingQualifier(ec,
                            StudentProject.studentUuids.is(oldUuidForUser));
                        for (StudentProject p : projects)
                        {
                            p.removeFromStudentUuidsRelationship(
                                oldUuidForUser);
                            oldUuidForUser.delete();
                            p.addToStudentUuidsRelationship(
                                uuidForConfirmedUser);
                        }
                        ec.saveChanges();

                        RetrieveUserResponse page =
                            pageWithName(RetrieveUserResponse.class);
                        page.uuid = uuidForConfirmedUser.uuid();
                        long end = System.currentTimeMillis();
                        if (trace.isDebugEnabled())
                        {
                            double diff = (end - start)/1000.0;
                            trace.debug("confirmUuidAction(): email = " + email
                                + ", userUuid = " + userUuid
                                + ", " + diff + "s");
                        }
                        return page;
                    }
                    // If the user with the given email does not have an
                    // associated UUID, we need to create one and return it.
                    else if (uuidForConfirmedUser == null)
                    {
                        UuidForUser newUuidForUser = UuidForUser.create(
                            ec, UUID.randomUUID().toString());
                        newUuidForUser.setUserRelationship(user);
                        ec.saveChanges();

                        RetrieveUserResponse page =
                            pageWithName(RetrieveUserResponse.class);
                        page.uuid = newUuidForUser.uuid();
                        long end = System.currentTimeMillis();
                        if (trace.isDebugEnabled())
                        {
                            double diff = (end - start)/1000.0;
                            trace.debug("confirmUuidAction(): email = " + email
                                + ", userUuid = " + userUuid
                                + ", " + diff + "s");
                        }
                        return page;
                    }
                    // We should never get here, as it's the same case as
                    // the else below.
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("confirmUuidAction(): email = " + email
                            + ", userUuid = " + userUuid
                            + ", " + diff + "s");
                    }
                    return response(
                        "This UUID is already associated with a user.");
                } else {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("confirmUuidAction(): email = " + email
                            + ", userUuid = " + userUuid
                            + ", " + diff + "s");
                    }
                    return response(
                        "This UUID is already associated with a user.");
                }
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Given a projectUri, user UUID, and assignmentName that a user has
     * downloaded, create the StudentProject and associate it with a
     * ProjectForAssignment for the appropriate assignment.
     *
     * @return The page with the studentProject UUID generated, or a failure
     *         message.
     */
    public WOActionResults projectDownloadAction()
    {
        return call(new ECActionWithResult<WOActionResults>() {

            public WOActionResults action()
            {
                long start = System.currentTimeMillis();
                WORequest request = request();
                log.debug("projectDownloadAction(): " + request.uri());

                // Get parameters
                String projectUri = request.stringFormValueForKey("projectUri");
                log.debug("    projectUri = " + projectUri);
                String userUuid = request.stringFormValueForKey("userUuid");
                log.debug("    userUuid = " + userUuid);
                String assignmentName =
                    request.stringFormValueForKey("assignmentName");
                log.debug("    assignmentName = " + assignmentName);

                UuidForUser uuidForUser =
                    UuidForUser.uniqueObjectMatchingQualifier(
                        ec, UuidForUser.uuid.is(userUuid));
                if (uuidForUser == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("projectDownloadAction(): " + request.uri()
                            + ", " + diff + "s");
                    }
                    return response("No UuidForUser found for that Uuid.");
                }
                User user = uuidForUser.user();
                if (user == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("projectDownloadAction(): " + request.uri()
                            + ", " + diff + "s");
                    }
                    return response("No user found for that UUID.");
                }

                Assignment assignment =
                    Assignment.uniqueObjectMatchingQualifier(
                        ec, Assignment.name.is(assignmentName));
                if (assignment == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("projectDownloadAction(): " + request.uri()
                            + ", " + diff + "s");
                    }
                    return response("No Assignment found for that name.");
                }
                AssignmentOffering offering = assignment.offeringForUser(user);

                if (offering == null)
                {
                    long end = System.currentTimeMillis();
                    if (trace.isDebugEnabled())
                    {
                        double diff = (end - start)/1000.0;
                        trace.debug("projectDownloadAction(): " + request.uri()
                            + ", " + diff + "s");
                    }
                    return response("No AssignmentOffering found for that "
                        + "user and assignment name.");
                }

                StudentProject studentProject = StudentProject.
                    uniqueObjectMatchingQualifier(
                        ec,
                        StudentProject.studentUuids.is(uuidForUser).
                        and(StudentProject.uri.is(projectUri)));
                if (studentProject == null)
                {
                    studentProject = StudentProject.create(ec);
                    studentProject.setUri(projectUri);
                    studentProject.setUuid(UUID.randomUUID().toString());
                    studentProject.addToStudentUuidsRelationship(uuidForUser);
                }
                else
                {
                    NSArray<ProjectForAssignment> pfas =
                        studentProject.projectsForAssignment();
                    for (ProjectForAssignment p : pfas)
                    {
                        if (p.students().contains(user)
                            && p.assignmentOffering().equals(offering))
                        {
                            long end = System.currentTimeMillis();
                            if (trace.isDebugEnabled())
                            {
                                double diff = (end - start)/1000.0;
                                trace.debug("projectDownloadAction(): "
                                    + request.uri() + ", " + diff + "s");
                            }
                            return response("StudentProject already part of "
                                + "the correct ProjectForAssignment.");
                        }
                    }
                }

                // Otherwise, create a new PFA.
                ProjectForAssignment pfa =
                    ProjectForAssignment.create(ec, offering);
                pfa.addToStudentsRelationship(user);
                pfa.addToStudentProjectsRelationship(studentProject);
                pfa.setStart(offering.availableFrom());
                pfa.setEnd(offering.lateDeadline());

                ec.saveChanges();
                long end = System.currentTimeMillis();
                if (trace.isDebugEnabled())
                {
                    double diff = (end - start)/1000.0;
                    trace.debug("projectDownloadAction(): " + request.uri()
                        + ", " + diff + "s");
                }
                return response("Starter project stored in database.");
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Given parameters for an exception that occurred in the plugin, record
     * those parameters for future debugging.
     *
     * @return The page indicating success.
     */
    public WOActionResults pluginExceptionHappenedAction()
    {
        long start = System.currentTimeMillis();
        log.debug("pluginExceptionHappenedAction()");
        WORequest request = request();

        // Get parameters
        String exceptionClass =
            request.stringFormValueForKey("exceptionClass");
        String exceptionMessage =
            request.stringFormValueForKey("exceptionMessage");

        // Screen out bogus FileNotFound exceptions
        if (java.io.FileNotFoundException.class.getName()
            .equals(exceptionClass)
            && exceptionMessage != null
            && exceptionMessage.contains(".uuid"))
        {
            return response("PluginError was ignored.");
        }

        PluginError.logError(request.formValues());

        long end = System.currentTimeMillis();
        if (trace.isDebugEnabled())
        {
            double diff = (end - start)/1000.0;
            trace.debug("pluginExceptionHappenedAction(): "
                + request.uri() + ", " + diff + "s");
        }
        return response("PluginError stored in database.");
    }


    // ----------------------------------------------------------
    /**
     * Given parameters for an exception that occurred in the plugin, record
     * those parameters for future debugging.
     *
     * @return The page indicating success.
     */
    private SimpleMessageResponse response(String message)
    {
        SimpleMessageResponse page =
            pageWithName(SimpleMessageResponse.class);
        page.message = message;
        return page;
    }


    //~ Instance/static variables .............................................

    private static final NSDictionary<String, String> PROPERTY_KEYS =
        new NSDictionary<String, String>(
            new String[] {
                "type",
                "subtype",
                "subsubtype",
                "unitType",
                "unitName",
                "currentSize",
                "currentStatements",
                "currentMethods"
            },
            new String[] {
                "Type",
                "Subtype",
                "SubsubType",
                "Unit-Type",
                "Unit-Name",
                "Current-Size",
                "Current-Statements",
                "Current-Methods"
            });

    private static final Object fileLock = new Object();

    static Logger log = Logger.getLogger(event.class);
    static Logger trace = Logger.getLogger(event.class.getName() + ".trace");

    private static PrintWriter sdLog;
    static {
        File data = new File("/home/webcat/sensor-data.txt");
        try
        {
            if (data.exists())
            {
                data.renameTo(new File(data.getPath() + "."
                    + System.currentTimeMillis()));
            }
        }
        catch (Exception e)
        {
            log.error("unable to rename file " + data, e);
        }
        try
        {
            sdLog = new PrintWriter(
                new FileOutputStream(data, true), true);
        }
        catch (FileNotFoundException e)
        {
            log.error(e);
        }
    }
}
