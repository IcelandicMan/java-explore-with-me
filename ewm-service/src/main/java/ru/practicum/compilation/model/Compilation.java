package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @Column(name = "pinned")
    Boolean pinned;

    @Column(name = "title")
    String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),     //внешний ключ для объекта, для которого определяем сопоставление ассоциации.
            inverseJoinColumns = @JoinColumn(name = "event_id"))    //внешний ключ связанного объекта.
    Set<Event> events;
}

