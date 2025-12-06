package ma.enset.eventsourcingcqrsaxonframework.commands.aggregates;

import lombok.extern.slf4j.Slf4j;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.AddAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.CreditAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.DebitAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.UpdateAccountStatusCommand;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;
import ma.enset.eventsourcingcqrsaxonframework.events.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private AccountStatus status;

    //For Axon Framework
    public AccountAggregate(){}

    //For handling command
    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        log.info("Handling AddAccountCommand: {}", command);
        //here we verify the command data (business logic)
        if (command.getInitialBalance()<=0) throw new IllegalArgumentException("Initial balance must be greater than zero");
        //everything is ok! so now we can apply event (we must create event first)
        AggregateLifecycle.apply(new AccountCreatedEvent(
           command.getId(),
           command.getInitialBalance(),
                AccountStatus.CREATED,
              command.getCurrency()
        ));
        //An other event - activate account
        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED
        ));
    }
    //for handling event and updating the aggregate state
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        log.info("Handling AccountCreatedEvent: {}", event);
        this.accountId= event.getAccountId();
        this.balance= event.getInitialBalance();
        this.status= event.getAccountStatus();
    }
    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        log.info("Handling AccountCreatedEvent: {}", event);
        this.accountId= event.getAccountId();
        this.status= event.getStatus();
    }

    //For handling command
    @CommandHandler
    public void handleCreditCommand(CreditAccountCommand command) {
        log.info("Handling CreditAccountCommand: {}", command);
        //here we verify the command data (business logic)
        if (!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account "+command.getId()+" is not ACTIVATED");
        if (command.getAmount()<=0) throw new IllegalArgumentException("The amount must be greater than zero");
        //everything is ok! so now we can apply event (we must create event first)
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }
    //for handling event and updating the aggregate state
    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        log.info("Handling AccountCreditedEvent: {}", event);
        this.accountId= event.getAccountId();
        this.balance= this.balance + event.getAmount();
    }
    @CommandHandler
    public void handleDebitCommand(DebitAccountCommand command) {
        log.info("Handling DebitAccountCommand: {}", command);
        //here we verify the command data (business logic)
        if (!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account "+command.getId()+" is not ACTIVATED");
        if (command.getAmount()<=0) throw new IllegalArgumentException("The amount must be greater than zero");
        if (command.getAmount() > this.balance) throw new RuntimeException("Insufficient balance");
        //everything is ok! so now we can apply event (we must create event first)
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        log.info("Handling AccountDebitedEvent: {}", event);
        this.accountId= event.getAccountId();
        this.balance= this.balance - event.getAmount();
    }

    @CommandHandler
    public void handleUpdateAccountStatusCommand(UpdateAccountStatusCommand command) {
        log.info("Handling UpdateAccountStatusCommand: {}", command);
        //here we verify the command data (business logic)
        if (this.status.equals(command.getStatus())) throw new RuntimeException("The account "+command.getId()+" is already in status "+command.getStatus());
        //everything is ok! so now we can apply event (we must create event first)
        AggregateLifecycle.apply(new AccountChangedStatusEvent(
                command.getId(),
                command.getStatus()
        ));
    }
    @EventSourcingHandler
    public void on(AccountChangedStatusEvent event){
        log.info("Handling AccountChangedStatusEvent: {}", event);
        this.accountId= event.getAccountId();
        this.status = event.getStatus();
    }
}
