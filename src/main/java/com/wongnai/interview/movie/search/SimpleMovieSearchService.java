package com.wongnai.interview.movie.search;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.wongnai.interview.movie.MovieMapper;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class
		MoviesResponse movies = movieDataService.fetchAll();

		List<MovieData> moviesFiltered = movies.stream()
				.filter(m -> validateTitleWords(m.getTitle(), queryText) && !validateTitle(m.getTitle(), queryText))
				.collect(Collectors.toList());

		return moviesFiltered.stream()
				.map(MovieMapper.INSTANCE::movieDataToMovie)
				.collect(Collectors.toList());
	}

	private boolean validateTitleWords(String title, String queryText) {
		return Arrays.asList(title.toLowerCase().split(" ")).contains(queryText.toLowerCase());
	}

	private boolean validateTitle(String title, String queryText) {
		return title.toLowerCase().equals(queryText.toLowerCase());
	}
}
