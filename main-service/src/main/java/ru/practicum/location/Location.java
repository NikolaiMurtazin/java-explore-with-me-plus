package ru.practicum.location;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private long id;
    @Column(name = "lat", nullable = false)
    private float lat;
    @Column(name = "lon", nullable = false)
    private float lon;
}
