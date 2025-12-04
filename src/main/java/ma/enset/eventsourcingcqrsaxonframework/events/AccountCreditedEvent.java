package ma.enset.eventsourcingcqrsaxonframework.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;
@Getter @AllArgsConstructor
public class AccountCreditedEvent {
   private String accountId;
    private double amount;
    private String currency;
}
