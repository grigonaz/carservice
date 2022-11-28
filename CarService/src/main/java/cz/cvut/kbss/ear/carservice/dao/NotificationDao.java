package cz.cvut.kbss.ear.carservice.dao;

import cz.cvut.kbss.ear.carservice.model.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
public class NotificationDao extends BaseDao<Notification> {
    protected NotificationDao() {
        super(Notification.class);
    }
}
