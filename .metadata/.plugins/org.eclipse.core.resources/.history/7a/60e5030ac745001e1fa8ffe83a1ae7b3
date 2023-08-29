package demo.boot.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class Consumer {

	@KafkaListener(topics = "codeDecode_Topic", groupId = "codeDecode_group")
	public String getMsg(String pgs) {
		System.out.println("The data from Managment side: " + pgs);
		return pgs;
	}

}
