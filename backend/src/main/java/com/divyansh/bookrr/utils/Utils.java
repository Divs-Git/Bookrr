package com.divyansh.bookrr.utils;

import com.divyansh.bookrr.dto.BookingDto;
import com.divyansh.bookrr.dto.RoomDto;
import com.divyansh.bookrr.dto.UserDto;
import com.divyansh.bookrr.entity.Booking;
import com.divyansh.bookrr.entity.Room;
import com.divyansh.bookrr.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDto mapUserEntityToUserDto(User user) {
        UserDto UserDto = new UserDto();

        UserDto.setId(user.getId());
        UserDto.setName(user.getName());
        UserDto.setEmail(user.getEmail());
        UserDto.setPhoneNumber(user.getPhoneNumber());
        UserDto.setRole(user.getRole());
        return UserDto;
    }

    public static RoomDto mapRoomEntityToRoomDto(Room room) {
        RoomDto RoomDto = new RoomDto();

        RoomDto.setId(room.getId());
        RoomDto.setRoomType(room.getRoomType());
        RoomDto.setRoomPrice(room.getRoomPrice());
        RoomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        RoomDto.setRoomDescription(room.getRoomDescription());
        return RoomDto;
    }

    public static BookingDto mapBookingEntityToBookingDto(Booking booking) {
        BookingDto BookingDto = new BookingDto();

        BookingDto.setId(booking.getId());
        BookingDto.setCheckInDate(booking.getCheckInDate());
        BookingDto.setCheckOutDate(booking.getCheckOutDate());
        BookingDto.setNumOfChildren(booking.getNumOfChildren());
        BookingDto.setNumOfAdults(booking.getNumOfAdults());
        BookingDto.setTotalNumOfGuests(booking.getTotalNumOfGuests());
        BookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return BookingDto;
    }

    public static RoomDto mapRoomEntityToRoomDtoPlusBookings(Room room) {
        RoomDto RoomDto = new RoomDto();

        RoomDto.setId(room.getId());
        RoomDto.setRoomType(room.getRoomType());
        RoomDto.setRoomPrice(room.getRoomPrice());
        RoomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        RoomDto.setRoomDescription(room.getRoomDescription());

        if (room.getBookings() != null) {
            RoomDto.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList()));
        }

        return RoomDto;
    }

    public static BookingDto mapBookingEntityToBookingDtoPlusBookedRooms(Booking booking, boolean mapUser) {
        BookingDto BookingDto = new BookingDto();

        BookingDto.setId(booking.getId());
        BookingDto.setCheckInDate(booking.getCheckInDate());
        BookingDto.setCheckOutDate(booking.getCheckOutDate());
        BookingDto.setNumOfChildren(booking.getNumOfChildren());
        BookingDto.setNumOfAdults(booking.getNumOfAdults());
        BookingDto.setTotalNumOfGuests(booking.getTotalNumOfGuests());
        BookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (mapUser) {
            BookingDto.setUser(Utils.mapUserEntityToUserDto(booking.getUser()));
        }

        if (booking.getRoom() != null) {

            RoomDto RoomDto = new RoomDto();

            RoomDto.setId(booking.getRoom().getId());
            RoomDto.setRoomType(booking.getRoom().getRoomType());
            RoomDto.setRoomPrice(booking.getRoom().getRoomPrice());
            RoomDto.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            RoomDto.setRoomDescription(booking.getRoom().getRoomDescription());

            BookingDto.setRoom(RoomDto);
        }

        return BookingDto;
    }

    public static UserDto mapUserEntityToUserDtoPlusUserBookingsAndRoom(User user) {
        UserDto UserDto = new UserDto();

        UserDto.setId(user.getId());
        UserDto.setName(user.getName());
        UserDto.setEmail(user.getEmail());
        UserDto.setPhoneNumber(user.getPhoneNumber());
        UserDto.setRole(user.getRole());

        if (!user.getBookings().isEmpty()) {
            UserDto.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDtoPlusBookedRooms(booking, false)).collect(Collectors.toList()));
        }
        return UserDto;
    }

    public static List<UserDto> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDto).collect(Collectors.toList());
    }

    public static List<RoomDto> mapRoomListEntityToRoomListDTO(List<Room> roomList) {
        return roomList.stream().map(Utils::mapRoomEntityToRoomDto).collect(Collectors.toList());
    }

    public static List<BookingDto> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList());
    }

}
