package ma.enset.eventsourcingcqrsaxonframework.commands.dtos;

public record DebitAccountRequestDTO(String accountId, double amount, String currency) {
}
