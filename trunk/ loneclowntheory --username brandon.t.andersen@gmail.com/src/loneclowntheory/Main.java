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
        String pwd = "passwordhere";
        String dbms = "mysql";
        String dbName = "loneclowntheory";

        try
        {
            con = DriverManager.getConnection(connStr, user, pwd);

            lct = new LCTAuthPolicyManager467(con, dbms, dbName);

            lct.newSubject("s1");
            lct.newObject("o1");

            System.out.println("Done.");
            printEntityTable( con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to have subject0 grant st ownership of 01, then have s1 give subject0 read on o1");
            printACM(con, dbName);
            lct.grant("subject0", "s1", "o", "o1");
            printACM(con, dbName);
            lct.grant("s1", "subject0", "r", "o1");
            printEntityTable( con, dbName);
            System.out.println("Attempting to remove subject0, s1 and 01");

            lct.removeSubject("subject0");
            lct.removeSubject("s1");
            lct.removeObject("o1");
            printEntityTable( con, dbName);
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }
    public static void printACM(Connection con, String dbName)
    {
        try
        {
        Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM " + LCTAuthPolicyManager467.acm);
        System.out.println("______________________________________________________________");
        System.out.println("s\te\tr\tu\to\tc\td");
        while(result.next())
            {
                System.out.print(result.getString(LCTAuthPolicyManager467.subject));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.object));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.read));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.update));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.own));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.takeCopy));
                System.out.println("\t" + result.getString(LCTAuthPolicyManager467.takeReadUpdate));
            }
        System.out.println("______________________________________________________________");
        }

        catch (SQLException e)
        {
            System.out.println(e);
        }
    }
        public static void printEntityTable(Connection con, String dbName)
    {
        try
        {
        Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM " + dbName + ".entityTable");
        System.out.println("====================================================");
        System.out.println("eID\t\teName\ts_or_o");
            while(result.next())
            {
                System.out.print(result.getString("entityID"));
                System.out.print("\t\t" + result.getString("entityName"));
                System.out.println("\t" + result.getString("subject_or_object"));
            }
        System.out.println("====================================================");
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

}
