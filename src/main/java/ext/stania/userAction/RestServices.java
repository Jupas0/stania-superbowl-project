package ext.stania.userAction;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ext.stania.repos.TeamRepository;

@RestController
public class RestServices {
	private static final Logger logger = LoggerFactory.getLogger(RestServices.class);
	HashMap<String, String> pass = new HashMap<>();
	
	
	
	@GetMapping(value="/connexion")
	public String connexion(@RequestParam(value="username")String name,@RequestParam(value="pass") String pass) {
		Connection conn;
		String retour =  null;
		String enCoursCheck =null;
		String passCheck =null;
		String hashPass =null;
		try{
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery("Select salt, username, hashed from user where username like '"+name+"'");
			while(res.next()) {
				passCheck =res.getString(1); //Récupération du sel de l'utilisateur
				enCoursCheck = res.getString(2); //Stockage de la valeur username en attendant d'authentifier l'utilisateur.	
				hashPass=res.getString(3); // Récupération de la valeur hashée du mot de passe.
			}
			passCheck=pass+passCheck; // Concaténation du mot de passe saisi et du sel de l'utilisateur.
			HashPassword hash =new HashPassword();
			if (hashPass.equals(hash.hash(passCheck))){
				retour = enCoursCheck;				
			}
			else {
					retour = "Invité";
			}
		}
		catch (Exception e){
				e.printStackTrace();
				return "erreur";
		}
		return retour;
	}
		
	@GetMapping(value="/inscription")
	public String inscription(@RequestParam(value="username")String username,@RequestParam(value="pass") String pass, @RequestParam(value="firstname")String firstName,@RequestParam(value="name")String name) {
			Connection conn;
			String retour =  null;
			String salt =null;
			try{
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
				HashPassword hash =new HashPassword();
				salt=hash.getSalt(10);
				PreparedStatement statement = conn.prepareStatement("Insert into user (username, pass, salt, firstname, name) values (?,?,?,?,?)");
				statement.setString(1,username);
				statement.setString(2, hash.hash(pass+salt));
				statement.setString(3,salt);
				statement.setString(4,firstName);
				statement.setString(5, name);
				statement.execute();
				retour = firstName + " " + name;
			}
			catch (Exception e){
					e.printStackTrace();
					return "erreur";
			}

		
		System.out.println("Bonjour, "+retour);
		return "Bienvenue, "+ retour;
	}
	
	@GetMapping(value="/detailsmatch")
	public List<String> detailsMatch(@RequestParam(value="matchid")int matchId) {
		logger.info("L'utilisateur a accès au match :"+matchId);
		Connection conn;
		String game = null;
		List<String> playerList =new ArrayList<>(Arrays.asList("Nom du joueur"+"|"+"Numéro"+"|"+"Nom de l'équipe"+"|"+"Nom de l'équipe"+"|"+"Nom du joueur"+"|"+"Numéro du joueur"));
		List<String> result = new ArrayList<>();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
		
		Statement statement = conn.createStatement();
		ResultSet resGame = statement.executeQuery("SELECT teamA.name, GAME.teamAscore, teamB.name,GAME.TEAMBSCORE, GAME.DATEmatch "
				+ "FROM TEAM as teamA, TEAM as teamB, GAME "
				+ "WHERE GAME.ID = "+matchId+" and teamA.id=GAME.teamAID and teamB.id=GAME.teambid");
		while(resGame.next()) {
			game = resGame.getString(1) +" | "+resGame.getString(2)+" | "+resGame.getString(3)+" | "+resGame.getString(4)+" | "+resGame.getString(5);
		}
		result.add(game);
		ResultSet joueursEquipeA = statement.executeQuery("SELECT playera.name, playera.number, teama.name, GAME.ID FROM TEAM as teama, PLAYER as playera, GAME WHERE GAME.ID = "+matchId+" and GAME.teamaid = playera.teamid and teama.id = playera.teamid");
		while(joueursEquipeA.next()) {
			playerList.add(joueursEquipeA.getString(1) +" | "+joueursEquipeA.getString(2)+" | "+joueursEquipeA.getString(3)+" | "+joueursEquipeA.getString(4));
		}
		ResultSet joueursEquipeB = statement.executeQuery("SELECT playerb.name, playerb.number, teamb.name, GAME.ID FROM TEAM as teamb, PLAYER as playerb, GAME WHERE GAME.ID = "+matchId+" and GAME.teambid = playerb.teamid and teamb.id = playerb.teamid");
		while(joueursEquipeB.next()) {
			playerList.add(joueursEquipeB.getString(1) +" | "+joueursEquipeB.getString(2)+" | "+joueursEquipeB.getString(3)+" | "+joueursEquipeB.getString(4));
		}
		playerList.forEach(player -> result.add(player));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			game = "erreur";
		}
		
		return (result);
	}
	
	
	@GetMapping(value="/creerjoueur")
	public String creerJoueur(@RequestParam(value="name")String name,@RequestParam(value="teamName") String teamName, @RequestParam(value="number") int number) {
		int teamId=0;
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
			Statement getTeamNumber =conn.createStatement();
			ResultSet res = getTeamNumber.executeQuery("Select id from team where name like '"+teamName+"'");
			while(res.next()) {
				teamId=res.getInt(1);
			}
			PreparedStatement statement = conn.prepareStatement("INSERT INTO PLAYER (NAME,NUMBER,TEAMID) VALUES (?,?,?)");
			statement.setString(1,name);
			statement.setInt(2, number);
			statement.setInt(3, teamId);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "Joueur "+name+" créée.";
	}
	@GetMapping(value="/voirmatchs")
	public List<String> voirMatchs() {
		Connection conn;
		List<String> result =new ArrayList<>(Arrays.asList("Team A"+"|"+"Score A"+"|"+"Team B"+"|"+"Score B"+"|"+"Date Match"));
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
			Statement getTeamNumber =conn.createStatement();
			ResultSet res = getTeamNumber.executeQuery("SELECT teamA.name, Game.teamAscore,teamB.name,GAME.TEAMBSCORE,GAME.DATEmatch FROM TEAM as teamA, "
					+ "TEAM as teamb, GAME WHERE teamA.id=game.teamAID and teamB.id=game.teambid order by game.id");
			while(res.next()) {
				result.add(((res.getString(1)!=null) ? res.getString(1):"Personne" ) 
						+" | "+((res.getString(2)!=null) ? res.getInt(2):0 ) 
						+" | "+((res.getString(3)!=null) ? res.getString(3):"Personne" )
						+" | "+((res.getString(4)!=null) ? res.getInt(4):0 )
						+" | "+((res.getString(5)!=null) ? res.getString(5):"TBD" ));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	@GetMapping(value="/creerequipe")
	public String creerEquipe(@RequestParam(value="name")String name){
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
			PreparedStatement statement=conn.prepareStatement("INSERT INTO TEAM (name) value (?)");
			statement.setString(1, name);
			statement.execute();
		}
		catch(Exception e) {
			return e.toString();
		}
		return "Equipe"+name+"créée.";
	}
	@GetMapping(value="/planifiermatch")
	public String creerMatch(@RequestParam(value="teamA")String teamAName, @RequestParam(value="teamB")String teamBName, @RequestParam(value="dateMatch")String dateMatch ){
		Connection conn;
		int teamAId=0;
		int teamBId=0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
			Statement getTeamAId = conn.createStatement();
			ResultSet idTeamA = getTeamAId.executeQuery("SELECT ID FROM TEAM WHERE NAME LIKE '"+teamAName+"'");
			while(idTeamA.next()) {
				teamAId = idTeamA.getInt(1);
			}
			Statement getTeamBId = conn.createStatement();
			ResultSet idTeamB = getTeamBId.executeQuery("SELECT ID FROM TEAM WHERE NAME LIKE '"+teamBName+"'");
			while(idTeamB.next()) {
				teamBId = idTeamB.getInt(1);
			}
			PreparedStatement statement=conn.prepareStatement("INSERT INTO GAME (teamaid, teambid, dateMatch) value (?,?,?)");
			statement.setInt(1, teamAId);
			statement.setInt(2, teamBId);
			statement.setString(3, dateMatch); //au format YYYY-MM-DD
			statement.execute();
		}
		catch(Exception e) {
			return e.toString();
		}
		return "Match entre "+teamAName+" et "+teamBName+" planifié au "+dateMatch+".";
		
	}
	@GetMapping(value="/miser")
	public String miser(@RequestParam(value="amount")int mise, @RequestParam(value="teamName")String teamName, @RequestParam(value="dateMatch") String dateMatch, @RequestParam(value="userId")int userId){
		Connection conn;
		int gameId=0;
		int teamId=0;
		try {
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
		Statement getTeamId = conn.createStatement();
		ResultSet idTeam = getTeamId.executeQuery("SELECT ID FROM TEAM WHERE NAME LIKE '"+teamName+"'");
		while(idTeam.next()) {
			teamId = idTeam.getInt(1);
		}
				
		Statement getMatchId = conn.createStatement();
		ResultSet idMatch = getMatchId.executeQuery("Select GAME.ID, Team.id FROM TEAM, GAME WHERE GAME.DATEMATCH='"+dateMatch+"' and (GAME.TEAMAID="+teamId+" or GAME.TEAMBID="+teamId+")");
		while(idMatch.next()) {
			gameId = idMatch.getInt(1);
		}
		PreparedStatement statement = conn.prepareStatement("INSERT INTO BET (amount, matchid, teamid, userid) values (?,?,?,?)");
		statement.setInt(1, mise);
		statement.setInt(2, gameId);
		statement.setInt(3, teamId);
		statement.setInt(4, userId);
		statement.execute();
		}
		catch(Exception e) {
			return e.toString();
		}
		return "Le pari d'un montant de "+mise+" a été placé sur "+teamName+"pour le match en date du "+dateMatch+"pour l'utilisateur "+userId;
	}
	
	@GetMapping(value="/historique")
	public List<String> historiqueDesMises(@RequestParam(value="userId")int userId){
		Connection conn;
		List<String> result =new ArrayList<>(Arrays.asList("Montant de la mise"+"|"+"Equipe"+"|"+"Adversaire"+"|"+"Date du Match"));
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "test");
			Statement getTeamNumber =conn.createStatement();
			ResultSet res = getTeamNumber.executeQuery("select bet.amount, teama.name, teamb.name, game.datematch from bet, team as teama, team as teamb, game where "
					+ "teamA.id=bet.teamID and teamB.id=game.teambid and bet.matchid = game.id and bet.userid="+userId);
			while(res.next()) {
				result.add(((res.getString(1)!=null) ? res.getInt(1)+"€":0 ) 
						+" | "+((res.getString(2)!=null) ? res.getString(2):"Personne" ) 
						+" | "+((res.getString(3)!=null) ? res.getString(3):"Personne" )
						+" | "+((res.getString(4)!=null) ? res.getString(4): "TBD" ));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	@GetMapping(value="/")
	public String bienvenue(){
		
		return "Bienvenu(e) sur la page d'accueil.\n Se connecter\nVisualiser tous les matchs\nParier";
	}
}
