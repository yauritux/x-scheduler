package com.gdn.x.scheduler.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gdn.x.scheduler.constant.CommandType;
import com.gdn.x.scheduler.model.Command;

/**
 * 
 * @author yauritux
 *
 */
public interface CommandDAO extends CrudRepository<Command, String> {
	
	public static final String FETCH_ALL 
		= "FROM Command c WHERE c.markForDelete = false ORDER BY createdDate DESC";
	
	public static final String FETCH_BY_COMMAND_TYPE 
		= "FROM Command c WHERE c.markForDelete = false AND c.commandType = :commandType ORDER BY createdDate DESC";

	public Command findById(String id);
	
	@Query(FETCH_ALL)
	public List<Command> fetchAll();
	
	@Query(FETCH_ALL)
	public Page<Command> fetchAll(Pageable pageable);
	
	@Query(FETCH_BY_COMMAND_TYPE)
	public List<Command> findByCommandType(@Param("commandType") CommandType commandType);
	
	@Query(FETCH_BY_COMMAND_TYPE)
	public Page<Command> findByCommandType(@Param("commandType") CommandType commandType, Pageable pageable);
	
	@Modifying
	@Query("UPDATE Command c SET c.markForDelete = true WHERE c.id = :id")
	public int deleteCommand(@Param("id") String id);
}
