package com.cg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.AccTransaction;
/**
 * @author raviraj
 *
 */
@Repository
public interface TxnDao extends JpaRepository<AccTransaction, Long>{

}
