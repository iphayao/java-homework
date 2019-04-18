package com.wongnai.interview.movie.external;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MovieData {
	private String title;
	private int year;
	private List<String> cast;
	private List<String> genres;
}
