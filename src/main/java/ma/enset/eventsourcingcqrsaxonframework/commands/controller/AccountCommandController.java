package ma.enset.eventsourcingcqrsaxonframework.commands.controller;

import ma.enset.eventsourcingcqrsaxonframework.commands.commands.AddAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.commands.CreditAccountCommand;
import ma.enset.eventsourcingcqrsaxonframework.commands.dtos.AddNewAccountRequestDTO;
import ma.enset.eventsourcingcqrsaxonframework.commands.dtos.CreditAccountRequestDTO;
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
    public CompletableFuture<String> addNewAccount(@RequestBody CreditAccountRequestDTO requestDTO) {
        //TO dispatch a command (CreditAccountCommand) to Command Bus we use CommandGateway
        CompletableFuture<String> response = commandGateway.send(new CreditAccountCommand(
                requestDTO.accountId(),
                requestDTO.amount(),
                requestDTO.currency()
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
