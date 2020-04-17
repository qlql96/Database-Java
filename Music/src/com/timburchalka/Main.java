package com.timburchalka;

import java.util.List;
import java.util.Scanner;

import model.Artist;
import model.Datasource;
import model.SongArtist;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Datasource datasource = new Datasource();
		if(!datasource.open()){
			System.out.println("Can't open datasource");
			return;
		}
		List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_ASC);
		if(artists == null){
			System.out.println("No Artist");
		}
		
		for(Artist artist : artists){
			System.out.println("ID= " + artist.getId() + ", Name = " + artist.getName());
		}
		
		List<String> albumsForArtist = datasource.queryAlbumsForArtist("Pink Floyd", Datasource.ORDER_BY_ASC);
		
		for(String album: albumsForArtist){
			System.out.println(album);
		}
		
		List<SongArtist> songArtists = datasource.queryArtistsForSong("Heartless", Datasource.ORDER_BY_ASC);
		if(songArtists == null){
			System.out.println("Couldn't find the artist for the song");
		}
		
		for(SongArtist artist: songArtists){
			System.out.println("Artist name = " + artist.getArtistName() + 
					" Album name = " + artist.getAlbumName() +
					" Track = " + artist.getTrack());
			}
		
		
		int count = datasource.getCount(Datasource.TABLE_SONG);
		System.out.println("Number of songs is : " + count );
		
		datasource.createViewForSongArtists();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a song title: ");
		String title = scanner.nextLine();
		
		songArtists = datasource.querySongInfoView(title);
		if(songArtists.isEmpty()){
			System.out.println("Couldn't find the artist for the song");
			return;
		}
		
		for(SongArtist artist: songArtists){
			System.out.println("FROM VIEW = Artist name = " + artist.getArtistName() +
					" Album name = " + artist.getArtistName()+
					"Track number = " + artist.getTrack());
		}
	
		datasource.insertSong("Touch of Grey", "Grateful Dead", "In the Dark", 1);
		datasource.close();
		//SELECT name, album, 
	}

}
