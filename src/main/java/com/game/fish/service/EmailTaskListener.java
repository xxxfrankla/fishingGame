package com.game.fish.service;

import com.game.fish.config.RabbitConfig;
import com.game.fish.model.PurchaseInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailTaskListener {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void processEmailQueue(PurchaseInfo message) {
        try {
            emailService.sendPurchaseConfirmationEmail(
                    message.getUserEmail(),
                    message.getUsername(),
                    message.getItemName(),
                    message.getCategory()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
