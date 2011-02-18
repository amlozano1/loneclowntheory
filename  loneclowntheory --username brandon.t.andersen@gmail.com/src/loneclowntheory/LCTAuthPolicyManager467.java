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
    //define columns in acm
    public static final String subject = acm + ".subject";
    public static final String entity = acm + ".entity";
    public static final String granter = acm + ".granter";
    public static final String right = acm + ".right";
    public static final String timestamp = acm + ".timestamp";
    //define rights for acm
    public static final String read = "r";
    public static final String update = "u";
    public static final String own = "o";
    public static final String copy = "c";
    public static final String takeReadUpdate = "t";
    public static final String takeCopy = "d";
    //define columns in entityTable
    public static final String entityID = entityTable + ".entityID";
    public static final String entityName = entityTable + ".entityName";
    public static final String subjectOrObject = entityTable + ".subject_or_object";

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

        String query = "SELECT * FROM " + dbName + "." + entityTable + " WHERE " + entityName + " = '" + Name + "'" + " AND " + subjectOrObject + " <> 1";

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
     * @param granterName       Subject granting the right
     * @param granteeName       Subject being granted the right
     * @param right       {"r", "u", "c", "o", "d", "t"}
     * @param entityName  The entity which subject Y will have rights
     * @return        "OK" on success, "NO" otherwise
     */
    public String grant(String granter, String grantee, String right, String entity)
    {
        String returnString = "NO"; //start pessimistic
        String rightName;//string to hold the database name for the rights
        if (right.length() != 1)//should only pass one character in the right String
        {
            return "NO";
        }
        switch (right.charAt(0))
        {
            case 'r':
                rightName = read;
                break;
            case 'u':
                rightName = update;
                break;
            case 'c':
                rightName = copy;
                break;
            case 'o':
                rightName = own;
                break;
            case 'e':
                rightName = takeCopy;
                break;
            case 'd':
                rightName = takeReadUpdate;
                break;
            default:
                return returnString;
        }

        //begin preparing the query database
        try
        {
            int grantersID, granteesID, entitysID;
            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            //get the granterID
            String query = "SELECT " + entityID + " FROM " + entityTable + " WHERE " + entityName + "='" + granter + "';";
            ResultSet granterName = stmt.executeQuery(query);
            if (granterName.next())
            {
                grantersID = granterName.getInt(entityID);
            }
            else//granter does not exist so...
            {
                return returnString;//return NO or alternatively could throw an exception
            }
            //get the granteeID
            query = "SELECT " + entityID + " FROM " + entityTable + " WHERE " + entityName + "='" + grantee + "';";
            ResultSet granteeName = stmt.executeQuery(query);
            if (granteeName.next())
            {
                granteesID = granteeName.getInt(entityID);
            }
            else//grantee does not exist so...
            {
                return returnString; //return NO or alternatively could throw an exception
            }
            //get the entityID
            query = "SELECT " + entityID + " FROM " + entityTable + " WHERE " + entityName + "='" + entity + "';";
            ResultSet entitysName = stmt.executeQuery(query);
            if (entitysName.next())
            {
                entitysID = entitysName.getInt(entityID);
            }
            else//entity does not exist so...
            {
                return returnString;//return NO or alternatively could throw an exception
            }
            //first look up the grantee's rights
            query = "SELECT " + own + "," + copy + " FROM " + dbName + "." + acm + " WHERE " + subject + "='" + grantersID + "'" + " AND object='" + entitysID + "';";
            boolean granterCanGrant = false;
            ResultSet grantersRights = stmt.executeQuery(query);
            if (grantersRights.next())
            {
                granterCanGrant = grantersRights.getBoolean(own) || grantersRights.getBoolean(copy);
                if (granterCanGrant)//if the granter can grant
                {
                    if (rightName.equals(own))//if the right granter is granting is ownership we must check to make sure it is not owned by anyone or only subject0 owns it
                    {

                        query = "SELECT" + subject + " FROM " + acm + " WHERE " + entity + "=" + entitysID + " AND " + own + "=" + "1;";
                        ///SELECT subject FROM acm WHERE object = entitysID AND own = 1;
                        ResultSet ownersOfEntity = stmt.executeQuery(query);//get all owners of the object
                        while (ownersOfEntity.next())//for each owner in the table
                        {
                            if (ownersOfEntity.getInt(subject) != 1)//if the owner is not subject zero
                            {
                                return returnString;//there is another owner present, the operation fails
                            }
                        }
                    }//if we make it out of that if and while loop we are all clear to grant the rights to grantee
                    query = "INSERT INTO" + acm + "(" + subject + "," + entity + "," + rightName + ") VALUES (" + granteesID + "," + entitysID + "," + 1 + "ON DUPLICATE KEY UPDATE " + rightName + " = 1;";
                    ///INSERT INTO acm (subject,object,rightName) VALUES (granteesID,entitysID,1) ON DUPLICATE KEY UPDATE rightName=1
                    returnString = "YES";
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return returnString;

    }

    /**
     * Subject X gets right R on the entity E_Name by "taking" it from subject Y.  Subject Y's
     * rights are unmodified.
     *
     * @param X       Subject taking the right
     * @param R       {"r", "u", "c", "o", "d", "t"}
     * @param E_Name  The entity which subject Y will have rights
     * @return        "OK" on success, "NO" otherwise
     */
    public String take(String X, String R, String E_Name)
    {
        throw new UnsupportedOperationException("Not supported yet.");
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
        String rtnStr = this.checkRights(E_Name, Y, R);

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
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return rtnStr;
    }
}
