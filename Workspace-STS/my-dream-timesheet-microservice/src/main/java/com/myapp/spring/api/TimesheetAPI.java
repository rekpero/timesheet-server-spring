package com.myapp.spring.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.myapp.spring.client.PhasesClient;
import com.myapp.spring.model.Phases;
import com.myapp.spring.model.Project;
import com.myapp.spring.model.Timesheet;
import com.myapp.spring.repository.TimesheetRepository;

@RestController
public class TimesheetAPI {

	@Autowired
	private TimesheetRepository repository;
	
	@Autowired
	private PhasesClient phasesClient;
	
	
	
	
	@GetMapping("/timesheet")
	public ResponseEntity<List<Timesheet>> findAll(){
		
		List<Timesheet> sheets=repository.findAll();
		Project project=sheets.stream().map(t->t.getProject()).findFirst().get().get(0);
		System.out.println("project "+project+" "+project.getPhases());
		
		
		List<Phases> phases=Arrays.asList(phasesClient.findById(project.getId()));
		project.setPhases(phases);
		//sheets.forEach(timesheet->timesheet.setPhases(phases));
		
		return new ResponseEntity<List<Timesheet>>(sheets, HttpStatus.OK);
	}
	
	@PostMapping("/timesheet")
	public ResponseEntity<Timesheet> addTimesheet(@RequestBody Timesheet timesheet){
		
		phasesClient.addNewPhases(timesheet.getProject().get(0).getPhases().get(0));
		//clientsClient.addNewClients(timesheet.getClients());
		timesheet=repository.save(timesheet);
		
		return new ResponseEntity<Timesheet>(timesheet, HttpStatus.CREATED);
	}
}