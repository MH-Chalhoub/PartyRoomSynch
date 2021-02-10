package info403.exam1920.q2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AccessTest {
	
	public static final int CAPACITE_DE_LA_SALLE = 10;
	private static Integer current_capacity = 0;
	
	public static void main(String[] args) {
		
		// This is the max number of groups (suppose that each group contains one person we could have CAPACITE_DE_LA_SALLE group)
		Group[] groups = new Group[CAPACITE_DE_LA_SALLE];
		Semaphore mutex = new Semaphore(1);
		ArrayList<Person> persons = new ArrayList<>();
		
		//initializing the groups with random variables as capacity and the semaphores with 0
		for (int i=0; i<groups.length; i++) {
			int rand = new Random().nextInt(CAPACITE_DE_LA_SALLE);
			groups[i] = new Group(rand, 0, "Group " + i, new Semaphore(0), current_capacity, mutex, groups);
			System.out.println(groups[i].toString());
		}
		
		//Making a ArrayList of Person to join the groups
		for (int j=0; j<=20; j++) {
			persons.add(new Person("Person " + j, groups, mutex));
			System.out.println(persons.get(j));
		}

		//Launching finding group process
		for(Person person : persons) {
			person.start();
		}
	}

}
