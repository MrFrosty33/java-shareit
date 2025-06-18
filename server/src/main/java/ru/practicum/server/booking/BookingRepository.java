package ru.practicum.server.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.models.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
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
    List<Booking> findCurrentByBookerId(@Param("bookerId") Long bookerId, @Param("date") LocalDateTime date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.startDate <= :date AND b.endDate >= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findCurrentByOwnerId(@Param("ownerId") Long ownerId, @Param("date") LocalDateTime date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId AND b.endDate <= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findPastByBookerId(@Param("bookerId") Long bookerId, @Param("date") LocalDateTime date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.endDate <= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findPastByOwnerId(@Param("ownerId") Long ownerId, @Param("date") LocalDateTime date);


    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId AND b.startDate >= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findFutureByBookerId(@Param("bookerId") Long bookerId, @Param("date") LocalDateTime date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.startDate >= :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findFutureByOwnerId(@Param("ownerId") Long ownerId, @Param("date") LocalDateTime date);

    @Query("""
            SELECT b.booker.id FROM Booking b
            WHERE b.booker.id = :bookerId AND b.item.id = :itemId
            """)
    List<Long> findBookerIdsByBookerIdAndItemId(@Param("bookerId") Long bookerId, @Param("itemId") Long itemId);

    @Query("""
            SELECT b.status FROM Booking b
            WHERE b.booker.id = :bookerId AND b.item.id = :itemId
            AND b.endDate < :date
            ORDER BY b.endDate DESC
            """)
    List<Status> getLastBookingStatusByBookerIdAndItemId(@Param("bookerId") Long bookerId,
                                                         @Param("itemId") Long itemId,
                                                         @Param("date") LocalDateTime date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.booker.id = :bookerId
            AND b.item.id = :itemId
            AND b.endDate < :date
            ORDER BY b.endDate DESC
            """)
    @Transactional(readOnly = true)
    List<Booking> getLastBookingByBookerIdAndItemId(@Param("bookerId") Long bookerId,
                                                    @Param("itemId") Long itemId,
                                                    @Param("date") LocalDateTime date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.id = :itemId
            AND b.startDate > :date
            ORDER BY b.startDate ASC
            """)
    List<Booking> findNextBooking(@Param("itemId") Long itemId,
                                  @Param("date") LocalDateTime date);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.item.id = :itemId
            AND b.endDate < :date
            ORDER BY b.endDate DESC
            """)
    List<Booking> findPreviousBooking(@Param("itemId") Long itemId,
                                      @Param("date") LocalDateTime date);
}
