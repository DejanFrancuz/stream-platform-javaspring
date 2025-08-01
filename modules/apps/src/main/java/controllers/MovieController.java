package controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import models.Movie;
import services.MovieService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/movies")
public class MovieController {




        private final MovieService movieService;

        public MovieController(MovieService movieService) {
            this.movieService = movieService;
        }

        //    @PreAuthorize("hasAuthority('can_read_users')")
        @GetMapping(value = "/all",
                produces = MediaType.APPLICATION_JSON_VALUE)
        public List<Movie> getAllMovie(){
            System.out.println("ru sam");
            return movieService.findAll();
        };

        @GetMapping(value = "/get-one",
                produces = MediaType.APPLICATION_JSON_VALUE)
        public Optional<Movie> getMovie(@RequestParam("movieId") Long id){
            return movieService.findById(id);
        };

        //    @PreAuthorize("hasAuthority('can_update_users')")
        @PutMapping(value = "/update",
                consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> updateMovie(@RequestBody Movie movieDto) {
            Optional<Movie> optionalMovie = movieService.findById(movieDto.getMovieId());

            if (optionalMovie.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Movie movie = optionalMovie.get();

            movie.setTitle(movieDto.getTitle());
            movie.setYear(movieDto.getYear());
            movie.setDescription(movieDto.getDescription());
            movie.setGenre(movieDto.getGenre());

            return ResponseEntity.ok(movieService.save(movie));

        }

        //    @PreAuthorize("hasAuthority('can_write_users')")
        @PostMapping(value = "/add",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public Movie createMovie(@RequestBody Movie movie) {
            return movieService.save(movie);
        }

        //    @PreAuthorize("hasAuthority('can_delete_users')")
        @DeleteMapping( value = "/delete")
        public ResponseEntity<?> deleteUser(@RequestParam("id") Long id){
            this.movieService.deleteById(id);
            return ResponseEntity.ok().build();
        }

    }
