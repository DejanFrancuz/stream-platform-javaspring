package movies.service;

import movies.model.Movie;
import movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import shared.interfaces.IService;

import java.util.List;
import java.util.Optional;

public class MovieService implements IService<Movie, Long> {

    private MovieRepository movieRepository;


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
