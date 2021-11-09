/*
 * This file is generated by jOOQ.
 */
package com.fligneul.srm.jooq.tables;


import com.fligneul.srm.jooq.DefaultSchema;
import com.fligneul.srm.jooq.Keys;
import com.fligneul.srm.jooq.tables.records.LicenseeRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row22;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Licensee extends TableImpl<LicenseeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>LICENSEE</code>
     */
    public static final Licensee LICENSEE = new Licensee();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<LicenseeRecord> getRecordType() {
        return LicenseeRecord.class;
    }

    /**
     * The column <code>LICENSEE.ID</code>.
     */
    public final TableField<LicenseeRecord, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>LICENSEE.LICENCENUMBER</code>.
     */
    public final TableField<LicenseeRecord, String> LICENCENUMBER = createField(DSL.name("LICENCENUMBER"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.FIRSTNAME</code>.
     */
    public final TableField<LicenseeRecord, String> FIRSTNAME = createField(DSL.name("FIRSTNAME"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.LASTNAME</code>.
     */
    public final TableField<LicenseeRecord, String> LASTNAME = createField(DSL.name("LASTNAME"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.MAIDENNAME</code>.
     */
    public final TableField<LicenseeRecord, String> MAIDENNAME = createField(DSL.name("MAIDENNAME"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.SEX</code>.
     */
    public final TableField<LicenseeRecord, String> SEX = createField(DSL.name("SEX"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.DATEOFBIRTH</code>.
     */
    public final TableField<LicenseeRecord, LocalDate> DATEOFBIRTH = createField(DSL.name("DATEOFBIRTH"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>LICENSEE.PLACEOFBIRTH</code>.
     */
    public final TableField<LicenseeRecord, String> PLACEOFBIRTH = createField(DSL.name("PLACEOFBIRTH"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.DEPARTMENTOFBIRTH</code>.
     */
    public final TableField<LicenseeRecord, String> DEPARTMENTOFBIRTH = createField(DSL.name("DEPARTMENTOFBIRTH"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.COUNTRYOFBIRTH</code>.
     */
    public final TableField<LicenseeRecord, String> COUNTRYOFBIRTH = createField(DSL.name("COUNTRYOFBIRTH"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.ADDRESS</code>.
     */
    public final TableField<LicenseeRecord, String> ADDRESS = createField(DSL.name("ADDRESS"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.ZIPCODE</code>.
     */
    public final TableField<LicenseeRecord, String> ZIPCODE = createField(DSL.name("ZIPCODE"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.CITY</code>.
     */
    public final TableField<LicenseeRecord, String> CITY = createField(DSL.name("CITY"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.EMAIL</code>.
     */
    public final TableField<LicenseeRecord, String> EMAIL = createField(DSL.name("EMAIL"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.PHONENUMBER</code>.
     */
    public final TableField<LicenseeRecord, String> PHONENUMBER = createField(DSL.name("PHONENUMBER"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.LICENCESTATE</code>.
     */
    public final TableField<LicenseeRecord, String> LICENCESTATE = createField(DSL.name("LICENCESTATE"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.FIRSTLICENCEDATE</code>.
     */
    public final TableField<LicenseeRecord, LocalDate> FIRSTLICENCEDATE = createField(DSL.name("FIRSTLICENCEDATE"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>LICENSEE.SEASON</code>.
     */
    public final TableField<LicenseeRecord, String> SEASON = createField(DSL.name("SEASON"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.AGECATEGORY</code>.
     */
    public final TableField<LicenseeRecord, String> AGECATEGORY = createField(DSL.name("AGECATEGORY"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>LICENSEE.HANDISPORT</code>.
     */
    public final TableField<LicenseeRecord, Boolean> HANDISPORT = createField(DSL.name("HANDISPORT"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>LICENSEE.BLACKLISTED</code>.
     */
    public final TableField<LicenseeRecord, Boolean> BLACKLISTED = createField(DSL.name("BLACKLISTED"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>LICENSEE.SHOOTINGLOGBOOKID</code>.
     */
    public final TableField<LicenseeRecord, Integer> SHOOTINGLOGBOOKID = createField(DSL.name("SHOOTINGLOGBOOKID"), SQLDataType.INTEGER, this, "");

    private Licensee(Name alias, Table<LicenseeRecord> aliased) {
        this(alias, aliased, null);
    }

    private Licensee(Name alias, Table<LicenseeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>LICENSEE</code> table reference
     */
    public Licensee(String alias) {
        this(DSL.name(alias), LICENSEE);
    }

    /**
     * Create an aliased <code>LICENSEE</code> table reference
     */
    public Licensee(Name alias) {
        this(alias, LICENSEE);
    }

    /**
     * Create a <code>LICENSEE</code> table reference
     */
    public Licensee() {
        this(DSL.name("LICENSEE"), null);
    }

    public <O extends Record> Licensee(Table<O> child, ForeignKey<O, LicenseeRecord> key) {
        super(child, key, LICENSEE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Identity<LicenseeRecord, Integer> getIdentity() {
        return (Identity<LicenseeRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<LicenseeRecord> getPrimaryKey() {
        return Keys.PK_LICENSEE;
    }

    @Override
    public List<ForeignKey<LicenseeRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK_LICENSEE_SHOOTINGLOGBOOK_ID);
    }

    private transient Shootinglogbook _shootinglogbook;

    public Shootinglogbook shootinglogbook() {
        if (_shootinglogbook == null)
            _shootinglogbook = new Shootinglogbook(this, Keys.FK_LICENSEE_SHOOTINGLOGBOOK_ID);

        return _shootinglogbook;
    }

    @Override
    public Licensee as(String alias) {
        return new Licensee(DSL.name(alias), this);
    }

    @Override
    public Licensee as(Name alias) {
        return new Licensee(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Licensee rename(String name) {
        return new Licensee(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Licensee rename(Name name) {
        return new Licensee(name, null);
    }

    // -------------------------------------------------------------------------
    // Row22 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row22<Integer, String, String, String, String, String, LocalDate, String, String, String, String, String, String, String, String, String, LocalDate, String, String, Boolean, Boolean, Integer> fieldsRow() {
        return (Row22) super.fieldsRow();
    }
}
