package objectprotocol;

import domain.Account;

import java.util.Optional;

public class LoginResponse implements Response {
    private final Account account;

    public LoginResponse(Optional<Account> account) {
        this.account = account.orElse(null);
    }

    public Optional<Account> getEmployee() {
        return Optional.ofNullable(account);
    }

}
