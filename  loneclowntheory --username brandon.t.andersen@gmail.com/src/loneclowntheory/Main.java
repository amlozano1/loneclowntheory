    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loneclowntheory;

import java.sql.*;

/**
 *
 * @author Brandon
 */
public class Main
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        LCTAuthPolicyManager467 lct;

        Connection con = null;

        String connStr = "jdbc:mysql://localhost:3306";
        String user = "root";
        String pwd = "your_password_here";
        String dbms = "mysql";
        String dbName = "loneclowntheory";

        try
        {
            con = DriverManager.getConnection(connStr, user, pwd);

            lct = new LCTAuthPolicyManager467(con, dbms, dbName);

            lct.newSubject("s1");
            lct.newObject("o1");

            System.out.println("Done.");
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }
}
