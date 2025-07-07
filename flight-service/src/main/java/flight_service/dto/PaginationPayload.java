package flight_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationPayload {
    private boolean paginate;
    private int page;
    private int size;
    private String airline;
    private String departure;
    private String destination;
    private String classType;
    private String tripType;
}
