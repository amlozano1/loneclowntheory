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
        String pwd = "root";
        String dbms = "mysql";
        String dbName = "loneclowntheory";

        try
        {
            con = DriverManager.getConnection(connStr, user, pwd);

            lct = new LCTAuthPolicyManager467(con, dbms, dbName);

            lct.newSubject("s1");
            lct.newSubject("s2");
            lct.newSubject("s3");
            lct.newObject("o1");

            System.out.println("1 " + lct.checkRights("s1", "subject0", "o"));
            System.out.println("2 " + lct.checkRights("o1", "subject0", "o"));
            System.out.println("3 " + lct.checkRights("o1", "subject0", "r"));
            System.out.println("4 " + lct.grant("subject0","s1","o","o1"));
            System.out.println("5 " + lct.grant("s1","s2","t","o1"));
            System.out.println("5.1 " + lct.grant("s1","s2","c","o1"));
            System.out.println("6 " + lct.take("s2", "r", "o1"));
            System.out.println("6.1 " + lct.grant("s2", "s3", "r", "o1"));
            System.out.println("7 " + lct.checkRights("o1", "s1", "o"));
            System.out.println("8 " + lct.revoke("subject0", "s1", "o", "o1", "C"));
            System.out.println("9 " + lct.revoke("s1", "s3", "r", "o1", "C"));
            System.out.print("10 ");
            lct.removeSubject("s1");
            System.out.print("11 ");
            lct.removeObject("o1");

            con.close();

//            System.out.println("Done.");
//            printEntityTable(con, dbName);
//            printACM(con, dbName);
//            System.out.println("Attempting to have subject0 grant st ownership of 01, then have s1 give subject0 read on o1");
//            printACM(con, dbName);
//            lct.grant("subject0", "s1", "o", "o1");
//            printACM(con, dbName);
//            lct.grant("s1", "subject0", "r", "o1");
//            printEntityTable(con, dbName);
//            System.out.println("Attempting to remove subject0, s1 and 01");
//
//            lct.removeSubject("subject0");
//            lct.removeSubject("s1");
//            lct.removeObject("o1");
//            printEntityTable(con, dbName);
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
            while (result.next())
            {
                System.out.print(result.getString(LCTAuthPolicyManager467.subject));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.entity));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.granter));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.right));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.timestamp));
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
            while (result.next())
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
