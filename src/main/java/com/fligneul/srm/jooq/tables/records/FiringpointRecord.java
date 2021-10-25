/*
 * This file is generated by jOOQ.
 */
package com.fligneul.srm.jooq.tables.records;


import com.fligneul.srm.jooq.tables.Firingpoint;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FiringpointRecord extends UpdatableRecordImpl<FiringpointRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>FIRINGPOINT.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>FIRINGPOINT.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>FIRINGPOINT.NAME</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>FIRINGPOINT.NAME</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Firingpoint.FIRINGPOINT.ID;
    }

    @Override
    public Field<String> field2() {
        return Firingpoint.FIRINGPOINT.NAME;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public FiringpointRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public FiringpointRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public FiringpointRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FiringpointRecord
     */
    public FiringpointRecord() {
        super(Firingpoint.FIRINGPOINT);
    }

    /**
     * Create a detached, initialised FiringpointRecord
     */
    public FiringpointRecord(Integer id, String name) {
        super(Firingpoint.FIRINGPOINT);

        setId(id);
        setName(name);
    }
}
