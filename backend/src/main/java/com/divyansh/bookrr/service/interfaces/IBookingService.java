package com.divyansh.bookrr.service.interfaces;

import com.divyansh.bookrr.dto.Response;
import com.divyansh.bookrr.entity.Booking;

public interface IBookingService {

    Response saveBooking(String roomId, String userId, Booking bookingRequest);

    Response findBookingByConfimationCode(String confimationCode);

    Response getAllBookings();

    Response cancelBooking(String bookingId);
}
