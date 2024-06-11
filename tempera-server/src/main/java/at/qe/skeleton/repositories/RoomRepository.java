package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Threshold;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface RoomRepository extends AbstractRepository<Room, String> {
    List<Room> findAll();
    boolean existsById(String roomId);
    Room findFirstByRoomId(String roomId);

}
