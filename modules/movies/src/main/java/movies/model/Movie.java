package movies.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
//import org.springframework.data.annotation.Id;

@Entity
@Getter
@Setter
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer year;

    private String description;

    private String posterUrl;

    private String videoPreviewUrl; // npr. link do kratkog videa (ako hostuješ na disku ili cloudu)

    private String genre;

    public Movie(){

    }
    public Movie(String title, Integer year) {
        this.title = title;
        this.year = year;
    }
}
