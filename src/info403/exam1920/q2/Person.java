package info403.exam1920.q2;

import java.util.concurrent.Semaphore;

public class Person extends Thread{
	String personName;
	Group[] groups;
	Semaphore mutex;
	

	public Person(String personName, Group[] groups, Semaphore mutex) {
		super();
		this.personName = personName;
		this.groups = groups;
		this.mutex = mutex;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	public Group[] getGroups() {
		return groups;
	}

	public void setGroups(Group[] groups) {
		this.groups = groups;
	}

	public Semaphore getMutex() {
		return mutex;
	}

	public void setMutex(Semaphore mutex) {
		this.mutex = mutex;
	}

	@Override
	public String toString() {
		return "Person [name=" + personName + "]";
	}
	
	@Override
	public void run() {

		//Finding a incomplete group
		Group incompleteGroup = null;
		try {
			incompleteGroup = getIncompleteGroup(groups);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (incompleteGroup == null) {
			System.out.println("I'm " + this.getPersonName() + ", i didn't find a incomplete group !");
		}
		else {
			System.out.println("I'm " + this.getPersonName() + ", trying to join");
			try {
				incompleteGroup.join(this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public Group getIncompleteGroup(Group[] groups) throws InterruptedException {
		if (groups != null) {
			for (Group group : groups) {
				mutex.acquire();
				if(group.getWaiting_people()<group.getCapacity()) {
					mutex.release();
					return group;
				}
				mutex.release();
			}
		}
		return null;
	}
}
