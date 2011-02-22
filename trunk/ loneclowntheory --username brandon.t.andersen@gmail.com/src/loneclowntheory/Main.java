    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loneclowntheory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        String dbName = "LoneClownTheory";

        try
        {
            con = DriverManager.getConnection(connStr, user, pwd);

            lct = new LCTAuthPolicyManager467(con, dbms, dbName);

            ///////////////////////////////////////
            //newSubject and newObject test cases//
            ///////////////////////////////////////

            lct.newSubject("s1"); //should pass 1st run, else fail
            lct.newSubject("s1"); //should fail - already exists
            lct.newSubject("s2"); //pass 1st run, else fail
            lct.newSubject("s3"); //pass 1st run, else fail
            lct.newObject("o1"); //should pass 1st run, else fail
            lct.newObject("o1"); //should fail - aready exists
            lct.newObject("o2"); //pass 1st run, else fail
            lct.newObject("o3"); //pass 1st run, else fail

            ///////////////////////////////////////

            System.out.println("1 " + lct.checkRights("s1", "subject0", "o"));
            System.out.println("2 " + lct.checkRights("o1", "subject0", "o"));
            System.out.println("3 " + lct.checkRights("o1", "subject0", "r"));
            System.out.println("4 " + lct.grant("subject0","s1","o","o1"));
            System.out.println("5 " + lct.grant("s1","s2","t","o1"));
            System.out.println("5.1 " + lct.grant("s1","s2","c","o1"));
            System.out.println("6 " + lct.take("s2", "r", "o1"));
            System.out.println("6.1 " + lct.grant("s2", "s3", "r", "o1"));
            System.out.println("7 " + lct.checkRights("o1", "s1", "o"));
            System.out.println("7.1 " + lct.grant("subject0", "s2", "r", "s1"));
            System.out.println("8 " + lct.revoke("subject0", "s1", "o", "o1", "C"));
            System.out.println("9 " + lct.revoke("s1", "s3", "r", "o1", "C"));
            System.out.print("10 ");
            lct.removeSubject("s1");
            System.out.print("11 ");
            lct.removeObject("o1");

            /////////////////////////////////////////////////////
            // Test cases for revoke and checkRights - Brandon //
            /////////////////////////////////////////////////////
            lct.newSubject("s100");
            lct.newSubject("s101");
            lct.newSubject("s102");
            lct.newObject("o100");
            lct.newObject("o101");

            System.out.println("100 " + lct.revoke("subject0", "subject0", "o", "s100", "C")); // Check subject0 cannot have rights revoked
            System.out.println("101 " + lct.grant("subject0", "s100", "o", "o100"));
            System.out.println("102 " + lct.revoke("s100", "subject0", "o", "o100", "C"));
            System.out.println("103 " + lct.grant("subject0", "subject0", "r", "o100"));
            System.out.println("104 " + lct.revoke("s100", "subject0", "r", "o100", "C"));
            System.out.println("105 " + lct.grant("s100", "s101", "r", "o100"));
            System.out.println("106 " + lct.checkRights("o100", "s101", "r"));
            System.out.println("107 " + lct.checkRights("o100", "s101", "o"));
            System.out.println("108 " + lct.checkRights("o100", "s100", "o"));
            System.out.println("109 " + lct.grant("s100", "s101", "c", "o100"));
            System.out.println("110 " + lct.grant("s101", "s102", "r", "o100"));
            System.out.println("111 " + lct.checkRights("o100", "s102", "r"));
            System.out.println("112 " + lct.revoke("s101", "s102", "r", "o100", "N"));
            System.out.println("113 " + lct.revoke("s100", "s102", "r", "o100", "N"));
            System.out.println("114 " + lct.grant("s101", "s102", "r", "o100"));
            System.out.println("115 " + lct.revoke("s100", "s101", "c", "o100", "N"));
            System.out.println("116 " + lct.checkRights("o100", "s101", "c"));
            System.out.println("117 " + lct.checkRights("o100", "s102", "r"));
            System.out.println("118 " + lct.grant("s100", "s101", "c", "o100"));
            System.out.println("119 " + lct.checkRights("o100", "s101", "c"));
            System.out.println("120 " + lct.revoke("s100", "s101", "c", "o100", "C"));
            System.out.println("121 " + lct.checkRights("o100", "s101", "c"));
            System.out.println("122 " + lct.checkRights("o100", "s102", "r"));
            System.out.println("123 " + lct.grant("s100", "s101", "c", "o100"));
            System.out.println("124 " + lct.grant("s100", "s101", "d", "o100"));
            System.out.println("125 " + lct.grant("s100", "s101", "t", "o100"));
            System.out.println("126 " + lct.grant("s100", "s101", "r", "o100"));
            System.out.println("127 " + lct.grant("s100", "s101", "u", "o100"));
            System.out.println("128 " + lct.grant("s101", "s102", "d", "o100"));
            System.out.println("129 " + lct.grant("s101", "s102", "t", "o100"));
            System.out.println("130 " + lct.grant("s101", "s102", "r", "o100"));
            System.out.println("131 " + lct.grant("s101", "s102", "u", "o100"));
            System.out.println("133 " + lct.checkRights("o100", "s100", "o"));
            System.out.println("134 " + lct.checkRights("o100", "s101", "c"));
            System.out.println("135 " + lct.checkRights("o100", "s101", "d"));
            System.out.println("136 " + lct.checkRights("o100", "s101", "t"));
            System.out.println("137 " + lct.checkRights("o100", "s101", "r"));
            System.out.println("138 " + lct.checkRights("o100", "s101", "u"));
            System.out.println("139 " + lct.checkRights("o100", "s102", "d"));
            System.out.println("140 " + lct.checkRights("o100", "s102", "t"));
            System.out.println("141 " + lct.checkRights("o100", "s102", "r"));
            System.out.println("142 " + lct.checkRights("o100", "s102", "u"));
            System.out.println("143 " + lct.revoke("subject0", "s100", "o", "o100", "C"));
            System.out.println("144 " + lct.checkRights("o100", "s100", "o"));
            System.out.println("145 " + lct.checkRights("o100", "s101", "c"));
            System.out.println("146 " + lct.checkRights("o100", "s101", "d"));
            System.out.println("147 " + lct.checkRights("o100", "s101", "t"));
            System.out.println("148 " + lct.checkRights("o100", "s101", "r"));
            System.out.println("149 " + lct.checkRights("o100", "s101", "u"));
            System.out.println("150 " + lct.checkRights("o100", "s102", "d"));
            System.out.println("151 " + lct.checkRights("o100", "s102", "t"));
            System.out.println("152 " + lct.checkRights("o100", "s102", "r"));
            System.out.println("153 " + lct.checkRights("o100", "s102", "u"));

            ///////////////////////////////////////////////
            // End Test cases for revoke and checkRights //
            ///////////////////////////////////////////////



//            System.out.println("Done.");
//            printEntityTable(con, dbName);
//            printACM(con, dbName);
//            System.out.println("Attempting to have subject0 grant st ownership of 01, then have s1 give subject0 read on o1");
//            printACM(con, dbName);
//            lct.grant("subject0", "s1", "o", "o1");
//            printACM(con, dbName);
//            lct.grant("s1", "subject0", "r", "o1");
//            printEntityTable(con, dbName);

/********************************************************/
/*        removeSubject and removeObject test cases     */
/********************************************************/

            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove subject0");
            lct.removeSubject("subject0");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove s1");
            lct.removeSubject("s1");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove s2");
            lct.removeSubject("s2");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove s3");
            lct.removeSubject("s3");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove o1");
            lct.removeObject("o1");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove o2");
            lct.removeObject("o2");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove o3");
            lct.removeObject("o3");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove s100");
            lct.removeSubject("s100");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove s101");
            lct.removeSubject("s101");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove s102");
            lct.removeSubject("s102");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove o100");
            lct.removeObject("o100");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to remove o101");
            lct.removeObject("o101");
            printEntityTable(con, dbName);
            printACM(con, dbName);

/********************************************************/
/*   End of removeSubject and removeObject test cases   */
/********************************************************/


            con.close();
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
