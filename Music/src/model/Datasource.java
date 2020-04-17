package model;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.PUBLIC_MEMBER;

public class Datasource {

	public static final String DB_NAME = "music.db";
	
	public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Qi Long\\Desktop\\Music\\"  + DB_NAME;
	
	public static final String TABLE_ALBUM = "albums";
	public static final String COLUMN_ALBUM_ID = "_id";
	public static final String COLUMN_ALBUM_NAME = "name";
	public static final String COLUMN_ALBUM_ARTIST = "artist";
	public static final int INDEX_ALBUM_ID =1;
	public static final int INDEX_ALBUM_NAME =2;
	public static final int INDEX_ALBUM_ARTIST =3;
	
	
	public static final String TABLE_ARTISTS = "artists";
	public static final String COLUMN_ARTIST_ID = "_id";
	public static final String COLUMN_ARTIST_NAME = "name";
	public static final int INDEX_ARTIST_ID =1;
	public static final int INDEX_ARTIST_NAME =2;
	
	public static final String TABLE_SONG = "songs";
	public static final String COLUMN_SONG_ID = "_id";
	public static final String COLUMN_SONG_TRACK = "track";
	public static final String COLUMN_SONG_TITLE = "title";
	public static final String COLUMN_SONG_ALBUM = "album";
	public static final int INDEX_SONG_ID =1;
	public static final int INDEX_SONG_TRACK =2;
	public static final int INDEX_SONG_TITLE =3;
	public static final int INDEX_SONG_ALBUM =4;
	
	public static final int ORDER_BY_NONE =1;
	public static final int ORDER_BY_ASC =2;
	public static final int ORDER_BY_DESC =3;
	
	
	public static final String QUERY_ALBUMS_BY_ARTIST_START = 
			"SELECT " + TABLE_ALBUM + "." + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUM + 
			" INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUM + "." + COLUMN_ALBUM_ARTIST + " = " 
			+ TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +" WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";
	
	public static final String QUERY_ALBUMS_BY_ARTIST_SORT=
			" ORDER BY " + TABLE_ALBUM + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";
	

	public static final String QUERY_ARTIST_FOR_SONG_START = 
			"SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
			TABLE_ALBUM + "." + COLUMN_ALBUM_NAME + ", " +
					TABLE_SONG + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONG +
					" INNER JOIN " + TABLE_ALBUM + " ON " + 
					TABLE_SONG + "." + COLUMN_SONG_ALBUM  + " = " + TABLE_ALBUM + "." + COLUMN_ALBUM_ID +
					" INNER JOIN " + TABLE_ARTISTS + " ON " +
					TABLE_ALBUM + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + 
					" WHERE " + TABLE_SONG + "." + COLUMN_SONG_TITLE + "= \"";
	
	public static final String QUERY_ARTIST_FOR_SORT = " ORDER BY " + TABLE_ARTISTS  + "." + COLUMN_ARTIST_NAME  + ", " + TABLE_ALBUM + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";				
	
//	CREATE VIEW IF NOT EXISTS artist_list AS SELECT artists.name, albums.name AS
//	 album, song.track, songs.title FROM songs INNER JOIN albums ON songs.album = albums._id 
//	INNER JOIN artists ON albums.artist = artists._id ORDER BY artists.name, albums.name, songs.track
	public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
	public static final String CREATE_ARTIST_FOR_SONG = "CREATE VIEW IF NOT EXISTS " +  
			TABLE_ARTIST_SONG_VIEW +" AS SELECT " + TABLE_ARTISTS +"." + COLUMN_ARTIST_NAME + ", " + 
			TABLE_ALBUM + "." + COLUMN_ALBUM_NAME +" AS "+ COLUMN_SONG_ALBUM + ", " + 
			TABLE_SONG + "." + COLUMN_SONG_TRACK+ ", "+ TABLE_SONG + "." + COLUMN_SONG_TITLE + 
			" FROM "+ TABLE_SONG + 
			" INNER JOIN " + TABLE_ALBUM + " ON " + TABLE_SONG + 
			"." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUM +"." + COLUMN_ALBUM_ID + 
			" INNER JOIN " + TABLE_ARTISTS+ " ON " + TABLE_ALBUM +"." + COLUMN_ALBUM_ARTIST+
			"=" + TABLE_ARTISTS +"." + COLUMN_ARTIST_ID +
			" ORDER BY "+ 
			TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + 
			TABLE_ALBUM  + "." + COLUMN_ALBUM_NAME + ", " + 
			TABLE_SONG + "." + COLUMN_SONG_TRACK;
		
	//SELECT name, album, track FROM artist_list WHERE title = "Go Your Own Way"
	public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTIST_NAME + ", " + 
			COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW + 
			" WHERE "+ COLUMN_SONG_TITLE + " = \"";
	
	
	
	public static final String  QUERY_VIEW_SONG_INFO_PREP = "SELECT "+ COLUMN_ARTIST_NAME +", " +
			COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW + 
			" WHERE "+ COLUMN_SONG_TITLE + " = ?";
	//SELECT name, album, track FROM artist_list WHERE title = ?		
	
	
	
	public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS + 
			'(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
	public static final String INSERT_ALBUMS = "INSERT INTO "+ TABLE_ALBUM + 
			'(' + COLUMN_ALBUM_NAME +", " + COLUMN_ALBUM_ARTIST + ") VALUES(?,?)";
	public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONG + '('+COLUMN_SONG_TRACK +", " + COLUMN_SONG_TITLE +", " + COLUMN_SONG_ALBUM+
			") VALUES(?,?,?)";
	
	
	public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";
	public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " + TABLE_ALBUM + " WHERE " + COLUMN_ALBUM_NAME +" = ?";
	
	private Connection conn;
	
	private PreparedStatement querySongInfoView;
	private PreparedStatement insertIntoArtists;
	private PreparedStatement insertIntoAlbums;
	private PreparedStatement insertIntoSongs;
	private PreparedStatement queryArtist;
	private PreparedStatement queryAlbum;

	public boolean open(){
		try{
			conn = DriverManager.getConnection(CONNECTION_STRING);
			querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
			insertIntoArtists  = conn.prepareStatement(INSERT_ARTIST,Statement.RETURN_GENERATED_KEYS);
			insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
			insertIntoSongs = conn.prepareStatement(INSERT_SONG);
			queryArtist = conn.prepareStatement(QUERY_ARTIST);
			queryAlbum = conn.prepareStatement(QUERY_ALBUM);
			
			return true;
		}catch(SQLException e){
			System.out.println("Couldn't connect to database" + e.getMessage());
			return false;
		}
	}
	
	public void close(){
		try{
			if(querySongInfoView != null){
				querySongInfoView.close();
			}
			if(insertIntoArtists != null){
				insertIntoArtists.close();
			}
			if(insertIntoAlbums != null){
				insertIntoAlbums.close();
			}
			if(insertIntoSongs != null){
				insertIntoSongs.close();
			}
			if(queryArtist != null){
				queryArtist.close();
			}
			if(queryAlbum != null){
				queryAlbum.close();
			}
			
			
			if(conn!=null){
				conn.close();
			}
		}catch(SQLException e){
			System.out.println("Couldn't close database" + e.getMessage());
		}
		
	}
	
	public List<Artist> queryArtists(int sortOrder){
	//	Statement statement = null;
	//	ResultSet results = null;
		StringBuilder sb = new StringBuilder("SELECT * FROM ");
		sb.append(TABLE_ARTISTS);
		if(sortOrder != ORDER_BY_NONE){
			sb.append(" ORDER BY ");
			sb.append(COLUMN_ARTIST_NAME);
			sb.append(" COLLATE NOCASE ");
			if(sortOrder == ORDER_BY_DESC){
				sb.append("DESC");
			}else{
				sb.append("ASC");
			}
		}
		
		
		try (Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(sb.toString())){
			//statement = conn.createStatement();
			
			List<Artist> artists = new ArrayList<>();
			while(results.next()){
				Artist artist = new Artist();
				artist.setId(results.getInt(INDEX_ARTIST_ID));
				artist.setName(results.getString(INDEX_ARTIST_NAME));
				artists.add(artist);
			}
			return artists;
			
		}catch(SQLException e){
			System.out.println("Query failed" + e.getMessage());
			return null;
		}
		
//		
//		finally{
//			try{
//				if(results!=null){
//					results.close();
//				}
//			}catch (SQLException e){
//				System.out.println("Error closing ResultSet" + e.getMessage());
//			}
//			try{
//				if(statement!=null){
//					statement.close();
//				}
//			}catch (SQLException e){
//				System.out.println("Error closing Statement" + e.getMessage());
//			}
//		}
	}
	
	
	public List<String> queryAlbumsForArtist(String artistName, int sortOrder){
		
		// SELECT albums.name FROM albums INNER JOIN artists ON albums.artist = artists._id WHERE artists.name = "Pink Floyd" ORDER BY albums.name COLLATE NOCASE ASC
		StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
		sb.append(artistName);
		sb.append("\"");
		
		
//		sb.append(TABLE_ALBUM);
//		sb.append(".");
//		sb.append(COLUMN_ALBUM_NAME);
//		sb.append(" FROM ");
//		sb.append(TABLE_ALBUM);
//		sb.append(" INNER JOIN ");
//		sb.append(TABLE_ARTISTS);
//		sb.append(" ON ");
//		sb.append(TABLE_ALBUM);
//		sb.append(".");
//		sb.append(COLUMN_ALBUM_ARTIST);
//		sb.append(" = ");
//		sb.append(TABLE_ARTISTS);
//		sb.append(".");
//		sb.append(COLUMN_ARTIST_ID);
//		sb.append(" WHERE ");
//		sb.append(TABLE_ARTISTS);
//		sb.append(".");
//		sb.append(COLUMN_ARTIST_NAME);
//		sb.append(" = \"");
//		sb.append(artistName);
//		sb.append("\"");
		
		if(sortOrder != ORDER_BY_NONE){
			sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
			
//			sb.append(TABLE_ALBUM);
//			sb.append(".");
//			sb.append(COLUMN_ALBUM_NAME);
//			sb.append(" COLLATE NOCASE ");
			
			if(sortOrder == ORDER_BY_DESC){
				sb.append("DESC");
			}else{
				sb.append("ASC");
			}
		}
		
		System.out.println("SQL statement = " + sb.toString());
		try(Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(sb.toString())){
			
			List<String> albums = new ArrayList<>();
			while(results.next()){
				albums.add(results.getString(1));
			}
			return albums;
			
		}catch(SQLException e){
			System.out.println("Query failed: " + e.getMessage());
			return null;
		}
		
	}
	
	public List<SongArtist> queryArtistsForSong(String songName, int sortOrder){
		
		StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
		sb.append(songName);
		sb.append("\"");
		

		if(sortOrder != ORDER_BY_NONE){
			sb.append(QUERY_ARTIST_FOR_SORT);
			if(sortOrder == ORDER_BY_DESC){
				sb.append("DESC");
			}else{
				sb.append("ASC");
			}
		}
		System.out.println("SQL statement = " + sb.toString());
		
		try(Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery(sb.toString())){
				
				List<SongArtist> songArtists = new ArrayList<>();
				while(results.next()){
					SongArtist songArtist = new SongArtist();
					songArtist.setArtistName(results.getString(1));
					songArtist.setAlbumName(results.getString(2));
					songArtist.setTrack(results.getInt(3));
					songArtists.add(songArtist);
					
				  
				}
				return songArtists;
				
			}catch(SQLException e){
				System.out.println("Query failed: " + e.getMessage());
				return null;
			}
		
	}
	
	public int getCount(String table){
		String sql = "SELECT COUNT(*) AS count FROM " + table;
		try(Statement statement = conn.createStatement();
				ResultSet results = statement.executeQuery(sql)){
			
			int count = results.getInt("count");
			//int min = results.getInt("min_id");
			System.out.format("Count = %d\n", count);
			return count;
			
		}catch (SQLException e){
			
			System.out.println("Query failed" + e.getMessage());
			return -1;
		}
		
	}
	public boolean createViewForSongArtists(){
		
		try(Statement statement = conn.createStatement()){
			statement.execute(CREATE_ARTIST_FOR_SONG);
			
			return true;
			
		}catch (SQLException e){
			
			System.out.println("Create View failed" + e.getMessage());
			return false;
		}
	}
	public List<SongArtist> querySongInfoView(String title){
//		StringBuilder sb = new StringBuilder(QUERY_VIEW_SONG_INFO);
//		sb.append(title);
//		sb.append("\"");
//		
//		System.out.println(sb.toString());
		try{
			querySongInfoView.setString(1, title);
			ResultSet results = querySongInfoView.executeQuery();
				
				List<SongArtist> songArtists = new ArrayList<>();
				while(results.next()){
					SongArtist songArtist = new SongArtist();
					songArtist.setArtistName(results.getString(1));
					songArtist.setAlbumName(results.getString(2));
					songArtist.setTrack(results.getInt(3));
					songArtists.add(songArtist);
				}
				return songArtists;
				
			}catch(SQLException e){
				System.out.println("Query failed: " + e.getMessage());
				return null;
			}
		
		
		
		
//		try(Statement statement = conn.createStatement();
//				ResultSet results = statement.executeQuery(sb.toString())){
//				
//				List<SongArtist> songArtists = new ArrayList<>();
//				while(results.next()){
//					SongArtist songArtist = new SongArtist();
//					songArtist.setArtistName(results.getString(1));
//					songArtist.setAlbumName(results.getString(2));
//					songArtist.setTrack(results.getInt(3));
//					songArtists.add(songArtist);
//				}
//				return songArtists;
//				
//			}catch(SQLException e){
//				System.out.println("Query failed: " + e.getMessage());
//				return null;
//			}
	}
	
	//SELECT name, album, track FROM artist_list WHERE title = "Go Your Own Way"
	
	private int insertArtist(String name) throws SQLException{
		queryArtist.setString(1, name);
		ResultSet results = queryArtist.executeQuery();
		if(results.next()){
			return results.getInt(1);
		}else{
			//Insert the artist
			insertIntoArtists.setString(1, name);
			int affectedRows = insertIntoArtists.executeUpdate();
			if(affectedRows !=1){
				throw new SQLException("Could not insert artist");
			}
			
			ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
			if(generatedKeys.next()){
				return generatedKeys.getInt(1);
			}else{
				throw new SQLException("Could not get _id for artist");
			}
			
		}
	}
	
	private int insertAlbum(String name, int artistId) throws SQLException{
		queryAlbum.setString(1, name);
		ResultSet results = queryAlbum.executeQuery();
		if(results.next()){
			return results.getInt(1);
		}else{
			//Insert the album
			insertIntoAlbums.setString(1, name);
			insertIntoAlbums.setInt(2, artistId);
			int affectedRows = insertIntoAlbums.executeUpdate();
			if(affectedRows !=1){
				throw new SQLException("Could not insert album");
			}
			
			ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
			if(generatedKeys.next()){
				return generatedKeys.getInt(1);
			}else{
				throw new SQLException("Could not get _id for album");
			}
			
		}
	}
	
	public void insertSong(String title, String artist, String album, int track){
		try {
			 conn.setAutoCommit(false);
			 
			 int artistId = insertArtist(artist);
			 int albumId = insertAlbum(album, artistId);
			 insertIntoSongs.setInt(1, track);
			 insertIntoSongs.setString(2, title);
			 insertIntoSongs.setInt(3, albumId);
				int affectedRows = insertIntoSongs.executeUpdate();
				if(affectedRows ==1){
					conn.commit();
				}else{
					throw new SQLException("The song insert failed");
				}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Insert song exception" + e.getMessage());
			try{
				System.out.println("Performing rollback");
				conn.rollback();
			}catch(SQLException e2){
				System.out.println("Oh boy! Things are really bad" + e2.getMessage());
			}
		}finally{
			try{
				System.out.println("Resetting default commit behaviour");
				conn.setAutoCommit(true);
			}catch(SQLException e){
				System.out.println("Could not reset auto commit" + e.getMessage());
			}
		}
		
	}
	
	
	
}
