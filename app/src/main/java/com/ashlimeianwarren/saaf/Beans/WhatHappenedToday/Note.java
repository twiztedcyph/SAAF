package com.ashlimeianwarren.saaf.Beans.WhatHappenedToday;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

/**
 * Note super class to allow text and media notes into same list.
 * <p/>
 * Defines the base properties of a media and text note.
 */
public abstract class Note implements Comparable<Note>
{
    protected int _id;
    protected int subjectId;
    protected DbCon dbCon;
    protected String type;

    /**
     * Default empty constructor
     */
    public Note()
    {
    }

    /**
     * Constructor for a note object
     *
     * @param subjectId The subject id for this note.
     * @param type      The type of note. Can be text, image or audio.
     */
    public Note(int subjectId, String type)
    {
        this.subjectId = subjectId;
        this.type = type;
    }

    /**
     * Constructor for a note object with id.
     *
     * @param _id       The note's id.
     * @param subjectId The subject id for this note.
     * @param type      The type of note. Can be text, image or audio.
     */
    public Note(int _id, int subjectId, String type)
    {
        this._id = _id;
        this.subjectId = subjectId;
        this.type = type;
    }

    /**
     * Get the type of this note.
     *
     * @return The type of this note.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Set the type for this note.
     *
     * @param type The type of note. Can be text, image or audio.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Get the subject id.
     *
     * @return The subject id.
     */
    public int getSubjectId()
    {
        return subjectId;
    }

    /**
     * Set the subject id.
     *
     * @param subjectId The subject id.
     */
    public void setSubjectId(int subjectId)
    {
        this.subjectId = subjectId;
    }

    /**
     * Get the database id for this MediaNote.
     *
     * @return The database id for this MediaNote.
     */
    public int get_id()
    {
        return _id;
    }

    /**
     * Compare this note's database id to another.
     *
     * @param another The note to be compared to.
     * @return 1 if this note is greater, -1 if this note is lesser, 0 if they are the same.
     */
    @Override
    public int compareTo(Note another)
    {
        return ((Integer) this.get_id()).compareTo(another._id);
    }
}
