package com.qa.business.repository;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import static javax.transaction.Transactional.TxType.REQUIRED;

import org.apache.log4j.Logger;

import com.qa.persistence.domain.Movie;
import com.qa.util.JSONUtil;

public class MovieDBRepository implements IMovieRepository {
	private static final Logger LOGGER = Logger.getLogger(MovieDBRepository.class);
   
	@PersistenceContext(unitName = "primary")
	private EntityManager manager;
	
    @Inject
    private JSONUtil util;
  
	
	@Override
	public String getAllMovies() {
		Query query = manager.createQuery("Select m FROM Movie m");
		Collection<Movie> movies = (Collection<Movie>) query.getResultList();
		return util.getJSONForObject(movies);
				
	}


	@Override
	public String getAMovie(Long id) {
		Movie aMovie = findMovie(id);
		if(aMovie != null) {
			return util.getJSONForObject(aMovie);
		}
		else {
			return "{\"message\":\"movie not found\"}";
		}
		
	}


	private Movie findMovie(Long id) {
		return manager.find(Movie.class, id);
	}


	@Override
	@Transactional(REQUIRED)
	public String createMovie(String movieJSON) {
	
		Movie aMovie = util.getObjectForJSON(movieJSON, Movie.class);
		manager.persist(aMovie);
		return "{\"message\":\"movie created\"}";
		
	}


	
	
	
	}



