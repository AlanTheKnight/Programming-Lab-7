package alantheknight.lab6.server.managers;


import alantheknight.lab6.common.models.Worker;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static alantheknight.lab6.server.Main.dbManager;

/**
 * The CollectionManager class is responsible for managing the collection of
 * workers.
 */
public class CollectionManager {
    public static Comparator<Worker> workerCoordinatesComparator = new Comparator<>() {
        @Override
        public int compare(Worker o1, Worker o2) {
            return o1.coordinates.getValue().compareTo(o2.coordinates.getValue());
        }
    };

    private Map<Integer, Worker> workers = Collections.synchronizedMap(new TreeMap<>());

    private LocalDateTime lastInitTime = null;

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public void pullFromDB() throws IllegalArgumentException, SQLException {
        workers = dbManager.getAllWorkers();
        lastInitTime = LocalDateTime.now();
    }

    public Map<Integer, Worker> getWorkers() {
        return workers;
    }

    public Collection<Worker> getCollection() {
        return workers.values();
    }

    public int removeWorkerById(int id, int userId) throws Exception {
        var res = dbManager.removeWorker(id, userId);
        if (res > 0) pullFromDB();
        return res;
    }

    public int removeGreaterKey(int key, int userId) throws Exception {
        var res = dbManager.removeGreaterId(key, userId);
        if (res > 0) pullFromDB();
        return res;
    }

    public int removeWorkerByEndDate(LocalDate endDate, int userId) throws Exception {
        var res = dbManager.removeAnyByEndDate(endDate, userId);
        if (res > 0) pullFromDB();
        return res;
    }

    public int removeGreaterEndDate(LocalDate endDate, int userId) throws Exception {
        var res = dbManager.removeGreaterEndDate(endDate, userId);
        if (res > 0)pullFromDB();
        return res;
    }

    public void insertWorker(Worker worker, int userId) throws Exception {
        dbManager.createWorker(worker, userId);
        pullFromDB();
    }

    public void updateWorker(Worker worker, int userId) throws Exception {
        dbManager.updateWorker(worker, userId);
        pullFromDB();
    }

    public void clear() throws Exception {
        dbManager.clear();
        pullFromDB();
    }

    public boolean hasWorkerWithId(int id) {
        return workers.containsKey(id);
    }
}
