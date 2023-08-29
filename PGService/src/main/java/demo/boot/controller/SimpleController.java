package demo.boot.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.boot.dao.PgMgmtDao;
import demo.boot.exception.PgNotFoundException;
import demo.boot.model.Pgmain;
import demo.boot.service.Producer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
public class SimpleController {

	private final PgMgmtDao pgDao;

	@Autowired
	public SimpleController(PgMgmtDao pgDao) {
		this.pgDao = pgDao;
	}

	@Autowired
	Producer producer;
//
//	@Autowired
//	Consumer consumer;

	@GetMapping("/greet")
	public String sayHello() {
		return "Hello guys!";
	}

	@GetMapping("/")
	public ResponseEntity<List<Pgmain>> getAllTasks() {
		List<Pgmain> pgs = (List<Pgmain>) pgDao.findAll();
		producer.sendMsgToTopic(pgs);
		for (Pgmain pg : pgs) {
			pg.add(linkTo(methodOn(SimpleController.class).getAllTasks()).withSelfRel());
			pg.add(linkTo(SimpleController.class).slash("id").withRel("Pgmain By Id"));
			pg.add(linkTo(methodOn(SimpleController.class).createTask(pg)).withRel("Create Pg"));
		}

		return ResponseEntity.ok(pgs);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Pgmain>> getPgById(@PathVariable Long id) {
		Optional<Pgmain> pgs = pgDao.findById(id);
		System.out.println(pgs);

		if (pgs.isEmpty()) {
			throw new PgNotFoundException("Pg with id not found");
		}

		return ResponseEntity.ok(pgs);
	}

	@PostMapping("/")
	@Transactional
	public ResponseEntity<Pgmain> createTask(@Valid @RequestBody Pgmain pg) {
		Pgmain createdPg = pgDao.save(pg);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPg);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pgmain> updatePg(@Valid @RequestBody Pgmain pg, @PathVariable Long id) {
		Optional<Pgmain> createdPg = pgDao.findById(id);

		Pgmain existingPg = createdPg.get();
//		existingPg.setLocation(pg.getLocation());
		existingPg.setRoom(pg.getRoom());
		Pgmain updatedPg = pgDao.save(existingPg);
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedPg);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<Pgmain>> deleteById(@PathVariable Long id) {
		Optional<Pgmain> pgs = pgDao.findById(id);
		pgDao.delete(pgs.get());
		System.out.println(pgs);

		if (!pgs.isPresent()) {
			throw new PgNotFoundException("Pg with id not found");
		}

		return ResponseEntity.noContent().build();
	}

}
