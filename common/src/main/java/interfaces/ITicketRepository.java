package interfaces;
import domain.Ticket;

import java.util.List;

public interface ITicketRepository extends IRepository<Integer, Ticket> {
    List<Ticket> findByName(String name);
}