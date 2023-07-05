package it.italiandudes.enhancedeconomy.objects;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class User {

    // Attributes
    private final Integer userID;
    private String name;
    private Date creationDate;

    // Constructors
    public User(final String name) throws ModuleException, SQLException {
        if (name == null) {
            this.userID = null;
        } else {
            String query = "SELECT * FROM users WHERE name=?;";
            PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                userID = result.getInt("user_id");
                this.name = name;
                creationDate = result.getDate("creation_date");
            } else {
                this.userID = null;
            }

            result.close();
        }
    }
    public User(final int userID) throws ModuleException, SQLException {
        String query = "SELECT * FROM users WHERE user_id=?;";
        PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
        ps.setInt(1, userID);
        ResultSet result = ps.executeQuery();

        if (result.next()) {
            this.userID = userID;
            name = result.getString("name");
            creationDate = result.getDate("creation_date");
        } else {
            this.userID = null;
        }

        result.close();
    }

    // Static Methods
    public static boolean exist(@NotNull final String name) throws ModuleException, SQLException {
        User user = new User(name);
        return user.userID != null;
    }
    public static boolean exist(final int userID) throws ModuleException, SQLException {
        User user = new User(userID);
        return user.userID != null;
    }

    // Methods
    public Integer getUserID() {
        return userID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        if (getUserID() != null ? !getUserID().equals(user.getUserID()) : user.getUserID() != null) return false;
        if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) return false;
        return getCreationDate() != null ? getCreationDate().equals(user.getCreationDate()) : user.getCreationDate() == null;
    }
    @Override
    public int hashCode() {
        int result = getUserID() != null ? getUserID().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getCreationDate() != null ? getCreationDate().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
