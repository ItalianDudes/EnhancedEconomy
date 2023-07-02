package it.italiandudes.enhancedeconomy.objects;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Country {

    // Attributes
    private Integer countryID;
    private String name;
    private Date creationDate;

    // Constructors
    public Country(@NotNull final String name) throws ModuleException, SQLException {
        String query = "SELECT * FROM countries WHERE name=?;";
        PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
        ps.setString(1, name);
        ResultSet result = ps.executeQuery();

        if (result.next()) {
            countryID = result.getInt("country_id");
            this.name = name;
            creationDate = result.getDate("creation_date");
        }
    }

    // Static Methods
    public static boolean exist(@NotNull final String name) throws ModuleException, SQLException {
        Country country = new Country(name);
        return country.countryID != null;
    }

    // Methods
    public Integer getCountryID() {
        return countryID;
    }
    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
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
        if (!(o instanceof Country country)) return false;

        if (getCountryID() != null ? !getCountryID().equals(country.getCountryID()) : country.getCountryID() != null)
            return false;
        if (getName() != null ? !getName().equals(country.getName()) : country.getName() != null) return false;
        return getCreationDate() != null ? getCreationDate().equals(country.getCreationDate()) : country.getCreationDate() == null;
    }
    @Override
    public int hashCode() {
        int result = getCountryID() != null ? getCountryID().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getCreationDate() != null ? getCreationDate().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Country{" +
                "countryID=" + countryID +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
