package it.italiandudes.enhancedeconomy.datatypes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class Currency {

    // Attributes
    @NotNull private final String currencyName;
    @NotNull private final String iso;
    private final char symbol;
    @Nullable private String creationDate;

    // Constructors
    public Currency(@NotNull final String currencyName, @NotNull final String iso, final char symbol) {
        this(currencyName, iso, symbol, null);
    }
    public Currency(@NotNull final String currencyName, @NotNull final String iso, final char symbol, @Nullable final String creationDate) {
        this.currencyName = currencyName;
        this.iso = iso;
        this.symbol = symbol;
        this.creationDate = creationDate;
    }

    // Methods
    @NotNull
    public String getCurrencyName() {
        return currencyName;
    }
    @NotNull
    public String getIso() {
        return iso;
    }
    public char getSymbol() {
        return symbol;
    }
    @Nullable
    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(@Nullable String creationDate) {
        this.creationDate = creationDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency currency = (Currency) o;

        if (getSymbol() != currency.getSymbol()) return false;
        if (!getCurrencyName().equals(currency.getCurrencyName())) return false;
        if (!getIso().equals(currency.getIso())) return false;
        return getCreationDate() != null ? getCreationDate().equals(currency.getCreationDate()) : currency.getCreationDate() == null;
    }
    @Override
    public int hashCode() {
        int result = getCurrencyName().hashCode();
        result = 31 * result + getIso().hashCode();
        result = 31 * result + (int) getSymbol();
        result = 31 * result + (getCreationDate() != null ? getCreationDate().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Currency{" +
                "currencyName='" + currencyName + '\'' +
                ", iso='" + iso + '\'' +
                ", symbol=" + symbol +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }
}
