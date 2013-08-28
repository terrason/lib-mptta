/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.mptta.operation;

import java.sql.Connection;
import org.terramagnet.mptta.utilities.SqlReader;

public abstract class AbstractDatabaseOperation {

    public static final int DRAG_LID = 0;
    public static final int DRAG_RID = 1;
    public static final int DROP_LID = 2;
    public static final int DROP_RID = 3;
    public static final int PRE_LID = 4;
    public static final int PRE_RID = 5;
    public static final int NEXT_LID = 6;
    public static final int NEXT_RID = 7;
    private SqlReader sqlReader = new SqlReader();
    private Connection connection;
    private String[] sql;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String[] getSql() {
        if (sql == null) {
            sql = sqlReader.read(getName());
        }
        return sql;
    }

    public void setSql(String[] sql) {
        this.sql = sql;
    }

    protected Connection getConnection() {
        return connection;
    }

    protected abstract String getName();
}
