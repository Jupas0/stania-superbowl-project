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
@Table(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Basic(optional = false)
	@Column(name = "USERNAME")
	String userName;
	@Basic(optional = false)
	@Column(name = "SALT")
	String salt;
	@Basic(optional = false)
	@Column(name = "STATUS")
	String status;
	@Basic(optional = false)
	@Column(name = "HASHED")
	String hashPass;

}