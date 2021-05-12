package interfaces;

import domain.Account;

import java.util.Optional;

public interface IAccountRepository extends IRepository<Integer, Account> {
    Optional<Account> findForLogin(String username, String password);
}
