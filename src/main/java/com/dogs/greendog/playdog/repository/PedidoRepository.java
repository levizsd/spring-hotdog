package com.dogs.greendog.playdog.repository;

import com.dogs.greendog.playdog.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
 
	 
}
