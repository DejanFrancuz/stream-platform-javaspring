package main.controllers;

import main.models.Movie;
import main.models.User;
import main.services.MovieService;
import main.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/movies")
public class MovieController {




        private final MovieService movieService;

        private final UserService userService;


        public MovieController(MovieService movieService, UserService userService) {
            this.movieService = movieService;
            this.userService = userService;
        }

        //    @PreAuthorize("hasAuthority('can_read_users')")
        @GetMapping(value = "/all",
                produces = MediaType.APPLICATION_JSON_VALUE)
        public Page<Movie> getAllMovie(
                @RequestParam("page") int page,
                @RequestParam("size") int size
        ){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user =  userService.loadUserByEmail(username);

            Pageable pageable = PageRequest.of(page, size, Sort.by("title").descending());
            return movieService.getAllMovies(user.getOwnedMovies(), pageable);
        };

    @GetMapping(value = "/my",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Movie> getMyMovies(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        System.out.println("pre username "+username);

        User user =  userService.loadUserByEmail(username);

        System.out.println("username "+ user.getEmail());

        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return movieService.getUserMovies(user.getOwnedMovies(), pageable);
    }

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

        @PostMapping(value = "/add-movie-for-person",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> addMovieForPerson(@RequestBody long movieId) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User user =  userService.loadUserByEmail(username);


            Optional<Movie> optionalMovie =  movieService.findById(movieId);

            if(optionalMovie.isEmpty()) return ResponseEntity.notFound().build();

            Movie movie = optionalMovie.get();

            user.getOwnedMovies().add(movie.getMovieId());
            userService.save(user);

            movie.getOwners().add(user.getUserId());
            movieService.save(movie);

            return ResponseEntity.ok().build();

        }

        //    @PreAuthorize("hasAuthority('can_delete_users')")
        @DeleteMapping( value = "/delete")
        public ResponseEntity<?> deleteUser(@RequestParam("id") Long id){
            this.movieService.deleteById(id);
            return ResponseEntity.ok().build();
        }

    }
