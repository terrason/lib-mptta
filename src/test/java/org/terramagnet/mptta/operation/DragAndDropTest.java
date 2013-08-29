/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.mptta.operation;

import org.terramagnet.mptta.operation.DragAndDrop;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.terramagnet.mptta.utilities.SqlReader;

/**
 *
 * @author lipei
 */
public class DragAndDropTest {

    public DragAndDropTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        File configFile = new File("application.properties");
        FileInputStream fis = new FileInputStream(configFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        Properties prop = new Properties();
        prop.load(bis);
        bis.close();
        dataSource = DruidDataSourceFactory.createDataSource(prop);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class DragAndDrop.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        Serializable drapId = 1000061;
        Serializable dropId = 3;
        int type = 0;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            DragAndDrop instance = new DragAndDrop();
            instance.setConnection(connection);
            instance.execute(drapId, dropId, type);
            connection.commit();
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}