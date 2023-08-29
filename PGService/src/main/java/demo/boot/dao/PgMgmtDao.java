package demo.boot.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo.boot.model.Pgmain;

@Repository
public interface PgMgmtDao extends CrudRepository<Pgmain, Long> {

}
