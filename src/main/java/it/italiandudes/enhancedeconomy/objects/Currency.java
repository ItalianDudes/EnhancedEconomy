package it.italiandudes.enhancedeconomy.objects;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Currency {

    // Attributes
    private Integer currencyID;
    private String name;
    private String iso;
    private String symbol;
    private Date creationDate;

    // Constructors
    public Currency(@NotNull final String iso) throws ModuleException, SQLException {
        String query = "SELECT * FROM currencies WHERE iso=?;";
        PreparedStatement ps = DBConnectionModule.getPreparedStatement(query);
        ps.setString(1, iso);
        ResultSet result = ps.executeQuery();

        if (result.next()) {
            currencyID = result.getInt("currency_id");
            name = result.getString("name");
            this.iso = iso;
            symbol = result.getString("symbol");
            creationDate = result.getDate("creation_date");
        }

        result.close();
    }

    // Static Methods
    public static boolean exist(@NotNull final String iso) throws ModuleException, SQLException {
        Currency currency = new Currency(iso);
        return currency.currencyID != null;
    }

    // Methods
    public Integer getCurrencyID() {
        return currencyID;
    }
    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIso() {
        return iso;
    }
    public void setIso(String iso) {
        this.iso = iso;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
        if (!(o instanceof Currency currency)) return false;

        if (getCurrencyID() != null ? !getCurrencyID().equals(currency.getCurrencyID()) : currency.getCurrencyID() != null)
            return false;
        if (getName() != null ? !getName().equals(currency.getName()) : currency.getName() != null) return false;
        if (getIso() != null ? !getIso().equals(currency.getIso()) : currency.getIso() != null) return false;
        if (getSymbol() != null ? !getSymbol().equals(currency.getSymbol()) : currency.getSymbol() != null)
            return false;
        return getCreationDate() != null ? getCreationDate().equals(currency.getCreationDate()) : currency.getCreationDate() == null;
    }
    @Override
    public int hashCode() {
        int result = getCurrencyID() != null ? getCurrencyID().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getIso() != null ? getIso().hashCode() : 0);
        result = 31 * result + (getSymbol() != null ? getSymbol().hashCode() : 0);
        result = 31 * result + (getCreationDate() != null ? getCreationDate().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Currency{" +
                "currencyID=" + currencyID +
                ", name='" + name + '\'' +
                ", iso='" + iso + '\'' +
                ", symbol='" + symbol + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
