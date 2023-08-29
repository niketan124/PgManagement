package demo.boot.service;

import java.util.List;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import demo.boot.model.Pgmain;

@Service
public class Producer {

//	@Autowired
//	KafkaTemplate<String, List<Pgmain>> kafkaTemplate;
//
//	public void sendMsgToTopic(List<Pgmain> pgs) {
//		kafkaTemplate.send("codeDecode_Topic", pgs);
//	}
//	

	@Autowired
	KafkaTemplate<List<Pgmain>, List<Pgmain>> kafkaTemplate;

	public void sendMsgToTopic(List<Pgmain> pg) {
		// TODO Auto-generated method stub
		kafkaTemplate.send("codeDecode_Topic", pg);
	}
}