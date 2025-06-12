package booking_service.external;

import booking_service.config.AppProperties;
import booking_service.external.dto.UserResponse;
import booking_service.exception.BadRequestException;
import booking_service.exception.ServerException;
import booking_service.external.dto.ApiResponse;
import booking_service.external.dto.FlightPackageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class ServiceCalls {

    private final WebClient webClient;
    private final AppProperties appProperties;

    @Autowired
    public ServiceCalls(WebClient webClient, AppProperties appProperties) {
        this.webClient = webClient;
        this.appProperties = appProperties;
    }


    /**
     * @description Fetches a specific flight package record by ID.
     * @param id the ID of the flight package to retrieve.
     * @return ResponseEntity containing the booking record and status info.
     * @author Emmanuel Yidana
     * @createdAt 6th, June 2025
     * */
    public Mono<FlightPackageResponse> getFlightPackage(UUID id) {
        return webClient.get()
                .uri(appProperties.getFlightServiceUrl().concat("/")+id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class) // Optional: read error message
                                .flatMap(msg -> Mono.error(new BadRequestException("Bad Request: " + msg)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ServerException("Server Error: " + msg)))
                )
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<FlightPackageResponse>>() {})
                .map(ApiResponse::getData);
    }

    /**
     * @description Fetches a specific flight package record by ID.
     * @param id the ID of the flight package to retrieve.
     * @param updatedSeats the updated flight package
     * @return ResponseEntity containing the flight package and status info.
     * @author Emmanuel Yidana
     * @createdAt 6th, June 2025
     * */
    public Mono<FlightPackageResponse> updateFlightPackage(UUID id, Integer updatedSeats) {
        FlightPackageResponse flightPackage = new FlightPackageResponse();
        flightPackage.setAvailableSeats(updatedSeats);
        return webClient.put()
                .uri(appProperties.getFlightServiceUrl().concat("/")+id)
                .bodyValue(flightPackage)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class) // Optional: read error message
                                .flatMap(msg -> Mono.error(new BadRequestException("Bad Request: " + msg)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ServerException("Server Error: " + msg)))
                )
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<FlightPackageResponse>>() {})
                .map(ApiResponse::getData);
    }

    /**
     * @description Fetches a specific user record by ID.
     * @param id the ID of the user to retrieve.
     * @return ResponseEntity containing the user and status info.
     * @author Emmanuel Yidana
     * @createdAt 6th, June 2025
     * */
    public Mono<UserResponse> getUserInfo(UUID id) {
        return webClient.get()
                .uri(appProperties.getUserServiceUrl().concat("/")+id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class) // Optional: read error message
                                .flatMap(msg -> Mono.error(new BadRequestException("Bad Request: " + msg)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ServerException("Server Error: " + msg)))
                )
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponse>>() {})
                .map(ApiResponse::getData);
    }

//    @Scheduled(fixedRate = 1000)
//    public void test(){
//        updateFlightPackage()
//                .subscribe((response)-> log.info("Response{}", response.getDestination()));
//    }
}