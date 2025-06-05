package booking_service.external;

import booking_service.exception.BadRequestException;
import booking_service.external.dto.ApiResponse;
import booking_service.external.dto.FlightPackageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class ServiceCalls {

    private final WebClient webClient;

    @Autowired
    public ServiceCalls(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<FlightPackageResponse> getFlightPackage(UUID id) {
        return webClient.get()
                .uri("/flight-package/" + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<FlightPackageResponse>>() {})
                .map(ApiResponse::getData);
    }

    public Mono<FlightPackageResponse> updateFlightPackage(UUID id) {
        FlightPackageResponse flightPackage = new FlightPackageResponse();
        flightPackage.setAvailableSeats(120);
        return webClient.put()
                .uri("/flight-package/"+id)
                .bodyValue(flightPackage)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<FlightPackageResponse>>() {})
                .map(ApiResponse::getData);
    }

//    @Scheduled(fixedRate = 1000)
//    public void test(){
//        updateFlightPackage()
//                .subscribe((response)-> log.info("Response{}", response.getDestination()));
//    }
}
