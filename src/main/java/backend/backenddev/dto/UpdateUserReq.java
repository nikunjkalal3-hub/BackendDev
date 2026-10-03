package backend.backenddev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserReq {

    @NotBlank(message = "name is required")
    private  String name;
    @NotBlank (message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    public UpdateUserReq(){


    }

}
