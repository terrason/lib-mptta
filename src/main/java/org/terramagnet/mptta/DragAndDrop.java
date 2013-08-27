/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.mptta;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DragAndDrop {

    private static final int DRAG_LID = 0;
    private static final int DRAG_RID = 1;
    private static final int DROP_LID = 2;
    private static final int DROP_RID = 3;
    private static final int PRE_LID = 4;
    private static final int PRE_RID = 5;
    private static final int NEXT_LID = 6;
    private static final int NEXT_RID = 7;
    private Connection connection;
    private String[] sql;

    public DragAndDrop(Connection connection, String[] sql) {
        this.connection = connection;
        this.sql = sql;
    }

    /**
     * 将节点A到节点B的前、后或内部.
     *
     * <ul>
     * <li><code>type&gt;0</code> A移动到B的前面</li>
     * <li><code>type&lt;0</code> A移动到B的后面</li>
     * <li><code>type==0</code> A移动到B的内部，会追加到B的最后一个子节点的位置上。</li>
     * </ul>
     *
     * @param drapId 节点A
     * @param dropId 节点B
     * @param type 移动点.
     *
     */
    public void execute(Serializable drapId, Serializable dropId, int type) throws SQLException {
        int[] x = selectBasicData(drapId, dropId, connection);
        if (x == null) {
            throw new IllegalArgumentException();
        }
        int sign = x[DRAG_LID] - x[DROP_LID];
        if (sign == 0) {
            return;
        }
        if (x[DROP_LID] > x[DRAG_LID] && x[DROP_LID] < x[DRAG_RID]) {
            throw new IllegalArgumentException("目标节点不能是自己的后代几点");
        }
        int shiftMin = 0;
        int shiftMax = 0;
        int pushMin = 0;
        int pushMax = 0;
        int shift = 0;
        int push = 0;
        if (sign > 0) {
            shiftMin = x[DRAG_LID];
            shiftMax = x[DRAG_RID];
            push = shiftMax - shiftMin + 1;
            pushMax = x[DRAG_LID] - 1;
            if (type > 0) {
                shift = x[DRAG_LID] - x[DROP_LID];
                pushMin = x[DROP_LID];
            } else if (type < 0) {
                shift = x[DRAG_LID] - x[NEXT_LID];
                pushMin = x[NEXT_LID];
            } else {
                shift = x[DRAG_LID] - x[DROP_RID];
                pushMin = x[DROP_RID];
            }
        } else if (sign < 0) {
            pushMin = x[DRAG_LID];
            pushMax = x[DRAG_RID];
            shift = x[DRAG_RID] - x[DRAG_LID] + 1;
            shiftMin = x[DRAG_RID] + 1;
            if (type > 0) {
                push = x[PRE_RID] - x[DRAG_RID];
                shiftMax = x[PRE_RID];
            } else if (type < 0) {
                push = x[DROP_RID] - x[DRAG_RID];
                shiftMax = x[DROP_RID];
            } else {
                push = x[DROP_RID] - x[DRAG_RID] - 1;
                shiftMax = x[DROP_RID] - 1;
            }
        }
        move(shift, shiftMin, shiftMax, push, pushMin, pushMax, connection);
    }

    private int[] selectBasicData(Object drapId, Object dropId, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql[0]);
        ps.setObject(1, drapId);
        ps.setObject(2, dropId);
        ResultSet rs = ps.executeQuery();
        int[] r = null;
        if (rs.next()) {
            r = new int[8];
            for (int i = 0; i < r.length; i++) {
                r[i] = rs.getInt(i + 1);
            }
        }
        rs.close();
        ps.close();
        return r;
    }

    private void move(int shift, int shiftMin, int shiftMax, int push, int pushMin, int pushMax, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql[1]);
        ps.setInt(1, shift);
        ps.setInt(2, shiftMin);
        ps.setInt(3, shiftMax);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sql[2]);
        ps.setInt(1, shift);
        ps.setInt(2, shiftMin);
        ps.setInt(3, shiftMax);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sql[3]);
        ps.setInt(1, push);
        ps.setInt(2, pushMin);
        ps.setInt(3, pushMax);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sql[4]);
        ps.setInt(1, push);
        ps.setInt(2, pushMin);
        ps.setInt(3, pushMax);
        ps.executeUpdate();
        ps.close();

        ps = connection.prepareStatement(sql[5]);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sql[6]);
        ps.executeUpdate();
        ps.close();
    }
}
