package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.Location;
import ru.practicum.category.model.State;

import java.time.LocalDateTime;

@Entity
@Table(name = "event", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name", nullable = false)
    private Category category;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location", nullable = false)
    private Location location;

    @Column(name = "created", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;
}
