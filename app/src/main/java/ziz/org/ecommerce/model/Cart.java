package ziz.org.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data
public class Cart {
    private String pid;
    private String pname;
    private String price;
    private String quantity;
    private String discount;
}
