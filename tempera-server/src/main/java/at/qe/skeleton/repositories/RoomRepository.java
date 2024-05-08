package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Room;

import java.util.List;

public interface RoomRepository extends AbstractRepository<Room, String> {
    List<Room> findAllByRoomId(String roomId);
    boolean existsById(String roomId);
    Room findFirstByRoomId(String roomId);
}
