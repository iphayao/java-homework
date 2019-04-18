package com.wongnai.interview.movie;

import com.wongnai.interview.movie.external.MovieData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "cast", target = "actors")
    Movie movieDataToMovie(MovieData movie);
}
