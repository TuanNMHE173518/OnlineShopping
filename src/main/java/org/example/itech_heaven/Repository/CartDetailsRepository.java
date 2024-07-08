package org.example.itech_heaven.Repository;

import org.example.itech_heaven.DTO.request.CartDetailDTO;
import org.example.itech_heaven.Entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartDetailsRepository extends JpaRepository<CartDetails, Integer> {
    Optional<CartDetails> findByCartIdAndDeviceId(Integer cartId, Integer deviceId);

    Optional<CartDetails> findByCartIdAndAccessoryId(Integer cartId, Integer accessoryId);

    @Query("SELECT cd FROM CartDetails cd WHERE cd.cart.user.id = :userId")
    List<CartDetails> findByCartUserId(Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartDetails cd WHERE cd.device.id = :deviceId")
    void deleteByDeviceId(@Param("deviceId") Integer deviceId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartDetails cd WHERE cd.accessory.id = :accessoryId")
    void deleteByAccessoryId(@Param("accessoryId") Integer accessoryId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartDetails cd WHERE cd.id = :Id")
    void deleteCartDetailsById(@Param("Id") int Id);


}
