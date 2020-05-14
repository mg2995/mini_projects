package LPM;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class FieldResponse {

    public  List<Field> fields;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Field {
         String name = "";
         String type= "";
         boolean indexed = false;
         boolean stored = false;
        Boolean required = null;
        Boolean multiValued = null;
    }
}
