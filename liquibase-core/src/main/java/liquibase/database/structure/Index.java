package liquibase.database.structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import liquibase.util.StringUtils;

public class Index implements DatabaseObject, Comparable<Index> {

    /** Marks Index as associated with Primary Key [PK] */
    public final static String MARK_PRIMARY_KEY = "primaryKey";
    /** Marks Index as associated with Foreign Key [FK] */
    public final static String MARK_FOREIGN_KEY = "foreignKey";
    /** Marks Index as associated with Unique Constraint [UC] */
    public final static String MARK_UNIQUE_CONSTRAINT = "uniqueConstraint";

    private String name;
    private Table table;
    private String tablespace;
    private Boolean unique;
    private List<String> columns = new ArrayList<String>();
    private String filterCondition;
    // Contain associations of index
    // for example: foreignKey, primaryKey or uniqueConstraint
    private Set<String> associatedWith = new HashSet<String>();

    public DatabaseObject[] getContainingObjects() {
        return new DatabaseObject[] {
                table
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getTablespace() {
        return tablespace;
    }

    public void setTablespace(String tablespace) {
        this.tablespace = tablespace;
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getColumnNames() {
        return StringUtils.join(columns, ", ");
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }

    public void setUnique(Boolean value) {
        this.unique = value;
    }

    public Boolean isUnique() {
        return this.unique;
    }

    public Set<String> getAssociatedWith() {
        return associatedWith;
    }

    public String getAssociatedWithAsString() {
        return StringUtils.join(associatedWith, ",");
    }

    public void addAssociatedWith(String item) {
        associatedWith.add(item);
    }

    public boolean isAssociatedWith(String keyword) {
        return associatedWith.contains(keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Index index = (Index) o;

        boolean equals = getColumnNames().equalsIgnoreCase(index.getColumnNames());

        equals &= ObjectUtils.equals(unique, index.unique);
        equals &= table.getName().equalsIgnoreCase(index.table.getName());

        return equals;
    }

    @Override
    public int hashCode() {
        final int hashCode = new HashCodeBuilder().append(table.getName().toUpperCase()).append(getColumnNames().toUpperCase()).append(BooleanUtils.toBoolean(unique)).toHashCode();
        return hashCode;
    }

    public int compareTo(Index o) {
        int returnValue = this.table.getName().compareToIgnoreCase(o.table.getName());

        if (returnValue == 0) {
            returnValue = getColumnNames().compareToIgnoreCase(o.getColumnNames());
        }

        if (returnValue == 0) {
            returnValue = getUniqueValue().compareTo(o.getUniqueValue());
        }

        return returnValue;
    }

    private Integer getUniqueValue() {
        if (unique == null) {
            return 0;
        }
        return unique ? 1 : 2;
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(getName());
        if (BooleanUtils.toBoolean(unique)) {
            stringBuffer.append(" unique ");
        }
        if (table != null && columns != null) {
            stringBuffer.append(" on ").append(table.getName());
            stringBuffer.append("(");
            for (String column : columns) {
                stringBuffer.append(column).append(", ");
            }
            stringBuffer.delete(stringBuffer.length() - 2, stringBuffer.length());
            stringBuffer.append(")");
        }
        return stringBuffer.toString();
    }
}
