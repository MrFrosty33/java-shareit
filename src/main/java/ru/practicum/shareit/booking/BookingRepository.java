package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    //todo вопрос по методу сортировки. По финальной ли дате сортировать?

    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId
            ORDER BY b.endDate DESC
            """)
    List<Booking> findAllByBookerId(@Param("bookerId") Long bookerId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId
            ORDER BY b.endDate DESC
            """)
    List<Booking> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId AND status = :status
            ORDER BY b.endDate DESC
            """)
    List<Booking> findWaitingOrRejectedByBookerId(@Param("status") String status,
                                                  @Param("bookerId") Long bookerId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId AND status = :status
            ORDER BY b.endDate DESC
            """)
    List<Booking> findWaitingOrRejectedByOwnerId(@Param("status") String status,
                                                 @Param("ownerId") Long ownerId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId AND b.startDate <= :date AND b.endDate >= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findCurrentByBookerId(@Param("bookerId") Long bookerId, @Param("date") LocalDate date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.startDate <= :date AND b.endDate >= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findCurrentByOwnerId(@Param("ownerId") Long ownerId, @Param("date") LocalDate date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId AND b.endDate <= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findPastByBookerId(@Param("bookerId") Long bookerId, @Param("date") LocalDate date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.endDate <= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findPastByOwnerId(@Param("ownerId") Long ownerId, @Param("date") LocalDate date);


    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId AND b.startDate >= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findFutureByBookerId(@Param("bookerId") Long bookerId, @Param("date") LocalDate date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.startDate >= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findFutureByOwnerId(@Param("ownerId") Long ownerId, @Param("date") LocalDate date);

    @Query("""
            UPDATE Booking b
            SET b.status = :status
            WHERE b.id = :bookingId
            """)
    Booking updateStatus(@Param("bookingId") Long bookingId, @Param("status") String status);
}
