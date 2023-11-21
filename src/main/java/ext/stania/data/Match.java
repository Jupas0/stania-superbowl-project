package ext.stania.data;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Game")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Basic(optional = false)
	@Column(name = "STATUS")
	String status;
	@Basic(optional = false)
	@Column(name = "TEAMAID")
	int teamAId;
	@Basic(optional = false)
	@Column(name = "TEAMBID")
	int teamBId;
	@Basic(optional = false)
	@Column(name = "TEAMASCORE")
	int teamAScore;
	@Basic(optional = false)
	@Column(name = "TEAMBSCORE")
	int teamBScore;
	
	@Basic(optional = false)
	@Column(name = "DATEMTACH")
	String dateMatchString;
	
	
}
