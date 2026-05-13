package ca.sheridancollege.bakerdam.authsystemapi.dto.request;

import ca.sheridancollege.bakerdam.authsystemapi.entity.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleRequest {
    @NotNull
    private Role role;
}
