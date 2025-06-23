package mx.edu.utez.U3_04_OMM.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private Object object;
    private HttpStatus status;
    private String message;
    private boolean error;

    public ApiResponse(Object object, HttpStatus status, String message, boolean error) {
        this.object = object;
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public ApiResponse(Object object, HttpStatus status, String message) {
        this.object = object;
        this.status = status;
        this.message = message;
    }

    @JsonIgnore
    public Object getData() {
        return this.object;
    }
}