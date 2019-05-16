package z.ivan.service.impl;

import z.ivan.model.entity.Account;
import java.util.Collection;

class SumAccounts {

    public static Long sumAccounts(Collection<Account> accounts) {
        return accounts.stream().map(Account::getAmount).reduce(0L, Long::sum);
    }
}
