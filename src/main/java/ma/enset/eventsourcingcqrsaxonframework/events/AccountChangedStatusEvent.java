package ma.enset.eventsourcingcqrsaxonframework.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;

@Getter @AllArgsConstructor
public class AccountChangedStatusEvent {
    private String accountId;
    private AccountStatus status;
}
