package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Collection<Cart> findAllByUserId(Integer userId);

    Cart findByUserEmail(String email);



}
