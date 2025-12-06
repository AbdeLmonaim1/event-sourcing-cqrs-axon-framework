package ma.enset.eventsourcingcqrsaxonframework.commands.controller;

import ma.enset.eventsourcingcqrsaxonframework.commands.commands.AddAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.CreditAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.DebitAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.UpdateAccountStatusCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.dtos.AddNewAccountRequestDTO;
import ma.enset.eventsourcingcqrsaxonframework.commands.dtos.CreditAccountRequestDTO;
import ma.enset.eventsourcingcqrsaxonframework.commands.dtos.DebitAccountRequestDTO;
import ma.enset.eventsourcingcqrsaxonframework.commands.dtos.UpdateAccountStatusRequestDTO;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;

    public AccountCommandController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(@RequestBody AddNewAccountRequestDTO requestDTO) {
        //TO dispatch a command (AddAccountCommand) to Command Bus we use CommandGateway
        CompletableFuture<String> response = commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                requestDTO.initialBalance(),
                requestDTO.currency()
        ));
        return response;
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO requestDTO) {
        //TO dispatch a command (CreditAccountCommand) to Command Bus we use CommandGateway
        CompletableFuture<String> response = commandGateway.send(new CreditAccountCommand(
                requestDTO.accountId(),
                requestDTO.amount(),
                requestDTO.currency()
        ));
        return response;
    }
    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO requestDTO) {
        //TO dispatch a command (DebitAccountCommand) to Command Bus we use CommandGateway
        CompletableFuture<String> response = commandGateway.send(new DebitAccountCommand(
                requestDTO.accountId(),
                requestDTO.amount(),
                requestDTO.currency()
        ));
        return response;
    }
    @PutMapping("/updateStatus")
    public CompletableFuture<String> debitAccount(@RequestBody UpdateAccountStatusRequestDTO requestDTO) {
        //TO dispatch a command (UpdateAccountStatusCommand) to Command Bus we use CommandGateway
        CompletableFuture<String> response = commandGateway.send(new UpdateAccountStatusCommand(
                requestDTO.accountId(),
                requestDTO.status()
        ));
        return response;
    }
    //For returning exception messages in case of exceptions of any type
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e){
        return e.getMessage();
    }
    //It's just for testing purposes to check the events stored in the event store by accountId, isn't a command
    //Get events by accountId
    @GetMapping("/events/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }
}
