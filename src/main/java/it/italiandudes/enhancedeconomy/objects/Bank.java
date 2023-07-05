package it.italiandudes.enhancedeconomy.objects;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Bank {

    // Attributes
    private final Integer bankID;
    private String name;
    private Country headquarterCountry;
    private User owner;
    private Date creationDate;

    // Constructors
    public Bank(@NotNull final String name) throws ModuleException, SQLException {
        String query = "SELECT * FROM banks WHERE name=?;";
        PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
        ps.setString(1, name);
        ResultSet result = ps.executeQuery();

        if (result.next()) {
            bankID = result.getInt("bank_id");
            this.name = name;
            headquarterCountry = new Country(result.getInt("headquarter_country"));
            owner = new User(result.getInt("owner_id"));
            creationDate = result.getDate("creation_date");
        } else {
            this.bankID = null;
        }

        result.close();
    }
    public Bank(final int bankID) throws ModuleException, SQLException {
        String query = "SELECT * FROM banks WHERE bank_id=?;";
        PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
        ps.setInt(1, bankID);
        ResultSet result = ps.executeQuery();

        if (result.next()) {
            this.bankID = bankID;
            name = result.getString("name");
            headquarterCountry = new Country(result.getInt("headquarter_country"));
            owner = new User(result.getInt("owner_id"));
            creationDate = result.getDate("creation_date");
        } else {
            this.bankID = null;
        }

        result.close();
    }

    // Static Methods
    public static boolean exist(@NotNull final String name) throws ModuleException, SQLException {
        Bank bank = new Bank(name);
        return bank.bankID!=null;
    }
    public static boolean exist(final int bankID) throws ModuleException, SQLException {
        Bank bank = new Bank(bankID);
        return bank.bankID!=null;
    }

    // Methods
    public Integer getBankID() {
        return bankID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Country getHeadquarterCountry() {
        return headquarterCountry;
    }
    public void setHeadquarterCountry(Country headquarterCountry) {
        this.headquarterCountry = headquarterCountry;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
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
        if (!(o instanceof Bank bank)) return false;

        if (getBankID() != null ? !getBankID().equals(bank.getBankID()) : bank.getBankID() != null) return false;
        if (getName() != null ? !getName().equals(bank.getName()) : bank.getName() != null) return false;
        if (getHeadquarterCountry() != null ? !getHeadquarterCountry().equals(bank.getHeadquarterCountry()) : bank.getHeadquarterCountry() != null)
            return false;
        if (getOwner() != null ? !getOwner().equals(bank.getOwner()) : bank.getOwner() != null) return false;
        return getCreationDate() != null ? getCreationDate().equals(bank.getCreationDate()) : bank.getCreationDate() == null;
    }
    @Override
    public int hashCode() {
        int result = getBankID() != null ? getBankID().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getHeadquarterCountry() != null ? getHeadquarterCountry().hashCode() : 0);
        result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
        result = 31 * result + (getCreationDate() != null ? getCreationDate().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Bank{" +
                "bankID=" + bankID +
                ", name='" + name + '\'' +
                ", headquarterCountry=" + headquarterCountry +
                ", owner=" + owner +
                ", creationDate=" + creationDate +
                '}';
    }
}
