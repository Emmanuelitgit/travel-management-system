package payment_service.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import payment_service.config.AppProperties;
import payment_service.config.kafka.dto.BookingUpdatePayload;
import payment_service.config.kafka.dto.PaymentUpdatePayload;
import payment_service.exception.BadRequestException;
import payment_service.external.dto.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class ServiceCalls {

    private final WebClient webClient;
    private final AppProperties appProperties;

    @Autowired
    public ServiceCalls(WebClient webClient, AppProperties appProperties) {
        this.webClient = webClient;
        this.appProperties = appProperties;
    }

    public Mono<PaymentResponse> makePayment(PaymentUpdatePayload paymentUpdatePayload) {
        return webClient.post()
                .uri(appProperties.getPayStackUrl())
                .header("Authorization", "Bearer " +appProperties.getPayStackSecret())
                .bodyValue(paymentUpdatePayload)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BadRequestException("Bad Request: " + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BadRequestException("Server Error: " + clientResponse.statusCode())))
                .bodyToMono(PaymentResponse.class);
    }

    public Mono<BookingResponse> getBookingById(UUID id) {
        return webClient.get()
                .uri(appProperties.getBookingServiceUrl()+"/"+id)
//                .header("Authorization", "Bearer " +appProperties.getPayStackSecret())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BadRequestException("Bad Request: " + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BadRequestException("Server Error: " + clientResponse.statusCode())))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<BookingResponse>>() {})
                .map(ApiResponse::getData);
    }

    public Mono<BookingResponse> updateBooking(UUID id, BookingUpdatePayload payload) {
        return webClient.put()
                .uri(appProperties.getBookingServiceUrl()+"/"+id)
//                .header("Authorization", "Bearer " +appProperties.getPayStackSecret())
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BadRequestException("Bad Request: " + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BadRequestException("Server Error: " + clientResponse.statusCode())))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<BookingResponse>>() {})
                .map(ApiResponse::getData);
    }

}