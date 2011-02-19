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
    //define table names
    public static final String acm = "acm";
    public static final String entityTable = "entityTable";
    ///define columns in acm
    public static final String subject = acm+".subject";
    public static final String entity = acm+".entity";
    public static final String right = acm+".right";
    public static final String granter = acm+".granter";
    public static final String timestamp = acm+".timestamp";
    //define columns in entityTable
    public static final String entityID = entityTable + ".entityID";
    public static final String entityName = entityTable + ".entityName";
    public static final String subjectOrObject = entityTable + ".subject_or_object";
    //define other constants
    public static final String subject0 = "subject0";

    public LCTAuthPolicyManager467(Connection connArg, String dbmsArg, String dbNameArg)
    {
        super();
        this.con = connArg;
        this.dbms = dbmsArg;
        this.dbName = dbNameArg;
        try
        {
            String query = "USE " + dbName;
            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            stmt.execute(query);
            con.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Creates a new subject.  Initially, subject "subject0" is the "owner" of all new entities.
     *
     * @param subjectName Name of new subject
     */
    public void newSubject(String subjectName)
    {
        Statement stmt = null;
        String query = "SELECT * FROM " + dbName + "." + entityTable + " WHERE " + entityName + "= '" + subjectName + "'";

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
                    rs.updateString(entityName, subjectName);
                    rs.updateString(subjectOrObject, "1");
                    rs.insertRow();
                    rs.close();

                    query = "INSERT INTO " + dbName + "." + acm + " (`subject`, `entity`, `granter`, `right`) VALUES ('subject0', '" + subjectName + "', 'subject0', 'o')";
                    stmt.executeUpdate(query);
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
     * Creates a new object.  Initially, subject "subject0" is the "owner" of all new entities.
     *
     * @param objectName Name of new object
     */
    public void newObject(String objectName)
    {
        Statement stmt = null;
        String query = "SELECT * FROM " + dbName + "." + entityTable + " WHERE " + entityName + "= '" + objectName + "'";

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
                    rs.updateString(entityName, objectName);
                    rs.updateString(subjectOrObject, "0");
                    rs.insertRow();
                    rs.close();

                    query = "INSERT INTO " + dbName + "." + acm + " (`subject`, `entity`, `granter`, `right`) VALUES ('subject0', '" + objectName + "', 'subject0', 'o')";
                    stmt.executeUpdate(query);
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

        String query = "SELECT * FROM " + dbName + "." + entityTable + " WHERE " + entityName + "= '" + Name + "'";

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(query);

            //Precondition: Subject exist in database
            //This also doesn't allow subject0 to be removed form the DB.
            if (!rs.next() || Name.equals(subject0))
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

        String query = "SELECT * FROM " + dbName + "." + entityTable + " WHERE " + entityName + " = '" + Name + "'" + " AND " + subjectOrObject + " <> 1";

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(query);

            //Precondition: Object exist in database
            //Even though subject0 should be considered a subject_or_object which
            //would not allow for his removale by this function it is also checked here just in case.
            if (!rs.next() || Name.equals(subject0))
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
     * Subject granterName grants subject granteeName right righToBeGranted  on the entityName.
     *
     * @param granterName       Subject granting the right
     * @param granteeName       Subject being granted the right
     * @param righToBeGranted      {"r", "u", "c", "o", "d", "t"}
     * @param entityName  The entity which subject Y will have rights
     * @return        "OK" on success, "NO" otherwise
     */
    public String grant(String granterGranting, String grantee, String rightToBeGranted, String entityGrantedOn)
    {
        String returnString = "NO"; //start pessimistic
        String rightName;//string to hold the database name for the rights
        if(rightToBeGranted.length()!=1)//should only pass one character in the right String
        {
            return "NO";
        }
        //begin preparing the query database
        try
        {
            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            // look up the grantee's rights
            if(checkRights(entityGrantedOn,granterGranting,"o").equals("OK") ||//if the granter is the owner OR the granter has the rights and has the copy right
                    checkRights(entityGrantedOn,granterGranting,rightToBeGranted).equals("OK") && checkRights(entityGrantedOn,granterGranting,"c").equals("OK"))
            {
                String query = "INSERT INTO " + acm + " (" + subject + " ," + entity + " ," +  right + " ," + granter + ")" +
                        " VALUES ('" + grantee + "', '" + entityGrantedOn + "', '" + rightToBeGranted +"', '"+granterGranting + "');";
                stmt.execute(query);
            ///INSERT INTO acm (subject,entity,right,granter) VALUES (grantee,entityGrantedOn,right,granter)
            }
            returnString = "YES";
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return returnString;

    }

    /**
     * Subject X gets right R on the entityToTakeRightsOn by "taking" it from
     * subject Y if subject Y has given them the appropriate take right.  Subject Y's
     * rights are unmodified.
     *
     * @param subjectTaking       Subject taking the right
     * @param rightToTake       {"r", "u", "c", "o", "d", "t"}
     * @param entityToTakeRightsOn  The entity which subject Y will have rights
     * @return        "OK" on success, "NO" otherwise
     */
    public String take(String subjectTaking, String rightToTake, String entityToTakeRightsOn)
    {
        String returnString = "NO";//start pessimistic
        try
        {
        Statement stmt = con.createStatement();
        String query = "INSERT INTO " + acm + " (" + subject + ", " + entity + ", " +  right + ", " + granter + ")" +
                        " VALUES ('" + subjectTaking + "', '" + entityToTakeRightsOn + "', '" + rightToTake +"', '"+subjectTaking + "');";

            switch(rightToTake.charAt(0))
            {
                case 'r':
                case 'u':
                {
                    if("OK".equals(checkRights(entityToTakeRightsOn, subjectTaking, "t")))
                    {
                        stmt.execute(query);
                        returnString = "OK";
                    }
                }break;
                case 'c':
                {
                    if("OK".equals(checkRights(entityToTakeRightsOn, subjectTaking, "d")))
                    {
                        stmt.execute(query);
                        returnString = "OK";
                    }
                }break;
                default:break;

            }
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return returnString;
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
        String rtnStr = "NO";

        String revoker = X;
        String revokee = Y;

        if (revokee.equals(subject0))
        {
            System.out.println("ERROR: Cannot revoke subject0's rights");
        }
        else
        {
            Statement stmt = null;
            String queryOwner = "SELECT * FROM " + dbName + "." + acm + " WHERE " + subject + " = '" + revoker + "' AND " + entity + " = '" + E_Name + "' AND " + right + " = 'o'";
            String queryRights = "SELECT * FROM " + dbName + "." + acm + " WHERE " + subject + " = '" + revokee + "' AND " + entity + " = '" + E_Name + "' AND " + right + " = 'r'";

            try
            {
                stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                ResultSet rsOwner = stmt.executeQuery(queryOwner);

                if (rsOwner.next())
                {
                    ResultSet rsRights = stmt.executeQuery(queryRights);

                    if (rsRights.next())
                    {
                        if (cascades.equals("N"))
                        {
                            do
                            {
                                rsRights.deleteRow();
                            }
                            while (rsRights.next());

                            rtnStr = "OK";
                        }
                        else if (cascades.equals("C"))
                        {
                            if (R.equals("o") && revoker.equals(subject0))
                            {
                                //revoke the revokee's ownership
                                do
                                {
                                    rsRights.deleteRow();
                                }
                                while (rsRights.next());

                                //revoke any other rights on the specified entity where the granter was not subject0
                                queryRights = "SELECT * FROM " + dbName + "." + acm + 
                                            " WHERE " + granter + " <> '" + subject0 +
                                            "' AND " + entity + " = '" + E_Name + "'";
                                rsRights = stmt.executeQuery(queryRights);

                                if (rsRights.next())
                                {
                                    do
                                    {
                                        rsRights.deleteRow();
                                    }
                                    while (rsRights.next());
                                }

                                rtnStr = "OK";
                            }
                            else if (R.equals("c"))
                            {
                                //revoke the revokee's copy right
                                do
                                {
                                    rsRights.deleteRow();
                                }
                                while (rsRights.next());

                                //revoke any 'd', 'r', 'u', 't' where revokee is the granter for the specified entity and the subject is not the revokee
                                queryRights = "SELECT * FROM " + dbName + "." + acm +
                                            " WHERE " + subject + " <> " + revokee +
                                            " AND " + granter + " = '" + revokee +
                                            "' AND " + entity + " = '" + E_Name + "'";
                                rsRights = stmt.executeQuery(queryRights);

                                if (rsRights.next())
                                {
                                    do
                                    {
                                        this.revoke(revokee, rsRights.getString(subject), rsRights.getString(right), rsRights.getString(entity), "C");
                                    }
                                    while (rsRights.next());
                                }

                                rtnStr = "OK";
                            }
                            else if (R.equals("t"))
                            {
                                //revoke the revokee's 't' right
                                do
                                {
                                    rsRights.deleteRow();
                                }
                                while (rsRights.next());

                                //revoke any 'r', 'u' where revokee is the granter for the specified entity and the subject is the revokee
                                queryRights = "SELECT * FROM " + dbName + "." + acm +
                                            " WHERE " + subject + " = " + revokee +
                                            " AND " + granter + " = '" + revokee +
                                            "' AND " + entity + " = '" + E_Name +
                                            "' AND (" + right + " = '" + "r" +
                                            "' OR " + right + " = '" + "u" + "')";
                                rsRights = stmt.executeQuery(queryRights);

                                if (rsRights.next())
                                {
                                    do
                                    {
                                        rsRights.deleteRow();
                                    }
                                    while (rsRights.next());
                                }
                            }
                            else if (R.equals("d"))
                            {
                                //revoke the revokee's 'd' right
                                do
                                {
                                    rsRights.deleteRow();
                                }
                                while (rsRights.next());

                                //revoke the revokee's 'c' right where revokee is the granter for the specified entity and the subject is the revokee
                                queryRights = "SELECT * FROM " + dbName + "." + acm +
                                            " WHERE " + subject + " <> " + revokee +
                                            " AND " + granter + " = '" + revokee +
                                            "' AND " + entity + " = '" + E_Name +
                                            "' AND " + right + " = 'c'";
                                rsRights = stmt.executeQuery(queryRights);

                                if (rsRights.next())
                                {
                                    do
                                    {
                                        this.revoke(revokee, rsRights.getString(subject), rsRights.getString(right), rsRights.getString(entity), "C");
                                    }
                                    while (rsRights.next());
                                }

                                rtnStr = "OK";
                            }
                            else
                            {
                                do
                                {
                                    rsRights.deleteRow();
                                }
                                while (rsRights.next());

                                rtnStr = "OK";
                            }
                        }
                        else
                        {
                            System.out.println("Incorrect cascade parameter");
                        }
                    }

                    rsRights.close();
                }

                rsOwner.close();
                stmt.close();
            }
            catch (SQLException e)
            {
                System.out.println(e);
            }
        }

        return rtnStr;
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
        String rtnStr = "NO";

        Statement stmt = null;
        String query = "SELECT " + subject + ", " + entity + ", " + right + " FROM " + dbName + "." + acm + " WHERE " + subject + " = '" + X + "' AND " + entity + " = '" + E_Name + "' AND " + right + " = '" + R + "'";

        try
        {
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next())
            {
                rtnStr = "OK";
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return rtnStr;
    }
}
