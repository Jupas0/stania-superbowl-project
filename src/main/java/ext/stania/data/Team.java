package ext.stania.data;

import lombok.*;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Team")
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Basic(optional = false)
	@Column(name = "name")
	private String name;
	
	@Basic(optional = false)
	@Column(name = "score")
	private int score;
	
	@Basic
	@Column(name = "rank")
	private int rank;


}
