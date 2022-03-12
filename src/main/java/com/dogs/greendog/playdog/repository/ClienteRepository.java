package com.dogs.greendog.playdog.repository;

import com.dogs.greendog.playdog.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

 

 @Repository
//@RepositoryRestResource(collectionResourceRel = "clientes",path = "clientes")
public interface ClienteRepository extends JpaRepository<Cliente,Long> {

	Cliente findByNome(String nome);

	Page<Cliente> findByNomeContainingAllIgnoringCase(@Param("nome") String nome,Pageable pageable);

	Cliente findByNomeAllIgnoringCase(@Param("nome") String nome);

}