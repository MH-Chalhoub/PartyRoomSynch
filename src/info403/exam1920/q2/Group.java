package info403.exam1920.q2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Group {
	int capacity;
	int waiting_people;
	String name;
	Semaphore s;
	Integer current_capacity;
	ArrayList<Person> persons = new ArrayList<>();
	Semaphore mutex;
	boolean inside_the_room = false;
	Group[] groups;
	
	public Group(int capacity,int waiting_people, String name, Semaphore s, Integer current_capacity, Semaphore mutex, Group[] groups) {
		super();
		this.capacity = capacity;
		this.waiting_people = waiting_people;
		this.name = name;
		this.s = s;
		this.current_capacity = current_capacity;
		this.mutex = mutex;
		this.groups = groups;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getWaiting_people() {
		return waiting_people;
	}

	public void setWaiting_people(int waiting_people) {
		this.waiting_people = waiting_people;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Semaphore getS() {
		return s;
	}

	public void setS(Semaphore s) {
		this.s = s;
	}

	public Integer getCurrent_capacity() {
		return current_capacity;
	}

	public void setCurrent_capacity(Integer current_capacity) {
		this.current_capacity = current_capacity;
	}

	public void join(Person person) throws InterruptedException {
		System.out.println("Trying to join the group : " + this.getName());
		if(!inside_the_room) {
			if(waiting_people < capacity) {
				waiting_people++;
				persons.add(person);
				System.out.println("Waiting until " + this.getName() + " is full");
				System.out.println(this.toString());
				s.acquire();
			}
			if(waiting_people == capacity) {
				releaseAllPersons();
				System.out.println("This Group reached the limit he will enter the room now");
				System.out.println(this.toString());
			}
		}
	}
	
	public void leave(Person person) throws InterruptedException {
		System.out.println("Trying to leave the group : " + this.getName());
		mutex.acquire();
		waiting_people--;
		current_capacity--;
		persons.remove(person);
		mutex.release();
		if(waiting_people == 0) {
			System.out.println("I'm the last memeber of " + this.getName() + 
					"and i'm trying to wake up the other group that are trying to enter the room");
			inside_the_room = false;
			for(int i=0; i<groups.length; i++) {
				if(!groups[i].inside_the_room && waiting_people == capacity) {
					releaseAllPersons();
					System.out.println("This Group " + this.getName() + " reached the limit but he was blocked but after " + groups[i].getName() + " leaved the room, he will enter the room in his place");
					System.out.println(this.toString());
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Group [capacity=" + capacity + ", waiting_people=" + waiting_people + ", name=" + name
				+ ", current_capacity_of_the_room=" + current_capacity + ", is_group_inside_the_room=" + inside_the_room + "]";
	}

	public void releaseAllPersons() throws InterruptedException {
		mutex.acquire();
		if(waiting_people + current_capacity < AccessTest.CAPACITE_DE_LA_SALLE) {
			for(int j=0; j<waiting_people; j++) {
				s.release();
				current_capacity++;
			}
			inside_the_room = true;
			mutex.release();
		}
		mutex.release();
	}
	
	
}
