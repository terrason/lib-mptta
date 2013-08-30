/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.mptta.operation;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DragAndDrop extends AbstractDatabaseOperation {

    @Override
    protected String getName() {
        return "drag_drop";
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
        int[] x = selectOperationPoints(drapId, dropId, getConnection());
        if (x == null) {
            throw new IllegalArgumentException();
        }
        int sign = x[DRAG_RID] - x[DROP_RID];
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
        move(shift, shiftMin, shiftMax, push, pushMin, pushMax, getConnection());
    }

    private void move(int shift, int shiftMin, int shiftMax, int push, int pushMin, int pushMax, Connection connection) throws SQLException {
        String[] sqls = getSql();
        PreparedStatement ps = connection.prepareStatement(sqls[1]);
        ps.setInt(1, shift);
        ps.setInt(2, shiftMin);
        ps.setInt(3, shiftMax);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sqls[2]);
        ps.setInt(1, shift);
        ps.setInt(2, shiftMin);
        ps.setInt(3, shiftMax);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sqls[3]);
        ps.setInt(1, push);
        ps.setInt(2, pushMin);
        ps.setInt(3, pushMax);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sqls[4]);
        ps.setInt(1, push);
        ps.setInt(2, pushMin);
        ps.setInt(3, pushMax);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sqls[5]);
        ps.executeUpdate();
        ps.close();
        ps = connection.prepareStatement(sqls[6]);
        ps.executeUpdate();
        ps.close();
    }

    private int[] selectOperationPoints(Object nodeId, Object targetNodeId, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(getSql()[0]);
        ps.setObject(1, nodeId == null ? -1 : nodeId);
        ps.setObject(2, targetNodeId);
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
}
