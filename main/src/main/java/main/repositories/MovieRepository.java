package main.repositories;

import main.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
//    Page<Movie> findAllByIdIn(List<Long> movieIds, Pageable pageable);
Page<Movie> findAll(Pageable pageable);
Page<Movie> findAllByMovieIdNotIn(List<Long> movieIds, Pageable pageable);

    Page<Movie> findAllByMovieIdIn(List<Long> movieIds, Pageable pageable);
//    Page<Movie> find
}
