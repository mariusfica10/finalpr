package domain;

import java.util.Objects;

public class Account extends Entity<Integer>{

    private final String username;
    private final String password;

    public Account(Integer id, String username, String password)
    {
        super(id);
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password)
    {
        super(0);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(getUsername(), account.getUsername()) &&
                Objects.equals(getPassword(), account.getPassword());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
