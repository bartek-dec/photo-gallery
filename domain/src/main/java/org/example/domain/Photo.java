package org.example.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "photo")
@Data
public class Photo implements Serializable {

    private static final long serialVersionUID = 9089647886114593402L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @Lob
    private Byte[] image;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    public Photo() {
    }
}
