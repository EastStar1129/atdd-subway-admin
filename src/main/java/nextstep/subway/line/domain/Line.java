package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.common.Distance;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> stations() {
        return sections.stations();
    }

    public Section toSection(Station upStation, Station downStation, Distance distance) {
        Section section = new Section(upStation, downStation, distance, this);
        sections.toSection(section);
        return section;
    }

    public void deleteSection(Station deleteStation) {
        sections.deleteSection(deleteStation);
    }
}
