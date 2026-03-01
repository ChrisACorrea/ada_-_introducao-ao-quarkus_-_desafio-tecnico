package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Lesson {

    protected Lesson() {
    }

    public Lesson(String name) {
        this(null, name, null);
    }

    public Lesson(String name, Course course) {
        this(null, name, course);
    }

    public Lesson(Long id, String name, Course course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, insertable = true, updatable = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, insertable = true, updatable = true)
    private Course course;

    public Long getId() {
        return id;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public Course getCourse() {
        return course;
    }

    public Lesson withCourse(Course course) {
        return new Lesson(this.id, this.name, course);
    }
}
