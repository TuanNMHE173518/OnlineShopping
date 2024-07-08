package org.example.itech_heaven.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cart")
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartDetails> cartDetails = new ArrayList<>();

    public void addCartDetail(CartDetails cartDetail) {
        cartDetails.add(cartDetail);
        cartDetail.setCart(this);
    }

    public void removeCartDetail(CartDetails cartDetail) {
        cartDetails.remove(cartDetail);
        cartDetail.setCart(null);
    }

    public String formatPrice(double pri){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###");
        return decimalFormat.format((int) pri) + " đ";
    }
}

