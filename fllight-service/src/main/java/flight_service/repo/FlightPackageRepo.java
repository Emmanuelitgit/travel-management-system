package flight_service.repo;

import flight_service.dto.projections.FlightPackageProjection;
import flight_service.models.FlightPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlightPackageRepo extends JpaRepository<FlightPackage, UUID> {

    @Query(value = "SELECT al.name AS airline, cl.name AS class_type, st.name AS seat_type, BIN_TO_UUID(fp.id) AS id," +
            "fp.available_seats, fp.trip_type, fp.non_stop, fp.departure_date, fp.arrival_date, " +
            "fp.description, fp.price, ap1.name AS departure, ap2.name AS destination " +
            "FROM flight_package_tb fp " +
            "JOIN flight_airline_type_tb al ON fp.airline_id=al.id " +
            "JOIN flight_class_type_tb cl ON cl.id=fp.class_type " +
            "JOIN flight_seat_type_tb st ON st.id=fp.seat_type " +
            "JOIN airport_tb ap1 ON fp.departure=ap1.id " +
            "JOIN airport_tb ap2 ON fp.destination=ap2.id ", nativeQuery = true)
    List<FlightPackageProjection> fetchAllFlightPackages();

    @Query(value = "SELECT al.name AS airline, cl.name AS class_type, st.name AS seat_type, BIN_TO_UUID(fp.id) AS id," +
            "fp.available_seats, fp.trip_type, fp.non_stop, fp.departure_date, fp.arrival_date, " +
            "fp.description, fp.price, ap1.name AS departure, ap2.name AS destination " +
            "FROM flight_package_tb fp " +
            "JOIN flight_airline_type_tb al ON fp.airline_id=al.id " +
            "JOIN flight_class_type_tb cl ON cl.id=fp.class_type " +
            "JOIN flight_seat_type_tb st ON st.id=fp.seat_type " +
            "JOIN airport_tb ap1 ON fp.departure=ap1.id " +
            "JOIN airport_tb ap2 ON fp.destination=ap2.id " +
            "WHERE fp.id=? ", nativeQuery = true)
    FlightPackageProjection findFlightPackageById(UUID id);
}
