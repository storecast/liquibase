package liquibase.sqlgenerator.core;

import liquibase.database.Database;
import liquibase.database.core.InformixDatabase;
import liquibase.database.typeconversion.TypeConverterFactory;
import liquibase.database.core.SybaseDatabase;
import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.AddDefaultValueStatement;

public class AddDefaultValueGeneratorInformix extends AddDefaultValueGenerator {
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(AddDefaultValueStatement statement, Database database) {
        return database instanceof InformixDatabase;
    }

    @Override
    public ValidationErrors validate(AddDefaultValueStatement addDefaultValueStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = super.validate(addDefaultValueStatement, database, sqlGeneratorChain);
        if (addDefaultValueStatement.getColumnDataType() == null) {
            validationErrors.checkRequiredField("columnDataType", addDefaultValueStatement.getColumnDataType());
        }
        return validationErrors;
    }

    @Override
    public Sql[] generateSql(AddDefaultValueStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {

        Object defaultValue = statement.getDefaultValue();
        return new Sql[]{
                new UnparsedSql("ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) + " MODIFY (" + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) + " " + TypeConverterFactory.getInstance().findTypeConverter(database).getDataType(statement.getColumnDataType(), false) + " DEFAULT " + TypeConverterFactory.getInstance().findTypeConverter(database).getDataType(defaultValue).convertObjectToString(defaultValue, database) + ")",
                        new Column()
                                .setTable(new Table(statement.getTableName()).setSchema(statement.getSchemaName()))
                                .setName(statement.getColumnName()))
        };
    }
}