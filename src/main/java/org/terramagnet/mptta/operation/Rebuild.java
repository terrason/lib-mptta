/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.mptta.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rebuild extends AbstractDatabaseOperation {

    private static Logger logger = LoggerFactory.getLogger(Rebuild.class);
    private int rootPid = 0;

    @Override
    protected String getName() {
        return "rebuild";
    }

    public void execute() throws SQLException {
        Connection connection = getConnection();
        ArrayList<int[]> batches=new ArrayList<int[]>();
        addBatchUpdate(connection, getSql(), rootPid, 0,batches);
        PreparedStatement ps = connection.prepareStatement(getSql()[1]);
        for (int i = 0; i < batches.size(); i++) {
            int[] x = batches.get(i);
            logger.debug("{}",x);
            ps.setInt(1, x[1]);
            ps.setInt(2, x[2]);
            ps.setInt(3, x[0]);
            ps.addBatch();
        }
        int[] c = ps.executeBatch();
        logger.debug("执行结果：{}",c);
        ps.close();
        ps=connection.prepareStatement(getSql()[2]);
        ps.executeUpdate();
        ps.close();
    }

    private int addBatchUpdate(Connection connection, String[] sql, int pid, int index,ArrayList<int[]> batches) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql[0]);
        ps.setInt(1, pid);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int[] i=new int[3];
            batches.add(i);
            i[0]=rs.getInt(1);
            i[1]=index++;
            index = addBatchUpdate(connection, sql, i[0], index,batches);
            i[2]=index++;
        }
        rs.close();
        ps.close();
        return index;
    }

    public int getRootPid() {
        return rootPid;
    }

    public void setRootPid(int rootPid) {
        this.rootPid = rootPid;
    }
}