package si.uni.prpo.group03.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;


@Configuration
public class Routes {

    @Value("${gateway.routes.userservice:http://localhost:8081}")
    private String userServiceUrl;

    @Value("${gateway.routes.paymentservice:http://localhost:8082}")
    private String paymentServiceUrl;

    @Value("${gateway.routes.eventservice:http://localhost:8083}")
    private String eventServiceUrl;

    @Value("${gateway.routes.venueservice:http://localhost:8084}")
    private String venueServiceUrl;

    @Value("${gateway.routes.guestservice:http://localhost:8085}")
    private String guestServiceUrl;

    @Value("${gateway.routes.notificationservice:http://localhost:8086}")
    private String notificationServiceUrl;


    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/api/users/**"), HandlerFunctions.http(userServiceUrl))
                .route(RequestPredicates.path("/api/auth/**"), HandlerFunctions.http(userServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentRoutes() {
        return GatewayRouterFunctions.route("payment_service")
                .route(RequestPredicates.path("/api/payments/**"), HandlerFunctions.http(paymentServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> eventRoutes() {
        return GatewayRouterFunctions.route("event_service")
                .route(RequestPredicates.path("/api/events/**"), HandlerFunctions.http(eventServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> venueRoutes() {
        return GatewayRouterFunctions.route("venue_service")
                .route(RequestPredicates.path("/api/venues/**"), HandlerFunctions.http(venueServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> guestRoutes() {
        return GatewayRouterFunctions.route("guest_service")
                .route(RequestPredicates.path("/api/guests/**"), HandlerFunctions.http(guestServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationRoutes() {
        return GatewayRouterFunctions.route("notification_service")
                .route(RequestPredicates.path("/api/notifications/**"), HandlerFunctions.http(notificationServiceUrl))
                .build();
    }

}
