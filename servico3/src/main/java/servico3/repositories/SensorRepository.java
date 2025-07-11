package servico3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import servico3.models.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long>{

}
