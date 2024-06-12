package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Room;


import java.util.List;



public interface RoomRepository extends AbstractRepository<Room, String> {
    List<Room> findAll();
    boolean existsById(String roomId);
    Room findFirstByRoomId(String roomId);



}
