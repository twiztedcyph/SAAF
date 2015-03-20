package com.ashlimeianwarren.saaf.Beans.WhatHappenedToday;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

/**
 * Created by Ash on 13/03/2015.
 */
public abstract class Note implements Comparable<Note>
{
    protected int _id;
    protected int subjectId;
    protected DbCon dbCon;
    protected String type;


    public Note() {
    }

    public Note(int subjectId, String type) {
        this.subjectId = subjectId;
        this.type = type;
    }

    public Note(int _id, int subjectId, String type) {
        this._id = _id;
        this.subjectId = subjectId;
        this.type = type;
    }



    public String getType() {
        return type;
    }


    public void setType(String type) {
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

    @Override
    public int compareTo(Note another)
    {
        return ((Integer)this.get_id()).compareTo(another._id);
    }
}
