package com.divyansh.bookrr.service.impl;

import com.divyansh.bookrr.dto.Response;
import com.divyansh.bookrr.dto.RoomDto;
import com.divyansh.bookrr.entity.Booking;
import com.divyansh.bookrr.entity.Room;
import com.divyansh.bookrr.exception.GlobalException;
import com.divyansh.bookrr.repository.BookingRepository;
import com.divyansh.bookrr.repository.RoomRepository;
import com.divyansh.bookrr.service.interfaces.IRoomService;
import com.divyansh.bookrr.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CloudinaryImageServiceImpl cloudinaryImageService;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();

        try {
            Map imageInfo = cloudinaryImageService.upload(photo);
            Room room = new Room();
            room.setRoomPhotoUrl(imageInfo.get("url").toString());
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);
            RoomDto roomDto = Utils.mapRoomEntityToRoomDto(savedRoom);
            response.setStatusCode(201);
            response.setMessage("Success");
            response.setRoom(roomDto);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " +  e.getMessage());
        }


        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomType();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.findAll();
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setRoomList(roomDtoList);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " +  e.getMessage());
        }


        return response;
    }

    @Override
    public Response deleteRoom(String roomId) {
        Response response = new Response();

        try {
            roomRepository.deleteById(roomId);

            response.setStatusCode(200);
            response.setMessage("Success");

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " +  e.getMessage());
        }


        return response;
    }

    @Override
    public Response updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
                imageUrl = cloudinaryImageService.upload(photo).get("url").toString();
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new GlobalException("Room Not Found"));
            if (roomType != null && !roomType.isEmpty()) {
                room.setRoomType(roomType);
            }
            if (description != null && !description.isEmpty()) {
                room.setRoomDescription(description);
            }
            if (roomPrice != null) {
                room.setRoomPrice(roomPrice);
            }
            if (imageUrl != null && !imageUrl.isEmpty()) {
                room.setRoomPhotoUrl(imageUrl);
            }

            Room savedRoom = roomRepository.save(room);
            RoomDto roomDto = Utils.mapRoomEntityToRoomDto(savedRoom);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setRoom(roomDto);

        }catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " +  e.getMessage());
        }


        return response;
    }

    @Override
    public Response getRoomById(String roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new GlobalException("Room Not Found"));
            RoomDto roomDto = Utils.mapRoomEntityToRoomDtoPlusBookings(room);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setRoom(roomDto);

        }catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " +  e.getMessage());
        }


        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findBookingByDateRange(checkInDate, checkOutDate);
            List<String> bookedRoomIds = bookingList.stream().map(Booking::getId).toList();

            List<Room> roomList = roomRepository.findByRoomTypeLikeAndIdNotIn(roomType, bookedRoomIds);
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setRoomList(roomDtoList);

        }catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " +  e.getMessage());
        }


        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.findAllAvailableRoom();
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(roomList);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setRoomList(roomDtoList);

        }catch (GlobalException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error: " +  e.getMessage());
        }


        return response;
    }
}
