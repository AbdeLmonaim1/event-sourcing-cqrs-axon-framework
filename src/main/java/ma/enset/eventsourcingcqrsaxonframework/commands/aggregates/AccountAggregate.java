package ma.enset.eventsourcingcqrsaxonframework.commands.aggregates;

import ma.enset.eventsourcingcqrsaxonframework.commands.commands.AddAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;
import org.axonframework.commandhandling.CommandHandler;
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
    }
}
