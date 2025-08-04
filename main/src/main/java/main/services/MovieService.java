package main.services;

import main.interfaces.IService;
import main.models.Movie;
import main.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService implements IService<Movie, Long> {

    private MovieRepository movieRepository;


    public Page<Movie> getAllMovies(List<Long> movieIds, Pageable pageable) {
        return movieIds.isEmpty() ? movieRepository.findAll(pageable) : movieRepository.findAllByMovieIdNotIn(movieIds,pageable);
    }

    public Page<Movie> getUserMovies(List<Long> movieIds, Pageable pageable) {
        return movieRepository.findAllByMovieIdIn(movieIds, pageable);
    }

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie save(Movie movie) {
        return  movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

}
