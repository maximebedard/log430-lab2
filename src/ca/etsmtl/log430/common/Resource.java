package ca.etsmtl.log430.common;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class defines the Resource object for the system. Besides the basic
 * attributes, there are two lists maintained. alreadyAssignedProjectList is a
 * ProjectList object that maintains a list of projects that the resource was
 * already assigned to prior to this execution of the system.
 * projectsAssignedList is also a ProjectList object that maintains a list of
 * projects assigned to the resource during the current execution or session.
 * 
 * @author A.J. Lattanze, CMU
 * @version 1.6, 2013-Oct-06
 */

/* Modification Log
 ****************************************************************************
 * v1.6, R. Champagne, 2013-Oct-06 - Various refactorings for new lab.
 * 
 * v1.5, R. Champagne, 2012-Jun-19 - Various refactorings for new lab.
 * 
 * v1.4, R. Champagne, 2012-May-31 - Various refactorings for new lab.
 * 
 * v1.3, R. Champagne, 2012-Feb-02 - Various refactorings for new lab.
 * 
 * v1.2, 2011-Feb-02, R. Champagne - Various refactorings, javadoc comments.
 *  
 * v1.1, 2002-May-21, R. Champagne - Adapted for use at ETS. 
 * 
 * v1.0, 12/29/99, A.J. Lattanze - Original version.

 ****************************************************************************/

public class Resource {

	/**
	 * Resource's last name
	 */
	private String lastName;
	
	/**
	 * Resource's first name
	 */
	private String firstName;
	
	/**
	 * Resource's identification number
	 */
	private String id;
	
	/**
	 * Resource role 
	 */
	private String role;

	/**
	 *  List of projects the resource is already allocated to
	 */
	private ProjectList alreadyAssignedProjectList = new ProjectList();

	/**
	 *  List of projects assigned to the resource in this session
	 */
	private ProjectList projectsAssignedList = new ProjectList();

	/**
	 * Assigns a project to a resource.
	 * 
	 * @param project
	 */
	public void assignProject(Project project) {

		getProjectsAssigned().addProject(project);

	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setPreviouslyAssignedProjectList(ProjectList projectList) {
		this.alreadyAssignedProjectList = projectList;
	}

	public ProjectList getPreviouslyAssignedProjectList() {
		return alreadyAssignedProjectList;
	}

	public void setProjectsAssigned(ProjectList projectList) {
		this.projectsAssignedList = projectList;
	}

	public ProjectList getProjectsAssigned() {
		return projectsAssignedList;
	}

    //Method to get the total workload of a ressource.
    //We calculate the ressource occupation and add it all up.
    //Returns the work load.
    public boolean canAcceptMoreWork(Project toBeAssignedProject){

        Project p = null;
        int ressourceOccupation;
        boolean canAcceptWork = true;

        ProjectList allProjectsN = new ProjectList();
        ProjectList allProjects = this.alreadyAssignedProjectList;


        //Add all newly assigned projects into the "all projects list"
        //Since it uses that weird List structure, we need to use a do..while and iterate
        //through all the projects and merge it into the allProjects list
        ProjectList newlyAssigned = this.projectsAssignedList;
        newlyAssigned.goToFrontOfList();
        do {
            p = newlyAssigned.getNextProject();

            if( p != null) {
                allProjectsN.addProject(p);
            }

        }
        while(p != null);


        do {
            p = allProjects.getNextProject();

            if( p != null) {
                allProjectsN.addProject(p);
            }

        }
        while(p != null);


       // allProjects.addProject(toBeAssignedProject);

        //Reset that index
        allProjects.goToFrontOfList();

        //Attemt to get all possible6 project overlaps.
        List overlaps =  getAllOverlaps(allProjectsN,toBeAssignedProject);
        ProjectList overlapsPl;

        //Again, that silly do..while
        do{

            overlapsPl = (ProjectList) overlaps.getItemFromList();

            //Try catch with no error handling? This is what happens when there's not .isEmpty() method in a custom built list.
            // YOLO
            try{
                //overlapsPl.addProject(toBeAssignedProject);

                ressourceOccupation = countRessourceOccupation(overlapsPl);

                if(ressourceOccupation > 100){
                    canAcceptWork = false;
                }

            }  catch (NullPointerException e){

            }

        } while(overlapsPl != null);

        return canAcceptWork;
    }

    private List getAllOverlaps(ProjectList pl, Project toBeAssignedProject){

        Project p;
        Project p2;

        pl.goToFrontOfList();

        //Secondary list. We need this list since we're gonna be parsing through it a couple of dozen times.
        ProjectList plist2 = pl;


        List allOverlaps = new List();

        pl.goToFrontOfList();

            p = toBeAssignedProject;


            if( p != null) {

                ProjectList overlapList = new ProjectList();
                try{

                    plist2.goToFrontOfList();
                    overlapList.addProject(p);

                    do{
                        p2 = plist2.getNextProject();
                        //Make sure that p2 is not null and it's not the same project.

                        if(p2 != null && p.getID().compareToIgnoreCase(p2.getID()) != 0){

                            System.out.println(p2.getID());
                            //Condition checking to make sure the projects overlap
                            if(
                                    (p2.getParsedEndDate().after(p.getParsedStartDate())
                                    && p2.getParsedStartDate().before(p.getParsedEndDate()) )
                                   || p2.getParsedEndDate().equals(p.getParsedEndDate())
                                    || p2.getParsedStartDate().equals(p.getParsedStartDate())
                                    ){
                                overlapList.addProject(p2);
                                System.out.println(p2.getID());
                            }

                        }


                    }  while(p2!=null);

                }
                catch(NullPointerException e){
                    System.out.print("** WARNING : Error detected with a Project!  **");
                }

                System.out.println(countRessourceOccupation(overlapList));
                //Might return a null list. Doesn't matter, everything works.
                allOverlaps.appendItemToList(overlapList);


            }


        return allOverlaps;
    }

    private int countRessourceOccupation(ProjectList pl){

        int ressourceOccupation = 0;

        Project p;

        pl.goToFrontOfList();

        do {
            p = pl.getNextProject();

            if( p != null) {
                ressourceOccupation += p.getRessourceOccupation();
            }

        }
        while(p != null);




        return ressourceOccupation;
    }


} // Resource class