/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terramagnet.mptta.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlReader {

    private static Logger logger = LoggerFactory.getLogger(SqlReader.class);

    public String[] read(String name) {
        String classpath = "/org/terramagnet/mptta/sql/" + name + ".sql";
        logger.debug(classpath);
        InputStream is = getClass().getResourceAsStream(classpath);
        if (is == null) {
            throw new RuntimeException(name + " 的sql文件不存在！");
        }
        Reader reader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(reader);
        String line;
        ArrayList<String> sql = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("--") || line.isEmpty()) {
                    continue;
                }
                if (line.endsWith(";")) {
                    builder.append(line).deleteCharAt(builder.length() - 1);
                    sql.add(builder.toString());
                    builder.setLength(0);
                }else{
                    builder.append(line).append("\n");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("读取文件出错！", ex);
        }
        return sql.toArray(new String[sql.size()]);
    }
}
