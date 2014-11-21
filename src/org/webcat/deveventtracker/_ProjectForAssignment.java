/*==========================================================================*\
 |  _ProjectForAssignment.java
 |*-------------------------------------------------------------------------*|
 |  Created by eogenerator
 |  DO NOT EDIT.  Make changes to ProjectForAssignment.java instead.
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2012 Virginia Tech
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

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.eof.ERXKey;
import org.apache.log4j.Logger;
import org.webcat.core.EOBasedKeyGenerator;
import org.webcat.woextensions.WCFetchSpecification;

// -------------------------------------------------------------------------
/**
 * An automatically generated EOGenericRecord subclass.  DO NOT EDIT.
 * To change, use EOModeler, or make additions in
 * ProjectForAssignment.java.
 *
 * @author Generated by eogenerator
 * @version version suppressed to control auto-generation
 */
public abstract class _ProjectForAssignment
    extends org.webcat.core.EOBase
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new _ProjectForAssignment object.
     */
    public _ProjectForAssignment()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * A static factory method for creating a new
     * ProjectForAssignment object given required
     * attributes and relationships.
     * @param editingContext The context in which the new object will be
     * inserted
     * @return The newly created object
     */
    public static ProjectForAssignment create(
        EOEditingContext editingContext
        )
    {
        ProjectForAssignment eoObject = (ProjectForAssignment)
            EOUtilities.createAndInsertInstance(
                editingContext,
                _ProjectForAssignment.ENTITY_NAME);
        return eoObject;
    }


    // ----------------------------------------------------------
    /**
     * Get a local instance of the given object in another editing context.
     * @param editingContext The target editing context
     * @param eo The object to import
     * @return An instance of the given object in the target editing context
     */
    public static ProjectForAssignment localInstance(
        EOEditingContext editingContext, ProjectForAssignment eo)
    {
        return (eo == null)
            ? null
            : (ProjectForAssignment)EOUtilities.localInstanceOfObject(
                editingContext, eo);
    }


    // ----------------------------------------------------------
    /**
     * Look up an object by id number.  Assumes the editing
     * context is appropriately locked.
     * @param ec The editing context to use
     * @param id The id to look up
     * @return The object, or null if no such id exists
     */
    public static ProjectForAssignment forId(
        EOEditingContext ec, int id)
    {
        ProjectForAssignment obj = null;
        if (id > 0)
        {
            NSArray<ProjectForAssignment> objects =
                objectsMatchingValues(ec, "id", new Integer(id));
            if (objects != null && objects.count() > 0)
            {
                obj = objects.objectAtIndex(0);
            }
        }
        return obj;
    }


    // ----------------------------------------------------------
    /**
     * Look up an object by id number.  Assumes the editing
     * context is appropriately locked.
     * @param ec The editing context to use
     * @param id The id to look up
     * @return The object, or null if no such id exists
     */
    public static ProjectForAssignment forId(
        EOEditingContext ec, String id)
    {
        return forId(ec, er.extensions.foundation.ERXValueUtilities.intValue(id));
    }


    //~ Constants (for key names) .............................................

    // Attributes ---
    public static final String END_KEY = "end";
    public static final ERXKey<NSTimestamp> end =
        new ERXKey<NSTimestamp>(END_KEY);
    public static final String START_KEY = "start";
    public static final ERXKey<NSTimestamp> start =
        new ERXKey<NSTimestamp>(START_KEY);
    // To-one relationships ---
    // To-many relationships ---
    public static final String STUDENT_PROJECTS_KEY = "studentProjects";
    public static final ERXKey<org.webcat.deveventtracker.StudentProject> studentProjects =
        new ERXKey<org.webcat.deveventtracker.StudentProject>(STUDENT_PROJECTS_KEY);
    public static final String STUDENTS_KEY = "students";
    public static final ERXKey<org.webcat.core.User> students =
        new ERXKey<org.webcat.core.User>(STUDENTS_KEY);
    // Fetch specifications ---
    public static final String ENTITY_NAME = "ProjectForAssignment";

    public transient final EOBasedKeyGenerator generateKey =
        new EOBasedKeyGenerator(this);


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Get a local instance of this object in another editing context.
     * @param editingContext The target editing context
     * @return An instance of this object in the target editing context
     */
    public ProjectForAssignment localInstance(EOEditingContext editingContext)
    {
        return (ProjectForAssignment)EOUtilities.localInstanceOfObject(
            editingContext, this);
    }


    // ----------------------------------------------------------
    /**
     * Get a list of changes between this object's current state and the
     * last committed version.
     * @return a dictionary of the changes that have not yet been committed
     */
    @SuppressWarnings("unchecked")
    public NSDictionary<String, Object> changedProperties()
    {
        return changesFromSnapshot(
            editingContext().committedSnapshotForObject(this));
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>id</code> value.
     * @return the value of the attribute
     */
    public Number id()
    {
        try
        {
            return (Number)EOUtilities.primaryKeyForObject(
                editingContext() , this).objectForKey("id");
        }
        catch (Exception e)
        {
            return er.extensions.eof.ERXConstant.ZeroInteger;
        }
    }

    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>end</code> value.
     * @return the value of the attribute
     */
    public NSTimestamp end()
    {
        return (NSTimestamp)storedValueForKey( "end" );
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>end</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setEnd( NSTimestamp value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setEnd("
                + value + "): was " + end() );
        }
        takeStoredValueForKey( value, "end" );
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>start</code> value.
     * @return the value of the attribute
     */
    public NSTimestamp start()
    {
        return (NSTimestamp)storedValueForKey( "start" );
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>start</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setStart( NSTimestamp value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setStart("
                + value + "): was " + start() );
        }
        takeStoredValueForKey( value, "start" );
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entities pointed to by the <code>studentProjects</code>
     * relationship.
     * @return an NSArray of the entities in the relationship
     */
    @SuppressWarnings("unchecked")
    public NSArray<org.webcat.deveventtracker.StudentProject> studentProjects()
    {
        return (NSArray<org.webcat.deveventtracker.StudentProject>)
            storedValueForKey("studentProjects");
    }


    // ----------------------------------------------------------
    /**
     * Replace the list of entities pointed to by the
     * <code>studentProjects</code> relationship.
     *
     * @param value The new set of entities to relate to
     */
    public void setStudentProjects(
        NSMutableArray<org.webcat.deveventtracker.StudentProject>  value)
    {
        if (log.isDebugEnabled())
        {
            log.debug("setStudentProjects("
                + value + "): was " + studentProjects());
        }
        takeStoredValueForKey(value, "studentProjects");
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>studentProjects</code>
     * relationship (DO NOT USE--instead, use
     * <code>addToStudentProjectsRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The new entity to relate to
     */
    public void addToStudentProjects( org.webcat.deveventtracker.StudentProject value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToStudentProjects("
                + value + "): was " + studentProjects() );
        }
        NSMutableArray<org.webcat.deveventtracker.StudentProject> array =
            (NSMutableArray<org.webcat.deveventtracker.StudentProject>)studentProjects();
        willChange();
        array.addObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>studentProjects</code>
     * relationship (DO NOT USE--instead, use
     * <code>removeFromStudentProjectsRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromStudentProjects( org.webcat.deveventtracker.StudentProject value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "RemoveFromStudentProjects("
                + value + "): was " + studentProjects() );
        }
        NSMutableArray<org.webcat.deveventtracker.StudentProject> array =
            (NSMutableArray<org.webcat.deveventtracker.StudentProject>)studentProjects();
        willChange();
        array.removeObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>studentProjects</code>
     * relationship.
     *
     * @param value The new entity to relate to
     */
    public void addToStudentProjectsRelationship( org.webcat.deveventtracker.StudentProject value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToStudentProjectsRelationship("
                + value + "): was " + studentProjects() );
        }
        addObjectToBothSidesOfRelationshipWithKey(
            value, "studentProjects" );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>studentProjects</code>
     * relationship.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromStudentProjectsRelationship( org.webcat.deveventtracker.StudentProject value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "removeFromStudentProjectsRelationship("
                + value + "): was " + studentProjects() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "studentProjects" );
    }


    // ----------------------------------------------------------
    /**
     * Create a brand new object that is a member of the
     * <code>studentProjects</code> relationship.
     *
     * @return The new entity
     */
    public org.webcat.deveventtracker.StudentProject createStudentProjectsRelationship()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "createStudentProjectsRelationship()" );
        }
        EOClassDescription eoClassDesc = EOClassDescription
            .classDescriptionForEntityName( "StudentProject" );
        EOEnterpriseObject eoObject = eoClassDesc
            .createInstanceWithEditingContext( editingContext(), null );
        editingContext().insertObject( eoObject );
        addObjectToBothSidesOfRelationshipWithKey(
            eoObject, "studentProjects" );
        return (org.webcat.deveventtracker.StudentProject)eoObject;
    }


    // ----------------------------------------------------------
    /**
     * Remove and then delete a specific entity that is a member of the
     * <code>studentProjects</code> relationship.
     *
     * @param value The entity to remove from the relationship and then delete
     */
    public void deleteStudentProjectsRelationship( org.webcat.deveventtracker.StudentProject value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteStudentProjectsRelationship("
                + value + "): was " + studentProjects() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "studentProjects" );
        editingContext().deleteObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove (and then delete, if owned) all entities that are members of the
     * <code>studentProjects</code> relationship.
     */
    public void deleteAllStudentProjectsRelationships()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteAllStudentProjectsRelationships(): was "
                + studentProjects() );
        }
        for (org.webcat.deveventtracker.StudentProject object : studentProjects())
        {
            deleteStudentProjectsRelationship(object);
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entities pointed to by the <code>students</code>
     * relationship.
     * @return an NSArray of the entities in the relationship
     */
    @SuppressWarnings("unchecked")
    public NSArray<org.webcat.core.User> students()
    {
        return (NSArray<org.webcat.core.User>)
            storedValueForKey("students");
    }


    // ----------------------------------------------------------
    /**
     * Replace the list of entities pointed to by the
     * <code>students</code> relationship.
     *
     * @param value The new set of entities to relate to
     */
    public void setStudents(
        NSMutableArray<org.webcat.core.User>  value)
    {
        if (log.isDebugEnabled())
        {
            log.debug("setStudents("
                + value + "): was " + students());
        }
        takeStoredValueForKey(value, "students");
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>students</code>
     * relationship (DO NOT USE--instead, use
     * <code>addToStudentsRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The new entity to relate to
     */
    public void addToStudents( org.webcat.core.User value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToStudents("
                + value + "): was " + students() );
        }
        NSMutableArray<org.webcat.core.User> array =
            (NSMutableArray<org.webcat.core.User>)students();
        willChange();
        array.addObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>students</code>
     * relationship (DO NOT USE--instead, use
     * <code>removeFromStudentsRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromStudents( org.webcat.core.User value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "RemoveFromStudents("
                + value + "): was " + students() );
        }
        NSMutableArray<org.webcat.core.User> array =
            (NSMutableArray<org.webcat.core.User>)students();
        willChange();
        array.removeObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>students</code>
     * relationship.
     *
     * @param value The new entity to relate to
     */
    public void addToStudentsRelationship( org.webcat.core.User value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToStudentsRelationship("
                + value + "): was " + students() );
        }
        addObjectToBothSidesOfRelationshipWithKey(
            value, "students" );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>students</code>
     * relationship.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromStudentsRelationship( org.webcat.core.User value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "removeFromStudentsRelationship("
                + value + "): was " + students() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "students" );
    }


    // ----------------------------------------------------------
    /**
     * Create a brand new object that is a member of the
     * <code>students</code> relationship.
     *
     * @return The new entity
     */
    public org.webcat.core.User createStudentsRelationship()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "createStudentsRelationship()" );
        }
        EOClassDescription eoClassDesc = EOClassDescription
            .classDescriptionForEntityName( "User" );
        EOEnterpriseObject eoObject = eoClassDesc
            .createInstanceWithEditingContext( editingContext(), null );
        editingContext().insertObject( eoObject );
        addObjectToBothSidesOfRelationshipWithKey(
            eoObject, "students" );
        return (org.webcat.core.User)eoObject;
    }


    // ----------------------------------------------------------
    /**
     * Remove and then delete a specific entity that is a member of the
     * <code>students</code> relationship.
     *
     * @param value The entity to remove from the relationship and then delete
     */
    public void deleteStudentsRelationship( org.webcat.core.User value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteStudentsRelationship("
                + value + "): was " + students() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "students" );
        editingContext().deleteObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove (and then delete, if owned) all entities that are members of the
     * <code>students</code> relationship.
     */
    public void deleteAllStudentsRelationships()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteAllStudentsRelationships(): was "
                + students() );
        }
        for (org.webcat.core.User object : students())
        {
            deleteStudentsRelationship(object);
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a fetch specification.
     *
     * @param context The editing context to use
     * @param fspec The fetch specification to use
     *
     * @return an NSArray of the entities retrieved
     */
    @SuppressWarnings("unchecked")
    public static NSArray<ProjectForAssignment> objectsWithFetchSpecification(
        EOEditingContext context,
        EOFetchSpecification fspec)
    {
        return context.objectsWithFetchSpecification(fspec);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve all objects of this type.
     *
     * @param context The editing context to use
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<ProjectForAssignment> allObjects(
        EOEditingContext context)
    {
        return objectsMatchingQualifier(context, null, null);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a qualifier.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<ProjectForAssignment> objectsMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier)
    {
        return objectsMatchingQualifier(context, qualifier, null);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a qualifier and sort orderings.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     * @param sortOrderings The sort orderings to use
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<ProjectForAssignment> objectsMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier,
        NSArray<EOSortOrdering> sortOrderings)
    {
        WCFetchSpecification<ProjectForAssignment> fspec =
            new WCFetchSpecification<ProjectForAssignment>(
                ENTITY_NAME, qualifier, sortOrderings);
        fspec.setUsesDistinct(true);
        return objectsWithFetchSpecification(context, fspec);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the first object that matches a qualifier, when
     * sorted with the specified sort orderings.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     * @param sortOrderings the sort orderings
     *
     * @return the first entity that was retrieved, or null if there was none
     */
    public static ProjectForAssignment firstObjectMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier,
        NSArray<EOSortOrdering> sortOrderings)
    {
        WCFetchSpecification<ProjectForAssignment> fspec =
            new WCFetchSpecification<ProjectForAssignment>(
                ENTITY_NAME, qualifier, sortOrderings);
        fspec.setUsesDistinct(true);
        fspec.setFetchLimit(1);
        NSArray<ProjectForAssignment> objects =
            objectsWithFetchSpecification(context, fspec);
        return (objects.size() > 0)
            ? objects.get(0)
            : null;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve a single object using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     *
     * @return the single entity that was retrieved
     *
     * @throws EOUtilities.MoreThanOneException
     *     if there is more than one matching object
     */
    public static ProjectForAssignment uniqueObjectMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier) throws EOUtilities.MoreThanOneException
    {
        NSArray<ProjectForAssignment> objects =
            objectsMatchingQualifier(context, qualifier);
        if (objects.size() > 1)
        {
            String msg = "fetching ProjectForAssignment";
            try
            {
                if (qualifier != null)
                {
                    msg += " where " + qualifier;
                }
                msg += ", result = " + objects;
            }
            catch (Exception e)
            {
                log.error("Exception building MoreThanOneException message, "
                    + "contents so far: " + msg, e);
            }
            throw new EOUtilities.MoreThanOneException(msg);
        }
        return (objects.size() > 0)
            ? objects.get(0)
            : null;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<ProjectForAssignment> objectsMatchingValues(
        EOEditingContext context,
        Object... keysAndValues)
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value "
                + "corresponding to every key that was passed. Args = "
                + java.util.Arrays.toString(keysAndValues));
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (key == null)
            {
                throw new IllegalArgumentException(
                    "Found null where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }
            else if (!(key instanceof String))
            {
                throw new IllegalArgumentException(
                    "Found a " + key.getClass().getName() + " [" + key + "]"
                    + " where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }

            valueDictionary.setObjectForKey(value, (String)key);
        }

        return objectsMatchingValues(context, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a dictionary of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return an NSArray of the entities retrieved
     */
    @SuppressWarnings("unchecked")
    public static NSArray<ProjectForAssignment> objectsMatchingValues(
        EOEditingContext context,
        NSDictionary<String, Object> keysAndValues)
    {
        return EOUtilities.objectsMatchingValues(context, ENTITY_NAME,
            keysAndValues);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the first object that matches a set of keys and values, when
     * sorted with the specified sort orderings.
     *
     * @param context The editing context to use
     * @param sortOrderings the sort orderings
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return the first entity that was retrieved, or null if there was none
     */
    public static ProjectForAssignment firstObjectMatchingValues(
        EOEditingContext context,
        NSArray<EOSortOrdering> sortOrderings,
        Object... keysAndValues)
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value "
                + "corresponding to every key that was passed. Args = "
                + java.util.Arrays.toString(keysAndValues));
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (key == null)
            {
                throw new IllegalArgumentException(
                    "Found null where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }
            else if (!(key instanceof String))
            {
                throw new IllegalArgumentException(
                    "Found a " + key.getClass().getName() + " [" + key + "]"
                    + " where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }

            valueDictionary.setObjectForKey(value, (String)key);
        }

        return firstObjectMatchingValues(
            context, sortOrderings, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieves the first object that matches a set of keys and values, when
     * sorted with the specified sort orderings.
     *
     * @param context The editing context to use
     * @param sortOrderings the sort orderings
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return the first entity that was retrieved, or null if there was none
     */
    public static ProjectForAssignment firstObjectMatchingValues(
        EOEditingContext context,
        NSArray<EOSortOrdering> sortOrderings,
        NSDictionary<String, Object> keysAndValues)
    {
        WCFetchSpecification<ProjectForAssignment> fspec =
            new WCFetchSpecification<ProjectForAssignment>(
                ENTITY_NAME,
                EOQualifier.qualifierToMatchAllValues(keysAndValues),
                sortOrderings);
        fspec.setFetchLimit(1);

        NSArray<ProjectForAssignment> objects =
            objectsWithFetchSpecification( context, fspec );

        if ( objects.count() == 0 )
        {
            return null;
        }
        else
        {
            return objects.objectAtIndex(0);
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve a single object using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return the single entity that was retrieved, or null if there was none
     *
     * @throws EOUtilities.MoreThanOneException
     *     if there is more than one matching object
     */
    public static ProjectForAssignment uniqueObjectMatchingValues(
        EOEditingContext context,
        Object... keysAndValues) throws EOUtilities.MoreThanOneException
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value "
                + "corresponding to every key that was passed. Args = "
                + java.util.Arrays.toString(keysAndValues));
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (key == null)
            {
                throw new IllegalArgumentException(
                    "Found null where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }
            else if (!(key instanceof String))
            {
                throw new IllegalArgumentException(
                    "Found a " + key.getClass().getName() + " [" + key + "]"
                    + " where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }

            valueDictionary.setObjectForKey(value, (String)key);
        }

        return uniqueObjectMatchingValues(context, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve an object using a dictionary of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return the single entity that was retrieved, or null if there was none
     *
     * @throws EOUtilities.MoreThanOneException
     *     if there is more than one matching object
     */
    public static ProjectForAssignment uniqueObjectMatchingValues(
        EOEditingContext context,
        NSDictionary<String, Object> keysAndValues)
        throws EOUtilities.MoreThanOneException
    {
        try
        {
            return (ProjectForAssignment)EOUtilities.objectMatchingValues(
                context, ENTITY_NAME, keysAndValues);
        }
        catch (EOObjectNotAvailableException e)
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of all objects of this type.
     *
     * @param context The editing context to use
     *
     * @return the count of all objects
     */
    public static int countOfAllObjects(EOEditingContext context)
    {
        return countOfObjectsMatchingQualifier(context, null);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of objects that match a qualifier.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     *
     * @return the count of objects matching the qualifier
     */
    public static int countOfObjectsMatchingQualifier(
        EOEditingContext context, EOQualifier qualifier)
    {
        return ERXEOControlUtilities.objectCountWithQualifier(
                context, ENTITY_NAME, qualifier);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of objects using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return the count of objects that match the specified values
     */
    public static int countOfObjectsMatchingValues(
        EOEditingContext context,
        Object... keysAndValues)
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value "
                + "corresponding to every key that was passed. Args = "
                + java.util.Arrays.toString(keysAndValues));
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (key == null)
            {
                throw new IllegalArgumentException(
                    "Found null where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }
            else if (!(key instanceof String))
            {
                throw new IllegalArgumentException(
                    "Found a " + key.getClass().getName() + " [" + key + "]"
                    + " where a String key was expected, arguments = "
                    + java.util.Arrays.toString(keysAndValues));
            }

            valueDictionary.setObjectForKey(value, (String)key);
        }

        return countOfObjectsMatchingValues(context, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of objects using a dictionary of keys and values to
     * match.
     *
     * @param context The editing context to use
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return the count of objects that matched the specified values
     */
    public static int countOfObjectsMatchingValues(
        EOEditingContext context,
        NSDictionary<String, Object> keysAndValues)
    {
        return countOfObjectsMatchingQualifier(context,
                EOQualifier.qualifierToMatchAllValues(keysAndValues));
    }


    // ----------------------------------------------------------
    /**
     * Produce a string representation of this object.  This implementation
     * calls UserPresentableDescription(), which uses WebObjects' internal
     * mechanism to print out the visible fields of this object.  Normally,
     * subclasses would override userPresentableDescription() to change
     * the way the object is printed.
     *
     * @return A string representation of the object's value
     */
    public String toString()
    {
        return userPresentableDescription();
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(ProjectForAssignment.class);
}