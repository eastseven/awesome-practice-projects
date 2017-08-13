package cn.eastseven;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dongqi on 17/7/18.
 */
@Data
public class FormObject {

    @NotNull
    @Size(min=2, max=10)
    private String username;

    @NotNull
    @Min(8)
    private String password;

    private boolean remember;
}
