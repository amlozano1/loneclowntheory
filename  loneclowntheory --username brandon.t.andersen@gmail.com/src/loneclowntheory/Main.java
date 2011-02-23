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

            /////////////////////////////////
            // Grant and Revoke test cases //
            /////////////////////////////////
            System.out.println("GRANT and TAKE");
            System.out.println(lct.grant("subject0", "s1", "o", "o1")); // "OK"
            System.out.println(lct.grant("s1", "s2", "o", "o1")); // "NO"
            System.out.println(lct.grant("s1", "s2", "d", "o1")); // "OK"
            System.out.println(lct.take("s2", "c", "o1")); // "OK"
            System.out.println(lct.take("s2", "r", "o1")); // "NO"
            System.out.println(lct.grant("s1", "s2", "t", "o1")); // "OK"
            System.out.println(lct.take("s2", "r", "o1")); // "OK"
            System.out.println(lct.take("s2", "u", "o1")); // "OK"
            System.out.println(lct.grant("s2", "s3", "d", "o1")); // "OK"
            System.out.println(lct.grant("s2", "s3", "r", "o1")); // "OK"
            System.out.println(lct.grant("s2", "s3", "u", "o1")); // "OK"
            System.out.println("END GRANT and TAKE");

            /////////////////////////////////////////////////////
            // Test cases for revoke and checkRights - Brandon //
            /////////////////////////////////////////////////////
            lct.newSubject("s100");
            lct.newSubject("s101");
            lct.newSubject("s102");
            lct.newObject("o100");
            lct.newObject("o101");

            System.out.println("100 " + lct.revoke("subject0", "subject0", "o", "s100", "C")); // "NO" Check subject0 cannot have rights revoked
            System.out.println("101 " + lct.grant("subject0", "s100", "o", "o100")); // "OK" Grant own to s100 on o100
            System.out.println("102 " + lct.revoke("s100", "subject0", "o", "o100", "C")); // "NO" Check another owner cannot revoke own from subject0
            System.out.println("103 " + lct.grant("subject0", "subject0", "r", "o100")); // "OK" Grant subject0 read on o100
            System.out.println("104 " + lct.revoke("s100", "subject0", "r", "o100", "C")); // "NO" Check subject0 cannot have other rights revoked
            System.out.println("105 " + lct.grant("s100", "s101", "r", "o100")); // "OK" Grant s101 read on o100 from s100
            System.out.println("106 " + lct.checkRights("o100", "s101", "r")); // "OK" Check s101 can read o100
            System.out.println("107 " + lct.checkRights("o100", "s101", "o")); // "NO" Check s101 owns o100
            System.out.println("108 " + lct.checkRights("o100", "s100", "o")); // "OK" check s100 owns o100
            System.out.println("109 " + lct.grant("s100", "s101", "c", "o100")); // "OK" Grant s101 copy on o100
            System.out.println("110 " + lct.grant("s101", "s102", "r", "o100")); // "OK" Grant s102 read on o100 via s101 copy
            System.out.println("111 " + lct.checkRights("o100", "s102", "r")); // "OK" Check s102 has read on o100
            System.out.println("112 " + lct.revoke("s101", "s102", "r", "o100", "N")); // "NO" Check s101 can't revoke read from s102 since it does not own o100
            System.out.println("113 " + lct.revoke("s100", "s102", "r", "o100", "N")); // "OK" Check s101 can revoke read from s102 on o100
            System.out.println("114 " + lct.grant("s101", "s102", "r", "o100")); // "OK" Grant s102 read on o100
            System.out.println("115 " + lct.revoke("s100", "s101", "c", "o100", "N")); // "OK" Revoke copy from s101 on o100 without cascade
            System.out.println("116 " + lct.checkRights("o100", "s101", "c")); // "NO" Check s101 lost copy on o100
            System.out.println("117 " + lct.checkRights("o100", "s102", "r")); // "OK" Check s102 did not lose read on o100 since no cascade
            System.out.println("118 " + lct.grant("s100", "s101", "c", "o100")); // "OK" Grant s101 copy on o100 again
            System.out.println("119 " + lct.checkRights("o100", "s101", "c")); // "OK" Check s101 has copy on o100
            System.out.println("120 " + lct.revoke("s100", "s101", "c", "o100", "C")); // "OK" Revoke copy from s101 on o100 with cascade
            System.out.println("121 " + lct.checkRights("o100", "s101", "c")); // "NO" Check s101 lost copy on o101
            System.out.println("122 " + lct.checkRights("o100", "s102", "r")); // "NO" Check s102 lost read on o100 due to cascade
            System.out.println("123 " + lct.grant("s100", "s101", "c", "o100")); // "OK"
            System.out.println("124 " + lct.grant("s100", "s101", "d", "o100")); // "OK"
            System.out.println("125 " + lct.grant("s100", "s101", "t", "o100")); // "OK"
            
            System.out.println("127 " + lct.grant("s100", "s101", "u", "o100")); // "OK"
            System.out.println("128 " + lct.grant("s101", "s102", "d", "o100")); // "OK"
            System.out.println("129 " + lct.grant("s101", "s102", "t", "o100")); // "OK"
            System.out.println("130 " + lct.grant("s101", "s102", "r", "o100")); // "OK"
            System.out.println("131 " + lct.grant("s101", "s102", "u", "o100")); // "OK"
            System.out.println("133 " + lct.checkRights("o100", "s100", "o")); // "OK"
            System.out.println("134 " + lct.checkRights("o100", "s101", "c")); // "OK"
            System.out.println("135 " + lct.checkRights("o100", "s101", "d")); // "OK"
            System.out.println("136 " + lct.checkRights("o100", "s101", "t")); // "OK"
            System.out.println("137 " + lct.checkRights("o100", "s101", "r")); // "OK"
            System.out.println("138 " + lct.checkRights("o100", "s101", "u")); // "OK"
            System.out.println("139 " + lct.checkRights("o100", "s102", "d")); // "OK"
            System.out.println("140 " + lct.checkRights("o100", "s102", "t")); // "OK"
            System.out.println("141 " + lct.checkRights("o100", "s102", "r")); // "OK"
            System.out.println("142 " + lct.checkRights("o100", "s102", "u")); // "OK"
            System.out.println("143 " + lct.revoke("subject0", "s100", "o", "o100", "C")); // "OK" Revoke own from S100 on o100 with cascade
            System.out.println("144 " + lct.checkRights("o100", "s100", "o")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("145 " + lct.checkRights("o100", "s101", "c")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("146 " + lct.checkRights("o100", "s101", "d")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("147 " + lct.checkRights("o100", "s101", "t")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("148 " + lct.checkRights("o100", "s101", "r")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("149 " + lct.checkRights("o100", "s101", "u")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("150 " + lct.checkRights("o100", "s102", "d")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("151 " + lct.checkRights("o100", "s102", "t")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("152 " + lct.checkRights("o100", "s102", "r")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("153 " + lct.checkRights("o100", "s102", "u")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("154 " + lct.grant("subject0", "s100", "o", "o100")); // "OK"
            System.out.println("155 " + lct.grant("s100", "s101", "c", "o100")); // "OK"
            System.out.println("156 " + lct.grant("s100", "s101", "d", "o100")); // "OK"
            System.out.println("157 " + lct.grant("s100", "s101", "t", "o100")); // "OK"
            System.out.println("158 " + lct.grant("s100", "s101", "r", "o100")); // "OK"
            System.out.println("159 " + lct.grant("s100", "s101", "u", "o100")); // "OK"
            System.out.println("160 " + lct.grant("s101", "s102", "d", "o100")); // "OK"
            System.out.println("161 " + lct.grant("s101", "s102", "t", "o100")); // "OK"
            System.out.println("162 " + lct.grant("s101", "s102", "r", "o100")); // "OK"
            System.out.println("163 " + lct.grant("s101", "s102", "u", "o100")); // "OK"
            System.out.println("164 " + lct.revoke("subject0", "s100", "o", "o100", "N")); // "OK" Revoke own from S100 on o100 without cascade
            System.out.println("165 " + lct.checkRights("o100", "s100", "o")); // "NO" Check rights lost on cascade of revoking own
            System.out.println("166 " + lct.checkRights("o100", "s101", "c")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("167 " + lct.checkRights("o100", "s101", "d")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("168 " + lct.checkRights("o100", "s101", "t")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("169 " + lct.checkRights("o100", "s101", "r")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("170 " + lct.checkRights("o100", "s101", "u")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("171 " + lct.checkRights("o100", "s102", "d")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("172 " + lct.checkRights("o100", "s102", "t")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("173 " + lct.checkRights("o100", "s102", "r")); // "OK" Check rights remianed on non-cascade of revoking own
            System.out.println("174 " + lct.checkRights("o100", "s102", "u")); // "OK" Check rights remianed on non-cascade of revoking own

            ///////////////////////////////////////////////
            // End Test cases for revoke and checkRights //
            ///////////////////////////////////////////////



            System.out.println("Done.");
            printEntityTable(con, dbName);
            printACM(con, dbName);
            System.out.println("Attempting to have subject0 grant st ownership of 01, then have s1 give subject0 read on o1");
            printACM(con, dbName);
            lct.grant("subject0", "s1", "o", "o1");
            printACM(con, dbName);
            lct.grant("s1", "subject0", "r", "o1");
            printEntityTable(con, dbName);

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
            System.out.println("Attempting to remove s1 again");
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
            System.out.println("Attempting to remove o1 again");
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
            System.out.println("subj\tent\tgranter\tright\ttimestamp");
            while (result.next())
            {
                System.out.print(result.getString(LCTAuthPolicyManager467.subject));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.entity));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.granter));
                System.out.print("\t" + result.getString(LCTAuthPolicyManager467.right));
                System.out.println("\t" + result.getString(LCTAuthPolicyManager467.timestamp));
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
