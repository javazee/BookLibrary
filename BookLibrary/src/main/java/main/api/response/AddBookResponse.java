package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;

@Data
public class AddBookResponse {

    private boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private HashMap<String, String> errors = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int id;
}
