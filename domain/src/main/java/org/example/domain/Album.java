package org.example.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "album")
@Data
public class Album implements Serializable {

    private static final long serialVersionUID = 2003091824768618971L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "trip_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tripDate;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Photo> photos = new ArrayList<>();

    public Album() {
    }
}
