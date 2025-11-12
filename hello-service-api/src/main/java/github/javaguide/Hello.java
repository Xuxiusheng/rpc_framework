package github.javaguide;

import lombok.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hello implements Serializable {
    private String msg;
    private String description;
}
