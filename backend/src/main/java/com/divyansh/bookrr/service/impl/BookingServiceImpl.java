package com.divyansh.bookrr.service.impl;

import com.divyansh.bookrr.dto.BookingDto;
import com.divyansh.bookrr.dto.Response;
import com.divyansh.bookrr.entity.Booking;
import com.divyansh.bookrr.entity.Room;
import com.divyansh.bookrr.entity.User;
import com.divyansh.bookrr.exception.GlobalException;
import com.divyansh.bookrr.repository.BookingRepository;
import com.divyansh.bookrr.repository.RoomRepository;
import com.divyansh.bookrr.repository.UserRepository;
import com.divyansh.bookrr.service.interfaces.IBookingService;
import com.divyansh.bookrr.service.interfaces.IRoomService;
import com.divyansh.bookrr.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IRoomService  roomService;

    @Autowired
    private RoomRepository roomRepository;


    @Override
    public Response saveBooking(String roomId, String userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Booking Check Out Date cannot be before Check In Date");
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new GlobalException("Room Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Not Found"));

            List<Booking> existingBookings = room.getBookings();
            if(!roomIsAvailable(bookingRequest,existingBookings)) {
                throw new GlobalException("Room not available for the selected date range");
            }

            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);


            // save booking
            Booking savedBooking = bookingRepository.save(bookingRequest);

            // Add the booking to the user's bookings list
            List<Booking> userBookings = user.getBookings();
            userBookings.add(savedBooking);
            user.setBookings(userBookings);
            userRepository.save(user);


            // Add the booking to the room's bookings list
            List<Booking> roomBookings = room.getBookings();
            roomBookings.add(savedBooking);
            room.setBookings(roomBookings);
            roomRepository.save(room);


            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (GlobalException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a  booking " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfimationCode(String confimationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confimationCode).orElseThrow(() -> new GlobalException("Booking Not Found"));
            BookingDto bookingDTO = Utils.mapBookingEntityToBookingDtoPlusBookedRooms(booking, true);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setBooking(bookingDTO);

        } catch (GlobalException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting booking by confirmation code " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDto> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setBookingList(bookingDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(String bookingId) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new GlobalException("Booking Not Found"));

            // Remove the booking from the associated user
            User user = booking.getUser();
            if (user != null) {
                user.getBookings().removeIf(b -> b.getId().equals(bookingId));
                userRepository.save(user);
            }

            // Remove the booking from the associated room
            Room room = booking.getRoom();
            if (room != null) {
                room.getBookings().removeIf(b -> b.getId().equals(bookingId));
                roomRepository.save(room);
            }

            // Delete the booking
            bookingRepository.deleteById(bookingId);

            response.setMessage("successful");
            response.setStatusCode(200);

        } catch (GlobalException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error cancelling a booking " + e.getMessage());
        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );

    }
}
