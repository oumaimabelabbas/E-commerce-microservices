package com.oumaima.notification.notification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface NotificationRepository extends MongoRepository<Notification,String> {
}
