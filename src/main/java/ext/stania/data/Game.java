package ext.stania.data;

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
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Basic(optional = false)
	@Column(name = "TEAMAID")
	int teamAID;
	@Basic(optional = true)
	@Column(name = "TEAMASCORE")
	int teamAScore;
	@Basic(optional = false)
	@Column(name = "TEAMBID")
	int teamBId;
	@Basic(optional = true)
	@Column(name = "TEAMBSCORE")
	int teamBScore;
	@Basic(optional = true)
	@Column(name = "DATEGAME")
	String dateMatch;
}