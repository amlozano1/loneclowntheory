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

        boolean cascade = cascades.equals("C");

        if (Y.equals(subject0))
        {
            rtnStr = "NO";
        }
        else
        {
            if (this.checkRights(E_Name, X, own).equals("OK"))
            {
                String query = "SELECT * FROM " + dbName + "." + acm
                        + " WHERE " + subject + " = '" + Y
                        + "' AND " + entity + " = '" + E_Name
                        + "' AND " + right + " = '" + R + "'";

                try
                {
                    Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                    ;
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next())
                    {
                        if (R.equals(own) && X.equals(subject0))
                        {
                            // Revoke ownership from revokee on the given entity
                            do
                            {
                                rs.deleteRow();
                            }
                            while (rs.next());

                            if (cascade)
                            {
                                // Revoke any rights granted by Y on the given entity
                                query = "SELECT * FROM " + dbName + "." + acm
                                        + " WHERE " + entity + " = '" + E_Name
                                        + "' AND " + granter + " = '" + Y + "'";

                                rs = stmt.executeQuery(query);

                                if (rs.next())
                                {
                                    do
                                    {
                                        this.revoke(Y, rs.getString(subject), rs.getString(right), E_Name, cascades);
                                    }
                                    while (rs.next());
                                }
                            }

                            rtnStr = "OK";
                        }
                        else if (R.equals(copy))
                        {
                            // Revoke any 'c' from revokee on the entity
                            do
                            {
                                rs.deleteRow();
                            }
                            while (rs.next());

                            if (cascade)
                            {
                                // Revoke any 'd', 't', 'r', 'u' granted by Y on the given entity
                                query = "SELECT * FROM " + dbName + "." + acm
                                        + " WHERE " + entity + " = '" + E_Name
                                        + "' AND " + granter + " = '" + Y
                                        + "' AND (" + right + " = '" + takeCopy
                                        + "' OR " + right + " = '" + takeReadUpdate
                                        + "' OR " + right + " = '" + read
                                        + "' OR " + right + " = '" + update + "')";

                                rs = stmt.executeQuery(query);

                                if (rs.next())
                                {
                                    do
                                    {
                                        this.cascadeRevoke(Y, rs.getString(subject), rs.getString(right), E_Name);
                                    }
                                    while (rs.next());
                                }
                            }

                            rtnStr = "OK";
                        }
                        else if (R.equals(takeCopy))
                        {
                            // Revoke any 'd' from revokee on the given entity
                            do
                            {
                                rs.deleteRow();
                            }
                            while (rs.next());

                            // Revoke any 'c' from revokee on the given entity
                            if (cascade)
                            {
                                this.revoke(X, Y, copy, E_Name, cascades);
                            }

                            rtnStr = "OK";
                        }
                        else if (R.equals(takeReadUpdate))
                        {
                            // Revoke any 't' from revokee on the given entity
                            do
                            {
                                rs.deleteRow();
                            }
                            while (rs.next());

                            if (cascade)
                            {
                                this.revoke(X, Y, read, E_Name, cascades);
                                this.revoke(X, Y, update, E_Name, cascades);
                            }

                            rtnStr = "OK";
                        }
                        else if (R.equals(read))
                        {
                            // Revoke any 'r' from revokee on the given entity
                            do
                            {
                                rs.deleteRow();
                            }
                            while (rs.next());

                            rtnStr = "OK";
                        }
                        else if (R.equals(update))
                        {
                            // Revoke any 'u' from revokee on the given entity
                            do
                            {
                                rs.deleteRow();
                            }
                            while (rs.next());

                            rtnStr = "OK";
                        }
                        else
                        {
                            rtnStr = "NO";
                        }
                    }
                    else
                    {
                        rtnStr = "NO";
                    }

                    rs.close();
                    stmt.close();
                }
                catch (SQLException e)
                {
                    System.out.println(e);
                    rtnStr = "NO";
                }
            }
            else
            {
                rtnStr = "NO";
            }
        }

        return rtnStr;
    }

    /**
     * Revoker revokes right R on the entity E_Name from revokee.
     *
     * @param revoker   Revoker of the right
     * @param revokee   Subject whose right is being revoked
     * @param R         {"r", "u", "c", "o", "d", "t"}
     * @param E_Name    The entity on which the revokee's right is being revoked
     * @return          "OK" on success, "NO" otherwise
     */
    private String cascadeRevoke(String revoker, String revokee, String R, String E_Name)
    {
        String rtnStr = "NO";
        String query = "SELECT * FROM " + dbName + "." + acm
                + " WHERE " + subject + " = '" + revokee
                + "' AND " + entity + " = '" + E_Name
                + "' AND " + right + " = '" + R
                + "' AND " + granter + " = '" + revoker + "'";

        try
        {
            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next())
            {
                if (R.equals(copy))
                {
                    String copyTimeStamp = rs.getString(timestamp);
                    String nextCopyTimeStamp = null;

                    Date copyGranted = Date.valueOf(copyTimeStamp);
                    Date nextCopyGranted = null;

                    // Revoke 'c' from revokee on the given entity
                    do
                    {
                        rs.deleteRow();
                    }
                    while (rs.next());

                    query = "SELECT * FROM " + dbName + "." + acm
                            + " WHERE " + subject + " = '" + revokee
                            + "' AND " + entity + " = '" + E_Name
                            + "' AND " + right + " = '" + copy
                            + "' AND " + granter + " <> '" + revoker
                            + "' AND " + timestamp + " > " + copyTimeStamp;

                    rs = stmt.executeQuery(query);

                    if (rs.next())
                    {
                        nextCopyTimeStamp = rs.getString(timestamp);
                        nextCopyGranted = Date.valueOf(nextCopyTimeStamp);

                        while (rs.next())
                        {
                            if (Date.valueOf(rs.getString(timestamp)).before(nextCopyGranted))
                            {
                                nextCopyTimeStamp = rs.getString(timestamp);
                                nextCopyGranted = Date.valueOf(nextCopyTimeStamp);
                            }
                        }
                    }

                    query = "SELECT * FROM " + dbName + "." + acm
                            + " WHERE " + entity + " = '" + E_Name
                            + "' AND " + granter + " <> '" + revokee
                            + "' AND " + timestamp + " > " + copyTimeStamp
                            + "' AND " + timestamp + " < " + nextCopyTimeStamp;

                    while (rs.next())
                    {
                        this.cascadeRevoke(revokee, rs.getString(subject), rs.getString(right), E_Name);
                    }

                    rtnStr = "OK";
                }
                else if (R.equals(takeCopy))
                {
                    // Revoke 'd' from revokee on the given entity
                    do
                    {
                        rs.deleteRow();
                    }
                    while (rs.next());

                    // Revoke 'c' from revokee where granter is revoker
                    this.cascadeRevoke(revoker, revokee, copy, E_Name);

                    rtnStr = "OK";
                }
                else if (R.equals(takeReadUpdate))
                {
                    // Revoke 't' from revokee on the given entity
                    do
                    {
                        rs.deleteRow();
                    }
                    while (rs.next());

                    // Revoke 'r' and 'u' from revokee where granter is revoker
                    this.cascadeRevoke(revoker, revokee, read, E_Name);
                    this.cascadeRevoke(revoker, revokee, update, E_Name);

                    rtnStr = "OK";
                }
                else if (R.equals(read))
                {
                    // Revoke 'r' from revokee on the given entity
                    do
                    {
                        rs.deleteRow();
                    }
                    while (rs.next());

                    rtnStr = "OK";
                }
                else if (R.equals(update))
                {
                    // Revoke 'u' from revokee on the given entity
                    do
                    {
                        rs.deleteRow();
                    }
                    while (rs.next());

                    rtnStr = "OK";
                }
                else
                {
                    rtnStr = "NO";
                }
            }
            else
            {
                rtnStr = "NO";
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(e);
            rtnStr = "NO";
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
            rtnStr = "NO";
        }

        return rtnStr;
    }
}
