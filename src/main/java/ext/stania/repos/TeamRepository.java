package ext.stania.repos;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ext.stania.data.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
	
	
	<S extends Team> S save(S team);
	
	List<Team> save(Iterable<? extends Team> Teams);
	
	long count();
		
	Team findById(String name);
	
	List<Team> findAll();
	
	List<Team> findAll(Sort sort);
	
	void deleteAllById(int id);

	void deleteById(int id);
	
}