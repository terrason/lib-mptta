/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.mptta.operation;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lipei
 */
public class RebuildTest {

    private DataSource dataSource;

    public RebuildTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

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
     * Test of execute method, of class Rebuild.
     */
//    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Rebuild instance = new Rebuild();
            instance.setConnection(connection);
            instance.execute();
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