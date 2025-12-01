package ma.enset.eventsourcingcqrsaxonframework.commands.aggregates;

import ma.enset.eventsourcingcqrsaxonframework.commands.commands.AddAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;
import ma.enset.eventsourcingcqrsaxonframework.events.AccountCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    private String accountId;
    private double balance;
    private AccountStatus status;

    //For Axon Framework
    public AccountAggregate(){}

    //For handling command
    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        //here we verify the command data (business logic)
        if (command.getInitialBalance()<=0) throw new IllegalArgumentException("Initial balance must be greater than zero");
        //everything is ok! so now we can apply event (we must create event first)
        AggregateLifecycle.apply(new AccountCreatedEvent(
           command.getId(),
           command.getInitialBalance(),
                AccountStatus.CREATED,
              command.getCurrency()
        ));
    }
    //for handling event and updating the aggregate state
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId= event.getAccountId();
        this.balance= event.getInitialBalance();
        this.status= event.getAccountStatus();
    }
}
