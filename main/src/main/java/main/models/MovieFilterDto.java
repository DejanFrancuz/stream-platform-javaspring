package main.models;

import lombok.Data;

@Data
public class MovieFilterDto {
    private String decade;
    private String genre;
}