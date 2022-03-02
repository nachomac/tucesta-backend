package es.tucesta.response;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderResponse {
    private Long orderId;
    private int progress;
    private String ingredientsAdded;
}
