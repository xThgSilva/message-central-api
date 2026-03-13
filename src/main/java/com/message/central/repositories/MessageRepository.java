package com.message.central.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.message.central.entities.Message;
import com.message.central.responses.MessageResponse;

@Repository
public interface MessageRepository extends JpaRepository <Message, Long>{

	// Query to find a conversation
	@Query("""
			   SELECT m FROM Message m
			   WHERE (m.senderUser.id = :user1 AND m.recipientUser.id = :user2)
			      OR (m.senderUser.id = :user2 AND m.recipientUser.id = :user1)
			   ORDER BY m.sendAt ASC
			""")
	List<MessageResponse> findConversation(Long user1, Long user2);
}
