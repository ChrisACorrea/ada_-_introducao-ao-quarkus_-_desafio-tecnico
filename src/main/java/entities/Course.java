package entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Course {

    protected Course() {
    }

    public Course(String name) {
        this(null, name, Collections.emptySet());
    }

    public Course(String name, Set<Lesson> lessons) {
        this(null, name, lessons);
    }

    public Course(Long id, String name, Set<Lesson> lessons) {
        this.id = id;
        this.name = name;
        this.lessons = lessons != null ? lessons : new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3)
    @Column(nullable = false, insertable = true, updatable = true)
    private String name;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(lessons);
    }

    public boolean addLesson(Lesson lesson) {
        return lessons.add(lesson);
    }

    public boolean addLessons(Set<Lesson> lessons) {
        return this.lessons.addAll(lessons);
    }

    public boolean removeLessonById(Long lessonId) {
        return lessons.removeIf(lesson -> lesson.getId().equals(lessonId));
    }

    public boolean removeLesson(Lesson lesson) {
        return lessons.remove(lesson);
    }
}
