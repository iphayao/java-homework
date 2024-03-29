package com.wongnai.interview.movie.search;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
    @Autowired
    private MovieRepository movieRepository;

    private List<Movie> movieList;
    private Map<String, Set<Long>> movieIndex;

    @Override
    public List<Movie> search(String queryText) {
        //TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
        // You must find a way to build inverted index before you do an actual search.
        // Inverted index would looks like this:
        // -------------------------------
        // |  Term      | Movie Ids      |
        // -------------------------------
        // |  Star      |  5, 8, 1       |
        // |  War       |  5, 2          |
        // |  Trek      |  1, 8          |
        // -------------------------------
        // When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
        // there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
        // Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
        // from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
        // you have to return can be union or intersection of those 2 sets of ids.
        // By the way, in this assignment, you must use intersection so that it left for just movie id 5.

        // Create In-Memory inverted index at first time
        if (movieIndex == null) {
            movieList = (List<Movie>) movieRepository.findAll();
            movieIndex = createMovieIndex(movieList);
        }

        // extract words of query text to search all word
        List<String> queryWord = Arrays.asList(queryText.toLowerCase().split(" "));
        // searching all word
        List<Set<Long>> result = queryWord.stream()
                .map(m -> movieIndex.get(m))
                .collect(Collectors.toList());

        // create new set for intersection results
        Set<Long> movies = createNewSet(result);
        // perform intersection of found results
        if (!movies.isEmpty()) {
            result.forEach(movies::retainAll);
        }

        // get movie information from movie list then return
        return movies.stream()
                .map(c -> movieList.stream()
                        .filter(x -> x.getId().equals(c))
                        .findFirst().get())
                .collect(Collectors.toList());
    }

    private HashSet<Long> createNewSet(List<Set<Long>> result) {
        return (result.get(0) != null) ? new HashSet<>(result.get(0)) : new HashSet<>();
    }

    private Map<String, Set<Long>> createMovieIndex(List<Movie> movies) {
        Map<String, Set<Long>> movieIndex = new HashMap<>();
        movies.forEach(m -> {
            Arrays.asList(m.getName().toLowerCase().split(" ")).forEach(n -> {
                if(movieIndex.containsKey(n)) {
                    movieIndex.get(n).add(m.getId());
                } else {
                    Set<Long> set = new HashSet<>();
                    set.add(m.getId());
                    movieIndex.put(n, set);
                }
            });
        });

        return movieIndex;
    }
}
