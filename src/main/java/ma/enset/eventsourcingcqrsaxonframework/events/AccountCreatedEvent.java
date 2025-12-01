package ma.enset.eventsourcingcqrsaxonframework.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;
@Getter @AllArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    private double initialBalance;
    private AccountStatus accountStatus;
    private String currency;
}
