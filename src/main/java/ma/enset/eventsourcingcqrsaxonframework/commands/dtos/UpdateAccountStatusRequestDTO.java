package ma.enset.eventsourcingcqrsaxonframework.commands.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.enset.eventsourcingcqrsaxonframework.enums.AccountStatus;

import java.io.Serializable;


public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus status) {
}
