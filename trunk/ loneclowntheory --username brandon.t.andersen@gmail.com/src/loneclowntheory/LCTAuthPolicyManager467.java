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
public class LCTAuthPolicyManager467 implements AuthPolicyManager467
{
    private String dbms;
    private String dbName;
    private Connection con;

    public LCTAuthPolicyManager467(Connection connArg, String dbmsArg, String dbNameArg)
    {
        super();
        this.con = connArg;
        this.dbms = dbmsArg;
        this.dbName = dbNameArg;
    }

    /**
     * Creates a new subject.  Initially, subject "subject0" is the "owner" of all new entities.
     *
     * @param subjectName Name of new subject
     */
    public void newSubject(String subjectName)
    {
        Statement stmt = null;
        String query = "SELECT * FROM " + dbName + ".entityTable WHERE entityName = '" + subjectName + "'";

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(query);

            //Precondition: subject does not already exist
            if (rs.next())
            {
                System.out.println("NO");
            }
            else
            {
                if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE)
                {
                    rs.moveToInsertRow();

                    rs.updateString("entityName", subjectName);

                    rs.updateString("subject_or_object", "1");

                    rs.insertRow();

                    rs.close();

//                    query = "INSERT INTO " + dbName + ".entityTable (entityName, subject_or_object) VALUES ('" + subjectName + "', 2)";
//
//                    stmt.executeUpdate(query);

                    stmt.close();

                    System.out.println("OK");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        //Create the subject

        //Postconditions:
        //1 - subject added to Subjects and Objects
        //2 - the subject has no rights and no entities have rights on the subject
        //3 - all other rights remain unchanged

        //Assign subject0 as the owner of the new subject
    }

    /**
     * Creates a new object.  Initially, subject "subject0" is the "owner" of all new entities.
     *
     * @param objectName Name of new object
     */
    public void newObject(String objectName)
    {
        Statement stmt = null;
        String query = "SELECT * FROM " + dbName + ".entityTable WHERE entityName = '" + objectName + "'";

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(query);

            //Precondition: subject does not already exist
            if (rs.next())
            {
                System.out.println("NO");
            }
            else
            {
                if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE)
                {
                    rs.moveToInsertRow();

                    rs.updateString("entityName", objectName);

                    rs.updateString("subject_or_object", "2");

                    rs.insertRow();

                    rs.close();

//                    query = "INSERT INTO " + dbName + ".entityTable (entityName, subject_or_object) VALUES ('" + subjectName + "', 2)";
//
//                    stmt.executeUpdate(query);

                    stmt.close();

                    System.out.println("OK");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Removes the subject with subjectName == Name.  Subject "subject0" cannot be removed.  All
     * right entries associated with the subject are also removed.
     *
     * @param Name      Subject's name to be removed
     */
    public void removeSubject(String Name)
    {
        Statement stmt = null;

        String query = "SELECT * FROM " + dbName + ".entityTable WHERE entityName = '" + Name + "'";

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(query);

            //Precondition: Subject exist in database
            //This also doesn't allow subject0 to be removed form the DB.
            if (!rs.next() || Name.equals("subject0"))
            {
                System.out.println("No");
            }
            else
            {
                if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE)
                {
                    rs.deleteRow();

                    System.out.println("OK");
                }
            }
         }
         catch (SQLException e)
         {
             System.out.println(e);
         }
    }

    /**
     * Removes the object with objectName == Name.  All right entries on the object are also removed
     * from the system.
     *
     * @param Name      Object's name to be removed
     */
    public void removeObject(String Name)
    {
        Statement stmt = null;

        String query = "SELECT * FROM " + dbName + ".entityTable WHERE entityName = '" + Name + "'" + " AND subject_or_object <> " + "1" ;

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(query);

            //Precondition: Object exist in database
            //Even though subject0 should be considered a subject_or_object which
            //would not allow for his removale by this function it is also checked here just in case.
            if (!rs.next() || Name.equals("subject0"))
            {
               System.out.println("No");
            }
            else
            {
               if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE)
               {
                   rs.deleteRow();

                   System.out.println("OK");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Subject X grants subject Y right R on the entity E_Name.
     *
     * @param X       Subject granting the right
     * @param Y       Subject being granted the right
     * @param R       {"r", "u", "c", "o", "d", "t"}
     * @param E_Name  The entity which subject Y will have rights
     * @return        "OK" on success, "NO" otherwise
     */
    public String grant(String X, String Y, String R, String E_Name)
    {
        return "";
    }

    /**
     * Subject X gets right R on the entity E_Name by "taking" it from subject Y.  Subject Y's
     * rights are unmodified.
     *
     * @param X       Subject from which the right is being taken
     * @param Y       Subject taking the right
     * @param R       {"r", "u", "c", "o", "d", "t"}
     * @param E_Name  The entity which subject Y will have rights
     * @return        "OK" on success, "NO" otherwise
     */
    public String take(String X, String Y, String R, String E_Name)
    {
        return "";
    }

    /**
     * Subject X revokes right R on the entity E_Name from subject Y.
     *
     * @param X         Revoker of the right
     * @param Y         Subject whose right is being revoked
     * @param R         {"r", "u", "c", "o", "d", "t"}
     * @param E_Name    The entity on which Y's right is being revoked
     * @param cascades  "N" for without cascades, "C" for with cascades
     * @return          "OK" on success, "NO" otherwise
     */
    public String revoke(String X, String Y, String R, String E_Name, String cascades)
    {
        return "";
    }

    /**
     * Queries the acm table if subject X has right R on entity E_Name.
     *
     * @param E_Name  Entity on which subject's rights are being checked
     * @param X       Subject whose right is being checked
     * @param R       {"r", "u", "c", "o", "d", "t"}
     * @return        "OK" on success, "NO" otherwise
     */
    public String checkRights(String E_Name, String X, String R)
    {
        return "";
    }
}
