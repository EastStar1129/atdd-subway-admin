package nextstep.subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    Optional<Station> findById(Long id);
}