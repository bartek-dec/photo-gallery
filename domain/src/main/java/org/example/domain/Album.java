package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "album")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album implements Serializable, Comparable {

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

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Photo> photos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (name != null ? !name.equals(album.name) : album.name != null) return false;
        return tripDate != null ? tripDate.equals(album.tripDate) : album.tripDate == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (tripDate != null ? tripDate.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        Album album = (Album) o;
        return id.compareTo(album.getId());
    }
}
