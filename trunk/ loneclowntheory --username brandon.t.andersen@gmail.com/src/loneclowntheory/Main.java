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

            Statement stmt = con.createStatement();

            ResultSet result = stmt.executeQuery("SELECT * FROM " + dbName + ".entityTable");

            while(result.next())
            {
                System.out.print(" " + result.getString("entityID"));
                System.out.print(" " + result.getString("entityName"));
                System.out.println(" " + result.getString("subject_or_object"));
            }
            result.close();

            System.out.println("Attempting to remove subject0, s1 and 01");

            lct.removeSubject("subject0");
            lct.removeSubject("s1");
            lct.removeObject("o1");

            result = stmt.executeQuery("SELECT * FROM " + dbName + ".entityTable");
            while(result.next())
            {
                System.out.print(" " + result.getString("entityID"));
                System.out.print(" " + result.getString("entityName"));
                System.out.println(" " + result.getString("subject_or_object"));
            }
            result.close();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }
}
